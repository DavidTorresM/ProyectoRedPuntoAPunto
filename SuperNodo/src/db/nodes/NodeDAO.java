/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.nodes;

import java.time.LocalTime;

/**
 *  Son clases de intercambio entre los observadores y la info
 * @author yo
 */
public class NodeDAO extends Node {
    public static final int UPDATE = 0;
    public static final int ADD    = 1;
    public static final int REMOVE = 2;
    private LocalTime time;
    private int typeNotify;
    public NodeDAO(Node host, LocalTime time, int typeNotify) {
        super(host.getIp(), host.getPorts());
        this.time = time;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getTypeNotify() {
        return typeNotify;
    }

    public void setTypeNotify(int typeNotify) {
        this.typeNotify = typeNotify;
    }

    @Override
    public String toString() {
        return "HostDAO{" + "time=" + time + ", typeNotify=" + typeNotify +", parent "+ 
                super.toString() +'}';
    }
    
    
    
    
}
