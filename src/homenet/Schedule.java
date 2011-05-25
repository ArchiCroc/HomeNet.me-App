/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homenet;

/**
 *
 * @author mdoll
 */
public class Schedule {

    int delay; //seconds
    int frequency;    //seconds
    Device device;
    int toNode;
    int toDevice;
    int command;
    Payload payload;

    Schedule(int d, int f, Device e, int t, int o, int c, Payload p) {
        delay = d;
        frequency = f;
        device = e;
        toNode = t;
        toDevice = o;
        command = c;
        payload = p;
    }
}
