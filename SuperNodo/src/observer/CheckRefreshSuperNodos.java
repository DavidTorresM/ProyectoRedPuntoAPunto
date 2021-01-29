// No usada
package observer;

import db.SuperNodoDAOSqlite;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.logging.Logger;
import model.SuperNodo;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class CheckRefreshSuperNodos extends Observable implements Runnable{

    @Override
    public void run() {
        int time = 3;//Tiempo del humbral para quitarlos 
        SuperNodoDAOSqlite db = new SuperNodoDAOSqlite();
        
        try{
        while(true){
            ArrayList<SuperNodo> sup = db.getAllSuperNodos();
            sup.forEach(System.out::println);
            LocalTime now = LocalTime.now();
            //paso del humbral
            SuperNodo[] out = sup.stream()
            .filter((x) -> {
                return this.isInPeriod(x.getTime(), now, VarsGlobal.MAX_TIME_NO_REFRESH_SUPERNODO);
            }).toArray(SuperNodo[]::new);
            
            if (out.length > 0) {//sobrepasado umbral
                Logger.getGlobal().info("Nodos desconectados =( "+Arrays.toString(out));
                
                //Borrar  Supernodo
                System.out.println("Delete super nodos"+db.deleteSuperNodos(out));
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
