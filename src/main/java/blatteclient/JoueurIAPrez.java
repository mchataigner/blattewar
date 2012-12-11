package blatteclient;
import java.io.*;
import blattewar.*;
import java.rmi.*;
import java.rmi.server.*;

public class JoueurIAPrez extends UnicastRemoteObject implements Joueur
{
    private Blatte blatte;
    
    public JoueurIAPrez()throws RemoteException{}
    public JoueurIAPrez(Blatte b)throws RemoteException{ this.blatte=b; }
    public String message(String message)throws RemoteException{ return message;	}
    
	public Action determinerAction(Blatte b, ChampVision cv)throws RemoteException{
		Action a=new Attaque(Direction.GAUCHE);
		Direction d=null;		
		d=chercherDirectionEnnemi(cv);
		System.out.println(d);
		if(d!=null){
		    a = new Attaque(d);
		    System.out.println("Attaque");
		 }
		else{
			if( chercherCaseVide(cv) != null){
		    		a = new Deplacement(chercherCaseVide(cv));
					System.out.println("Deplacement");
			}
		}	
		return a;
	}

	public String getNomBlatte()throws RemoteException {return "BlattoPrez"+(int)(10000.*Math.random());}
	
	private Direction chercherDirectionEnnemi(ChampVision cv)throws RemoteException{
		Direction d=null;
		int vieEnnemi=150;
		if (cv.getDroite() instanceof Sol && ((Sol)(cv.getDroite())).getBlatte() != null){
			vieEnnemi=(((Sol)(cv.getDroite())).getBlatte()).getVies();
			d = Direction.DROITE;
		}
		if (cv.getHaut() instanceof Sol && ((Sol)cv.getHaut()).getBlatte() != null){	
			if(vieEnnemi>(((Sol)(cv.getHaut())).getBlatte()).getVies()){
				vieEnnemi=(((Sol)(cv.getHaut())).getBlatte()).getVies();
				d = Direction.HAUT;
			}
		}
        if (cv.getGauche() instanceof Sol && ((Sol)cv.getGauche()).getBlatte() != null){	
			if(vieEnnemi>(((Sol)(cv.getGauche())).getBlatte()).getVies()){
				vieEnnemi=(((Sol)(cv.getGauche())).getBlatte()).getVies();
				d = Direction.GAUCHE;
			}
		}
      	if (cv.getBas() instanceof Sol && ((Sol)cv.getBas()).getBlatte() != null){	
			if(vieEnnemi>(((Sol)(cv.getGauche())).getBlatte()).getVies()){
				vieEnnemi=(((Sol)(cv.getBas())).getBlatte()).getVies();
				d = Direction.BAS;
			 }
        }
        return d;
	}
	
	private Direction chercherCaseVide(ChampVision cv)throws RemoteException {
	    Direction d = null;
	    if (cv.getDroite() instanceof Sol && ((Sol)(cv.getDroite())).getBlatte() == null){ d = Direction.DROITE; }
        else if (cv.getHaut() instanceof Sol && ((Sol)cv.getHaut()).getBlatte() == null){ d = Direction.HAUT; }
        else if (cv.getGauche() instanceof Sol && ((Sol)cv.getGauche()).getBlatte() == null){ d = Direction.GAUCHE; }
        else if (cv.getBas() instanceof Sol && ((Sol)cv.getBas()).getBlatte() == null){ d = Direction.BAS; }	    
	    return d;
	}
}
