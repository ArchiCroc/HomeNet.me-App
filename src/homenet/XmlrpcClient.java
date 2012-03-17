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



/*
XmlrpcClient - Basic xmlrpc client implementation

Copyright (c) 2006 Burak Arikan

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General
Public License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330,
Boston, MA  02111-1307  USA
 */
// TODO: try to simplify primitive type handling (int, float, double, boolean)
// TODO: executeAsync(method, param, callback)
package homenet;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XmlrpcClient {

    XmlRpcClient client;
    XmlRpcClientConfigImpl config;
    String apikey;

    public XmlrpcClient(String address, String key) throws MalformedURLException {
            config = new XmlRpcClientConfigImpl();
            apikey = key;
            //XmlrpcClient("http://homenet.me/xmlrpc.php?apikey="+homenetApikey);
            //System.out.println("Start client for"+"http://"+address+"/xmlrpc.php?apikey="+key);
            System.out.println("Start client for"+"http://"+address+"/xmlrpc.php");
            //config.setServerURL(new URL("http://"+address+"/xmlrpc.php?apikey="+key));
            config.setServerURL(new URL("http://localhost/xmlrpc.php"));
            client = new XmlRpcClient();
            client.setConfig(config);
    }

    /**
     *	The core execution method.
     */
    public Object execute(String method, Object parameter) throws Exception  {
        Vector<Object> params = new Vector<Object>();
        params.addElement(parameter);

            Object result = client.execute(method, params);
            return result;
        
    }

    /**
     *	Execute an array of Objects.
     */
    public Object execute(String method, Object[] parameters) throws Exception {
        Vector<Object> params = new Vector<Object>();
        for (int i = 0; i < parameters.length; i++) {
            params.addElement(parameters[i]);
        }
        try {
            Object result = client.execute(method, params);
            return result;
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } 
        return null;
    }

    /**
     *	Thre rest is overwritten to have a simple interface.
     */
    public Object execute(String method, int parameter) throws Exception  {
        return execute(method, new Integer(parameter));
    }

    public Object execute(String method, int[] parameters) throws Exception  {
        Integer[] params = new Integer[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            params[i] = new Integer(parameters[i]);
        }
        return execute(method, params);
    }

    public Object execute(String method, float parameter) throws Exception  {
        return execute(method, new Float(parameter));
    }

    public Object execute(String method, float[] parameters) throws Exception  {
        Float[] params = new Float[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            params[i] = new Float(parameters[i]);
        }
        return execute(method, params);
    }

    public Object execute(String method, double parameter) throws Exception  {
        return execute(method, new Double(parameter));
    }

    public Object execute(String method, double[] parameters) throws Exception  {
        Double[] params = new Double[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            params[i] = new Double(parameters[i]);
        }
        return execute(method, params);
    }

    public Object execute(String method, boolean parameter) throws Exception  {
        return execute(method, new Boolean(parameter));
    }

    public Object execute(String method, boolean[] parameters) throws Exception  {
        Boolean[] params = new Boolean[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            params[i] = new Boolean(parameters[i]);
        }
        return execute(method, params);
    }

    public Object execute(String method, byte[] parameters) throws Exception  {
        Vector<Object> params = new Vector<Object>();
        params.addElement(parameters);

            Object result = client.execute(method, params);
            return result;

    }

    /**
     *	Returns the IP of the xmlrpc server that we re connected to.
     */
    public String address() {
        URL url = config.getServerURL();
        return url.toString();
    }
}
