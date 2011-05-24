/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homenetappgui;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author mdoll
 */
public class HomeNet {

    final int PACKET_LENGTH = 0;
    final int PACKET_SETTINGS = 1;
    final int PACKET_FROM = 2;
    final int PACKET_TO = 4;
    final int PACKET_TTL = 4;
    final int PACKET_ID = 6;
    final int PACKET_COMMAND = 7;
    final int PACKET_PAYLOAD = 8;
    final int CMD_BATTERYLEVEL = 0x03;
    final int CMD_MEMORYFREE = 0x04;
    final int CMD_PING = 0x33;
    final int CMD_PONG = 0x3E;
    final int CMD_ACK = 0x11;
    final int CMD_GETNODEID = 0x21;
    final int CMD_SETNODEID = 0x22;
    final int CMD_GETDEVICE = 0x23;
    final int CMD_SETDEVICE = 0x24;
    final int CMD_AUTOSENDSTART = 0xB1;
    final int CMD_AUTOSENDSTOP = 0xB2;
    final int CMD_ON = 0xC0;
    final int CMD_OFF = 0xC1;
    final int CMD_LEVEL = 0xC2;
    final int CMD_CLEAR = 0xC3;
    final int CMD_GETVALUE = 0xD0;
    final int CMD_GETBYTE = 0xD1;
    final int CMD_GETSTRING = 0xD2;
    final int CMD_GETINT = 0xD3;
    final int CMD_GETFLOAT = 0xD4;
    final int CMD_GETLONG = 0xD5;
    final int CMD_GETBINARY = 0xD6;
    final int CMD_SETVALUE = 0xE0;
    final int CMD_SETBYTE = 0xE1;
    final int CMD_SETSTRING = 0xE2;
    final int CMD_SETINT = 0xE3;
    final int CMD_SETFLOAT = 0xE4;
    final int CMD_SETLONG = 0xE5;
    final int CMD_SETBINARY = 0xE6;
    final int CMD_REPLYVALUE = 0xF0;
    final int CMD_REPLYBYTE = 0xF1;
    final int CMD_REPLYSTRING = 0xF2;
    final int CMD_REPLYINT = 0xF3;
    final int CMD_REPLYFLOAT = 0xF4;
    final int CMD_REPLYLONG = 0xF5;
    final int CMD_REPLYBINARY = 0xF6;

    /*
    #define CMD_AUTOSENDSTART   0xB1
    #define CMD_AUTOSENDSTOP    0xB2
    
    #define CMD_ON              0xC0
    #define CMD_OFF             0xC1
    #define CMD_LEVEL           0xC2
    #define CMD_CLEAR           0xC3
    
    #define CMD_GETBYTE         0xD0
    #define CMD_GETSTRING       0xD1
    #define CMD_GETINT          0xD2
    #define CMD_GETFLOAT        0xD3
    #define CMD_GETLONG         0xD4
    #define CMD_GETBINARY       0xD5
    
    #define CMD_SETBYTE         0xE0
    #define CMD_SETSTRING       0xE1
    #define CMD_SETINT          0xE2
    #define CMD_SETFLOAT        0xE3
    #define CMD_SETLONG         0xE4
    #define CMD_SETBINARY       0xE5
    
    #define CMD_REPLYBYTE       0xF0
    #define CMD_REPLYSTRING     0xF1
    #define CMD_REPLYINT        0xF2
    #define CMD_REPLYFLOAT      0xF3
    #define CMD_REPLYLONG       0xF4
    #define CMD_REPLYBINARY     0xF5
    
     */
    final int CMD_MULTIPACKET = 0xA0;
//an enum might be better here
    final int STATUS_CLEAR = 0;
    final int STATUS_RECEVING = 1;
    final int STATUS_RECEIVED = 2;
    final int STATUS_WAITING = 3;
    final int STATUS_READY = 4;
    final int STATUS_SENDING = 5;
    final int STATUS_SENT = 6;
    final int STATUS_ACK = 7;
    final int STATUS_SUCCESS = 8;
    final int STATUS_FAILED = 9;
    final int OFFSET_PACKET = 10;
    final int OFFSET_HEADER = 8;
    final int OFFSET_FOOTER = 2;
    final int PACKET_TCP = 1;
    final int PACKET_UDP = 2;
    final int PACKET_BROADCAST = 3;
    final int PACKET_BUFFER = 10;
    final int PACKET_TIMEOUT = 100;
    final int PACKET_RETRY = 1;
    final int DEVICE_UPDATE = 1000;
//schedule timer (millis)  = DEVICE_SCHEDULE * DEVICE_UPDATE;
    final int DEVICE_SCHEDULE = 1;
    final int NODE_BOOT_TIME = 3000;
    final int SERIAL_SPEED = 115200;
    int InterruptCount = 0;

//payload type
    class Payload {

