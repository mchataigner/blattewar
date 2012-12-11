package blattewar;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
public interface Joueur extends Remote
{
    public Action determinerAction(Blatte _blatte, ChampVision _vue)throws RemoteException;
    public String getNomBlatte()throws RemoteException;
    public String message(String message)throws RemoteException;
    //public String 
}
