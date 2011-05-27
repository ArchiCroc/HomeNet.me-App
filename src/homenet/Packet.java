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
public class Packet implements Cloneable {
    
    final static int PACKET_LENGTH = 0;
    final static int PACKET_SETTINGS = 1;
    final static int PACKET_FROM = 2;
    final static int PACKET_TO = 4;
    final static int PACKET_TTL = 4;
    final static int PACKET_ID = 6;
    final static int PACKET_COMMAND = 7;
    final static int PACKET_PAYLOAD = 8;
    final static int CMD_BATTERYLEVEL = 0x03;
    final static int CMD_MEMORYFREE = 0x04;
    final static int CMD_PING = 0x33;
    final static int CMD_PONG = 0x3E;
    final static int CMD_ACK = 0x11;
    final static int CMD_GETNODEID = 0x21;
    final static int CMD_SETNODEID = 0x22;
    final static int CMD_GETDEVICE = 0x23;
    final static int CMD_SETDEVICE = 0x24;
    final static int CMD_AUTOSENDSTART = 0xB1;
    final static int CMD_AUTOSENDSTOP = 0xB2;
    final static int CMD_ON = 0xC0;
    final static int CMD_OFF = 0xC1;
    final static int CMD_LEVEL = 0xC2;
    final static int CMD_CLEAR = 0xC3;
    final static int CMD_GETVALUE = 0xD0;
    final static int CMD_GETBYTE = 0xD1;
    final static int CMD_GETSTRING = 0xD2;
    final static int CMD_GETINT = 0xD3;
    final static int CMD_GETFLOAT = 0xD4;
    final static int CMD_GETLONG = 0xD5;
    final static int CMD_GETBINARY = 0xD6;
    final static int CMD_SETVALUE = 0xE0;
    final static int CMD_SETBYTE = 0xE1;
    final static int CMD_SETSTRING = 0xE2;
    final static int CMD_SETINT = 0xE3;
    final static int CMD_SETFLOAT = 0xE4;
    final static int CMD_SETLONG = 0xE5;
    final static int CMD_SETBINARY = 0xE6;
    final static int CMD_REPLYVALUE = 0xF0;
    final static int CMD_REPLYBYTE = 0xF1;
    final static int CMD_REPLYSTRING = 0xF2;
    final static int CMD_REPLYINT = 0xF3;
    final static int CMD_REPLYFLOAT = 0xF4;
    final static int CMD_REPLYLONG = 0xF5;
    final static int CMD_REPLYBINARY = 0xF6;

    final static int CMD_MULTIPACKET = 0xA0;
//an enum might be better here
    final static int STATUS_CLEAR = 0;
    final static int STATUS_RECEVING = 1;
    final static int STATUS_RECEIVED = 2;
    final static int STATUS_WAITING = 3;
    final static int STATUS_READY = 4;
    final static int STATUS_SENDING = 5;
    final static int STATUS_SENT = 6;
    final static int STATUS_ACK = 7;
    final static int STATUS_SUCCESS = 8;
    final static int STATUS_FAILED = 9;
    final static int OFFSET_PACKET = 10;
    final static int OFFSET_HEADER = 8;
    final static int OFFSET_FOOTER = 2;
    final static int PACKET_TCP = 1;
    final static int PACKET_UDP = 2;
    final static int PACKET_BROADCAST = 3;
    final static int PACKET_BUFFER = 10;
    final static int PACKET_TIMEOUT = 100;
    final static int PACKET_RETRY = 1;
    final static int DEVICE_UPDATE = 1000;
//schedule timer (millis)  = DEVICE_SCHEDULE * DEVICE_UPDATE;
    final static int DEVICE_SCHEDULE = 1;
    final static int NODE_BOOT_TIME = 3000;
    
    
    
    
    
    

    String toPort = null;
    String fromPort = null;
    int status;
    byte[] data = new byte[66];
    Date received;

    public Packet() {
        received = new Date();
    }