        int length = 0;
        byte[] data = new byte[56];

        Payload(final byte value) {
            length = 1;
            data[0] = value;
        }

        Payload(final byte[] value, final int size) {
            System.arraycopy(value, 0, data, 0, size);
            length = size;
        }

        Payload(final byte[] value) {
            this(value, value.length);
        }

        Payload(final int value) {
            length = 2;
            data[1] = (byte) ((value >> 8) & 0xFF);
            data[0] = (byte) (value & 0xFF);
        }

        Payload(final int[] value) {
            length = value.length * 2;
            for (int i = 0; i < value.length; i += 2) {
                data[i * 2] = (byte) ((value[i] >> 8) & 0xFF);
                data[(i * 2) + 1] = (byte) (value[i] & 0xFF);
            }
        }

        Payload(final String string) {
            length = string.length();
            for (int i = 0; i < length; i++) {
                data[i] = (byte) (string.charAt(i));
            }
        }
        /*
        Payload(final char value[]) {
        int i = 0;
        
        while (value[i] != null) {
        data[i] = value[i];
        i++;
        }
        length = i;
        }
         */

        Payload(final float value) {
            length = 0;
            /*
            union {
            float in;
            long out;
            } number;
            
            number.in = value;
            
            Payload payload;
            length = 4;
            
            data[0] = (number.out >> 24) &0xFF;
            data[1] = (number.out >> 16) &0xFF;
            data[2] = (number.out >> 8) &0xFF;
            data[3] = number.out & 0xFF;
             */
        }

        Payload(final float[] value) {
            length = 0;
        }

        Payload(final long value) {
            length = 4;
            data[0] = (byte) ((value >> 24) & 0xFF);
            data[1] = (byte) ((value >> 16) & 0xFF);
            data[2] = (byte) ((value >> 8) & 0xFF);
            data[3] = (byte) (value & 0xFF);
        }

        Payload() {
            length = 0;
        }

        public int getByte() {
            return (getByte(0));
        }

        public int getByte(int place) {
            return (data[place]) & 0xFF;
        }

        public int getInt() {
            return (int) (data[1] << 8) | (int) (data[0]);
        }

        public int getLength() {
            return length;
        }

        public float getFloat() {

            if (length != 4) {
                return 0.0F;
            }

            // Same as DataInputStream's 'readInt' method
            int i = (((data[0] & 0xff) << 24) | ((data[1] & 0xff) << 16)
                    | ((data[2] & 0xff) << 8) | (data[3] & 0xff));


            return Float.intBitsToFloat(i);
        }
    }

    class PartialPacket {

        int fromDevice;
        int command;
        Payload payload;

        PartialPacket(int f, int c, Payload p) {
            fromDevice = f;
            command = c;
            payload = p;
        }
    }

    class Schedule {

        int delay; //seconds
        int frequency;    //seconds
        Device device;
        int toNode;
        int toDevice;
        int command;
        Payload payload;

        Schedule(int d, int f, Device e, int t, int o, int c, Payload p) {
            delay = d;
            frequency = f;
            device = e;
            toNode = t;
            toDevice = o;
            command = c;
            payload = p;
        }
    }

    class Interrupt {

        Device device;
        int toNode;
        int toDevice;
        int command;
        Payload payload;

        Interrupt(Device e, int t, int o, int c, Payload p) {
            device = e;
            toNode = t;
            toDevice = o;
            command = c;
            payload = p;
        }
    }

    /*
    class PayloadBuffer : public Print {
    
    public:
    Payload stor;
    inline PayloadBuffer() { reset(); }
    inline uint8_t* data() { return stor.data; }
    char* getString();
    inline Payload payload() { return stor; }
    inline uint8_t length() { return stor.length; }
    inline void reset() { stor.length = 0; }
    void write(uint8_t ch);
    };
     */
    class Packet implements Cloneable {

        String toPort = null;
        String fromPort = null;
        int status;
        byte[] data = new byte[66];
        Date received;

        Packet() {
            received = new Date();
        }

