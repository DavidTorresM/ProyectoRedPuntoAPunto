//Esta se usa para enviar refresh cada x tiempo al supernodo
// sirve para saber si el nodo esta conectado
package connectorsnet;

import RMIInterface.RMIi;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.VarsGlobal;

/**
 *
 * @author yo
 */
public class SenderRefresher implements Runnable{
    String ipServ;
    String id;
    int portServ;
    int time;

    boolean DEBUG = true;
    public SenderRefresher(String ipServ, String id, int portServ) {
        this.ipServ = ipServ;
        this.id = id;
        this.portServ = portServ;
        this.time = VarsGlobal.TIME_REFRESH_CONN;//Sender.TIME_REFRESH_CONN;
    }
    

    @Override
    public void run() {
        ClienteRMI cli = new ClienteRMI(ipServ, portServ);
        RMIi rmiObj = cli.getObjRMI();
        try {
            while(true){
                Object rs = rmiObj.sendRefresh(VarsGlobal.getInstance().getId());
                System.out.println("Sender Refresh: "+rs);
                Thread.sleep(1000*time);
            }
        } catch (InterruptedException ex) {
                Logger.getLogger(SenderRefresher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(SenderRefresher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
