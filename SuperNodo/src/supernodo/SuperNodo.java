/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supernodo;

import db.DBCreator;
import db.nodes.DBNodes;
import db.sources.DBSources;
import gui.GUISuperNodo;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import multicast.ListenerMulticastSuperNode;
import multicast.SenderMulticastSuperNode;
import observer.CheckRefreshNodos;
//import observer.CheckRefreshSuperNodos;
import observer.CheckRefreshSuperNodos;
import rmi.ManagerRMI;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class SuperNodo implements Runnable{
    int port;
    private VarsGlobal G;
    private GUISuperNodo pgui;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        int tam = 1;
        Thread []hilos = new Thread[tam];
        for (int i = 0; i < tam; i++) {
            int puerto = (int) (Math.random()*9999 + 6666);
            System.out.println("PORT:"+ puerto);
            SuperNodo sup = new SuperNodo(puerto,
                    "/home/yo/Documents/shred"+((int) (Math.random()*10 + 1))+"/base.db");
            hilos[i] = new Thread(sup);
        }
        for (int i = 0; i < tam; i++) {
            hilos[i].start();
        }
        for (int i = 0; i < tam; i++) {
            hilos[i].join();
        }
        
    }
    public SuperNodo(int port, String db) {
        this.port = port;
        G = VarsGlobal.getInstance();
        G.setInterfaceName("lo");
        G.setPort(port);
        G.setIp(ListenerMulticastSuperNode.getDirIpv4Interface("lo", 0).getHostString());
        G.setId(G.getIp()+":"+G.getPort());
        G.setDbUrl(db);
        DBCreator.createDB(db);//creamos la db 
        //DBNodes.getInstance().addSuperNode(G.getId()); //add supernodo defecto
    }
    
    //init metodo
    public void main() throws RemoteException{
        //Lanzamos El enviador multicast
        (new ManagerRMI()).initServer(port);
        Thread hilo1 = new Thread(new SenderMulticastSuperNode(this.port));
        ListenerMulticastSuperNode listenerM = new ListenerMulticastSuperNode();
        Thread hilo2 = new Thread(listenerM);
        CheckRefreshSuperNodos refreshSupNodos = new CheckRefreshSuperNodos();
        CheckRefreshNodos refreshNodos = new CheckRefreshNodos();
        DBSources dbSrc = DBSources.getInstance();
        dbSrc.addObserver(pgui);
        listenerM.addObserver(this.pgui);
        refreshSupNodos.addObserver(this.pgui);
        refreshNodos.addObserver(this.pgui);
        DBNodes.getInstance().addObserver(this.pgui);
        
        //checkRefresh.addObserver(this.pgui);
        Thread hilo3 = new Thread(refreshSupNodos);//hilo para ver el humbral en la db
        Thread hilo4 = new Thread(refreshNodos);//hilo para ver el humbral en la db
        hilo3.start();
        hilo4.start();
        /*
        (new ManagerRMI()).initServer();
         */
        hilo1.start();
        hilo2.start();
        /*
        Thread hilo3 = new Thread(checkRefresh);
        hilo3.start();
        */
    }

    @Override
    public void run() {
        try {
            this.main();
        } catch (RemoteException ex) {
            Logger.getLogger(SuperNodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public GUISuperNodo getPgui() {
        return pgui;
    }

    public void setPgui(GUISuperNodo pgui) {
        this.pgui = pgui;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static class wtachDB implements Runnable{

        @Override
        public void run() {
            try{
            DBSources db = DBSources.getInstance();
            while(true){
                System.out.println(db.toString());
                Thread.sleep(1000*3);
            }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    
}
