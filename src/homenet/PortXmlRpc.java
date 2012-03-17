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
import java.text.SimpleDateFormat;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author mdoll
 */
//http://www.dynamicobjects.com/d2r/archives/003057.html
public class PortXmlrpc extends Port {

    XmlrpcClient _client;
    int _node;
    long _timer;
    final int retryDelay = 30; //in seconds

    public PortXmlrpc(Stack homeNet, XmlrpcClient c, int n) {
        
        super(homeNet);
        _client = c;
        _node = n;
    }

    public PortXmlrpc(Stack homeNet, XmlrpcClient c) {
        super(homeNet);
        _client = c;
        _node = 0;
    }

    @Override
    public void init(String id) {
        
        System.out.println("Init Port: "+id);
        
        _id = id;
        started = true;
       
    }

    @Override
    public void send(Packet packet) {
        _sending = true;
        if ((_node == 0) || (_node == packet.getToNode())) {
            for (PortListener l : _homeNet._portListeners) {
                l.portSendingStart(_id);
            }
            Hashtable<String, String> xmlpacket = new Hashtable<String, String>();
            System.out.println(new String(Base64.encodeBase64(packet.getData())));
            String packetBase64 = new String(Base64.encodeBase64(packet.getData()));
            xmlpacket.put("apikey", _client.apikey);
            xmlpacket.put("timestamp", getDateAsISO8601String(packet.getTimestamp()));
            xmlpacket.put("packet", packetBase64);
            //System.out.println("DAte: "+packet.getTimestamp().toString());
            Boolean reply = false;

            try {
                reply = (Boolean) _client.execute("homenet.packet.submit", xmlpacket);
                //reply = (String)homeNetXmlrpcClient.execute("HomeNet.ping", "test test3242342");
            } catch (Exception e) {
                //@todo there are probably some specfic exception we need to filter out to kill bad packets
                System.out.println("XMLRPC Error: " + e);
                packet.setStatus(STATUS_READY);
                System.err.println("Possible network error. Will retry in "+retryDelay+" seconds");
                Thread timer = new Thread(){
                    public void run(){
                        try { Thread.sleep(retryDelay*1000); } catch(Exception e){}
                        _sending = false;
                    }
                };
                timer.start();
                for (PortListener l : _homeNet._portListeners) {
                    l.portSendingEnd(_id);
                }
                return;
            }

            if (reply == true) {
                System.out.println("Packet Successfuly sent to HomeNet.me");
            } else {
                System.out.println("Fatal Error");
            }
            

        } else {
            System.out.println("Packet Skipped");
        }

        // debugPacket(packet);

        packet.setStatus(STATUS_SENT);
        _sending = false;
        for (PortListener l : _homeNet._portListeners) {
            l.portSendingEnd(_id);
        }
    }

        public void stop() {
    }
//

    public void receive(Packet packet) {
            for(PortListener l : _homeNet._portListeners){
            l.portReceivingStart(_id);
        }
            //Packet receivingPacket = _homeNet._getNewPacket();
            //receivingPacket = packet;
            System.out.println("Receiving XMLRPC: ");
            packet.setFromPort(_id);
            packet.received = new Date();
            packet.setStatus(STATUS_RECEIVED);
            _homeNet.addPacket(packet);
            System.out.println("added XMLRPC packet to stack");
            _receiving = false;
            for(PortListener l : _homeNet._portListeners){
            l.portReceivingEnd(_id);
        }
    }
//

    @Override
    public void receive() {
    }
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
}
