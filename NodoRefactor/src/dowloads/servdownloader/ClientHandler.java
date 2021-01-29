/*
 * Atiende a peticiones de descargas
 */
package dowloads.servdownloader;

import dowloads.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Message;
import java.util.*;
import java.util.stream.Collectors;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class ClientHandler implements Runnable{
    public static final int TAMBUFF = 1500;
    private ObjectInputStream inObj;
    private ObjectOutputStream outObj;
    private InputStream in;
    private OutputStream out;
    private Socket sock;

    public ClientHandler(Socket sock) {
        this.inObj = null;
        this.outObj = null;
        this.sock = sock;
        Logger.getGlobal().info("ClientHandler-> Start new handler");
    }
    private void configStreams() throws IOException{
        this.in = sock.getInputStream();
        this.out = sock.getOutputStream();
        this.inObj = new ObjectInputStream(in);
        this.outObj = new ObjectOutputStream(out);
    }
    
    /*
        Se debe dividir el archivo solicitado
    */
    @Override
    public void run() {
        try {
            Message mensaje = null;
            this.in = this.sock.getInputStream();
            this.inObj = new ObjectInputStream(this.in);
            mensaje = (Message)this.inObj.readObject(); //read filename
            Logger.getGlobal().info("ClientHandler-> Recv FileName");
            String file = mensaje.getText();
            mensaje = (Message)this.inObj.readObject(); //read range
            Logger.getGlobal().info("ClientHandler-> Recv Range");
            String []range = mensaje.getText().split(",");
            
            //
            byte[] fragmento = this.divideFile(file, 
                    Integer.parseInt(range[0]), 
                    Integer.parseInt(range[1]));
            Logger.getGlobal().info("ClientHandler-> Divide Fragment");
            this.out = this.sock.getOutputStream();
            this.out.write(fragmento);
            Logger.getGlobal().info("ClientHandler-> Send Fragment");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(this.in != null)
                this.in.close();
                if(this.inObj != null)
                this.inObj.close();
                if(this.out != null)
                this.out.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    /**
    * Obtiene los bytes dados en el offset dado.
    * @param file File to dive fragments.
    * @param num_div Number of divitions of fragment.
    * @param offset  Position in te fragment.
    * @return File's fragment.
    */
    public byte[] divideFile(String file,long num_div,long offset) throws IOException{
        File inputFile = null;
        RandomAccessFile inputFd = null;
        byte[] buffer = null;
        try {
            inputFile = new File(file);
            inputFd = new RandomAccessFile(inputFile, "r");
            
            long q = inputFd.length() / num_div;
            long r = inputFd.length() % num_div;
            long extra = (r != 0 && num_div - 1 == offset) ? r : 0;
            long init = q * offset;
            long fin  = q * (offset + 1);
            buffer = new byte[(int)(fin - init + extra)];
            
            inputFd.seek(init);
            inputFd.read(buffer);
            
        } catch (FileNotFoundException e) {
            System.err.println("FileStreamsTest: " + e);
        } catch (IOException e) {
            System.err.println("FileStreamsTest: " + e);
        } finally {
                inputFd.close();
        }
        return buffer;
    }
    
    
    

    public ObjectInputStream getInObj() {
        return inObj;
    }

    public void setInObj(ObjectInputStream inObj) {
        this.inObj = inObj;
    }

    public ObjectOutputStream getOutObj() {
        return outObj;
    }

    public void setOutObj(ObjectOutputStream outObj) {
        this.outObj = outObj;
    }

    public Socket getSock() {
        return sock;
    }

    public void setSock(Socket sock) {
        this.sock = sock;
    }

    
    
    
    
    
    
}
