/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import RMIInterface.RMIi;
import RMIInterface.Source;
import db.NodoDAOSqlite;
import db.nodes.DBNodes;
import db.nodes.Node;
import db.nodes.NodeDAO;
import db.sources.DBSources;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class ManagerRMI extends UnicastRemoteObject implements RMIi {
    private static final long serialVersionUID = 35236253426L;
    private final String id;
    private final NodoDAOSqlite dbNodes;
    public ManagerRMI()throws RemoteException{
        this.id = VarsGlobal.getInstance().getId();
        this.dbNodes = new NodoDAOSqlite();
    }
    public void initServer(int port){
        try {
            Registry reg = LocateRegistry.createRegistry(port);
            reg.bind("Manager", (RMIi)this);
        } catch (RemoteException ex) {
            Logger.getLogger(ManagerRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(ManagerRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Quitar nodo
    @Override
    public Object sendRefresh(Object obj) throws RemoteException {
        try {
            String idNodo = (String) obj;
            DBNodes db = DBNodes.getInstance();
            db.update(new Node(idNodo));
        } catch (UnknownHostException ex) {
            Logger.getLogger(ManagerRMI.class.getName()).log(Level.SEVERE, null, ex);
        }return true;
    }
    
    /*
        Solicita la DB de nodos que hay dados de alta 
    */
    @Override
    public Object requestConnection(Object obj) throws RemoteException {
        DBNodes dbN = DBNodes.getInstance();
        NodoDAOSqlite db = new NodoDAOSqlite();
        boolean ret = false;
        int tam = db.getAllNodos(this.id).size();//dbN.getSize(this.id);
        System.out.println("requestConnection Tam supernodos : "+tam);
        try{
        if(tam < VarsGlobal.getInstance().MAX_NUM_NODES && tam >= 0){
            //add to db
            String id = (String) obj;
            dbN.add(new Node(id,LocalTime.now()));
            ret = true;
        }else{
            ret = false;
        }
        }catch(Exception e){
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    @Override
    public Object updateFile(Source obj) throws RemoteException {
        //System.out.println("ManagerRMI update archivos "+ obj);
        DBSources db = DBSources.getInstance();
        Source source = obj,nodoOld=null;
        switch (source.getTypeOp()) {
            case NodeDAO.ADD:
                db.add(source);
                break;
            case NodeDAO.REMOVE:
                db.delete(source);
                break;
            case NodeDAO.UPDATE:
                nodoOld = db.update(source);
                break;
            default:
                throw new AssertionError("Opcion no valida =( (ADD|REMOVE|UPDATE)");
        }
        return nodoOld;
    }

    @Override
    public Object updateFiles(Source[] obj) throws RemoteException {
        Source nodoOld = null;
        for (int i = 0; i < obj.length; i++) {
            Source source = obj[i];
            nodoOld = (Source) this.updateFile(source);
        }
        return nodoOld;
    }
    //Busca un archivo y retorna el id de los super nodos
    @Override
    public Source[] searchFile(String file) throws RemoteException {
        DBSources db = DBSources.getInstance();
        Source[] supNodosId = db.searchByFileName(file);
        return supNodosId;
    }

}
