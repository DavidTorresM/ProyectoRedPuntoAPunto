/*
 * 
 */
package nodorefactor;

import connectorsnet.ConnectorNetwork;
import connectorsnet.ListenerMulticastNode;
import connectorsnet.MD5Checksum;
import connectorsnet.Sender;
import connectorsnet.SenderFilesRMI;
import connectorsnet.SenderRefresher;
import dowloads.downloader.Downloader;
import dowloads.servdownloader.DownloaderServer;
import filesystem.ManagerFiles;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import RMIInterface.Source;
import connectorsnet.SenderUpdateFiles;
import dowloads.SearcherSourceRMI;
import filesystem.ManagerFilesDaemon;
import java.util.Arrays;
import javax.swing.JScrollPane;
import logger.ILog;
import logger.LogGui;
import utils.Host;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class NodoRefactor {
    VarsGlobal G ;
    String sharedDir;// Path del Directorio compartido
    int port; // puerto Elegido por el usuario
    String ip; //Ip tuya 
    String id; //Id del proceso lanzado
    ConnectorNetwork conn; // Realiza la conexion a la red p2p
    ManagerFiles mng; // Hace el manejo de los archivos  
    ManagerFilesDaemon mngD; // Hace la esucha de los archivos
    private Downloader down; // realiza descargas de archivos 
    DownloaderServer downServer; // Servidor que escucha las peticiones de descarga
    private String ipServ;
    private int portServ;
    
    
    
    public NodoRefactor(String sharedDir, int port, String interfaceName) {
        //init funcion
        try {
            this.sharedDir = sharedDir;
            this.port = port;
            this.ip = ListenerMulticastNode.getDirIpv4Interface("lo",0).getHostName();
            this.id = this.ip+":"+this.port;
            
            
            G = VarsGlobal.getInstance();
            G.setSharedDir(sharedDir);
            G.setInterfaceName(interfaceName);
            G.setPort(port);
            G.setIp(ListenerMulticastNode.getDirIpv4Interface(interfaceName, 0).getHostName());
            G.setId(this.id);
            G.setInterfaceName("lo");
            G.setIpM("228.1.1.10");
            G.setPortM(6767);
            conn = new ConnectorNetwork(new ListenerMulticastNode());
            mng = new ManagerFiles(sharedDir,id);
            downServer = new DownloaderServer(port);
            mngD = new ManagerFilesDaemon(sharedDir, id);
            Thread downServHilo = new Thread(downServer);
            downServHilo.start();
        } catch (IOException ex) {
            Logger.getLogger(NodoRefactor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public NodoRefactor() {
        this("/home/yo/Documents/shred/",7778,"lo");
    }
    public NodoRefactor(String dir, int port){
        this(dir,port,"lo");
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodoRefactor n = new NodoRefactor();//TODO Nota pon parameros basicos aqui
        n.main();
    }

    public NodoRefactor(String shreadDir, int port, JScrollPane jScrollPaneLog) {
        this(shreadDir, port,"lo");
    }
    public void main(){
        try {
            Logger log = Logger.getLogger("nodos");
            //funcion init
            String id = conn.connectNetwork();
            String infoSup[] = id.split(":");//ip:port
            this.ipServ = infoSup[0];
            this.portServ = Integer.parseInt(infoSup[1]);
            ArrayList<Path> archivos = this.mng.getAllFilesDirectory();
            List<Source> archivosMd5 = this.mapFiles(archivos);
            Logger.getGlobal().info("Sending files ...");
            //envio archivos
            Sender send = new SenderFilesRMI(ipServ,portServ);
            if(archivosMd5.size()>0){
                log.info("Enviando archivos =) ************+");
                send.sendMenssages(archivosMd5.toArray());
            }
            VarsGlobal.getInstance().setIdServ(ipServ+":"+portServ);
            //downloader 
            this.setDown(new Downloader(new SearcherSourceRMI()));
            //demonio para enviar refresh
            Logger.getGlobal().info("Create Refresher...");
            Thread refreshHilo = new Thread(new SenderRefresher(ipServ,id,portServ));
            refreshHilo.start();
            Logger.getGlobal().info("Create updater files...");
            mngD.setSender(new SenderUpdateFiles(this.id,ipServ,portServ));
            mngD.setSender(send);
            Thread h = new Thread(mngD);
            h.start();
        } catch (Exception ex) {
            Logger.getLogger(NodoRefactor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<Source> mapFiles(ArrayList<Path> archivos) {
        List<Source> archivosDao;
        archivosDao = archivos.stream()
                .map((Path path) -> {
                    String file = path.toString();
                    String md5File = MD5Checksum.getMD5Checksum(file);
                    Source src = new Source(file, md5File, id);
                    return new Source(file, md5File, id, Source.ADD);
                }).collect(toList());
        return archivosDao;
    }


    public Downloader getDown() {
        return down;
    }

    public void setDown(Downloader down) {
        this.down = down;
    }
    
    
    
    
    
}
