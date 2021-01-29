package filesystem;

/*
 * Clase que obtiene todos los archivos que hay en la carpeta compartida.
 */

import RMIInterface.Source;
import connectorsnet.Sender;
import java.io.File;
import static java.nio.file.StandardWatchEventKinds.*;
 
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import utils.VarsGlobal;


/**
* <h1>ManagerFiles</h1>
* Clase que Monitorea los cambios en el sistema de archivos.
* <p>
* 
*
* @author  yo
* @version 1.0
* @since   2020-07-10
*/
public class ManagerFiles {
    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private List<String> extensions;
    private String SharedDir;
    private String id;
    /**
     * Constructor new.
     *
     * @param dir Directorio a monitorear.
     */
    public ManagerFiles(Path dir) throws IOException {
        this.SharedDir = dir.toString();
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        this.extensions = VarsGlobal.getInstance().getExtensions();
        walkAndRegisterDirectories(dir);
    }

    public ManagerFiles(String sharedDir) throws IOException {
        this(Paths.get(sharedDir));
    }

    public ManagerFiles() throws IOException {
        this(Paths.get("."));
    }
    public ManagerFiles(String sharedDir, String id) throws IOException {
        this(sharedDir);
        this.id = id;
    }
 
    /**
     * Register the given directory with the WatchService; This function will be called by FileVisitor
     */
    private void registerDirectory(Path dir) throws IOException 
    {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }
 
    /**
     * Register the given directory, and all its sub-directories, with the WatchService.
     */
    private void walkAndRegisterDirectories(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                registerDirectory(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Procesa los eventos en el directorio dado.
     *
     * @param nameFile Nombre del archivo que cambio.
     * @param opFile Typo de operacion que se realizo en el archivo.
     * @param pathFile Ruta completa del archivo modificado.
     * @param send Sender envia notificaciones.
     * @return Source, retorna el objeto con las operaciones (ADD|UPDATE|REMOVE)
     * en caso de presentar cambio envia null
     */
    private Source addChangesSource(Path source, String opFile, String pathFile) {
        RandomAccessFile rdf = null; 
        Source src = null;
        //Message msn = new Message(""); //mensage para enviar al sender
        byte []buff = new byte[32];
        String path = source.toString();
        String nameFile = source.getFileName().toString();
        System.out.println(source.getParent());
        try{
        if( opFile.length() > 0 && !nameFile.startsWith(".")){
            if (opFile.equals(ENTRY_CREATE.name())) {
                    String keyFile = MD5Checksum.getMD5Checksum(pathFile);
                    rdf = new RandomAccessFile(source.getParent()+".md5-" + nameFile, "rw");
                    rdf.write(keyFile.getBytes()); rdf.close();
                    src = new Source(path, keyFile, id, Source.ADD);
            } else if (opFile.equals(ENTRY_MODIFY.name())) {
                rdf = new RandomAccessFile(source.getParent()+".md5-" + nameFile, "rw");
                rdf.read(buff);rdf.close();
                String keyFile = MD5Checksum.getMD5Checksum(pathFile);
                if (!keyFile.equals(new String(buff))) {
                    rdf = new RandomAccessFile(source.getParent()+".md5-" + nameFile, "rw");
                    rdf.write(keyFile.getBytes());rdf.close();
                    src = new Source(path, keyFile, id, Source.UPDATE);
                }
            } else if (opFile.equals(ENTRY_DELETE.name())) {
                src = new Source(path, null, id, Source.REMOVE);
                File archMd5 = new File(source.getParent()+".md5-" + nameFile);
                archMd5.delete();
            } else{
                Logger.getGlobal().info("NO Op type...");  
            }
        }
        } catch (Exception ex) {
            Logger.getLogger(ManagerFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return src;
    }
    
    /**toString
     * Obtiene todos los archivos del directorio dado.
     *
     * 
     * @return ArrayList<Path> Array de todos los archivos encontrados en el 
     * directorio.
     */
    public ArrayList<Path> getAllFilesDirectory() {
        try {
            ArrayList<Path> files = new ArrayList<>();
            String extend = extensions.stream().collect(Collectors.joining("|"));
            System.out.println(this.SharedDir);
            Files.walkFileTree(Paths.get(this.SharedDir), new HashSet<>(), Integer.MAX_VALUE, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException {
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    String nameFile = file.getFileName().toString();
                    if(!nameFile.startsWith(".") &&
                            nameFile.matches(".*.(".concat(extend).concat(")")))
                        files.add(file);
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)
                        throws IOException {
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                        throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
            
            return files;
        } catch (IOException ex) {
            Logger.getLogger(ManagerFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getSharedDir() {
        return SharedDir;
    }

    public void setSharedDir(String SharedDir) {
        this.SharedDir = SharedDir;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
 
    
    
}
