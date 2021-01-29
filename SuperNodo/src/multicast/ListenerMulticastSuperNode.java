/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast;

import db.nodes.DBNodes;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SuperNodo;
import utils.VarsGlobal;

/**
* ListenerMulticastSuperNode es una clase para escuchar la conectividad de otros
 supernodos asi como transmitir via multicast a un determinado grupo.
* 
*
* @author  Yo
* @version 1.0
* @since   2020-07-11 
*/
public class ListenerMulticastSuperNode extends Observable
        implements ListenerMulticast,Runnable{
    String groupIp;
    int portMulti;
    String interfaceName;
    boolean LOGGS = false;
    public ListenerMulticastSuperNode() {
        this(ListenerMulticast.PORT_MULTICAST, ListenerMulticast.INTERFACE_NAME);
        this.groupIp = ListenerMulticast.IP_MULTICAST;
    }
    public ListenerMulticastSuperNode(int portMulti, String interfaceName) {
        this.portMulti = portMulti;
        this.interfaceName = interfaceName;
    }
    
    @Override
    public void listen(String interfaceName, String groupIp, int port) {
        VarsGlobal G = VarsGlobal.getInstance();
        byte[] buf = new byte[100];
        String msg,ip,myIp = G.getIp(); int myPort = G.getPort();
        int puerto,tam, suPuerto;
        DatagramPacket msgPacket;
        DBNodes db = DBNodes.getInstance();
        String idSup = "";
        try (MulticastSocket clientSocket = new MulticastSocket(port)) {
            // Entrar al grupo multicast
            clientSocket.joinGroup(new InetSocketAddress(groupIp, port),
                    NetworkInterface.getByName(interfaceName));
            while (true) {
                // Receive the information and print it.
                msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);
                msg = new String(buf, 0, msgPacket.getLength());
                ip = msgPacket.getAddress().getHostAddress();
                puerto = msgPacket.getPort();
                tam = msgPacket.getLength();
                suPuerto = Integer.parseInt(msg.split(":")[1]);
                //if(true && !(ip.equals(myIp) && suPuerto == myPort)){
                idSup = ip+":"+suPuerto;
                if(LOGGS)
                System.out.println("Soy: " + myIp + ":" + myPort + " recibi de: " + ip + ":" + suPuerto);
                long res = db.getNumSuperNodeById(idSup);
                System.out.println();
                if(LOGGS)
                System.out.println(res);
                if(res <= 0){//si no esta lo agrego
                    if(LOGGS)
                    System.out.println("No esta en la BD");
                    SuperNodo sup = new SuperNodo(idSup, LocalTime.now());
                    db.addSuperNode(idSup);
                    setChanged();
                    notifyObservers(sup);
                }else{//update en la DB
                    if(LOGGS)
                    System.out.println("Esta en la BD "+idSup);
                    LocalTime old = db.updateTimeSupernodo(idSup);
                    LocalTime now  = LocalTime.now();
                    setChanged();
                    notifyObservers(new SuperNodo[]{new SuperNodo(idSup, old),new SuperNodo(idSup, now)});
                }
                //}
                if(LOGGS)
                System.out.println(tam + " bytes from: " + ip + " port: " + puerto + "\n" + msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static InetSocketAddress getDirIpv4Interface(String nameInterface, Integer port) {
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
        Logger.getGlobal().info("Interface: "+interfaceName+ "Multicast: "+groupIp+":"+portMulti);
        this.listen(interfaceName, groupIp, portMulti);
    }

    @Override
    public void listen(String interfaceName, String groupIp, int port, int time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
