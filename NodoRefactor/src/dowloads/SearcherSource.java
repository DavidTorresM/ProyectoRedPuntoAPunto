/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dowloads;

import RMIInterface.Host;
import RMIInterface.Source;


/**
 *
 * @author yo
 */
public interface SearcherSource {
    //Array superNodo para 
    public Source[] searchFile(String file,String superNodo);
}
