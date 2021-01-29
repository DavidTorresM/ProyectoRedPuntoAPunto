/*
 * No usadas
 */
package logger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SocketHandler;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 *
 * @author yo
 */
public class LogGui implements ILog{
    private static LogGui instance = null;
    private JTextArea fileLog;
    private LogGui(){}
    public static LogGui getInstance(){
        if(instance == null){
            instance = new LogGui();
        }
        return instance;
    }


    @Override
    public void log(String mensaje) {
        System.out.println("log "+mensaje);
        Logger log = Logger.getLogger("test");
        
        log.info("algoooooooooooooooooooo");
    }

    public JTextArea getFileLog() {
        return fileLog;
    }

    public void setFileLog(JTextArea fileLog) {
        this.fileLog = fileLog;
    }
    

    
    
    
}