        Packet(byte[] packet) {
            received = new Date();
            data = packet;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public String getToPort() {
            return toPort;
        }

        public String getFromPort() {
            return fromPort;
        }

        public int getStatus() {
            return status;
        }

        public int getLength() {
            return data[PACKET_LENGTH] & 0xFF;

        }

        public int getSettings() {
            return data[PACKET_SETTINGS] & 0x0F;
        }

        public int getType() {
            return data[PACKET_SETTINGS] & 0x0F;
        }

        public int getFromNode() {
            return (int) (data[PACKET_FROM] << 4) | (int) (data[PACKET_FROM + 1] >> 4);
        }

        public int getFromDevice() {
            return (int) (data[PACKET_FROM + 1] & 0x0F);
        }

        public int getToNode() {
            return (int) (data[PACKET_TO] << 4) | (int) (data[PACKET_TO + 1] >> 4);
        }

        public int getToDevice() {
            return (int) (data[PACKET_TO + 1] & 0x0F);
        }

        public int getId() {
            return data[PACKET_ID] & 0xff;
        }

        public int getCommand() {
            return data[PACKET_COMMAND] & 0xff;
        }

        public Payload getPayload() {
            Payload payload = new Payload();
            for (int i = 0; i < getPayloadLength(); i++) {
                payload.data[i] = data[i + OFFSET_HEADER];
            }
            return payload;
        }

        public int getPayloadAt(int place) {
            if ((getLength() - OFFSET_FOOTER) > (OFFSET_HEADER + place)) {
                return data[OFFSET_HEADER + place] & 0xFF;

            }
            return -1;
        }

        public int getPayloadLength() {
            return (data[PACKET_LENGTH] - OFFSET_PACKET) & 0xFF;
        }

        public int getChecksum() {
            return (((int) (data[getLength() - OFFSET_FOOTER])) << 8) | (int) data[getLength() - 1];
        }

        public Date getTimestamp() {
            return received;
        }

        public byte[] getData() {
            byte[] packet = new byte[getLength()];

            packet = Arrays.copyOfRange(data, 0, getLength());
            return packet;
        }

        public void setToPort(String value) {
            toPort = value;
        }

        public void setFromPort(String value) {
            fromPort = value;
        }

        public void setStatus(int value) {
            status = value;
        }

        public void setLength(int value) {
            data[PACKET_LENGTH] = (byte) (value & 0xFF);
        }

        public void setSettings(int value) {
            data[PACKET_SETTINGS] |= (byte) (value & 0x0F);
        }

        public void setType(int value) {
            data[PACKET_SETTINGS] |= (byte) (value & 0x0F);
        }

        public void setFromNode(int value) {
            data[PACKET_FROM] = (byte) ((value >> 4) & 0xFF);
            data[PACKET_FROM + 1] &= 0x0F; //clear bottom 4 bits
            data[PACKET_FROM + 1] |= (byte) ((value << 4) & 0xF0);
        }

        public void setToNode(int value) {
            data[PACKET_TO] = (byte) ((value >> 4) & 0xFF);
            data[PACKET_TO + 1] &= 0x0F; //clear bottom 4 bits
            data[PACKET_TO + 1] |= (byte) ((value << 4) & 0xF0);
        }

        public void setFromDevice(int value) {
            data[PACKET_FROM + 1] &= (byte) (0xF0); //clear top 4 bits
            data[PACKET_FROM + 1] |= (byte) (value & 0x0F);
        }

        public void setToDevice(int value) {
            data[PACKET_TO + 1] &= (byte) (0xF0); //clear top 4 bits
            data[PACKET_TO + 1] |= (byte) (value & 0x0F);
        }

        public void setId(int value) {
            data[PACKET_ID] = (byte) (value & 0xFF);
        }

        public void setCommand(int value) {
            data[PACKET_COMMAND] = (byte) (value & 0xFF);
        }

        public void setPayload(final Payload payload) {
            setPayloadLength(payload.length);
            System.arraycopy(payload.data, 0, data, OFFSET_HEADER, payload.length);
        }

        public void setPayloadLength(int value) {
            data[PACKET_LENGTH] = (byte) (value + OFFSET_PACKET);
        }
    }

    abstract class Port {

        protected HomeNet.Stack _homeNet;
        protected String _id;
        protected boolean _sending;// = false;
        protected boolean _receiving;// = false;
        boolean started;
        long timeAdded;

