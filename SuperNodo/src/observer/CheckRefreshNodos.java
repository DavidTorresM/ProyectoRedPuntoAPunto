// No usada
package observer;

import db.NodoDAOSqlite;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.logging.Logger;
import model.Nodo;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class CheckRefreshNodos extends Observable implements Runnable{


        
    @Override
    public void run() {
        int time = 3;//Tiempo del humbral para quitarlos 
        NodoDAOSqlite db = new NodoDAOSqlite();
        try{
        while(true){
            ArrayList<Nodo> sup = db.getAllNodos();
            sup.forEach(System.out::println);
            LocalTime now = LocalTime.now();
            //paso del humbral
            Nodo[] out = sup.stream()
            .filter((x) -> {
                return this.isInPeriod(x.getTime(), now, VarsGlobal.MAX_TIME_NO_REFRESH_NODO);
            }).toArray(Nodo[]::new);
            
            if (out.length > 0) {//sobrepasado umbral
                Logger.getGlobal().info("Nodos desconectados =( "+Arrays.toString(out));
                //Borrar  Supernodo
                System.out.println("Delete super nodos"+db.deleteNodos(out));
                //Borra de la gui
                setChanged();
                notifyObservers(out);
            }
            Thread.sleep(1000*time);
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private boolean isInPeriod(LocalTime aDate, LocalTime bDate, int time) {
        Duration period = Duration.ofSeconds(time);
        Duration duracion = Duration.between(aDate, bDate);
        int x = period.compareTo(duracion);
        return (x < 0) ? true : false;
    }

}
