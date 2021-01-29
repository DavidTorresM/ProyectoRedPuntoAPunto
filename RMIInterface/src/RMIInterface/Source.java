/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIInterface;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * @author yo
 */
public class Source extends File{
    
    public static final int UPDATE = 0;
    public static final int ADD    = 1;
    public static final int REMOVE = 2;
    
    private String md5;
    private String id;
    private int typeOp;
    public Source(Source src) {
        this(src.getPath(),src.getMd5(),src.getId());
    }
    public Source(String pathname,String md5, String id) {
        super(pathname);
        this.md5 = md5;
        this.id = id;
    }

    public Source(String pathname,String md5, String id, int typeOp) {
        this(pathname,md5,id);
        this.typeOp = typeOp;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public String toString() {
        return "Source{" + "md5=" + md5 + ", id=" + id + ", typeOp=" + typeOp + '}';
    }

    
    
    
    

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTypeOp() {
        return typeOp;
    }

    public void setTypeOp(int typeOp) {
        this.typeOp = typeOp;
    }
    
    
}
