/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import dao.NodoDAO;
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
import model.Nodo;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class NodoDAOSqlite implements NodoDAO {
    

    @Override
    public Nodo getNodo(String id) {
        Nodo src = null;
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement("select * from nodo where id = ?");
            pstm.setString(1, id);
            rs = pstm.executeQuery();
            while(rs.next()){
                Integer[] numeros = Arrays.asList(rs.getString("time").split(":")).stream()
                        .map(Integer::parseInt).toArray(Integer[]::new);
                LocalTime time = LocalTime.of(numeros[0], numeros[1], numeros[2]);
                src = new Nodo(rs.getString("id"), rs.getString("id_fk"), time);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(NodoDAOSqlite.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return src;
    }

    @Override
    public ArrayList<Nodo> getAllNodos() {
        ArrayList<Nodo> srcs = new ArrayList<>();
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement("select * from nodo where id_fk = ?");
            pstm.setString(1, VarsGlobal.getInstance().getId());
            rs = pstm.executeQuery();
            Nodo src = null;
            while(rs.next()){
                Integer[] numeros = Arrays.asList(rs.getString("time").split(":")).stream()
                        .map(Integer::parseInt).toArray(Integer[]::new);
                LocalTime time = LocalTime.of(numeros[0], numeros[1], numeros[2]);
                src = new Nodo();
                src.setId(rs.getString("id"));
                src.setId_fk(rs.getString("id_fk"));
                src.setPort(rs.getInt("port"));
                src.setIp(rs.getString("ip"));
                src.setTime(time);
                srcs.add(src);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NodoDAOSqlite.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return srcs;
    }

    @Override
    public ArrayList<Nodo> getAllNodos(String where) {ArrayList<Nodo> srcs = new ArrayList<>();
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement("select * from nodo where id_fk = ?");
            pstm.setString(1, where);
            rs = pstm.executeQuery();
            Nodo src = new Nodo();
            while(rs.next()){
                Integer[] numeros = Arrays.asList(rs.getString("time").split(":")).stream()
                        .map(Integer::parseInt).toArray(Integer[]::new);
                LocalTime time = LocalTime.of(numeros[0], numeros[1], numeros[2]);
                src.setId(rs.getString("id"));
                src.setId_fk(rs.getString("id_fk"));
                src.setPort(rs.getInt("port"));
                src.setIp(rs.getString("ip"));
                src.setTime(time);
                srcs.add(src);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NodoDAOSqlite.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pstm.close();
                conn.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBNodes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return srcs;
    }

    @Override
    public boolean insertNodo(Nodo supN) {
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        int res = -1;
        try {
            pstm = conn.prepareStatement("insert into nodo (id, port, ip, time, id_fk) "
                    + "values (?,?,?,time('now','localtime'),?)");
            pstm.setString(1, supN.getId());
            pstm.setInt(2, supN.getPort());
            pstm.setString(3, supN.getIp());
            pstm.setString(4, supN.getId_fk());
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
    public boolean updateNodo(Nodo supN) {
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        int res = -1;
        try {
            pstm = conn.prepareStatement("update nodo set time = time('now','localtime') where id=?");
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
    public boolean deleteNodo(Nodo supN) {
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        int res = -1;
        try {
            pstm = conn.prepareStatement("delete from nodo where id=?");
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
    public boolean deleteNodos(Nodo[] supN) {
        
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        int res = -1;
        try {
            for (Nodo nodo : supN) {
                pstm = conn.prepareStatement("delete from nodo where id = ?");
                pstm.setString(1, nodo.getId());
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
