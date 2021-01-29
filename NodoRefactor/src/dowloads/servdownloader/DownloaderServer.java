/*
 * Servidor que esta a la escucha de peticiones de descarga
 */
package dowloads.servdownloader;

import dowloads.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class DownloaderServer extends ServerSocket implements Runnable{
    
    private InputStream in;
    private OutputStream out;

    public DownloaderServer(int port) throws IOException {
        super(port);
    }

    public DownloaderServer(int port, int backlog) throws IOException {
        super(port, backlog);
        Logger.getGlobal().info("DownloaderServer-> Create instance");
    }
    
    
    
    @Override
    public void run() {
        this.listen();
    }
    public void configStreams(Socket sock) throws IOException{
        in = sock.getInputStream();
        out = sock.getOutputStream();
    }
    
    public void listen(){
        Logger.getGlobal().info("DownloaderServer-> Listen thread mode...");
        byte []buff = new byte[VarsGlobal.TAMBUFF];
        int x;
        Socket sock;
        try {
        while(true){
            sock = this.accept();
            Logger.getGlobal().info("DownloaderServer-> Accept connection from: "
                    +sock.getInetAddress().getHostAddress());
            
            Thread hilo = new Thread(new ClientHandler(sock));
            hilo.start();
            
        }
        } catch (IOException ex) {
            Logger.getLogger(DownloaderServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public OutputStream getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }
}
 