package blattewar;
import java.rmi.*;
import java.rmi.server.*;

public class JoueurIALocal extends UnicastRemoteObject implements Joueur
{
    public JoueurIALocal()throws RemoteException{}
    public String message(String message)throws RemoteException
    {
        return message;
    }
	public Action determinerAction(Blatte b, ChampVision cv)throws RemoteException
	{
		Action r=null;
		Direction d=null;
		switch((int)(3.*Math.random()))
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
		switch((int)(3.*Math.random()))
		{
			case 0:
				r=new Attaque(d);	
				break;
			case 1:
			case 2:
				r=new Deplacement(d);
				break;
			default:
				r=new Attente();
				break;
		}
		return r;
	}
	public String getNomBlatte()throws RemoteException
	{
		return "Blatte locale "+(int)(10000.*Math.random());
	}
}