        Port(HomeNet.Stack homeNet) {
            _homeNet = homeNet;
            timeAdded = System.currentTimeMillis(); //might need to offset or use java datetime
            started = false;
        }

        public abstract void init(String id);

        public abstract void send(Packet packet);

        public abstract void receive();

        public void stop() {
        }

        ;
    public boolean isSending() {
            return _sending;
        }

        public boolean isReceiving() {
            return _receiving;
        }

        public String getId() {
            return _id;
        }
    };

    abstract class Device {

        protected HomeNet _homeNet;
        protected int _location;

        Device(HomeNet homeNet) {
            _homeNet = homeNet;
        }

        public abstract void getId();

        public void init(int location) {
            _location = location;
        }

        public void update() {
        }

        public void updateOften() {
        }

        public PartialPacket process(int fromNode, int fromDevice, int command, Payload payload) {
            return process(command, payload);
        }

        public PartialPacket process(int command, Payload payload) {
            return process(command);
        }

        public PartialPacket process(int command) {
            return null;
        }

        public PartialPacket interrupt(int command, Payload payload) {
            return process(0, 0, command, payload);
        }

        public PartialPacket schedule(int command, Payload payload) {
            return process(0, 0, command, payload);
        }

        public int getLocation() {
            return _location;
        }
    }

////////////////////////////HomeNet/////////////////////////////////////////
    class Stack {

        public int _nodeId;
        private ArrayList<Packet> _packets = new ArrayList();
        private HashMap<String, Port> _ports = new HashMap();
        private HashMap<Integer, Device> _devices = new HashMap();
        private ArrayList<Schedule> _deviceSchedule = new ArrayList();
        private ArrayList<Interrupt> _deviceInterrupts = new ArrayList();
        private int _uniqueId;
        private int _scheduleCount;
        private long _deviceTimer;

        public void addPort(String name, Port port) {
            _ports.put(name, port);
        }

        public void removePort(String name) {
            _ports.remove(name);
        }

        public HashMap getPorts() {
            return _ports;
        }

        private int _getId() {
            if (_uniqueId == 255) {
                _uniqueId = 0;
            }
            return _uniqueId++;
        }

