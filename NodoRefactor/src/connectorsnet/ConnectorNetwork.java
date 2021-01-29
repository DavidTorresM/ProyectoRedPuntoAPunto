/*
 * Tiene funciones para conectar el nodo con algun super nodo disponible.
 */
package connectorsnet;

import RMIInterface.RMIi;
import filesystem.ManagerFiles;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.VarsGlobal;

/**
 * Conecta con los super nodos
 * @author yo
 */
public class ConnectorNetwork {
    ListenerMulticast listenerMulticast;
    public ConnectorNetwork(ListenerMulticast listenerMulticast){
        this.listenerMulticast = listenerMulticast;
    }
    //Conecta con la red y retorna el id del supernodo
    public String connectNetwork() throws Exception{
        Logger.getGlobal().info("connectNetwork ....");
        boolean conect = false;
        String keyValue = null;
        while (!conect) {
            //lista de todos los nodos lanzando paquetes
            Logger.getGlobal().info("Listen supernodos multicast....");
            List<Map.Entry<String, Integer>> supNodos = listenerMulticast.listen();
            Logger.getGlobal().info("Searching supernodes =) ");
            //Conectamos con alguno de los super nodos
            for (Map.Entry<String, Integer> supNodo : supNodos) {
                String[] infoSupNodo = supNodo.getKey().split(":");
                String ip = infoSupNodo[0];
                int port = Integer.parseInt(infoSupNodo[1]);
                Logger.getGlobal().info("request Connection SuperNode " + supNodo.getKey() + " ....");
                if ((conect = requestConnectionSuperNode(ip, port))) {
                    Logger.getGlobal().info("Connection accepted =) " + supNodo.getKey());
                    keyValue = supNodo.getKey();
                    break;
                }
            }
        }
        if (!conect) {
            throw new Exception("No se pudo conectar =( ");
        }
        return keyValue;//id ip:port
        /*
            Supernodos
            ip:portRMI
            Nodos 
            ip:portDownloadesServ
        */
    }
    private boolean requestConnectionSuperNode(String ip, int port) {
        boolean exito = false;
        try {
            //List<Map.Entry<String, Integer>> x = Nodo.getSuperNodes();
            RMIi x = (RMIi) (new ClienteRMI(ip, port)).getObjRMI();
            exito = (Boolean) x.requestConnection(VarsGlobal.getInstance().getId());
        } catch (RemoteException ex) {
            Logger.getLogger(ConnectorNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exito;
    }
    
    //Obtiene todos los archivos de la carpeta compartida
    /* //FUNCION NO USADA
    private Source[] mapFileSystem() {
        Source[] archivosDaoA = null;
        try {
            ManagerFiles watch = new ManagerFiles();
            watch.setSharedDir(sharedDir);
            ArrayList<Path> archivos = watch.getAllFilesDirectory();
            List<Source> archivosDao;
            archivosDao = archivos.stream()
                    .map((Path path) -> {
                        String file = path.toString();
                        String md5File = MD5Checksum.getMD5Checksum(file);
                        Source src = new Source(file, md5File, "", Source.ADD);//TODO NOTA pusiste el id en blanco
                        return src;
                    }).collect(toList());
            //archivosDao.forEach(System.out::println);
            archivosDaoA = new Source[archivos.size()];
            archivosDao.toArray(archivosDaoA);
        } catch (IOException ex) {
            Logger.getLogger(ConnectorNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return archivosDaoA;
    }
    */
    
    
    
    
    
    
    
    
    
    
}
