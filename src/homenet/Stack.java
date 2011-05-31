/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homenet;

import java.util.*;

import static homenet.Packet.*;

/**
 *
 * @author mdoll
 */
public class Stack {
    
    static int InterruptCount = 0;

    public int _nodeId;
    private ArrayList<Packet> _packets = new ArrayList();
    private HashMap<String, Port> _ports = new HashMap();
    private HashMap<Integer, Device> _devices = new HashMap();
    private ArrayList<Schedule> _deviceSchedule = new ArrayList();
    private ArrayList<Interrupt> _deviceInterrupts = new ArrayList();
    private int _uniqueId;
    private int _scheduleCount;
    private long _deviceTimer;
    
    private ProcessThread _processThread; 
    
    
    private ArrayList<Listener> _listeners = new ArrayList();

    public Stack(int id) {
        _nodeId = id;
        _uniqueId = 0;
        _processThread = new ProcessThread(this, 10);
        _processThread.start();
    }
    
    public void addListener(Listener listener){
        _listeners.add(listener);
    }

    public void addPort(String name, Port port) {
        _ports.put(name, port);
        port.init(name);
    }

    public void removePort(String name) {
        _ports.remove(name);
    }

    public HashMap getPorts() {
        return _ports;
    }
    
    public Port getPort(String port) {
        return _ports.get(port);
    }    

    private int _getId() {
        if (_uniqueId == 255) {
            _uniqueId = 0;
        }
        return _uniqueId++;
    }
    
    

    public Packet getNewPacket() {
        if (_packets.size() > 10) {
            System.out.println("WARNING PacketStack is getting Large. " + _packets.size() + " Packets");
        }
        System.out.println("Creating New Packet. Stack has " + _packets.size() + " Packets");
        Packet packet = new Packet();
        _packets.add(packet);
        return (Packet) _packets.get(_packets.size() - 1);
    }

    public void addPacket(Packet packet) {
        System.out.println("Adding New Packet. Stack has " + _packets.size() + " Packets");
        _packets.add(packet);
    }

    public void init() {
        _ports = new HashMap();
        _devices = new HashMap();
    }

    public void init(HashMap ports, HashMap devices) {
        _ports = ports;
        _devices = devices;
        //setup ports

        Iterator iterator = _ports.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry portSet = (Map.Entry) iterator.next();
            ((Port) portSet.getValue()).init((String) portSet.getKey());
        }