        private Packet _getNewPacket() {
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

        public Stack(int id) {
            _nodeId = id;
            _uniqueId = 0;
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
            Iterator iterator = _ports.values().iterator();
            while (iterator.hasNext()) {
                ((Port) iterator.next()).receive();
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
                    return false;
                // break;
                case STATUS_RECEVING:
                    return false;
                //  break;
                case STATUS_RECEIVED:
                    //debugPacket(packet);

                    if (packet.getToNode() == _nodeId) {
                        //@todo process received packet
                        // packetReceivedEvent(packet);
                        packet.status = STATUS_CLEAR;
                    } else {
                        packet.status = STATUS_READY;
                    }
                    break;
                case STATUS_WAITING:
                    return false;
                //break;
                case STATUS_READY:

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
                    System.out.println("Status: Packet Sent");
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
                    //option to post status to screen
                    packet.setStatus(STATUS_CLEAR);
                    break;
                case STATUS_FAILED:
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

            for (int i = 0; i < packet.getLength(); i++) {
                payload.data[i] = packet.data[i];
            }
            return payload;
        }

        public int getNodeId() {
            return _nodeId;
        }

        public Packet addTcpPacket(int fromDevice, int toNode, int toDevice, int command, Payload payload) {
            return addTcpPacket(fromDevice, toNode, toDevice, command, payload, STATUS_READY);
        }

        public Packet addTcpPacket(int fromDevice, int toNode, int toDevice, int command, Payload payload, int status) {
            Packet packet = _getNewPacket();
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
            Packet packet = _getNewPacket();
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
                dataChecksum = _crc16_update(dataChecksum, (int) (packet.data[i] & 0xFF));
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
    ;
    //ported from the avr crc lib

    };

    class PortSerial extends Port {

        private int _ptr;
        private Serial _serial;
        private int _receivingChecksum;
        private Packet _sendingPacket;
        private Packet _receivingPacket;
        private int _sendingRetryCount;
        private long _receivingTimer;
        private long _sendingTimer;
//

        PortSerial(HomeNet.Stack homeNet, Serial s) {
            super(homeNet);
            _serial = s;
        }

        @Override
        public void init(String id) {
            _sending = false;
            _receiving = false;
            _id = id;
            _receivingChecksum = 0;
            _ptr = 0;
            _serial.begin(SERIAL_SPEED);
        }

        @Override
        public void send(Packet packet) {
            if (_sending == false) {
                _sending = true;
                //copy pointer
                _sendingRetryCount = 0;
            }
            _sendingTimer = System.currentTimeMillis();
            _sendingPacket = packet;

            System.out.print("Sending: ");
            for (int i = 0; i < packet.getLength(); i++) {
                System.out.print((int) packet.data[i] + ",");
                try {
                    _serial.write(packet.data[i]);
                } catch (Exception e) {
                    System.out.print("Exception Caught: " + e);
                    stop();
                    _homeNet.removePort(getId());
                    return;
                }

            }
            System.out.println("");
            packet.setStatus(STATUS_SENT);
        }

        @Override
        public void stop() {
            _serial.end();
            System.out.println("stopping port");
        }

        @Override
        public void receive() {

            while (_serial.available() > 0) {
                int byteIn;
                try {
                    byteIn = _serial.read();
                } catch (Exception e) {
                    System.out.println("caught exception 2");
                    stop();
                    _homeNet.removePort(getId());
                    return;
                }

                if (_ptr == 0) { //new byte in


                    if (byteIn <= 2) { //resend last
                        if (_sending == true) {
                            _sendingPacket.setStatus(STATUS_SENDING);
                        }
                        return;
                    }
                    if (byteIn == 255) { //aknowledge
                        if (_sending == true) {
                            _sendingPacket.setStatus(STATUS_SUCCESS);
                            _sending = false; //allow next packet to send
                            _ptr = 0;
                        }
                        return;
                    }

                    if ((byteIn < 10) || (byteIn > 66)) { //bad byte, send 0 to start over
                        _serial.flush();
                        //   try { 
                        _serial.write(0);
                        //} catch (Exception e){ }
                        return;
                    }


                    _receiving = true;
                    _receivingTimer = System.currentTimeMillis();
                    _receivingPacket = _homeNet._getNewPacket();
                    System.out.print("Receiving: ");
                    _receivingPacket.setFromPort(_id);
                    _receivingPacket.received = new Date();
                    _receivingPacket.setLength(byteIn);
                    _receivingPacket.setStatus(STATUS_RECEVING);
                    _receivingChecksum = 0;
                }
                System.out.print(byteIn + ",");
                buildPacket(byteIn);
            }

            //process_stack();
            //check to see if Packet timed out
            if ((_receiving == true) && ((System.currentTimeMillis() - _receivingTimer) > PACKET_TIMEOUT)) {
                System.out.println("Recieving Packet Timed Out");
                _receivingPacket.setStatus(STATUS_CLEAR);
                _receiving = false;

                //Serial.flush();
                _serial.write(0); //tell the node to resend last
            }
            //since this loops every cycle
            //might be better to move this to it's own function so it clearer
            //check for packets that are taking too long to veryify that they sent


            if ((_sending == true) && ((System.currentTimeMillis() - _sendingTimer) > PACKET_TIMEOUT)) {
                System.out.println("Sending Packet Timed Out");
                _sendingRetryCount++;
                if (_sendingRetryCount <= PACKET_RETRY) {
                    _sendingPacket.setStatus(STATUS_SENDING);

                } else {
                    _sendingPacket.setStatus(STATUS_FAILED);
                    _sending = false;
                }
            }
        }

        void buildPacket(int byteIn) {

            if (_ptr < (_receivingPacket.getLength() - OFFSET_FOOTER)) { //add to check sum
                _receivingPacket.data[_ptr] = (byte) byteIn;
                _receivingChecksum = _crc16_update(_receivingChecksum, byteIn);
                _ptr++;
                return;
            } else if (_ptr == (_receivingPacket.getLength() - OFFSET_FOOTER)) { //get first byte of checksum
                _receivingPacket.data[_ptr] = (byte) byteIn;
                _ptr++;
                return;
            } else { //get second byte and check the checksum
                int checksum = _receivingPacket.data[_ptr - 1] & 0xFF;
                _receivingPacket.data[_ptr] = (byte) byteIn;
                checksum = (checksum << 8) | (byteIn & 0xFF);
                System.out.println("");
                if (_receivingChecksum != checksum) {
                    //packet failed crc check
                    _receivingPacket.setStatus(STATUS_CLEAR);
                    _serial.flush();
                    _serial.write(0); //tell the node to resend last
                    System.out.println("Receive failed Checksum " + _receivingChecksum + " " + checksum);
                } else {

                    _receivingPacket.setStatus(STATUS_RECEIVED);
                    //Serial.flush();
                    _serial.write(255); //tell the node the packet was successful
                    System.out.println("Receive Passed Checksum " + _receivingChecksum + " " + checksum);
                }
                //reset incomingPacket
            }
            //*/
            _receiving = false;
            _ptr = 0;
        }
    }
    //http://www.dynamicobjects.com/d2r/archives/003057.html
    public static SimpleDateFormat ISO8601FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    public static SimpleDateFormat RFC822DATEFORMAT = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
    public static SimpleDateFormat msgDateTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);

    public static String getDateAsRFC822String(Date date) {
        return RFC822DATEFORMAT.format(date);
    }

    public static String getmsgDateTimeString(Date date) {
        return msgDateTimeFormat.format(date);
    }

    public static String getDateAsISO8601String(Date date) {
        String result = ISO8601FORMAT.format(date);
        //convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
        //- note the added colon for the Timezone
        result = result.substring(0, result.length() - 2)
                + ":" + result.substring(result.length() - 2);
        return result;
    }

    class PortXmlrpc extends Port {

//        XmlrpcClient _client;
//        int _node;
//
//        PortXmlrpc(HomeNet homeNet, XmlrpcClient c, int n) {
//            super(homeNet);
//            _client = c;
//            _node = n;
//
//
//        }
//
        PortXmlrpc(HomeNet.Stack homeNet /*, XmlrpcClient c*/) {
            super(homeNet);
//            _client = c;
//            _node = 0;
//
//
        }
//

        @Override
        public void init(String id) {
//            _id = id;
//            started = true;
        }
//

        @Override
        public void send(Packet packet) {
//
//            if ((_node == 0) || (_node == packet.getToNode())) {
//
//                Hashtable xmlpacket = new Hashtable();
//                System.out.println(new String(Base64.encodeBase64(packet.getData())));
//                String packetBase64 = new String(Base64.encodeBase64(packet.getData()));
//                xmlpacket.put("timestamp", getDateAsISO8601String(packet.getTimestamp()));
//                xmlpacket.put("packet", packetBase64);
//                //System.out.println("DAte: "+packet.getTimestamp().toString());
//                String reply = "";
//
//                try {
//                    reply = (String) homeNetXmlrpcClient.execute("HomeNet.packet", xmlpacket);
//                    //reply = (String)homeNetXmlrpcClient.execute("HomeNet.ping", "test test3242342");
//                } catch (Exception e) {
//                    System.out.println("XMLRPC Error: " + e);
//                }
//
//                if (reply.equals("true")) {
//                    msg("Packet Successfuly sent to HomeNet.me");
//                } else if (!reply.equals(null)) {
//                    msg("HomeNet.me Error- " + reply);
//                } else {
//                    System.out.println("Unknown Error");
//                }
//
//            } else {
//                System.out.println("Packet Skipped");
//            }
//
//            debugPacket(packet);
//
//            packet.setStatus(STATUS_SENT);
//            _sending = false;
//        }
//
//        void stop() {
        }
//

        public void receive(Packet packet) {
//
//            //Packet receivingPacket = _homeNet._getNewPacket();
//            //receivingPacket = packet;
//            print("Receiving XMLRPC: ");
//            packet.setFromPort(_id);
//            packet.received = new Date();
//            packet.setStatus(STATUS_RECEIVED);
//            _homeNet.addPacket(packet);
//            System.out.println("added XMLRPC packet to stack");
//            _receiving = false;
        }
//

        @Override
        public void receive() {
        }
    }

    int _crc16_update(int crc, int a) {
        crc ^= a;
        for (int i = 0; i < 8; ++i) {
            if ((crc & 1) == 1) {
                crc = (crc >> 1) ^ 0xA001;
            } else {
                crc = (crc >> 1);
            }
        }
        return crc & 0xFFFF;
    }

//based on SimpleThread
    class HomeNetProcessThread extends Thread {

        boolean running;           // Is the thread running?  Yes or no?
        int wait;                  // How many milliseconds should we wait in between executions?
        String id;    // Thread name
        HomeNet.Stack _homeNet;
        int count;

        // Constructor, create the thread
        // It is not running by default
        HomeNetProcessThread(HomeNet.Stack h, int w) {
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
}
