/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIInterface;

import java.net.InetAddress;

/**
 *
 * @author yo
 */
public class Host {
    private InetAddress ip;
    private int []ports;
    public Host(InetAddress ip, int[] ports) {
        this.ip = ip;
        this.ports = ports;
    }

    public Host(InetAddress ip, int port) {
        this(ip, new int[]{port});
    }
    
    public String getId() {
        return this.ip+":"+this.ports[0];
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
    
    
    
}
