/*
    Envia las actualizaciones de los archivo en el demonio usando RMI.
 */
package connectorsnet;

import RMIInterface.RMIi;
import RMIInterface.Source;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yo
 */
public class SenderUpdateFiles implements Sender{
    private String id;
    private String ipServ;
    private int portServ;

    public SenderUpdateFiles(String id, String ipServ, int portServ) {
        this.id = id;
        this.ipServ = ipServ;
        this.portServ = portServ;
    }
    
    
    
    
    @Override
    public void sendMenssage(Object msn) {
        try {
            Logger.getGlobal().info("Enviando update file ...");
            Source srcs = (Source) msn;
            System.out.println(srcs);
            ClienteRMI cli = new ClienteRMI(ipServ, portServ);
            RMIi rmiObj = cli.getObjRMI();
            rmiObj.updateFile(srcs);
        } catch (RemoteException ex) {
            Logger.getLogger(SenderUpdateFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Object[] sendMenssages(Object[] msn) {
        try {
            System.out.println("**** cambio en mensajes");
            Source []srcs = (Source[]) msn;
            ClienteRMI cli = new ClienteRMI(ipServ, portServ);
            RMIi rmiObj = cli.getObjRMI();
            rmiObj.updateFiles(srcs);
        } catch (RemoteException ex) {
            Logger.getLogger(SenderUpdateFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object sendRecvMenssage(Object msn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] sendRecvMenssages(Object[] msn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    
    
    
}
