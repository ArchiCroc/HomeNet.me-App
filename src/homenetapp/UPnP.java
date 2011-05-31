/*
 * UPnP.java
 * 
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
 * along with HomeNet.  If not, see <http ://www.gnu.org/licenses/>.
 */
package homenetapp;

//import org.teleal.cling.UpnpService;
//import org.teleal.cling.mock.MockUpnpService;
//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.LocalDevice;
//import org.teleal.cling.model.meta.LocalService;
//import org.teleal.cling.model.types.UDAServiceId;
//import org.teleal.cling.registry.RegistryListener;
//import org.teleal.cling.support.igd.callback.PortMappingAdd;
//import org.teleal.cling.support.model.PortMapping;
//import org.teleal.cling.support.igd.callback.PortMappingDelete;
//import org.teleal.cling.support.igd.PortMappingListener;
//import org.teleal.cling.UpnpServiceImpl;
import org.wetorrent.upnp.*;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.net.*;
import java.io.*;

/**
 *
 * @author Matthew Doll <mdoll at homenet.me>
 */
public class UPnP {
    // UpnpService upnpService;

    String ipAddress;
    GatewayDevice d;
    int port;

    public UPnP() {
    }

    public void start() {
    }

    public String getIpAddress() {

        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            return thisIp.getHostAddress().toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public void forwardPort(int port) {
        this.port = port;

//        PortMapping desiredMapping =
//                new PortMapping(port, getIpAddress(), PortMapping.Protocol.TCP, "HomeNet.me App Server");
//        
//        System.out.println("UPnP: Forwarding port "+port+" to "+getIpAddress());
//
//         upnpService = new UpnpServiceImpl(new PortMappingListener(desiredMapping));
//
//        upnpService.getControlPoint().search();
//         PortMapping desiredMapping =
//                 new PortMapping(
//                         8123,
//                         "192.168.0.123",
//                        PortMapping.Protocol.TCP,
//                         "My Port Mapping"
//                );
//
//         UpnpService upnpService = new UpnpServiceImpl();
// 
//         LocalDevice device = IGDSampleData.createIGDevice(TestConnection.class);
//         upnpService.getRegistry().addDevice(device);
//
//         LocalService service = device.findService(new UDAServiceId("WANIPConnection"));         // DOC: PM1
//
//         upnpService.getControlPoint().execute(
//            new PortMappingAdd(service, desiredMapping) {
//
//               @Override
//                public void success(ActionInvocation invocation) {
//                     // All OK
//                    tests[0] = true;                                                        // DOC: EXC1
//                 }
// 
//                 @Override
//                 public void failure(ActionInvocation invocation,
//                                     UpnpResponse operation,
//                                     String defaultMsg) {
//                     // Something is wrong
//                 }
//             }
//         );
        try {


            //  int port = 2443;
            int WAIT_TIME = 10;

            System.out.println("Starting weupnp");

            GatewayDiscover discover = new GatewayDiscover();
            System.out.println("Looking for Gateway Devices");
            discover.discover();
            d = discover.getValidGateway();

            if (null != d) {
                System.out.println("Gateway device found. " + d.getModelName() + " (" + d.getModelDescription() + ")");
            } else {
                System.out.println("No valid gateway device found.");
                return;
            }

            InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println("Using local address: " + localAddress);
            String externalIPAddress = d.getExternalIPAddress();
            System.out.println("External address: " + externalIPAddress);
            PortMappingEntry portMapping = new PortMappingEntry();

            System.out.println("Attempting to map port " + port);
            System.out.println("Querying device to see if mapping for port " + port + " already exists");

            if (!d.getSpecificPortMappingEntry(port, "TCP", portMapping)) {
                System.out.println("Sending port mapping request");

                if (d.addPortMapping(port, port, localAddress.getHostAddress(), "TCP", "HomeNet App")) {
                    System.out.println("Mapping succesful");


                }

            } else {
                System.out.println("Port was already mapped. Aborting");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        try {
            d.deletePortMapping(port, "TCP");
            System.out.println("Port mapping removed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
