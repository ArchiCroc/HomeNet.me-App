/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homenet;

/**
 *
 * @author mdoll
 */
class Payload {

    int length = 0;
    byte[] data = new byte[56];

    Payload(final byte value) {
        length = 1;
        data[0] = value;
    }

    Payload(final byte[] value, final int size) {
        System.arraycopy(value, 0, data, 0, size);
        length = size;
    }

    Payload(final byte[] value) {
        this(value, value.length);
    }

    Payload(final int value) {
        length = 2;
        data[1] = (byte) ((value >> 8) & 0xFF);
        data[0] = (byte) (value & 0xFF);
    }

    Payload(final int[] value) {
        length = value.length * 2;
        for (int i = 0; i < value.length; i += 2) {
            data[i * 2] = (byte) ((value[i] >> 8) & 0xFF);
            data[(i * 2) + 1] = (byte) (value[i] & 0xFF);
        }
    }

    Payload(final String string) {
        length = string.length();
        for (int i = 0; i < length; i++) {
            data[i] = (byte) (string.charAt(i));
        }
    }
    /*
    Payload(final char value[]) {
    int i = 0;
    
    while (value[i] != null) {
    data[i] = value[i];
    i++;
    }
    length = i;
    }
     */

    Payload(final float value) {
        length = 0;
        /*
        union {
        float in;
        long out;
        } number;
        
        number.in = value;
        
        Payload payload;
        length = 4;
        
        data[0] = (number.out >> 24) &0xFF;
        data[1] = (number.out >> 16) &0xFF;
        data[2] = (number.out >> 8) &0xFF;
        data[3] = number.out & 0xFF;
         */
    }

    Payload(final float[] value) {
        length = 0;
    }

    Payload(final long value) {
        length = 4;
        data[0] = (byte) ((value >> 24) & 0xFF);
        data[1] = (byte) ((value >> 16) & 0xFF);
        data[2] = (byte) ((value >> 8) & 0xFF);
        data[3] = (byte) (value & 0xFF);
    }

    Payload() {
        length = 0;
    }

    public int getByte() {
        return (getByte(0));
    }

    public int getByte(int place) {
        return (data[place]) & 0xFF;
    }

    public int getInt() {
        return (int) (data[1] << 8) | (int) (data[0]);
    }

    public int getLength() {
        return length;
    }

    public float getFloat() {

        if (length != 4) {
            return 0.0F;
        }

        // Same as DataInputStream's 'readInt' method
        int i = (((data[0] & 0xff) << 24) | ((data[1] & 0xff) << 16)
                | ((data[2] & 0xff) << 8) | (data[3] & 0xff));


        return Float.intBitsToFloat(i);
    }
}
