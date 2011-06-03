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

/**
 *
 * @author mdoll
 */
public abstract class Device {

    protected homenet.Stack _homeNet;
    protected int _location;

    public Device(homenet.Stack homeNet) {
        _homeNet = homeNet;
    }

    public abstract void getId();

    public void init(int location) {
        _location = location;
    }

    public void update() {
    }

    public void updateOften() {
    }

    public PartialPacket process(int fromNode, int fromDevice, int command, Payload payload) {
        return process(command, payload);
    }

    public PartialPacket process(int command, Payload payload) {
        return process(command);
    }

    public PartialPacket process(int command) {
        return null;
    }

    public PartialPacket interrupt(int command, Payload payload) {
        return process(0, 0, command, payload);
    }

    public PartialPacket schedule(int command, Payload payload) {
        return process(0, 0, command, payload);
    }

    public int getLocation() {
        return _location;
    }
}
