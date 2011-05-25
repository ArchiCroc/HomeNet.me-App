/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
