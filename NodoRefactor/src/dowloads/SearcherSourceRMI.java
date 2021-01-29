/*
 * Hace la busqueda de una archivo usando RMI.
   * implemententa la interface SearcherSource por que es posible que se busque 
   * un archivo de diferente manera por ejemplo en vez de buscar usando RMI buscar 
   * usando un socket
 */
package dowloads;

import RMIInterface.RMIi;
import connectorsnet.ClienteRMI;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import RMIInterface.Host;
import RMIInterface.Source;


/**
 *
 * @author yo
 */
public class SearcherSourceRMI implements SearcherSource {

    @Override
    public Source[] searchFile(String file, String superNodoId) {
        Source[] listaHost = null;
        String ip = superNodoId.split(":")[0];
        int port = Integer.parseInt(superNodoId.split(":")[1]);
        try {
            ClienteRMI cli = new ClienteRMI(ip,port);
            RMIi rmiRemote = cli.getObjRMI();
            listaHost = rmiRemote.searchFile(file);
        } catch (RemoteException ex) {
            Logger.getLogger(SearcherSourceRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaHost;
    }
    
}
