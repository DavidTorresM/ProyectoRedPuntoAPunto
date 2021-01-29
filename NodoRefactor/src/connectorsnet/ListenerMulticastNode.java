/* 
 * Hace la escucha durante x tiempo de los super nodos que estan transmitiendo.
 * 
 */
package connectorsnet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import utils.VarsGlobal;

/**
* ListenerMulticastNode es una clase para escuchar en un grupo de multicast y
* conectarte a un supernodo.
*
* @author  Yo
* @version 1.0
* @since   2020-07-11 
*/
public class ListenerMulticastNode implements ListenerMulticast, Runnable{
    HashMap<String,Integer> superNodos;
    String groupIp;
    int portMulti;
    String interfaceName;
    int time;
    public ListenerMulticastNode(String ipM,int portMulti, String interfaceName) {
        this.groupIp = ipM;
        this.portMulti = portMulti;
        this.interfaceName = interfaceName;
    }
    //default info
    public ListenerMulticastNode() {
        this(VarsGlobal.IP_MULTICAST,
                VarsGlobal.PORT_MULTICAST, 
                    VarsGlobal.INTERFACE_NAME);
        this.time = VarsGlobal.TIME_LISTEN;
    }
    
    
    @Override
    public void listen(String interfaceName, String groupIp, int port) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * Metodo para escuchar los paquetes de un grupo multicast en un tiempo 
     * especificado en segundos y obtener una lista de supernodos.
     * @param interfaceName Nombre de la interface a usar.
     * @param groupIp Direccion ip del grupo multicast desde 224.0.0.0 a la
     * 239.255.255.255.
     * @param port Puerto donde transmitira el grupo multicast.
     * @param time Tiempo (en segundos) en que se estaran escuchando los
     * paquetes.
     * @return Retorna la lista entera de supernodos.
     */
    @Override
    public List<Map.Entry<String, Integer>> listen(String interfaceName, String groupIp, int port, int time) {
        byte[] buf = new byte[100];
        InetSocketAddress x = this.getDirIpv4Interface(interfaceName, port);
        String msg,ip;int puerto,tam;DatagramPacket msgPacket;
        this.superNodos = new HashMap<String, Integer>();
        try (MulticastSocket clientSocket = new MulticastSocket(port)) {
            System.out.println(new Date());
            // Entrar al grupo multicast
            clientSocket.joinGroup(new InetSocketAddress(groupIp, port),
                    NetworkInterface.getByName(interfaceName));
            LocalDateTime a_date = LocalDateTime.now();
            while (true) {
                if(this.isInPeriod(a_date,time)){
                    break;
                }
                // Receive the information and print it.
                msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);
                msg = new String(buf, 0, msgPacket.getLength());
                ip = msgPacket.getAddress().getHostAddress();
                puerto = msgPacket.getPort();tam = msgPacket.getLength();
                System.out.println(tam + " bytes from: " + ip + " port: " + puerto + "\n" + msg);
                if(msg.matches("\\d*:\\d*$")){ // TODO (opcional) Recivo mensaje en un formato x.
                    this.superNodos.put(ip+":"+msg.split(":")[1], port);
                }
            }
            System.out.println(new Date());
            System.out.println(superNodos.size());
            System.out.println("Imprimir lista de nodos");
            superNodos.forEach((String k, Integer v) -> System.out.println(k+" Contiene: "+v));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (superNodos.size() == 0) {
            throw new IllegalArgumentException("La lista de supernodos esta vacia");
        }
        List<Map.Entry<String, Integer>> superNodo = this.superNodos.entrySet().stream()
                .filter(y -> y.getValue() > 0).collect(Collectors.toList());

        return superNodo;
        
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //listen por default
    public List<Map.Entry<String, Integer>>  listen(){
        return this.listen(interfaceName, groupIp, portMulti, time);
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
            Logger.getLogger(ListenerMulticastNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dirSock;
    }



    /**
     * Verifica si el tiempo actual se encuentra en un intervalo de tiempo.
     *
     * @param aDate Punto inicial donde se empezara a contar el tiempo.
     * @param time Periodo de tiempo en segundos.
     * @return boolean true si han pasado los segundos especificados.
     */
    private boolean isInPeriod(LocalDateTime aDate,int time) {
        Duration period = Duration.ofSeconds(time);
        LocalDateTime bDate = LocalDateTime.now();
        Duration duracion = Duration.between(aDate, bDate);
        int x = period.compareTo(duracion);
        return (x > 0)?false:true;
    }
    /**
     * Selecciona un algun supernodo que este disponible.
     *
     * @return String Es la información del super nodo en formato ip:port.
     * @exception IllegalArgumentException Se lanza cuando la lista de supernodos 
     * esta vacia.
     * @see IllegalArgumentException
     */
    public String chooseSupernode(){
        if(this.superNodos.size() == 0){
            throw new IllegalArgumentException("La lista de supernodos esta vacia");
        }
        String key = "";
        List<Map.Entry<String, Integer>> superNodo = this.superNodos.entrySet().stream()
                .filter(x -> x.getValue() > 0 ).limit(1).collect(Collectors.toList());
        
        Map.Entry<String, Integer> res = superNodo.get(0);
        key = res.getKey();
        return key;
    }
    public List<Map.Entry<String, Integer>> getListSupernodes() {
        if (this.superNodos.size() == 0) {
            throw new IllegalArgumentException("La lista de supernodos esta vacia");
        }
        //String key = "";
        List<Map.Entry<String, Integer>> superNodo = this.superNodos.entrySet().stream()
                .filter(x -> x.getValue() > 0).collect(Collectors.toList());
        
        return superNodo;
    }
    
    
}
