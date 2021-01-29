/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author yo
 */
public interface RMIi extends Remote{
    public Object updateFile(Source obj) throws RemoteException;
    public Object updateFiles(Source []obj) throws RemoteException;
    public Source[] searchFile(String obj) throws RemoteException;
    public Object sendRefresh(Object obj) throws RemoteException;
    public Object requestConnection(Object obj) throws RemoteException;

}
