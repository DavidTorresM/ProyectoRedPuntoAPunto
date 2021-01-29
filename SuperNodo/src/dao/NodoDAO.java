/*
Capa de comunicacion con la DB
 */
package dao;

import model.Nodo;
import java.util.ArrayList;

/**
 *
 * @author yo
 */
public interface NodoDAO {

    public Nodo getNodo(String id);

    public ArrayList<Nodo> getAllNodos();

    public ArrayList<Nodo> getAllNodos(String where);

    public boolean insertNodo(Nodo supN);

    public boolean updateNodo(Nodo supN);

    public boolean deleteNodo(Nodo supN);
    public boolean deleteNodos(Nodo []supN);
}