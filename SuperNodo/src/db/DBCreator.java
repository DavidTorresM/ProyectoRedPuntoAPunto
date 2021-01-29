/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import db.nodes.DBNodes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class DBCreator {

    private String urlSql;
    private String url;
    private Connection conn;
    
    
    private DBCreator() {}
    public void getConn(){
        try {
            //this.urlSql = "/home/yo/Documents/shred/otro.db";
            this.urlSql = VarsGlobal.getInstance().getDbUrl();
            // db parameters
            this.url = "jdbc:sqlite:" + urlSql;
            this.conn = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void createDB(String dbUrl){
        System.out.println("DB manejador " + dbUrl);
        PreparedStatement pstm  = null;
        //this.urlSql = "/home/yo/Documents/shred/otro.db";
        String urlSql = dbUrl;//VarsGlobal.getInstance().getDbUrl();
        // db parameters
        String url = "jdbc:sqlite:" + urlSql;
        Connection conn = null;
        try {
            conn =  DriverManager.getConnection(url);
            String sql = "CREATE TABLE IF NOT EXISTS \"nodo\" (\n" +
                    "	\"id\"	TEXT NOT NULL UNIQUE,\n" +
                    "	\"port\"	INTEGER NOT NULL,\n" +
                    "	\"ip\"	TEXT NOT NULL,\n" +
                    "	\"time\"	TEXT,\n" +
                    "	\"id_fk\"	TEXT,\n" +
                    "	PRIMARY KEY(\"id\")\n" +
                    ");";
            pstm = conn.prepareStatement(sql);
            pstm.execute();
            sql = "CREATE TABLE IF NOT EXISTS \"supernodo\" (\n" +
                    "	\"id\"	TEXT NOT NULL UNIQUE,\n" +
                    "	\"time\"	TEXT\n" +
                    ");";
            pstm = conn.prepareStatement(sql);
            pstm.execute();
            sql = "CREATE TABLE IF NOT EXISTS \"source\" (\n" +
                    "	\"name\"	TEXT,\n" +
                    "	\"md5\"	TEXT,\n" +
                    "	\"path\"	TEXT,\n" +
                    "	\"id_fk\"	TEXT\n" +
                    ");";
            pstm = conn.prepareStatement(sql);
            pstm.execute();
            
            sql = "delete from source";
            pstm = conn.prepareStatement(sql);
            pstm.execute();
            
            sql = "delete from nodo";
            pstm = conn.prepareStatement(sql);
            pstm.execute();
            sql = "delete from supernodo";
            pstm = conn.prepareStatement(sql);
            pstm.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DBCreator.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
}
