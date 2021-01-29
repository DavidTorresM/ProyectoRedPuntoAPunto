/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast;

/**
 *
 * @author yo
 */
public interface SenderMulticast {
    public static final String IP_MULTICAST ="228.1.1.10";
    public static final String INTERFACE_NAME ="lo";
    public static final int PORT_MULTICAST  = 6767;
    public static int TIME_SEND = 2;
    public static int TAMBUFF = 1500;
    public void sendGroup(String interfaceName,String ipGroup,int port,int time, byte []data);
}
