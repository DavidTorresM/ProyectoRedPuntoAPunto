/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.sources;

import java.io.File;
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
    public Source(Source src,int typeOp) {
        this(src.getPath(),src.getMd5(),src.getId(),typeOp);
    }
    public Source(Source src) {
        this(src.getPath(),src.getMd5(),src.getId());
    }
    public Source(String pathname,String md5, String id) {
        super(pathname);
        this.md5 = md5;
        this.id = id;
    }

    public Source(String file, String md5File, String id, int typeOp) {
        this(file,md5File,id);
        this.typeOp = typeOp;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Source other = (Source) obj;
        if (!Objects.equals(this.md5, other.md5)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        if (!Objects.equals(this.getPath(), other.getPath())) {
            return false;
        }
        return true;
    }
    

    /**
     * @return the md5
     */
    public String getMd5() {
        return md5;
    }

    /**
     * @param md5 the md5 to set
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return super.getName()+" "+this.md5+" "+this.id+" \n"; //To change body of generated methods, choose Tools | Templates.
    }

    public int getTypeOp() {
        return typeOp;
    }

    public void setTypeOp(int typeOp) {
        this.typeOp = typeOp;
    }
    
    
}
