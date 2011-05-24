/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homenetappgui;

import gnu.io.*;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 *
 * @author mdoll
 */
public class Serial {
    
    public Serial()
    {
        super();
    }
    
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                               
                (new Thread(new SerialWriter(out))).start();
                
                serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    /**
     * Handles the input coming from the serial port. A new line character
     * is treated as the end of a block in this example. 
     */
    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0) {
            int data;
          
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                System.out.print(new String(buffer,0,len));
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }             
        }

    }

    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {                
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }            
        }
    }



    static ArrayList listPorts()
    {
        ArrayList ports = new ArrayList<String>();
        
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while ( portEnum.hasMoreElements() ) 
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            if(portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL){
                ports.add(portIdentifier.getName());
                //System.out.println(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()) );
            }
        }    
        
        return ports;
    }
    
    static String getPortTypeName ( int portType )
    {
        switch ( portType )
        {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }
}
/*

void checkSerialPortInit(){
  
  checkSerialPorts();
  drawSerialGUI();
  loadSerialPorts();
  
  sThread = new SerialCheckThread(1000);
  sThread.setPriority(3);
  sThread.start();
}


void checkSerialPorts(){
    
  boolean redrawGUI = false;
  HashSet currentList = buildSerialPortList();
  
  //check for new ports
  Iterator i = currentList.iterator();
  while (i.hasNext()) {
      String k = (String)i.next();

      if(!portList.contains(k)){
         portList.add(k);
         redrawGUI = true;
      }
  }
  
  //check for removed ports
  i = portList.iterator();
  while (i.hasNext()) {
    boolean found = false;
    String k = (String)i.next();
    
    if(currentList.contains(k)) {
      found = true; 
    }
    
    if(found == false){
      //remove from portlist too;
      println("auto remove "+k);
      if(ports.containsKey(k)){
        ports.get(k).stop();
        ports.remove(k);
      }
      
      portList.remove(k);
      redrawGUI = true;
      msg("Port "+k+" was Disconnected");
    }
  }
  if(redrawGUI == true){
    println("Checking Serial Ports");
    drawSerialGUI();
  }

}

void loadSerialPorts(){
  String[] lines = loadStrings(sketchPath("ports.txt"));
  
  HashSet currentList = buildSerialPortList();
  
  for(int i = 0; i<lines.length;i++){
    if(currentList.contains(lines[i]) == true) {
        serialCheckboxes.activate(lines[i]);
      //  Serial myPort = new HomeNetSerialPort(this,  lines[i], 115200);
      //  ports.put(lines[i],new HomeNetPortSerial(stack, myPort));
    } 
  }
  println("Serial Ports Loaded");
}

public static boolean in_array(String[] haystack, String needle) {
    for(int i=0;i<haystack.length;i++) {
        if(haystack[i].equals(needle)) {
            return true;
        }
    }
    return false;
}



void addSerialPort(String portName){
  
  serialCheckboxes.addItem(portName,1);
  //serialCheckboxes.removeItem(portName);
}


void removeSerialPort(String portName){
  
  println("removing item");
  serialCheckboxes.removeItem(portName);
  
}
boolean guiRunOnce = false;

void drawSerialGUI(){
  
 controlP5.remove("label");
 controlP5.remove("checkBox");

 guiRunOnce = true;
  //controlP5 = new ControlP5(this);
  int count = Serial.list().length;
//  serialLabel
  if(count == 0){
      serialLabel = controlP5.addTextlabel("label","No Serial Ports Detected",10,20);
  } else if(count == 1) {
      serialLabel = controlP5.addTextlabel("label","Serial Port Detected",10,20);   
  } else {
     serialLabel = controlP5.addTextlabel("label", count+" Serial Ports Detected",10,20);
  }
  serialLabel.setFont(ControlP5.standard56);
  
  //draw check boxes
  serialCheckboxes = controlP5.addCheckBox("checkBox",20,40);  
  //make adjustments to the layout of a checkbox.
  serialCheckboxes.setColorForeground(color(120));
  serialCheckboxes.setColorActive(color(255));
  serialCheckboxes.setColorLabel(color(128));
  serialCheckboxes.setItemsPerRow(3);
  serialCheckboxes.setSpacingColumn(50);
  serialCheckboxes.setSpacingRow(10);
  serialCheckboxes.setId(1);
  
  synchronized (portList){
    Iterator i = portList.iterator();

    while (i.hasNext()) {
      String k = (String)i.next();
      serialCheckboxes.addItem(k,1);
    }
  }
 // println(portList);
  println("Redrawing GUI");
  //controlP5.save("test.txt");
}

/*
void controlEvent(ControlEvent theEvent) {

  if(theEvent.isGroup()) {
    //myColorBackground = 0;
    print("got an event from "+theEvent.group().name()+"\t");
    // checkbox uses arrayValue to store the state of 
    // individual checkbox-items. usage:

  }
}*/
/*
void processSerialCheckBoxes(){
  synchronized (portList){ //keeps this from stepping on the sThreads toes
   
   float boxArray[] = serialCheckboxes.arrayValue();
   Object currentList[] = portList.toArray();
    
    for(int i  =0; i < boxArray.length; i++) {
       float v = boxArray[i];
       String k = (String)currentList[i];
       
       //add port
       if(!ports.containsKey(k) && (v > 0)){
          Serial myPort;
          try {
            myPort = new HomeNetSerialPort(this,  k, 115200);
          } catch (Exception e) {
            serialCheckboxes.deactivate(k);
            msg("Failed to add serial port "+k);
            return;
          }
          
          ports.put(k,new HomeNetPortSerial(stack, myPort));
          msg("Serial port "+k+" was successfully added");
          
  
      } else if(ports.containsKey(k) && (v == 0)) {
          msg("Removing Port "+k+". Please Wait");
          ports.get(k).stop();
          delay(100);
          ports.remove(k);
          msg("Serial port "+k+" was successfully removed");
      }
  
    }
    println("Processed Serial Checkboxes"); 
  }
}

void saveSerialPorts(){
  if(ports.size() > 0){
    String[] savePorts = new String[ports.size()];
    Iterator i = ports.keySet().iterator();
    int c = 0;
    while (i.hasNext()) {
      savePorts[c] = (String)i.next();
      c++;
    }   
    saveStrings(sketchPath("ports.txt"), savePorts);
  } else {
    String[] savePorts = new String[1];
    saveStrings(sketchPath("ports.txt"), savePorts);
  }
  println("Saved Serial Ports");
}





//based on SimpleThread
class SerialCheckThread extends Thread {

  boolean running;           // Is the thread running?  Yes or no?
  int wait;                  // How many milliseconds should we wait in between executions?
  String id;                 // Thread name
  int count;                 // counter
  boolean check;

  // Constructor, create the thread
  // It is not running by default
  SerialCheckThread (int w) {
    wait = w;
    running = false;
    check = true;
  }
  
  // Overriding "start()"
  void start () {
    // Set running equal to true
    running = true;
    
    check = true;
    // Print messages
    println("Starting Serial Port Check Thread (will execute every " + wait + " milliseconds.)"); 
    // Do whatever start does in Thread, don't forget this!
    super.start();
  }
  
  void startChecking(){
    check = true;
  }
  void stopChecking(){
    check = false;
  }

  // We must implement run, this gets triggered by start()
  void run () {
    while (running){
      if(check == true){
       checkSerialPorts();
      }
      // Ok, let's wait for however long we should wait
      try {
        Thread.sleep((long)(wait));
      } catch (Exception e) {
      }
    }
    System.out.println(id + " thread is done!");  // The thread is done when we get to the end of run()
  }


  // Our method that quits the thread
  void quit() {
    System.out.println("Quitting."); 
    running = false;  // Setting running to false ends the loop in run()
    // IUn case the thread is waiting. . .
    interrupt();
  }
}

*/