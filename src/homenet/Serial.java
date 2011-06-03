/*
 * Copyright (c) 2011 Matthew Doll <mdoll at homenet.me>.
 *
 * This file is part of HomeNet.
 *
 * HomeNet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HomeNet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HomeNet.  If not, see <http://www.gnu.org/licenses/>.
 */
package homenet;

/**
 *
 * @author mdoll
 */
import gnu.io.*;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author mdoll
 * @see proccessing serial lib
 */
public class Serial {

    public String port;
    SerialPort serialPort;
    InputStream input;
    OutputStream output;
   // byte buffer[] = new byte[1024];
   // int bufferIndex = 0;
  //  int bufferLast = 0;

    public Serial(String port) {
        super();
        this.port = port;
    }

    public boolean begin(int speed) {
        
        System.out.println("Beginning Serial Port "+port+" @ "+speed);
        
         try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(this.port);
            if (portIdentifier.isCurrentlyOwned()) {
                //System.out.println("Error: Port is currently in use");
                throw new Exception("Error: Port is currently in use");
            } else {
                CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

                if (commPort instanceof SerialPort) {
                    serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(speed, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                    input = serialPort.getInputStream();
                    System.out.println("InputStream type: "+input.getClass().getName());
                    output = serialPort.getOutputStream();
                } else {
                    throw new Exception("Only Supports Serial Ports");
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(e.getMessage());
            //exception = e;
            //e.printStackTrace();
            serialPort = null;
            input = null;
            output = null;
            return false;
        }
         return true;
    }
    
    public boolean begin() {
       return begin(115200);
    }

    public void end() {
        try {
            // do io streams need to be closed first?
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        input = null;
        output = null;

        try {
            if (serialPort != null) {
                serialPort.close();  // close the port
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        serialPort = null;

    }

    public void flush() {
        try {
            input.skip(input.available()); 
        } catch (IOException e){ }
    }

    public int read() throws Exception {
      //  System.out.println("Serial: Reading Data");
      //  byte[] buffer = new byte[1]; 
        try{
       //     if( (input.read(buffer,0,1)) > -1){
                
                return input.read();
          //      return buffer[0];
         //   } 
        } catch (IOException e){
            return -1;
        }
        
      //  return -1;
    }

    public int available() {
        try {
            return input.available();
        } catch (Exception e){
            return -1;
        }
        
      //  return (bufferLast - bufferIndex);
    }

    public void print() {
    }

    /**
     * This will handle both ints, bytes and chars transparently.
     */
    public void write(int what) throws Exception {  // will also cover char
      //  System.out.println("Serial: Writing Data");
        if(output == null){
            throw new Exception("Output stream not Setup");
        }
        
            output.write(what & 0xff);  // for good measure do the &
            output.flush();   // hmm, not sure if a good idea
    }

    public void write(byte bytes[]) throws Exception {
            output.write(bytes);
            output.flush();   // hmm, not sure if a good idea
    }

    public static synchronized  ArrayList listPorts() {
        //System.out.println("Generating Serial Port List");
        ArrayList ports = new ArrayList<String>();

        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                ports.add(portIdentifier.getName());
              //  System.out.println(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()) );
            }
        }

        return ports;
    }

    public static String getPortTypeName(int portType) {
        switch (portType) {
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