    public Packet(byte[] packet) {
        received = new Date();
        data = packet;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getToPort() {
        return toPort;
    }

    public String getFromPort() {
        return fromPort;
    }

    public int getStatus() {
        return status;
    }

    public int getLength() {
        return data[PACKET_LENGTH] & 0xFF;

    }

    public int getSettings() {
        return data[PACKET_SETTINGS] & 0x0F;
    }

    public int getType() {
        return data[PACKET_SETTINGS] & 0x0F;
    }

    public int getFromNode() {
        return (int) (data[PACKET_FROM] << 4) | (int) (data[PACKET_FROM + 1] >> 4);
    }

    public int getFromDevice() {
        return (int) (data[PACKET_FROM + 1] & 0x0F);
    }

    public int getToNode() {
        return (int) (data[PACKET_TO] << 4) | (int) (data[PACKET_TO + 1] >> 4);
    }

    public int getToDevice() {
        return (int) (data[PACKET_TO + 1] & 0x0F);
    }

    public int getId() {
        return data[PACKET_ID] & 0xff;
    }

    public int getCommand() {
        return data[PACKET_COMMAND] & 0xff;
    }

    public Payload getPayload() {
        Payload payload = new Payload();
        for (int i = 0; i < getPayloadLength(); i++) {
            payload.data[i] = data[i + OFFSET_HEADER];
        }
        return payload;
    }

    public int getPayloadAt(int place) {
        if ((getLength() - OFFSET_FOOTER) > (OFFSET_HEADER + place)) {
            return data[OFFSET_HEADER + place] & 0xFF;

        }
        return -1;
    }

    public int getPayloadLength() {
        return (data[PACKET_LENGTH] - OFFSET_PACKET) & 0xFF;
    }

    public int getChecksum() {
        return (((int) (data[getLength() - OFFSET_FOOTER])) << 8) | (int) data[getLength() - 1];
    }

    public Date getTimestamp() {
        return received;
    }

    public byte[] getData() {
        byte[] packet = new byte[getLength()];

        packet = Arrays.copyOfRange(data, 0, getLength());
        return packet;
    }

    public void setToPort(String value) {
        toPort = value;
    }

    public void setFromPort(String value) {
        fromPort = value;
    }

    public void setStatus(int value) {
        status = value;
    }

    public void setLength(int value) {
        data[PACKET_LENGTH] = (byte) (value & 0xFF);
    }

    public void setSettings(int value) {
        data[PACKET_SETTINGS] |= (byte) (value & 0x0F);
    }

    public void setType(int value) {
        data[PACKET_SETTINGS] |= (byte) (value & 0x0F);
    }

    public void setFromNode(int value) {
        data[PACKET_FROM] = (byte) ((value >> 4) & 0xFF);
        data[PACKET_FROM + 1] &= 0x0F; //clear bottom 4 bits
        data[PACKET_FROM + 1] |= (byte) ((value << 4) & 0xF0);
    }

    public void setToNode(int value) {
        data[PACKET_TO] = (byte) ((value >> 4) & 0xFF);
        data[PACKET_TO + 1] &= 0x0F; //clear bottom 4 bits
        data[PACKET_TO + 1] |= (byte) ((value << 4) & 0xF0);
    }

    public void setFromDevice(int value) {
        data[PACKET_FROM + 1] &= (byte) (0xF0); //clear top 4 bits
        data[PACKET_FROM + 1] |= (byte) (value & 0x0F);
    }

    public void setToDevice(int value) {
        data[PACKET_TO + 1] &= (byte) (0xF0); //clear top 4 bits
        data[PACKET_TO + 1] |= (byte) (value & 0x0F);
    }

    public void setId(int value) {
        data[PACKET_ID] = (byte) (value & 0xFF);
    }

    public void setCommand(int value) {
        data[PACKET_COMMAND] = (byte) (value & 0xFF);
    }

    public void setPayload(final Payload payload) {
        setPayloadLength(payload.length);
        System.arraycopy(payload.data, 0, data, OFFSET_HEADER, payload.length);
    }

    public void setPayloadLength(int value) {
        data[PACKET_LENGTH] = (byte) (value + OFFSET_PACKET);
    }
    
    
    static int crc16_update(int crc, int a) {
        crc ^= a;
        for (int i = 0; i < 8; ++i) {
            if ((crc & 1) == 1) {
                crc = (crc >> 1) ^ 0xA001;
            } else {
                crc = (crc >> 1);
            }
        }
        return crc & 0xFFFF;
}
}
