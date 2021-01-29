/*
 * Archivo para enviar mensajes por los sockets de flujo
 */
package utils;

import java.io.Serializable;

/**
 *
 * @author yo
 */
public class Message implements Serializable{
    private String text;

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
    
    
    
    
}