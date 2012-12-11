package blatteclient;
import java.io.*;
import blattewar.*;
import java.rmi.*;
import java.rmi.server.*;
public class Moot extends UnicastRemoteObject implements Joueur
{
    private Action actionCourante;
    private boolean ready=false;
    private boolean coherent=true;
    private Blatte blatte;
    private String nomBlatte="";
    public Moot()throws RemoteException
    {
    }
    public Moot(String nom)throws RemoteException
    {
        nomBlatte=nom;
    }
    public Moot(Blatte b)throws RemoteException
    {
        this.blatte=b;
    }
    public String message(String message)
    {
        System.out.println(message);
        
        return message;
    }
    //comportement par défault.
    public Action determinerAction(Blatte b, ChampVision cv)throws RemoteException
    {
        System.out.println("demande du serveur");
        
        Action r=null;
        Direction d=null;
        switch ((int)(4.*Math.random()))
        {
        case 0:
            d=Direction.HAUT;
            break;
        case 1:
            d=Direction.BAS;
            break;
        case 2:
            d=Direction.GAUCHE;
            break;
        default:
            d=Direction.DROITE;
            break;
        }
        
        return new Deplacement(d);
    }
    //comportement par défault
    public String getNomBlatte()throws RemoteException
    {
        return nomBlatte;
    }
    public Blatte getBlatte()throws RemoteException
    {
        return this.blatte;
    }
    
}
