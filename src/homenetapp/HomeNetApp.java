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

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.text.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.zip.*;
import java.lang.Throwable;
import homenet.*;

//import org.apache.xmlrpc.XmlRpcClient;


import org.apache.commons.configuration.*;

/**
 *
 * @author mdoll
 */
public class HomeNetApp {

    public HashMap<Integer, String[]> commands;
    public PropertiesConfiguration config = null; 
    public homenet.Stack homenet;
    public SerialManager serialmanager;
    private XmlrpcClient _xmlrpcClient = null;
    private XmlrpcServer _xmlrpcServer = null;
    private UPnP upnp = new UPnP();
    //core settings
    
    final public String version = "0.0.1";
    
    public int nodeId = 0xff;
    
    public String clientServer = "homenet.me";
    public String clientApiKey = "";
    
    
    public boolean serverEnabled = true;
    public boolean serverUpnpEnabled = true;
    public int serverPort = 2443;
    public boolean configDone = false;

    List<String> portsSerial = new ArrayList();
    

    // public HashMap Por;
    public HomeNetApp() {
        loadConfig();
        loadCommands();
        
        homenet = new homenet.Stack(0xff);
        homenet.init();
    }

    public void start() throws Exception {

        

        // HashMap<String,Port> ports = new HashMap<String,Port>();
        //  HashMap<Integer,Device> devices = new HashMap<Integer,Device>();
        serialmanager = new SerialManager(homenet);
        
        //start client
         startClient();
         
        //start server
        if(serverEnabled == true){
          
            startServer();
           
            if(serverUpnpEnabled == true){
               startUpnp();
            }
        }

    }
    
    public void startServer() throws Exception {
        System.out.println("Starting XML RPC Server on port " + serverPort);
        _xmlrpcServer = new XmlrpcServer(serverPort);
        XmlrpcCalls calls = new XmlrpcCalls(this);
        _xmlrpcServer.add("HomeNet", calls);


    }
    
    public void stopServer(){
        System.out.println("Stopping XML RPC Server on port " + serverPort);
        if(_xmlrpcServer != null){
            _xmlrpcServer.stop();
            _xmlrpcServer = null;
        }
    }
    
    public void startClient() throws Exception{
        System.out.println("Starting XML RPC Client to " + clientServer );
        System.out.println("    with Api Key: " + clientApiKey );
        _xmlrpcClient = new XmlrpcClient(clientServer, clientApiKey);
        
        boolean reply = (Boolean)_xmlrpcClient.execute("homenet.apikey.validate", clientApiKey);
        
        if(reply == false){
            throw new Exception("Invalid API Key");
        }
        
       // boolean loadXmlrpc = true;
        homenet.addPort("xmlrpc", new PortXmlrpc(homenet,_xmlrpcClient));

    }
    
    public void stopClient(){
        System.out.println("Stopping XML RPC Client");
         homenet.removePort("xmlrpc");
        _xmlrpcClient = null;
    }
    
    public void startUpnp(){
        System.out.println("Starting UPnP port forwarding for port " + serverPort);
        Thread runUpnp = new Thread() {
              public void run(){
                  upnp = new UPnP();
                  upnp.forwardPort(serverPort);
              }  
            };
            runUpnp.start();
    }
    
    public void stopUpnp(){
        System.out.println("Stopping UPnP port forwarding for port " + upnp.port);
        upnp.exit();
    }
    
    
    

    private boolean loadConfig() {
        try {
            config = new PropertiesConfiguration("homenet.properties.txt");
             
        if(config.getString("client.server") != null){
             clientServer = config.getString("client.server");
        }
            
        //core settings
       
        clientApiKey = config.getString("client.apikey");
        serverEnabled = config.getBoolean("server.enabled");
        serverUpnpEnabled = config.getBoolean("server.upnp");
        serverPort = config.getInt("server.port");
        
        configDone = config.getBoolean("config.done");
        
        portsSerial = config.getList("ports.serial");
         } catch (Exception e) {
             config = new PropertiesConfiguration();
            return false;
        }  
        return true;
    }

    public void saveConfig() throws Exception {
        System.out.println("Saving Config");
        config.setProperty("client.server", clientServer);
        config.setProperty("client.apikey", clientApiKey);
        config.setProperty("server.enabled", serverEnabled);
        config.setProperty("server.upnp", serverUpnpEnabled);
        config.setProperty("server.port", serverPort);
        config.setProperty("config.done", configDone);

        config.save("homenet.properties.txt");
    }

