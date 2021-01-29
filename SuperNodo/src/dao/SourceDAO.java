/*
Capa de comunicacion con la DB
 */
package dao;

import java.util.ArrayList;
import model.Source;

/**
 *
 * @author yo
 */
public interface SourceDAO {
    public Source getSource(String id);
    public ArrayList<Source> getAllSources();
    public ArrayList<Source> getAllSources(String where);
    public boolean insertSource(Source supN);
    public boolean updateSource(Source supN);
    public boolean deleteSource(Source supN);
}