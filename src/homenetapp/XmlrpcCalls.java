/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homenetapp;

import homenet.Packet;
import homenet.PortXmlrpc;
import org.apache.commons.codec.binary.Base64;
import java.util.*;

/**
 *
 * @author mdoll
 */
public class XmlrpcCalls {
    
    HomeNetApp app;
    
    public XmlrpcCalls(){
    }
    
    public XmlrpcCalls(HomeNetApp app){
        this.app = app;
    }
    
    public String testConnection(String value) {
        //  apikeyMsg("Remote Command");
        return "true";
    }

    public int getNodeModel() {

        return 1;
    }

public int getNodeId(){

     return app.homenet.getNodeId();
}

public String getApikey(){

     return app.config.getString("setting.apikey");
}

public String packet(Hashtable value){
  return send(value);
}

public String send(Hashtable value){
     //apikeyMsg("Remote Command");
     
     
     
     String p = (String)value.get("packet");
    // println(p);
     byte[] b = Base64.decodeBase64(p.getBytes());

 // println("Server Reply: "+homeNetXmlrpcClient.execute("HomeNet.addPacket", send)); */
     
     Packet packet = new Packet(b);
     
    // debugPacket(packet);
     try {
        ((PortXmlrpc)app.homenet.getPort("Xmlrpc")).receive(packet);
     } catch (Exception e){
       e.printStackTrace();
      //   return e.toString();
     }
     
     
     
     return "true";
}
}
