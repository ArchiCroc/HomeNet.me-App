/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homenet;

/**
 *
 * @author mdoll
 */
public class XMLRPC {
    
    public String testConnection(String value) {
        //  apikeyMsg("Remote Command");
        return "true";
    }

    public int getNodeModel() {

        return 1;
    }

//public int getNodeId(){
//
//   //  return stack.getNodeId();
//}

//public String getApikey(){
//
//     return homenetApikey;
//}

//public String packet(Hashtable value){
//  return send(value);
//}

//public String send(Hashtable value){
//     apikeyMsg("Remote Command");
//     
//     
//     
//     String p = (String)value.get("packet");
//     println(p);
//     byte[] b = Base64.decodeBase64(p.getBytes());
//
// // println("Server Reply: "+homeNetXmlrpcClient.execute("HomeNet.addPacket", send)); */
//     
//     HomeNetPacket packet = new HomeNetPacket(b);
//     
//     debugPacket(packet);
//     try {
//    ((HomeNetPortXmlrpc)ports.get("Xmlrpc")).receive(packet);
//     } catch (Exception e){
//       e.printStackTrace();
//      //   return e.toString();
//     }
//     
//     
//     
//     return "true";
//}
}
