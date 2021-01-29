/*
 * Clase usada por Downloader para buscar los nodos de descarga
 */
package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author yo
 */
public class Host {
    private InetAddress ip;
    private int []ports;
    public Host(String id) throws UnknownHostException {
        this(id.split(":")[0],Integer.parseInt(id.split(":")[1]));
    }
    public Host(String ip, int port) throws UnknownHostException {
        this(InetAddress.getByName(ip) ,port);
    }
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

    @Override
    public String toString() {
        return "Host{" + "ip=" + ip + ", ports=" + ports[0] + '}';
    }
    
    
    
}
