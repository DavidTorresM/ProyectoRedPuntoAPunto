/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import RMIInterface.RMIi;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import RMIInterface.Source;

/**
 *
 * @author yo
 */
public class ClienteRMI implements Runnable{
    public static final short SEND_REFRESH = 1;
    public static final short SEND_UPDATE_FILE = 2;
    public static final short SEND_UPDATE_FILES = 3;
    public static final short SEARCH_FILE = 4;
    private RMIi servObj;
    private String serverAddr;
    private int serverPort;
    private short typeOperacion;
    private Source []objectsSend;
    private Source objectSend;
    private int time;//tiempo para enviar el refresh
    public ClienteRMI(String serverAddr, int serverPort) {
        this.serverAddr = serverAddr;
        this.serverPort = serverPort;
    }
    
    
    public RMIi getObjRMI(){
        RMIi servObj = null;
        try {
            Registry registro = LocateRegistry.getRegistry(this.serverAddr, this.serverPort);
            servObj = (RMIi) registro.lookup("Manager");
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(ClienteRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return servObj;
    }

    @Override
    public void run() {
        RMIi rmiObj = this.getObjRMI();
        try{
        switch (this.typeOperacion) {
            case SEND_UPDATE_FILE:
                rmiObj.updateFile(objectSend);
                break;
            case SEND_UPDATE_FILES:
                rmiObj.updateFiles(objectsSend);
                break;
            default:
                throw new IllegalArgumentException("Falto enviar parametos a la "
                        + "clase o parametro no soportado");
        }
        }catch(RemoteException re){
            re.printStackTrace();
            Logger.getLogger(ClienteRMI.class.getName()).log(Level.SEVERE, null, re);
        }
        
    }

    public RMIi getServObj() {
        return servObj;
    }

    public void setServObj(RMIi servObj) {
        this.servObj = servObj;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public short getTypeOperacion() {
        return typeOperacion;
    }

    public void setTypeOperacion(short typeOperacion) {
        this.typeOperacion = typeOperacion;
    }

    public Source[] getObjectsSend() {
        return objectsSend;
    }

    public void setObjectsSend(Source[] objectsSend) {
        this.objectsSend = objectsSend;
    }

    public Source getObjectSend() {
        return objectSend;
    }

    public void setObjectSend(Source objectSend) {
        this.objectSend = objectSend;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    
    
    
    
    
    
}