        //setup devices
        iterator = _devices.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry deviceSet = (Map.Entry) iterator.next();
            ((Port) deviceSet.getValue()).init((String) deviceSet.getKey());
        }
    }

    public void receive() {       
        for(Port p : _ports.values()){
            p.receive();
        }
    }

    public Packet clonePacket(Packet packet) {
        //Packet packet2 = new;
        try {
            Packet packet2 = (Packet) packet.clone();
            addPacket(packet2);
            return packet2;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return new Packet();

    }

    public boolean process() {
        boolean process = false;

        for (int i = _packets.size() - 1; i >= 0; i--) {
            if (_processPacket(_packets.get(i)) == true) {
                process = true;
            } else {
                _packets.remove(i);
            }
        }
        return process;
    }

    private boolean _processPacket(Packet packet) {
        switch (packet.getStatus()) {
            case STATUS_CLEAR:
            //    System.out.println("Packet Status: Clear");
                return false;
            // break;
            case STATUS_RECEIVING:
           //     System.out.println("Packet Status: Receving");
                return false;
            //  break;
            case STATUS_RECEIVED:
            //    System.out.println("Packet Status: Received");
                //debugPacket(packet);
                //process Listeners
                for(Listener l : _listeners){
                    l.packetRecieved(packet);
                }
                
                

                if (packet.getToNode() == _nodeId) {
                    //@todo process received packet
                    // packetReceivedEvent(packet);
                    packet.setStatus(STATUS_CLEAR);
                } else if(_ports.size()> 1) {
                    packet.setStatus(STATUS_READY);
                }
                packet.setStatus(STATUS_CLEAR);
                break;
            case STATUS_WAITING:
              //  System.out.println("Packet Status: Waiting");
                return false;
            //break;
            case STATUS_READY:
               // System.out.println("Packet Status: Ready");
                if (packet.getToNode() == _nodeId) { //check for packets to self and process them
                    processCommand(packet);
                    packet.setStatus(STATUS_CLEAR);
                }

                //if their are not any ports just let the packets sit
                if (_ports.isEmpty()) {
                    packet.setStatus(STATUS_FAILED);
                    System.out.println("No ports to process Packets");
                    break;
                }

                //set toPort address. a unique packet is required for each port
                //system built the packet and it needs to go to all the ports
                if (packet.fromPort.equals(packet.toPort)) { //the only times these are equal is when it's a brand new pack that needs to go out to all ports
                    System.out.println("system built packet");
                    Iterator iterator = _ports.keySet().iterator();
                    packet.toPort = (String) iterator.next(); //set first port
                    System.out.println("packet1 to: " + packet.toPort);
                    while (iterator.hasNext()) {
                        Packet p = clonePacket(packet);
                        p.toPort = (String) iterator.next();
                        // @todo display debug packet
                        // debugPacket(p);
                        System.out.println("packet2 to: " + p.toPort);
                        System.out.println("packet1 to: " + packet.toPort);
                    }

                    //packet came from a port and needs to passed to the other ports
                    //@todo can this just be an else?
                } else if (packet.toPort == null) { //has a fromPort but no toPort
                    System.out.println("to port null");
                    Iterator iterator = _ports.keySet().iterator();
                    while (iterator.hasNext()) {
                        String port = (String) iterator.next();
                        if (!packet.fromPort.equals(port)) {
                            if (packet.toPort == null) {
                                packet.toPort = port;
                            } else {
                                System.out.println("Cloning packet");
                                Packet p = clonePacket(packet);
                                p.toPort = port;
                            }
                        }
                    }
                }
                System.out.println("toPort: " + packet.getToPort()); //=null?
                if (((Port) _ports.get(packet.getToPort())).isSending() == false) {
                    System.out.println("changing status to send");
                    packet.setStatus(STATUS_SENDING);
                } else {
                    //allow a pause if it can't send
                    return false;
                }
                // only mark packet per type to send per loop. sending serial is blocking.
                //if reset pointer and set to send

                break;
            case STATUS_SENDING:
           //     System.out.println("Packet Status: Sending");
                //because of the above, only one packet will be changed to sending, this keeps from blocking the loop too long
                //debugPacket(packet);
                if (_ports.containsKey(packet.toPort)) {
                    //check to see if homenet has booted up
                    //print("Trying to send: "+_ports.get(packet.toPort).started);
                    if (_ports.get(packet.toPort).started == true) {
                        System.out.println("Status: Packet Sending: " + packet.toPort);
                        _ports.get(packet.toPort).send(packet);
                    } else if (_ports.get(packet.toPort).timeAdded + NODE_BOOT_TIME < System.currentTimeMillis()) {
                        _ports.get(packet.toPort).started = true;
                        //@todo display message
                        //msg("Serial Port " + packet.toPort + " Started");
                    }

                } else {
                    System.out.println("Missing Port, Removing Packet");
                    packet.setStatus(STATUS_FAILED);
                }
                break;
            case STATUS_SENT:
                System.out.println("Packet Status: Sent");
                if (packet.getType() == 1) {
                    packet.setStatus(STATUS_ACK);
                    //start packets ack timer
                } else {
                    packet.setStatus(STATUS_SUCCESS);
                }
                break;
            case STATUS_ACK:
                packet.setStatus(STATUS_CLEAR);
                //if execeded timer resen
                //count resends if exceds max kill packet and add error
                break;
            case STATUS_SUCCESS:
                System.out.println("Packet Status: Success");
                //option to post status to screen
                packet.setStatus(STATUS_CLEAR);
                break;
            case STATUS_FAILED:
                System.out.println("Packet Status: Failed");
                //option to post status to screen
                packet.setStatus(STATUS_CLEAR);
                break;
        }
        return true;
    }

    private boolean processCommand(Packet packet) {
        int device = packet.getToDevice();

        PartialPacket partial = _devices.get(device).process(packet.getFromNode(), packet.getFromDevice(), packet.getCommand(), packet.getPayload());
        if (partial != null) {
            // if(packetType == 2){
            addUdpPacket(partial.fromDevice, packet.getFromNode(), packet.getFromDevice(), partial.command, partial.payload);
        }

        return true;
    }

    Payload packetToPayload(Packet packet) {      
        Payload payload = new Payload();
        java.lang.System.arraycopy(packet.data, 0, payload.data, 0, packet.getLength());
//        for (int i = 0; i < packet.getLength(); i++) {
//            payload.data[i] = packet.data[i];
//        }
         return payload;
    }

    public int getNodeId() {
        return _nodeId;
    }

    public Packet addTcpPacket(int fromDevice, int toNode, int toDevice, int command, Payload payload) {
        return addTcpPacket(fromDevice, toNode, toDevice, command, payload, STATUS_READY);
    }

    public Packet addTcpPacket(int fromDevice, int toNode, int toDevice, int command, Payload payload, int status) {
        Packet packet = getNewPacket();
        packet.setStatus(STATUS_READY);
        packet.setLength(payload.length + OFFSET_PACKET); //header 8 crc 2
        packet.setType(1);
        packet.setFromNode(_nodeId);
        packet.setFromDevice(fromDevice);
        packet.setToNode(toNode);
        packet.setToDevice(toDevice);
        packet.setId(_getId());
        packet.setCommand(command);
        packet.setPayload(payload);

        return addCrc(packet);
    }

    public Packet addUdpPacket(int fromDevice, int toNode, int toDevice, int command, Payload payload) {
        return addUdpPacket(fromDevice, toNode, toDevice, command, payload, STATUS_READY);
    }

    public Packet addUdpPacket(int fromDevice, int toNode, int toDevice, int command, Payload payload, int status) {
        Packet packet = getNewPacket();
        packet.setStatus(STATUS_READY);
        packet.setLength(payload.length + OFFSET_PACKET); //header 8 crc 2
        packet.setType(2);
        packet.setFromNode(_nodeId);
        packet.setFromDevice(fromDevice);
        packet.setToNode(toNode);
        packet.setToDevice(toDevice);
        packet.setId(_getId());
        packet.setCommand(command);
        packet.setPayload(payload);

        return addCrc(packet);
    }

    public void debugPayload(final Payload payload) {

        System.out.println("==Debug Payload==");
        System.out.print("Length: ");
        System.out.println(payload.length);

        System.out.print("Payload: ");
        for (int i = 0; i < payload.length; i++) {
            System.out.print(payload.data[i]);
            System.out.print(",");
        }
        System.out.println("");
    }

    public Packet addCrc(Packet packet) {
        int dataChecksum = 0;
        int i;
        for (i = 0; i < (packet.getLength() - OFFSET_FOOTER); i++) {
            dataChecksum = crc16_update(dataChecksum, (int) (packet.data[i] & 0xFF));
        }
        packet.data[i++] = (byte) ((dataChecksum >> 8) & 0xFF);
        packet.data[i] = (byte) (dataChecksum & 0xFF);
        return packet;

    }

    public void deviceUpdate() {

        for (int i = 0; i < _devices.size(); i++) {
            _devices.get(i).updateOften();
        }

        if (System.currentTimeMillis() > (_deviceTimer + DEVICE_UPDATE)) {
            for (int i = 0; i < _devices.size(); i++) {
                _devices.get(i).update();
            }
            _scheduleCount++;
            _deviceTimer = System.currentTimeMillis();
        }
    }

    public void registerSchedule(ArrayList s) {
        _deviceSchedule = s;
    }

    public void registerInterrupts(ArrayList i) {
        _deviceInterrupts = i;
    }

    public void deviceSchedule() {
        //_scheduleCount++;
        if (_scheduleCount == DEVICE_SCHEDULE) {
            //PayloadBuffer buffer;
            for (int i = 0; i < _deviceSchedule.size(); i++) {
                if (_deviceSchedule.get(i).delay == 0) {

                    PartialPacket partial = _deviceSchedule.get(i).device.schedule(_deviceSchedule.get(i).command, _deviceSchedule.get(i).payload);
                    if (partial != null) {
                        addUdpPacket(partial.fromDevice, _deviceSchedule.get(i).toNode, _deviceSchedule.get(i).toDevice, partial.command, partial.payload);

                    }
                    _deviceSchedule.get(i).delay = _deviceSchedule.get(i).frequency - 1;

                } else {
                    _deviceSchedule.get(i).delay--;
                }
            }
            _scheduleCount = 0;
        }
    }

    public void deviceInterrupt() {
        deviceInterrupt(false);
    }

    public void deviceInterrupt(boolean run) {

        if ((InterruptCount > 0) || run) {
            InterruptCount = 0;
            for (int i = 0; i < _deviceInterrupts.size(); i++) {

                PartialPacket partial = _deviceInterrupts.get(i).device.interrupt(_deviceInterrupts.get(i).command, _deviceInterrupts.get(i).payload);

                if (partial != null) {
                    addUdpPacket(partial.fromDevice, _deviceInterrupts.get(i).toNode, _deviceInterrupts.get(i).toDevice, partial.command, partial.payload);
                }
            }
        }
    }

    public void loop() {
        //receive incoming packets
        receive();
        process();

        deviceUpdate();

        //still working on how often to call these
        receive();
        process();

        deviceSchedule();

        receive();
        process();

        deviceInterrupt();
    }

    public int getVersion() {
        return 0x01;
    }

//ported from the avr crc lib
    
    
    
    class ProcessThread extends Thread {

    boolean running;           // Is the thread running?  Yes or no?
    int wait;                  // How many milliseconds should we wait in between executions?
    String id;    // Thread name
    Stack _homeNet;
    int count;

    // Constructor, create the thread
    // It is not running by default
    ProcessThread(Stack h, int w) {
        _homeNet = h;
        wait = w;
        running = false;
        count = 0;
    }

    // Overriding "start()"
    @Override
    public void start() {
        // Set running equal to true
        running = true;

        // Print messages
        System.out.println("Starting HomeNet Process Stack Thread (will execute every " + wait + " milliseconds.)");
        // Do whatever start does in Thread, don't forget this!
        super.start();
    }

    // We must implement run, this gets triggered by start()
    @Override
    public void run() {
        while (running) {
            count = 0;
            _homeNet.receive();
            while (_homeNet.process() == true) {
                count++;
            };
            // Ok, let's wait for however long we should wait
            try {
                if (count > 1) {
                    System.out.println("Processed Stack " + count + " times");
                }
                Thread.sleep((long) (wait));
            } catch (Exception e) {
            }
        }
        System.out.println(id + " thread is done!");  // The thread is done when we get to the end of run()
    }

    // Our method that quits the thread
    public void quit() {
        System.out.println("Quitting.");
        running = false;  // Setting running to false ends the loop in run()
        // IUn case the thread is waiting. . .
        interrupt();
    }
}

};
