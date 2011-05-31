/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homenet;

import java.util.*;

/**
 *
 * @author mdoll
 */
public abstract class Port {

    protected Stack _homeNet;
    protected String _id;
    protected boolean _sending;// = false;
    protected boolean _receiving;// = false;
    boolean started = false;
    long timeAdded;

    Queue<Packet> sendQueue = new java.util.concurrent.ConcurrentLinkedQueue();
    
    Port(Stack homeNet) {
        _homeNet = homeNet;
        timeAdded = System.currentTimeMillis(); //might need to offset or use java datetime
    }

    public abstract void init(String id);

    public abstract void send(Packet packet);

    public abstract void receive();

    public void stop() {
    }

    ;
    public boolean isSending() {
        return _sending;
    }

    public boolean isReceiving() {
        return _receiving;
    }

    public String getId() {
        return _id;
    }
};
