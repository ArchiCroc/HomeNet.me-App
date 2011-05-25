/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homenet;

/**
 *
 * @author mdoll
 */
public class Interrupt {
    
    

    Device device;
    int toNode;
    int toDevice;
    int command;
    Payload payload;

    Interrupt(Device e, int t, int o, int c, Payload p) {
        device = e;
        toNode = t;
        toDevice = o;
        command = c;
        payload = p;
    }
}
