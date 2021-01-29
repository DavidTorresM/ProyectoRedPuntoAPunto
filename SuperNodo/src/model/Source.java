/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author yo
 */
public class Source {
    private String name;
    private String md5;
    private String ip;
    private int port;
    private String path;
    private String id_fk;

    public Source(String name, String md5, String ip, int port, String path, String id_fk) {
        this.name = name;
        this.md5 = md5;
        this.ip = ip;
        this.port = port;
        this.path = path;
        this.id_fk = id_fk;
    }

    public Source() {
    }

    public String getId_fk() {
        return id_fk;
    }

    public void setId_fk(String id_fk) {
        this.id_fk = id_fk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
}