    private void loadCommands() {
        
        getAppPath("commands.txt");
        
        commands = new HashMap<Integer, String[]>();

        String[] strings = loadStrings("commands.txt");

        for (int i = 0; i < strings.length; i++) {
            String[] r = splitTokens(strings[i], "\t");
            commands.put(Integer.parseInt(r[0], 16), r);
        }

    }

    class compareCommands implements Comparator {

        public int compare(Object i1, Object i2) {
            return ((Integer) i1).intValue() - ((Integer) i2).intValue();
        }
    }

    public Object[] getCommandKeys() {

        //System.out.println(commands.keySet().size());
        // System.out.println("Size: "+commands.size());
        
        

        Object[] rows = commands.keySet().toArray();
        Arrays.sort(rows, new compareCommands());
        System.out.println("Commands Loaded: " + rows.length);


        //Object[] rows = new Object[1]; 
        // Integer[] test =  {new Integer(2),new Integer(2),new Integer(2),new Integer(2)};

        // System.exit(-1);
        return rows;

    }

    public String getAppPath(String filename) {
        //@todo find the right path
        String path = HomeNetApp.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.replaceAll("%20", " ");
        path = path.substring(0,path.lastIndexOf("/"));
        
        System.err.println(path + "/"+ filename);
        return path +"/"+ filename;
        //return "C:\\Users\\mdoll\\Documents\\NetBeansProjects\\HomeNet.me-App\\" + filename;
        // return "C:\\Projects (Safe)\\HomeNet.me-App\\" + filename;
    }

    public void exit() {
        serialmanager.exit();
        try {
            saveConfig();
        } catch (Exception e) {
        }
        
    }

    class SerialManager {

        public List<String> portList = new CopyOnWriteArrayList();
        public List<String> selectedPorts = new CopyOnWriteArrayList();
        private SerialCheckThread sThread;
        private homenet.Stack _homeNet;
        private List<SerialListener> _listeners = new CopyOnWriteArrayList();

        public SerialManager(homenet.Stack stack) {

            _homeNet = stack;


        }

        public void start() throws Exception {
            //loadSerialPorts();
            checkSerialPorts();

           // List<String> list = config.getList("ports.serial");
            for (String s : portsSerial) {
                serialmanager.activatePort(s);
            }

            sThread = new SerialCheckThread(1000);
            sThread.setPriority(3);
            sThread.start();
        }

        public void exit() {
            config.setProperty("ports.serial", selectedPorts);
        }

        public void addListener(SerialListener l) {
            _listeners.add(l);
        }

        public boolean activatePort(String port) throws Exception {
            System.out.println("Activate Port: " + port);
            if (!portList.contains(port)) {
                return false;
            }


           //@throws Exception
           _homeNet.addPort(port, new PortSerial(_homeNet, new Serial(port)));
           
            if(!selectedPorts.contains(port)){
                selectedPorts.add(port);
            }
            

            for (SerialListener l : _listeners) {
                l.portActivated(port);
            }
            return true;
        }
         public void deactivatePort(String port){
             deactivatePort(port, true);
         }

        public void deactivatePort(String port, boolean remove) {
            System.out.println("Deactivate Port: " + port);
            //  if (currentPorts.containsKey(port)) {
            _homeNet.removePort(port);
            //     }
            if(remove == true){
                selectedPorts.remove(port);
            }

            for (SerialListener l : _listeners) {
                l.portDeactivated(port);
            }
        }

        public void checkSerialPorts() {

            // System.out.println("HomeNetApp.checkSerialPorts");
            List<String> currentList = Serial.listPorts();
            Map currentPorts = _homeNet.getPorts();

            //check for new ports
            for (String k : currentList) {
                if (!portList.contains(k)) {
                    portList.add(k);
                    System.out.println("New port found: " + k);
                    
                    //check to see if this one is on our favorites list
                    if(selectedPorts.contains(k))
                    {
                        final String l = k;
                        
                        System.out.println("Hey! port " + l+" is on the VIP list, will try and add it in 10 seconds");
                        //brand new serial ports are grumpy. this will delay adding it for 10 seconds
                        Thread delay = new Thread(){
                            public void run(){
                                try { Thread.sleep(10*1000); 
                                activatePort(l);
                                } catch(Exception e){}
                                
                            }
                        };
                                delay.start();
                    }
                    
                    //    System.out.println("List size: " + _listeners.size());
                    //process Listeners
                    for (SerialListener l : _listeners) {
                        l.portAdded(k);
                    }
                }
            }

            //check for removed ports 
            //loops throught the old list and compares it to the new one

            for (String k : portList) {
                if (!currentList.contains(k)) {
                    //remove from portlist too;
                    System.out.println("Port " + k + " was disconnected");
                    System.out.println("Auto remove " + k);

                    deactivatePort(k, false);
                    currentPorts.remove(k);

                    portList.remove(k);

                    //process Listeners
                    for (SerialListener l : _listeners) {
                        l.portRemoved(k);
                    }

                }
            }
        }

