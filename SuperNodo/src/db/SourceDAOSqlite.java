/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import dao.SourceDAO;
import db.nodes.DBNodes;
import db.sources.DBSources;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Source;

/**
 *  Clase para hacer querys a la bd de sqlite
 * @author yo
 */
public class SourceDAOSqlite implements SourceDAO{

    @Override
    public Source getSource(String idNodo) {
        Source src = new Source();
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement("select * from source where id_fk= ?");
            pstm.setString(1, idNodo);
            rs = pstm.executeQuery();
            while(rs.next()){
                src.setId_fk(idNodo);
                src.setMd5(rs.getString("md5"));
                src.setName(rs.getString("name"));
                src.setPath(rs.getString("path"));
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
        return src;
    }

    @Override
    public ArrayList<Source> getAllSources() {
        ArrayList<Source> srcs = new ArrayList<>();
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement("select * from source");
            rs = pstm.executeQuery();
            Source src = null;
            while(rs.next()){
                src = new Source();
                src.setId_fk(rs.getString("id_fk"));
                src.setMd5(rs.getString("md5"));
                src.setName(rs.getString("name"));
                src.setPath(rs.getString("path"));
                srcs.add(src);
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
        return srcs;
    }

    @Override
    public ArrayList<Source> getAllSources(String where) {
        ArrayList<Source> srcs = new ArrayList<>();
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement("select * from source where ?");
            pstm.setString(1, where);
            rs = pstm.executeQuery();
            Source src = null;
            while(rs.next()){
                src = new Source();
                src.setId_fk(rs.getString("id_fk"));
                src.setMd5(rs.getString("md5"));
                src.setName(rs.getString("name"));
                src.setPath(rs.getString("path"));
                srcs.add(src);
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
        return srcs;
    }

    @Override
    public boolean insertSource(Source supN) {
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        int res = -1;
        try {
            pstm = conn.prepareStatement("insert into source (name, md5, path,id_fk) values (?,?,?,?)");
            pstm.setString(1, supN.getName());
            pstm.setString(2, supN.getMd5());
            pstm.setString(3, supN.getPath());
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
    public boolean updateSource(Source supN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteSource(Source supN) {
        Connection conn = Connector.getConnection();
        PreparedStatement pstm = null;
        int res = -1;
        try {
            pstm = conn.prepareStatement("delete from source where id_fk = ?");
            pstm.setString(1, supN.getId_fk());
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
}