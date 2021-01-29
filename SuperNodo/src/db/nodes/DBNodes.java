/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.nodes;

import db.NodoDAOSqlite;
import db.sources.DBSources;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Nodo;
import utils.VarsGlobal;


/**
 * Lista de nodos y saber si sigue habiendo conexion con algun nodo.
 * @author yo
 */
public class DBNodes extends Observable{
    
    ArrayList<Node> nodos;
    static DBNodes instance = null;
    
    private Statement stmt;
    private ResultSet rs;
    
    private Connection conn;
    private String urlSql;
    private String url;
    private PreparedStatement pstm;
    public static DBNodes getInstance(){ // get instance 
        if(instance == null){
            instance = new DBNodes();
        }
        return instance;
    }
    
    public void cleanTable() throws SQLException{
        String sql = "delete from nodo";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.execute();
        sql = "delete from supernodo";
        pstm = conn.prepareStatement(sql);
        pstm.execute();
        pstm.close();
    }
    private DBNodes() {
        try {
            //this.urlSql = "/home/yo/Documents/shred/otro.db";
            this.urlSql = VarsGlobal.getInstance().getDbUrl();
            // db parameters
            this.url = "jdbc:sqlite:" + urlSql;
            this.nodos = nodos;
            this.conn = DriverManager.getConnection(url);
            cleanTable();
        } catch (SQLException ex) {
            Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    synchronized public long getSize(String idSupNodo){
        long tam = -1;
        try {
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("select count(*) as tam from nodo where id_fk = ?");
            pstm.setString(1, idSupNodo);
            rs = pstm.executeQuery();
            while(rs.next()){
                tam = Integer.parseInt(rs.getString("tam"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tam;
    }
    synchronized public void add(Node nodo){
        try {
            
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("insert into nodo (id,id_fk,ip,time,port) values (?,?,?,time('now','localtime'),?)");
            pstm.setString(1, nodo.getId());
            pstm.setString(2, VarsGlobal.getInstance().getId());
            pstm.setString(3, nodo.getIp().getHostAddress());
            pstm.setInt(4, nodo.getPorts()[0]);
            pstm.executeUpdate();
            
            setChanged();
            notifyObservers(nodo);
        
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private boolean checkNode(Node hosta, Node hostb){
        return hosta.getIp().equals(hostb.getIp())
                && hosta.getPorts()[0] == hostb.getPorts()[0];
    }
    synchronized public Node update(Node nodo){
        try {
            //Send update to observer 
            NodoDAOSqlite n = new NodoDAOSqlite();
            Nodo node1 = n.getNodo(nodo.getId());
            Nodo node2 = new Nodo(node1);
            /*//Log nodos consultados
            System.out.println("id "+nodo.getId());
            System.out.println("DBnodes id "+node1);
            System.out.println("DBnodes id "+node2);
            */
            node2.setTime(LocalTime.now());
            setChanged();
            notifyObservers(new Nodo[]{node1,node2});
            //Updates time
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("update nodo set time = time('now','localtime') where id=?");
            pstm.setString(1, nodo.getId());
            pstm.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return nodo;
    }
    
    synchronized public void delete(Node nodo){
        try {
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("delete from nodo where id = ?");
            pstm.setString(1, nodo.getId());
            pstm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private int searchIndex(Node nodo){
        int index = -1;
        for (Node nodoi : this.nodos) {
            if (this.checkNode(nodo, nodoi)) {
                break;
            }
            index++;
        }
        return index;
    }
    
    
    public LocalTime getTimeSuperNodeById(String idSupNodo){
        LocalTime time = null;
         String tiempoS="";
        try {
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("select time from supernodo where id= ?");
            pstm.setString(1, idSupNodo);
            rs = pstm.executeQuery();
            while(rs.next()){
                tiempoS = rs.getString("time");break;
            }
            Integer[] numeros = Arrays.asList(tiempoS.split(":")).stream()
                    .map(Integer::parseInt).toArray(Integer[]::new);
            time = LocalTime.of(numeros[0], numeros[1], numeros[2]);
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return time;
    }
    synchronized public long getNumSuperNodeById(String idSupNodo){
        long tam = -1;
        try {
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("select count(*) as tam from supernodo where id= ?");
            pstm.setString(1, idSupNodo);
            rs = pstm.executeQuery();
            while(rs.next()){
                tam = Integer.parseInt(rs.getString("tam"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tam;
    }
    
    synchronized public void addSuperNode(String id){
        try {
            //System.out.println("add superNode: " + id);
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("insert into supernodo (id,time) values (?,time('now','localtime'))");
            pstm.setString(1, id);
            pstm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public LocalTime updateTimeSupernodo(String id) {
        LocalTime time = null; 
        try {
            time = this.getTimeSuperNodeById(id);
            //System.out.println("Uppdate time : "+id+" yo soy "+VarsGlobal.getInstance().getId());
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("update supernodo set time = time('now','localtime') where id=?");
            pstm.setString(1, id);
            pstm.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return time;
    }
    
    public Node select(String table, String where) {
        try {
            conn = DriverManager.getConnection(url);
            pstm = conn.prepareStatement("select * from ? where ?");
            pstm.setString(1, table);
            pstm.setString(2, where);
            rs = pstm.executeQuery();
            while (rs.next()) {
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    
    
}