        public void loadSerialPorts() {
        }

        void saveSerialPorts() {
            config.setProperty("test", "test");
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
            SerialCheckThread(int w) {
                wait = w;
                running = false;
                check = true;
            }

            // Overriding "start()"
            public void start() {
                // Set running equal to true
                running = true;

                check = true;
                // Print messages
                System.out.println("Starting Serial Port Check Thread (will execute every " + wait + " milliseconds.)");
                // Do whatever start does in Thread, don't forget this!
                super.start();
            }

            void startChecking() {
                check = true;
            }

            void stopChecking() {
                check = false;
            }

            // We must implement run, this gets triggered by start()
            public void run() {
                while (running) {
                    if (check == true) {
                        checkSerialPorts();
                    }
                    // Ok, let's wait for however long we should wait
                    try {
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
//helper functions based on functions from processing.org
    static final String WHITESPACE = " \t\n\r\f\u00A0";

    /**
     * Call openStream() without automatic gzip decompression.
     */
    public InputStream createInputRaw(String filename) {
        InputStream stream = null;

        if (filename == null) {
            return null;
        }

        if (filename.length() == 0) {
            // an error will be called by the parent function
            //System.err.println("The filename passed to openStream() was empty.");
            return null;
        }

        // safe to check for this as a url first. this will prevent online
        // access logs from being spammed with GET /sketchfolder/http://blahblah
//        if (filename.indexOf(":") != -1) {  // at least smells like URL
//            try {
//                URL url = new URL(filename);
//                stream = url.openStream();
//                return stream;
//
//            } catch (MalformedURLException mfue) {
//                // not a url, that's fine
//            } catch (FileNotFoundException fnfe) {
//                // Java 1.5 likes to throw this when URL not available. (fix for 0119)
//                // http://dev.processing.org/bugs/show_bug.cgi?id=403
//            } catch (IOException e) {
//                // changed for 0117, shouldn't be throwing exception
//                e.printStackTrace();
//                //System.err.println("Error downloading from URL " + filename);
//                return null;
//                //throw new RuntimeException("Error downloading from URL " + filename);
//            }
//        }
//
//        // Moved this earlier than the getResourceAsStream() checks, because
//        // calling getResourceAsStream() on a directory lists its contents.
//        // http://dev.processing.org/bugs/show_bug.cgi?id=716
//        try {
//            // First see if it's in a data folder. This may fail by throwing
//            // a SecurityException. If so, this whole block will be skipped.
//            System.err.println("Load file");
//            File file = new File(getAppPath(filename));
//
//            if (file.isDirectory()) {
//                return null;
//            }
//            if (file.exists()) {
//                try {
//                    // handle case sensitivity check
//                    
//                    String filePath = file.getCanonicalPath();
//                    System.err.println("file path: Load file"+filePath);
//
//                    String filenameActual = new File(filePath).getName();
//                    // make sure there isn't a subfolder prepended to the name
//                    String filenameShort = new File(filename).getName();
//                    // if the actual filename is the same, but capitalized
//                    // differently, warn the user.
//                    //if (filenameActual.equalsIgnoreCase(filenameShort) &&
//                    //!filenameActual.equals(filenameShort)) {
//                    if (!filenameActual.equals(filenameShort)) {
//                        throw new RuntimeException("This file is named "
//                                + filenameActual + " not "
//                                + filename + ". Rename the file "
//                                + "or change your code.");
//                    }
//                } catch (IOException e) {
//                }
//            }
//
//            // if this file is ok, may as well just load it
//            stream = new FileInputStream(file);
//            if (stream != null) {
//                return stream;
//            }
//
//            // have to break these out because a general Exception might
//            // catch the RuntimeException being thrown above
//        } catch (IOException ioe) {
//            System.err.println("File IO Error");
//            ioe.printStackTrace();
//        } catch (SecurityException se) {
//            System.err.println("File Security Error");
//        }

        // Using getClassLoader() prevents java from converting dots
        // to slashes or requiring a slash at the beginning.
        // (a slash as a prefix means that it'll load from the root of
        // the jar, rather than trying to dig into the package location)
        ClassLoader cl = getClass().getClassLoader();
//
//        // by default, data files are exported to the root path of the jar.
//        // (not the data folder) so check there first.
//        stream = cl.getResourceAsStream(filename);
//        if (stream != null) {
//            String cn = stream.getClass().getName();
//            // this is an irritation of sun's java plug-in, which will return
//            // a non-null stream for an object that doesn't exist. like all good
//            // things, this is probably introduced in java 1.5. awesome!
//            // http://dev.processing.org/bugs/show_bug.cgi?id=359
//            if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
//                return stream;
//            }
//        }

        // When used with an online script, also need to check without the
        // data folder, in case it's not in a subfolder called 'data'.
        // http://dev.processing.org/bugs/show_bug.cgi?id=389
        stream = cl.getResourceAsStream(filename);
        if (stream != null) {
            String cn = stream.getClass().getName();
            if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
                return stream;
            }
        }

        try {
            // attempt to load from a local file, used when running as
            // an application, or as a signed applet
            try {  // first try to catch any security exceptions
                try {
                    stream = new FileInputStream(getAppPath(filename));
                    if (stream != null) {
                        return stream;
                    }
                } catch (IOException e2) {
                }



                try {
                    stream = new FileInputStream(filename);
                    if (stream != null) {
                        return stream;
                    }
                } catch (IOException e1) {
                }

            } catch (SecurityException se) {
            }  // online, whups

        } catch (Exception e) {
            //die(e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    public InputStream createInput(String filename) {
        InputStream input = createInputRaw(filename);
        if ((input != null) && filename.toLowerCase().endsWith(".gz")) {
            try {
                return new GZIPInputStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return input;
    }

    static public InputStream createInput(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File passed to createInput() was null");
        }
        try {
            InputStream input = new FileInputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                return new GZIPInputStream(input);
            }
            return input;

        } catch (IOException e) {
            System.err.println("Could not createInput() for " + file);
            e.printStackTrace();
            return null;
        }
    }

    public String[] loadStrings(String filename) {
        InputStream is = createInput(filename);
        if (is != null) {
            return loadStrings(is);
        }

        System.err.println("The file \"" + filename + "\" "
                + "is missing or inaccessible, make sure "
                + "the URL is valid or that the file has been "
                + "added to your sketch and is readable.");
        return null;
    }

    static public String[] loadStrings(InputStream input) {
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(input, "UTF-8"));

            String lines[] = new String[100];
            int lineCount = 0;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (lineCount == lines.length) {
                    String temp[] = new String[lineCount << 1];
                    System.arraycopy(lines, 0, temp, 0, lineCount);
                    lines = temp;
                }
                lines[lineCount++] = line;
            }
            reader.close();

            if (lineCount == lines.length) {
                return lines;
            }

            // resize array to appropriate amount for these lines
            String output[] = new String[lineCount];
            System.arraycopy(lines, 0, output, 0, lineCount);
            return output;

        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException("Error inside loadStrings()");
        }
        return null;
    }

    /**
     * Split the provided String at wherever whitespace occurs.
     * Multiple whitespace (extra spaces or tabs or whatever)
     * between items will count as a single break.
     * <P>
     * The whitespace characters are "\t\n\r\f", which are the defaults
     * for java.util.StringTokenizer, plus the unicode non-breaking space
     * character, which is found commonly on files created by or used
     * in conjunction with Mac OS X (character 160, or 0x00A0 in hex).
     * <PRE>
     * i.e. splitTokens("a b") -> { "a", "b" }
     *      splitTokens("a    b") -> { "a", "b" }
     *      splitTokens("a\tb") -> { "a", "b" }
     *      splitTokens("a \t  b  ") -> { "a", "b" }</PRE>
     */
    static public String[] splitTokens(String what) {
        return splitTokens(what, WHITESPACE);
    }

    /**
     * Splits a string into pieces, using any of the chars in the
     * String 'delim' as separator characters. For instance,
     * in addition to white space, you might want to treat commas
     * as a separator. The delimeter characters won't appear in
     * the returned String array.
     * <PRE>
     * i.e. splitTokens("a, b", " ,") -> { "a", "b" }
     * </PRE>
     * To include all the whitespace possibilities, use the variable
     * WHITESPACE, found in PConstants:
     * <PRE>
     * i.e. splitTokens("a   | b", WHITESPACE + "|");  ->  { "a", "b" }</PRE>
     */
    static public String[] splitTokens(String what, String delim) {
        StringTokenizer toker = new StringTokenizer(what, delim);
        String pieces[] = new String[toker.countTokens()];

        int index = 0;
        while (toker.hasMoreTokens()) {
            pieces[index++] = toker.nextToken();
        }
        return pieces;
    }
}
