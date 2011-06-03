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
