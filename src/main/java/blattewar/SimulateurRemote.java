package blattewar;
import javax.ejb.Remote;
import java.rmi.*;
@Remote
public interface SimulateurRemote
{
    public String helloSimulation();
    public String helloSimulation(java.rmi.Remote j)throws RemoteException;
    public String rejoindrePartie(java.rmi.Remote j)throws RemoteException;
    public boolean partieEnCours();
}
