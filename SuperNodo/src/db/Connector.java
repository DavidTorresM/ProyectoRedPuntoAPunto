/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import db.nodes.DBNodes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class Connector {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            //urlSql = "/home/yo/Documents/shred/otro.db";
            String urlSql = VarsGlobal.getInstance().getDbUrl();
            // db parameters
            String url = "jdbc:sqlite:" + urlSql;
            conn = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
}
