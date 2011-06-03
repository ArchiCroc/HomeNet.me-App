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
