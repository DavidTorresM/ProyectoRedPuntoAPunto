/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.nodes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;

/**
 *
 * @author yo
 */
public class Node {
    private InetAddress ip;
    private int []ports;
    public static final int UPDATE = 0;
    public static final int ADD    = 1;
    public static final int REMOVE = 2;
    private LocalTime time;
    private int typeNotify;
    
    public Node(InetAddress ip, int[] ports) {
        this.ip = ip;
        this.ports = ports;
    }

    public Node(InetAddress ip, int port) {
        this(ip, new int[]{port});
    }
    public Node(String ip, int port) throws UnknownHostException {
        this(InetAddress.getByName(ip), new int[]{port});
    }

    public Node(String idNodo) throws UnknownHostException {
        this(idNodo.split(":")[0],Integer.parseInt(idNodo.split(":")[1]));
    }
    public Node(String idNodo, LocalTime time) throws UnknownHostException {
        this(idNodo.split(":")[0],Integer.parseInt(idNodo.split(":")[1]));
        this.time = time;
    }
    
    public String getId(){
        return this.ip.getHostAddress()+":"+this.ports[0];
    }
    
    /**
     * @return the ip
     */
    public InetAddress getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    /**
     * @return the ports
     */
    public int[] getPorts() {
        return ports;
    }

    /**
     * @param ports the ports to set
     */
    public void setPorts(int[] ports) {
        this.ports = ports;
    }

    @Override
    public String toString() {
        return "Host{" + "ip=" + ip + ", ports=" + ports + '}';
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
    
    
    
}

