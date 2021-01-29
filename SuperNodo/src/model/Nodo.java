/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalTime;

/**
 *
 * @author yo
 */
public class Nodo {
    private String id;
    private String id_fk;
    private String ip;
    private int port;
    private LocalTime time;

    public Nodo(String id, String id_fk, LocalTime time) {
        this.id = id;
        this.id_fk = id_fk;
        this.time = time;
    }
    
    public Nodo(String id, String id_fk, String ip, int port, LocalTime time) {
        this.id = id;
        this.id_fk = id_fk;
        this.ip = ip;
        this.port = port;
        this.time = time;
    }

    public Nodo() {
    }

    public Nodo(Nodo node1) {
        this(node1.getId(),node1.getId_fk(),node1.getTime());
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_fk() {
        return id_fk;
    }

    public void setId_fk(String id_fk) {
        this.id_fk = id_fk;
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

    @Override
    public String toString() {
        return "Nodo{" + "id=" + id + ", id_fk=" + id_fk + ", time=" + time + '}';
    }
    
    
}
