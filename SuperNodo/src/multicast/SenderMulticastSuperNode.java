/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast;

import db.nodes.DBNodes;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class SenderMulticastSuperNode implements SenderMulticast, Runnable{
    DBNodes databaseNodes;
    int portRmi; // Puerto del servidor RMI
    int portGroup;
    int time;
    String ipGroup;
    String interfaceName;
    boolean LOGGS = false;
    public SenderMulticastSuperNode(int portRmi) {
        this.databaseNodes = DBNodes.getInstance();
        this.portRmi = portRmi;
        this.portGroup = VarsGlobal.PORT_MULTICAST;
        this.interfaceName = VarsGlobal.INTERFACE_NAME;
        this.time = VarsGlobal.TIME_SEND_MULTICAST;
        this.ipGroup = VarsGlobal.IP_MULTICAST;
    }
    
    
    
    
    public SenderMulticastSuperNode(DBNodes databaseNodes, int portRmi, 
            int portGroup, int time, String interfaceName) {
        this.time = 5;
        this.ipGroup = "228.1.1.10";
    }
    public SenderMulticastSuperNode(int portRmi, int portGroup, String interfaceName) {
        this.databaseNodes = DBNodes.getInstance();
        this.portRmi = portRmi;
        this.portGroup = portGroup;
        this.interfaceName = interfaceName;
    }
    @Override
    public void sendGroup(String interfaceName, String ipGroup, int portGroup, int time, byte[] data) {
        InetSocketAddress dirSock = this.getDirIpv4Interface(interfaceName, 0);
        // Open a new DatagramSocket, which will be used to send the data.
        try (DatagramSocket serverSocket = new DatagramSocket(dirSock)) {
            //serverSocket.bind(, 0));
            InetAddress grup = InetAddress.getByName(ipGroup);//200.1.
            while (true) {
                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket;
                msgPacket = new DatagramPacket(data,
                        data.length, grup, portGroup);
                if(LOGGS)
                    System.out.println(msgPacket.getAddress());
                serverSocket.send(msgPacket);
                //consulta del numero de nodos disponibles
                long numNodos = databaseNodes.getSize(VarsGlobal.getInstance().getId());
                data = (numNodos + ":" + portRmi).getBytes();
                Thread.sleep(1000 * time);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(ListenerMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private InetSocketAddress getDirIpv4Interface(String nameInterface, Integer port) {
        InetSocketAddress dirSock = null;
        try {
            NetworkInterface nif = NetworkInterface.getByName(nameInterface);
            Enumeration<InetAddress> nifAddresses = nif.getInetAddresses();
            //System.out.println(nifAddresses.nextElement());
            //System.out.println(nifAddresses.nextElement());
            nifAddresses.nextElement();
            dirSock = new InetSocketAddress(nifAddresses.nextElement(), port);
        } catch (SocketException ex) {
            System.err.println("Error en la interface seleccionada");
            Logger.getLogger(ListenerMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dirSock;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000*3);
            Logger.getGlobal().info("Query number of nodes");
            long numNodos = databaseNodes.getSize(VarsGlobal.getInstance().getId());
            String mensaje = numNodos+":"+portRmi;
            Logger.getGlobal().info("Seending to group");
            this.sendGroup(interfaceName, ipGroup, portGroup, time, mensaje.getBytes());
        } catch (InterruptedException ex) {
            Logger.getLogger(SenderMulticastSuperNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
