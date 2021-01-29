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
public class SuperNodo {
    private String id;
    private LocalTime time;

    public SuperNodo(String id, LocalTime time) {
        this.id = id;
        this.time = time;
    }

    public SuperNodo() {
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

    @Override
    public String toString() {
        return "SuperNodo{" + "id=" + id + ", time=" + time + '}';
    }
    
    
    
}
