/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import dao.SuperNodoDAO;
import db.nodes.DBNodes;
import db.sources.DBSources;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SuperNodo;

/**
 *  Clase para hacer querys a la bd de sqlite
 * @author yo
 */
public class SuperNodoDAOSqlite implements SuperNodoDAO{

    @Override
    public SuperNodo getSuperNodo(String id) {
        SuperNodo supN = new SuperNodo();
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement("select * from supernodo where id= ?");
            pstm.setString(1, id);
            rs = pstm.executeQuery();
            while(rs.next()){
                //Convert time
                Integer[] numeros = Arrays.asList(rs.getString("time").split(":")).stream()
                    .map(Integer::parseInt).toArray(Integer[]::new);
                LocalTime time = LocalTime.of(numeros[0], numeros[1], numeros[2]);
                supN.setId(id);
                supN.setTime(time);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return supN;
        
    }

    @Override
    public ArrayList<SuperNodo> getAllSuperNodos() {
        ArrayList<SuperNodo> supNs = new ArrayList<SuperNodo>();
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement("select * from supernodo");
            rs = pstm.executeQuery();
            while (rs.next()) {
                //Convert time
                Integer[] numeros = Arrays.asList(rs.getString("time").split(":")).stream()
                        .map(Integer::parseInt).toArray(Integer[]::new);
                LocalTime time = LocalTime.of(numeros[0], numeros[1], numeros[2]);
                supNs.add(new SuperNodo(rs.getString("id"), time));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstm.close();
                conn.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return supNs;
    }

    @Override
    public ArrayList<SuperNodo> getAllSuperNodos(String where) {
        ArrayList<SuperNodo> supNs = new ArrayList<SuperNodo>();
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement("select * from supernodo where "+where);
            rs = pstm.executeQuery();
            while (rs.next()) {
                //Convert time
                Integer[] numeros = Arrays.asList(rs.getString("time").split(":")).stream()
                        .map(Integer::parseInt).toArray(Integer[]::new);
                LocalTime time = LocalTime.of(numeros[0], numeros[1], numeros[2]);
                supNs.add(new SuperNodo(rs.getString("id"), time));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBSources.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstm.close();
                conn.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return supNs;
    }

    @Override
    public boolean insertSuperNodo(SuperNodo supN) {
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int res = -1;
        try {
            pstm = conn.prepareStatement("insert into supernodo (id,time) values (?,time('now'))");
            pstm.setString(1, supN.getId());
            res = pstm.executeUpdate();
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
        return (res >= 1);
    }

    @Override
    public boolean updateSuperNodo(SuperNodo supN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteSuperNodo(SuperNodo supN) {
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int res = -1;
        try {
            pstm = conn.prepareStatement("delete from supernodo where id = ?");
            pstm.setString(1, supN.getId());
            res = pstm.executeUpdate();
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
        return (res >= 1);
    }

    @Override
    public boolean deleteSuperNodos(SuperNodo[] supN) {
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        int res = -1;
        try {
            for (SuperNodo superNodo : supN) {
            pstm = conn.prepareStatement("delete from supernodo where id = ?");
            pstm.setString(1, superNodo.getId());
            res = pstm.executeUpdate();
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
        return (res >= 1);
    }
    
}
