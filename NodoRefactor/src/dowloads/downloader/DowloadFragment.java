/*
* Descarga un fragmento del archivo
 */
package dowloads.downloader;

import dowloads.*;
import static connectorsnet.ListenerMulticast.TAMBUFF;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Host;
import utils.Message;

class DowloadFragment implements Runnable{
    private Host host;
    private int fragPosition;
    private int numThreads;
    private String fileName;
    
    private InputStream in = null;
    private OutputStream out = null;
    private ObjectInputStream inObj = null;
    private ObjectOutputStream outObj = null;
    private RandomAccessFile rdf = null;

    public DowloadFragment(Host host,String fileName, 
            int fragPosition, int numThreads) {
        this.host = host;
        this.fragPosition = fragPosition;
        this.numThreads = numThreads;
        this.fileName = fileName;
    }
    
    @Override
    public void run() {
        Logger.getGlobal().info("DowloadFragment-> Paraller download "
                + "Thread["+this.fragPosition+"]");
        byte[] buff = new byte[TAMBUFF];
        try {
            System.out.println(host.getIp().getHostAddress());
            System.out.println(host.getPorts()[0]);
            Socket sock = new Socket(host.getIp().getHostAddress(), host.getPorts()[0]);
            this.out = sock.getOutputStream();
            this.outObj = new ObjectOutputStream(this.out);
            this.outObj.writeObject(new Message(this.fileName)); //filename query
            Logger.getGlobal().info("DowloadFragment-> Enviando filename");
            
            
            StringBuilder sb = new StringBuilder();
            sb.append(this.numThreads).append(",").append(this.fragPosition);
            this.outObj.writeObject(new Message(sb.toString())); //Num threads, offset
            Logger.getGlobal().info("DowloadFragment-> Enviando range");
            this.in = sock.getInputStream();
            
            
            Logger.getGlobal().info("DowloadFragment-> Create File and recv fragment");
            FileWriter fileWriter = new FileWriter(this.fileName+this.fragPosition, false);
            this.rdf = new RandomAccessFile(this.fileName+this.fragPosition, "rw");
            int bytesNum; // response fragment
            while( (bytesNum = this.in.read(buff)) != -1)
                this.rdf.write(buff,0, bytesNum); //write file new 
            this.rdf.close();
            Logger.getGlobal().info("DowloadFragment-> Finish fragments");
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(DowloadFragment.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                this.outObj.close();
                this.in.close();
                this.out.close();
            } catch (IOException ex) {
                Logger.getLogger(DowloadFragment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    private void prepareStreams(Socket sock) throws IOException{
        this.in = sock.getInputStream();
        this.out = sock.getOutputStream();
        this.inObj = new ObjectInputStream(in);
        this.outObj = new ObjectOutputStream(out);
    }
    private void closeStreams() throws IOException{
       this.in.close();
        this.out.close();
        this.inObj.close();
        this.outObj.close();
        this.rdf.close();
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

    public RandomAccessFile getRdf() {
        return rdf;
    }

    public void setRdf(RandomAccessFile rdf) {
        this.rdf = rdf;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public int getFragPosition() {
        return fragPosition;
    }

    public void setFragPosition(int fragPosition) {
        this.fragPosition = fragPosition;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    
    
    
}