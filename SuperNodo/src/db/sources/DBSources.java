/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.sources;
import RMIInterface.Source;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.VarsGlobal;

/**
 * Lista de nodos y saber si sigue habiendo conexion con algun nodo.
 * @author yo
 */
public class DBSources extends Observable implements Runnable{
    ArrayList<Source> nodos;
    static DBSources instance = null;
    private Connection conn;
    private String urlSql;
    private String url;
    private PreparedStatement pstm;
    public static DBSources getInstance(){ // get instance 
        if(instance == null){
            try {
                instance = new DBSources(new ArrayList<Source>());
            } catch (SQLException ex) {
                Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }
    private Statement stmt;
    private ResultSet rs;
    private DBSources(ArrayList<Source> nodos) throws SQLException {
        //this.urlSql = "/home/yo/Documents/shred/final.db";
        this.urlSql = VarsGlobal.getInstance().getDbUrl();
        // db parameters
        this.url = "jdbc:sqlite:" + urlSql;
        this.nodos = nodos;
        this.conn = DriverManager.getConnection(url);
        cleanTable();
    }
    public void cleanTable() throws SQLException{
        String sql = "delete from source";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.execute();
    }
    public static void main(String[] args) {
        DBSources db = DBSources.getInstance();
        Thread hilos[] = new Thread[600];
        for (int i = 0; i < 600; i++) {
            try {
                hilos[i] = new Thread(new DBSources(new ArrayList<>()));
                hilos[i].start();
            } catch (SQLException ex) {
                Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
     
        //db.update(new Source("Hola10", "md5555555555", "a"));
        
        
        
        
    }
    
    
    synchronized public void add(Source nodo){
        try {
            //System.out.println("DBSources ARCHIVOS Adddd "+nodo);
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("insert into source (name,md5,path,id_fk) values (?,?,?,?)");
            pstm.setString(1, nodo.getName());
            pstm.setString(2, nodo.getMd5());
            pstm.setString(3, nodo.getPath());
            pstm.setString(4, nodo.getId());
            pstm.executeUpdate();
            pstm.close();conn.close();
            
            setChanged();
            notifyObservers(nodo);
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private boolean checkNode(Source hosta, Source hostb){
        boolean band = false;
        band = hosta.getId().equals(hostb.getId()) && hosta.getName().equals(hostb.getName()) ;
        return band;
    }
    synchronized public Source update(Source nodo){
        try {
            //System.out.println("Uppdate: "+nodo);
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("update source set md5 = ? "
                    + "where id_fk = ? and path = ? and name = ?");
            pstm.setString(1, nodo.getMd5());
            pstm.setString(2, nodo.getId()); 
            pstm.setString(3, nodo.getPath());
            pstm.setString(4, nodo.getName());
            pstm.execute();
            pstm.close();
            conn.close();
            
            setChanged();
            notifyObservers(nodo);
            return nodo;
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nodo;
    }
    synchronized public void delete(Source nodo){
        try {
            System.out.println("delete: "+nodo);
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("delete from source where id_fk = ? and md5 = ?");
            pstm.setString(1, nodo.getId());
            pstm.setString(2, nodo.getMd5());
            pstm.executeUpdate();
            pstm.close();
            conn.close();
            
            setChanged();
            notifyObservers(nodo);
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Source[] searchByFileName(String fileName ){
        List<Source> lista = new LinkedList<>();
        try{
            System.out.println("Viendo archivos "+fileName);
        conn = DriverManager.getConnection(url);
        pstm = conn.prepareStatement("select * from source where name = ?");
        pstm.setString(1, fileName);
        rs = pstm.executeQuery();
        while(rs.next()){
            lista.add(new Source(rs.getString("path"), rs.getString("md5"), rs.getString("id_fk")));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        Source []res = new Source[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            res[i] = lista.get(i);
        }
        return res;
    }
    
    
    private int searchIndex(Source nodo){
        int index = -1;
        for (Source nodoi : this.nodos) {
            if (this.checkNode(nodo, nodoi)) {
                break;
            }
            index++;
        }
        return index;
    }

    @Override
    public String toString() {
        return "DBSources{" + "sources =" + nodos + '}';
    }

    @Override
    public void run() {
        try {
            DBSources db = DBSources.getInstance();
            Thread.sleep(10*((int)Math.random()*10));
            db.add(new Source("ajsdkf", "md55", ""+Thread.activeCount()));
                    } catch (InterruptedException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
