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

import static homenet.Packet.*;
import java.util.*;
/**
 *
 * @author mdoll
 */
public class PortSerial extends Port {
    
    final static int SERIAL_SPEED = 115200;

    private int _ptr;
    private Serial _serial;
    private int _receivingChecksum;
    private Packet _sendingPacket;
    private Packet _receivingPacket;
    private int _sendingRetryCount;
    private long _receivingTimer;
    private long _sendingTimer;
//

    public PortSerial(Stack homeNet, Serial s) {
        super(homeNet);
        _serial = s;
    }

    @Override
    public void init(String id) {
        
        System.out.println("Init Port: "+id);
        
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
        for(PortListener l : _homeNet._portListeners){
            l.portSendingStart(_id);
        }
        for (int i = 0; i < packet.getLength(); i++) {
            System.out.print((int) packet.data[i] + ",");
            try {
                _serial.write(packet.data[i]);
            } catch (Exception e) {
                e.printStackTrace();
                stop();
                _homeNet.removePort(getId());
                packet.setStatus(STATUS_FAILED);
                for(PortListener l : _homeNet._portListeners){
            l.portSendingEnd(_id);
        }
                return;
            }
        }
        System.out.println("");
        packet.setStatus(STATUS_SENT);
         for(PortListener l : _homeNet._portListeners){
            l.portSendingEnd(_id);
        }
    }

    @Override
    public void stop() {
        _serial.end();
        System.out.println("stopping port");
    }
    
    private void write(int data) {
        try {
            _serial.write(data);
        } catch (Exception e) {
            System.out.print("Exception Caught: " + e);
            stop();
            _homeNet.removePort(getId());
            return;
        }
    }

    @Override
    public void receive() {

        while (_serial.available() > 0) {
           // System.out.println("Receiving: "+_serial.available());
            int byteIn;
            try {
                byteIn = _serial.read();
            } catch (Exception e) {
                System.out.println("caught exception 2");
                e.printStackTrace();
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
                    
                    write(0);
                   
                    return;
                }

 for(PortListener l : _homeNet._portListeners){
            l.portReceivingStart(_id);
        }
                _receiving = true;
                _receivingTimer = System.currentTimeMillis();
                _receivingPacket = _homeNet.getNewPacket();
                System.out.print("Receiving: ");
                _receivingPacket.setFromPort(_id);
                _receivingPacket.received = new Date();
                _receivingPacket.setLength(byteIn);
                _receivingPacket.setStatus(STATUS_RECEIVING);
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
            for(PortListener l : _homeNet._portListeners){
            l.portReceivingEnd(_id);
        }
            //Serial.flush();
            write(0); //tell the node to resend last
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
            _receivingChecksum = crc16_update(_receivingChecksum, byteIn);
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
                write(0); //tell the node to resend last
                System.out.println("Receive failed Checksum " + _receivingChecksum + " " + checksum);
            } else {

                _receivingPacket.setStatus(STATUS_RECEIVED);
                //Serial.flush();
                write(255); //tell the node the packet was successful
                System.out.println("Receive Passed Checksum " + _receivingChecksum + " " + checksum);
            }
            //reset incomingPacket
        }
        //*/
        for(PortListener l : _homeNet._portListeners){
            l.portReceivingEnd(_id);
        }
        _receiving = false;
        _ptr = 0;
    }
}
