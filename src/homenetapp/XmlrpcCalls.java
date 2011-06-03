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

public String packet(java.util.Map value){
  return send(value);
}

public String send(java.util.Map value){
     //apikeyMsg("Remote Command");
     
     
     
     String p = (String)value.get("packet");
    // println(p);
     byte[] b = Base64.decodeBase64(p.getBytes());

 // println("Server Reply: "+homeNetXmlrpcClient.execute("HomeNet.addPacket", send)); */
     
     Packet packet = new Packet(b);
     
    // debugPacket(packet);
     try {
        ((PortXmlrpc)app.homenet.getPort("xmlrpc")).receive(packet);
     } catch (Exception e){
       e.printStackTrace();
      //   return e.toString();
     }
     
     
     
     return "true";
}
}
