/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast;

/**
* ListenerMulticast es una interfaz que especifica los metodos de escucha de un 
* socket multicast.
*
* @author  Yo
* @version 1.0
* @since   2020-07-11 
*/
public interface ListenerMulticast {
    public static final String IP_MULTICAST ="228.1.1.10";
    public static final String INTERFACE_NAME ="lo";
    public static final int PORT_MULTICAST  = 6767;
    public static int TIME_LISTEN = 5;
    public static int TAMBUFF = 1500;
    /**
     * Metodo para escuchar indefinidamente los paquetes entrantes del grupo.
     *
     * @param interfaceName Nombre de la interface a usar.
     * @param groupIp Direccion ip del grupo multicast desde 224.0.0.0 a la 
     *                239.255.255.255.
     * @param port Puerto donde transmitira el grupo multicast.
     * @return void.
     */
    public void listen(String interfaceName,String groupIp, int port);
    /**
     * Metodo para escuchar los paquetes de un grupo multicast en un tiempo 
     * especificado.
     *
     * @param interfaceName Nombre de la interface a usar.
     * @param groupIp Direccion ip del grupo multicast desde 224.0.0.0 a la 
     *                239.255.255.255.
     * @param port Puerto donde transmitira el grupo multicast.
     * @param time Tiempo en que se estaran escuchando los paquetes.
     * @return void.
     */
    public void listen(String interfaceName,String groupIp, int port, int time);
}