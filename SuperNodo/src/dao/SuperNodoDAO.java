/*
Capa de comunicacion con la DB
 */
package dao;

import java.util.ArrayList;
import model.SuperNodo;

/**
 *
 * @author yo
 */
public interface SuperNodoDAO {
    public SuperNodo getSuperNodo(String id);
    public ArrayList<SuperNodo> getAllSuperNodos();
    public ArrayList<SuperNodo> getAllSuperNodos(String where);
    public boolean insertSuperNodo(SuperNodo supN);
    public boolean updateSuperNodo(SuperNodo supN);
    public boolean deleteSuperNodo(SuperNodo supN);
    public boolean deleteSuperNodos(SuperNodo []supN);
}