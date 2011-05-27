package homenet;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author mdoll
 */
//http://www.dynamicobjects.com/d2r/archives/003057.html
public class PortXmlrpc extends Port {

    XmlrpcClient _client;
    int _node;

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
        _id = id;
        started = true;
    }

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
