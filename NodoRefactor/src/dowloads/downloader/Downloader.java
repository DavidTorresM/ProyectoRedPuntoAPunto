/*
 * Descarga archivos de la red p2p
 *  Busca con el super nodo los nodos que tienen el archivo con igual nombre y 
 *  despues solicita descargarlos
 */
package dowloads.downloader;

import RMIInterface.Source;
import dowloads.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import utils.Host;
import utils.VarsGlobal;

/**
* <h1>Downloader</h1>
* Es una clase en la cual realiza descarga en paralelo de un archivo
* en diferentes nodos.
* <p>
* 
*
* @author  yo
* @version 1.0
* @since   2020-07-09 
*/
public class Downloader {
    private String file;
    private List<Host> lista; //lista de host que tienen el archivo a descargar
    SearcherSource searcher; //Envia la peticion de busqueda en archivos

    public Downloader(SearcherSource searcher) {
        this.searcher = searcher;
    }

    public Downloader() {
        this(new SearcherSourceRMI()); //para evitar el hardcoding
    }
    public List<Source> searchFile(String file,String superNode){
        return Arrays.asList(searcher.searchFile(file, superNode));
    }
    
    public void dowloadFile(String file, List<Host> lista){
        file = VarsGlobal.getInstance().getSharedDir()+"/"+file;
        Logger.getGlobal().info("Downloader-> Dowload fragments...");
        int numNodos = lista.size(), cont = 0;
        Thread []hilos = new Thread[numNodos];
        Iterator iter = lista.iterator();
        while(iter.hasNext()){//TODO directorios compartidos
            hilos[cont] = new Thread(new DowloadFragment((Host)iter.next(), 
                    file, cont, numNodos));
            hilos[cont].start();
            cont++;
        }
        Logger.getGlobal().info("Downloader-> Esperando hilos...");
        try {
            for (Thread hilo : hilos)
                hilo.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.mergeFiles(file,cont);
        
    }
    private boolean mergeFiles(String file, int cont) {
        FileOutputStream out = null;
        boolean band = false;
        try {
            out = new FileOutputStream(new File(file));
            for (int i = 0; i < cont; i++) {
                FileInputStream in = new FileInputStream(new File(file + i));
                byte[] buf = new byte[VarsGlobal.TAMBUFF];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                new File(file + i).delete();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
                band = true;
            } catch (IOException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return band;
    }
    public List<Source> searchNodeId(String fileName){
        //this.down.dowloadFile(fileName, lista);
        SearcherSourceRMI searcher = new SearcherSourceRMI();
        System.out.println("NodoRefactor ----search file ......");
        Source[] listaNodos = searcher.searchFile(fileName,VarsGlobal.getInstance().getIdServ());
        System.out.println("NodoRefactor ------ Lista de nodos ");
        List<Source> listaHost = 
            Arrays.asList(listaNodos).stream().collect(toList());
        System.out.println("Nodos buscados con archivo *****************************");
        listaHost.forEach(System.out::print);
        return listaHost;
    }
    

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @return the lista
     */
    public List<Host> getLista() {
        return lista;
    }

    /**
     * @param lista the lista to set
     */
    public void setLista(List<Host> lista) {
        this.lista = lista;
    }

}

