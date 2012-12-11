package blattewar;
import java.rmi.*;
import java.rmi.server.*;

public class JoueurIALocalMoyen extends UnicastRemoteObject implements Joueur
{
    public JoueurIALocalMoyen()throws RemoteException{}
    public String message(String message)throws RemoteException
    {
        return message;
    }
	public Action determinerAction(Blatte b, ChampVision cv)throws RemoteException
	{
		Action a=null;
		Direction d=null;
		
		if (chercherDirectionEnnemi(cv) != null)
		{
		    a = new Attaque(chercherDirectionEnnemi(cv));
		}
		else if( chercherCaseVide(cv) != null)
		{
		    a = new Deplacement(chercherCaseVide(cv));
		}
		else
		{
		    a = new Attente();
		}
		
		return a;
	}
	public String getNomBlatte()throws RemoteException
	{
		return "Blatte locale moyenne"+(int)(10000.*Math.random());
	}
	
	private Direction chercherDirectionEnnemi(ChampVision cv)throws RemoteException
	{
	    Direction d = null;
	        
        if (cv.getDroite() instanceof Sol && ((Sol)(cv.getDroite())).getBlatte() != null)
        {
            d = Direction.DROITE;
        }
        else if (cv.getHaut() instanceof Sol && ((Sol)cv.getHaut()).getBlatte() != null)
        {
            d = Direction.HAUT;
        }
        else if (cv.getGauche() instanceof Sol && ((Sol)cv.getGauche()).getBlatte() != null)
        {
            d = Direction.GAUCHE;
        }
        else if (cv.getBas() instanceof Sol && ((Sol)cv.getBas()).getBlatte() != null)
        {
            d = Direction.BAS;
        }
        
	    return d;
	}
	
	private Direction chercherCaseVide(ChampVision cv)throws RemoteException
	{
	    Direction d = null;
	    if (cv.getDroite() instanceof Sol && ((Sol)(cv.getDroite())).getBlatte() == null)
        {
            d = Direction.DROITE;
        }
        else if (cv.getHaut() instanceof Sol && ((Sol)cv.getHaut()).getBlatte() == null)
        {
            d = Direction.HAUT;
        }
        else if (cv.getGauche() instanceof Sol && ((Sol)cv.getGauche()).getBlatte() == null)
        {
            d = Direction.GAUCHE;
        }
        else if (cv.getBas() instanceof Sol && ((Sol)cv.getBas()).getBlatte() == null)
        {
            d = Direction.BAS;
        }
	    
	    return d;
	}
}
