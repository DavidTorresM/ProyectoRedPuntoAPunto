/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author yo
 */
public class VarsGlobal {
    private static VarsGlobal instance = null;
    public static int MAX_TIME_NO_REFRESH_NODO = 15;//doc 15s
    private String sharedDir;// Path del Directorio compartido
    private int port; // puerto Elegido por el usuario
    private String ip; //Ip tuya 
    private String interfaceName; //Ip tuya 
    private String id; //Id del proceso lanzado
    private String destDir; //directorio destino 
    private String idServ; //id del servidor rmi 
    private String ipServ; //ip servidor rmi 
    private int portServ; //Puerto servidor rmi
    private String dbUrl;
    public VarsGlobal() {
    }
    /*//Listener multicast nodo*/
    public static final String IP_MULTICAST ="228.1.1.10";
    public static final String INTERFACE_NAME ="lo";
    public static final int PORT_MULTICAST  = 6767;
    public static int TIME_LISTEN_MULTICAST = 15;//tiempo de escucha el canal multicast
    public static int TAMBUFF = 1500;
    public static boolean LOGGS = true;
    /*//Tiempo de refresh del refresh*/
    public static final int TIME_REFRESH_CONN = 10; //
    public static final int TIME_SEND_MULTICAST = 5; //tiempo envio de cada paquete multicast
    public static final int MAX_TIME_NO_REFRESH_SUPERNODO = 10;//20 doc
    public static final int MAX_NUM_NODES = 2;//TODO CAMBIAAR
    
    public static VarsGlobal getInstance(){
        if(instance == null){
            instance = new VarsGlobal();
        }
        return instance;
    }

    public String getSharedDir() {
        return sharedDir;
    }

    public void setSharedDir(String sharedDir) {
        this.sharedDir = sharedDir;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestDir() {
        return destDir;
    }

    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }

    public String getIdServ() {
        return idServ;
    }

    public void setIdServ(String idServ) {
        this.idServ = idServ;
    }

    public String getIpServ() {
        return ipServ;
    }

    public void setIpServ(String ipServ) {
        this.ipServ = ipServ;
    }

    public int getPortServ() {
        return portServ;
    }

    public void setPortServ(int portServ) {
        this.portServ = portServ;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
    
    
}
