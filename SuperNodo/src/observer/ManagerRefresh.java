/* Sirve para ver si algun nodo rebaso el humbral 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package observer;

import db.NodoDAOSqlite;
import db.SourceDAOSqlite;
import db.SuperNodoDAOSqlite;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import model.Nodo;
import model.Source;
import model.SuperNodo;
import multicast.ListenerMulticastSuperNode;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class ManagerRefresh implements Observer, Runnable{
    private final SuperNodoDAOSqlite dbSN;
    private final NodoDAOSqlite dbN;
    private final SourceDAOSqlite dbS;
    private ManagerRefresh() {
        this.dbSN = new SuperNodoDAOSqlite();
        this.dbN = new NodoDAOSqlite();
        this.dbS = new SourceDAOSqlite();
    }
    

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof ListenerMulticastSuperNode){ //si viene del multicast
            if(arg instanceof String){//mande un id 
                String id = (String) arg;
                ArrayList<SuperNodo> lSupNodos = this.dbSN.getAllSuperNodos();
                //calculamos el tiempo desde ahora hasta la ultima update
                LocalTime now = LocalTime.now();
                SuperNodo[] out = lSupNodos.stream()
                    .filter((x)->{
                    return this.isInPeriod(x.getTime(), now,VarsGlobal.MAX_TIME_NO_REFRESH_SUPERNODO);
                    }).toArray(SuperNodo[]::new);
                if(out.length > 0){//sobrepasado umbral
                    procesarUmbralRebasado(out);
                }
                
            }else if(arg instanceof LocalTime[]){//mande update 
                LocalTime[] tiempos = (LocalTime[]) arg;
            }
        }
    }

    private boolean isInPeriod(LocalTime aDate,LocalTime bDate,int time) {
        Duration period = Duration.ofSeconds(time);
        Duration duracion = Duration.between(aDate, bDate);
        int x = period.compareTo(duracion);
        return (x < 0)?false:true;
    }

    @Override
    public void run() {
        while(true){
            
        }
    }


    private ArrayList<Nodo> getAllNodesSuperNode(SuperNodo[] out) {
        ArrayList<Nodo> nodos = new ArrayList<Nodo>();
        for (SuperNodo superNodo : out) {//get all nodos del super nodo
            nodos.addAll(this.dbN.getAllNodos(" id_fk = " + superNodo.getId()));
        }
        return nodos;
    }

    private ArrayList<Source> getAllSourcesSuperNode(ArrayList<Nodo> nodos) {
        String []idsNodos = nodos.stream().map((n)->n.getId()).toArray(String[]::new);
        ArrayList<Source> sources = new ArrayList<Source>();
        for (String idNode : idsNodos) {//get all nodos del super nodo
            sources.addAll(this.dbS.getAllSources("id_fk = "+idNode));
        }
        return sources;
    }
    // altaba procesar el humbral
    private void procesarUmbralRebasado(SuperNodo[] out) {
        ArrayList<Nodo> nodos = getAllNodesSuperNode(out);
        ArrayList<Source> sources = getAllSourcesSuperNode(nodos);
        
    }
    
    
    
}
