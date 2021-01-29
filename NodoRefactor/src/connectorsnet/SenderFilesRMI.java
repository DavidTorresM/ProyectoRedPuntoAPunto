/*
* Sirve para enviar un conjunto de mensajes port rmi
 */
package connectorsnet;

import RMIInterface.Source;
import java.util.Arrays;

/**
 *
 * @author yo
 */
public class SenderFilesRMI implements Sender{
    String ip;
    int port;

    public SenderFilesRMI(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    

    @Override
    public void sendMenssage(Object msn) {
    if(msn instanceof Source){
        Source archivo = (Source) msn;
        System.out.println("ManagerFilesDaemon enviador archivos: "+archivo);
        ClienteRMI cli = new ClienteRMI(ip, port);
        cli.setTypeOperacion(ClienteRMI.SEND_UPDATE_FILE);
        cli.setObjectSend(archivo);
        Thread cliHilo = new Thread(cli);
        cliHilo.start();
    }
    }

    @Override
    public Object[] sendMenssages(Object[] msn) {
        Source[] archivos = new Source[msn.length];
        for (int i = 0; i < msn.length; i++) {
            archivos[i] = (Source) msn[i];
        }
        ClienteRMI cli = new ClienteRMI(ip, port);
        cli.setTypeOperacion(ClienteRMI.SEND_UPDATE_FILES);
        cli.setObjectsSend(archivos);
        Thread cliHilo = new Thread(cli);
        cliHilo.start();
        return null;
    }

    @Override
    public Object sendRecvMenssage(Object msn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] sendRecvMenssages(Object[] msn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
