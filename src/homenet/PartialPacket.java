/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homenet;

/**
 *
 * @author mdoll
 */
public class PartialPacket {
    int fromDevice;
    int command;
    Payload payload;

    PartialPacket(int f, int c, Payload p) {
        fromDevice = f;
        command = c;
        payload = p;
    }
}
