/*
 * Variables globales 
 */
package utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yo
 */
public class VarsGlobal {
    private static VarsGlobal instance = null;
    private String sharedDir;// Path del Directorio compartido
    private int port; // puerto Elegido por el usuario
    private String ip; //Ip tuya 
    private String interfaceName; //Ip tuya 
    private String id; //Id del proceso lanzado
    private String destDir; //directorio destino 
    private String idServ; //id del servidor rmi 
    private String ipServ; //ip servidor rmi 
    private int portServ; //Puerto servidor rmi
    
    private String ipM;
    private int portM;
    private final List<String> extensions;
    public VarsGlobal() {
        this.extensions = new ArrayList<>();
        this.extensions.add("txt");
        this.extensions.add("pdf");
        this.extensions.add("zip");
    }
    /*//Listener multicast nodo*/
    public static final String IP_MULTICAST ="228.1.1.10";
    public static final String INTERFACE_NAME ="lo";
    public static final int PORT_MULTICAST  = 6767;
    public static int TIME_LISTEN = 5;
    public static int TAMBUFF = 1500;
    public static boolean LOGGS = true;
    /*//Tiempo de refresh del refresh*/
    public static final int TIME_REFRESH_CONN = 10;

    public List<String> getExtensions() {
        return extensions;
    }
    
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

    public String getIpM() {
        return ipM;
    }

    public void setIpM(String ipM) {
        this.ipM = ipM;
    }

    public int getPortM() {
        return portM;
    }

    public void setPortM(int portM) {
        this.portM = portM;
    }
    
    
}
