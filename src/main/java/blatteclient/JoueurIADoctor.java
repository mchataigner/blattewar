package blatteclient;
import java.io.*;
import blattewar.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
public class JoueurIADoctor extends UnicastRemoteObject implements Joueur
{
    private Blatte blatte;
	private class Pos
	{
		public int x;
		public int y;
		public Pos(int _x, int _y){x=_x; y=_y;}
		public boolean equals(Pos p)
		{
			return (p.x==x)&&(p.y==y);
		}
	}
    private ArrayList<Pos> memory;
	private Direction lastDir;
    public JoueurIADoctor()throws RemoteException{ memory=new ArrayList<Pos>();}
    public JoueurIADoctor(Blatte b)throws RemoteException{ this.blatte=b; memory=new ArrayList<Pos>(); }
    public String message(String message)throws RemoteException{ return message;	}
    
	public Action determinerAction(Blatte b, ChampVision cv)throws RemoteException{
		Action a=null;
		Direction d=null;		
		if( chercherDirection(cv,b) != null)
		{
	  		a = new Deplacement(chercherDirection(cv,b));
			lastDir=chercherDirection(cv,b);
		}
		memory.add(new Pos(b.getPosition()[0],b.getPosition()[1]));		
		return a;
	}

	public String getNomBlatte()throws RemoteException {return "BlattDoctor"+(int)(10.*Math.random());}
	
	private Direction chercherDirection(ChampVision cv,Blatte b)throws RemoteException {
	    Direction d = null;
	    if (cv.getDroite() instanceof Sol 
			&& ((Sol)(cv.getDroite())).getBlatte() == null
			&& !memory.contains(new Pos(b.getPosition()[0]+1,b.getPosition()[1])))
		{ d = Direction.DROITE; }
        else if (cv.getHaut() instanceof Sol 
			&& ((Sol)cv.getHaut()).getBlatte() == null
			&& !memory.contains(new Pos(b.getPosition()[0],b.getPosition()[1]+1)))
		{ d = Direction.HAUT; }
        else if (cv.getGauche() instanceof Sol 
			&& ((Sol)cv.getGauche()).getBlatte() == null
			&& !memory.contains(new Pos(b.getPosition()[0]-1,b.getPosition()[1])))
		{ d = Direction.GAUCHE; }
        else if (cv.getBas() instanceof Sol 
			&& ((Sol)cv.getBas()).getBlatte() == null
			&& !memory.contains(new Pos(b.getPosition()[0],b.getPosition()[1]-1)))
		{ d = Direction.BAS; }	 
	else
	{
		if(lastDir!=null)
			switch(lastDir)
			{
				case GAUCHE:
				if (cv.getGauche() instanceof Sol 
						&& ((Sol)cv.getGauche()).getBlatte() == null)
					{ d = Direction.GAUCHE; }
				break;
				case DROITE:			
				 if (cv.getDroite() instanceof Sol 
						&& ((Sol)(cv.getDroite())).getBlatte() == null)
					{ d = Direction.DROITE; }
				break;		
				case BAS:
				if (cv.getBas() instanceof Sol 
						&& ((Sol)cv.getBas()).getBlatte() == null)
					{ d = Direction.BAS; }
				break;
				case HAUT:
				if (cv.getHaut() instanceof Sol 
						&& ((Sol)cv.getHaut()).getBlatte() == null)
					{ d = Direction.HAUT; }
				break;
			}
		if(d!=null)
		{		
		 if (cv.getDroite() instanceof Sol 
				&& ((Sol)(cv.getDroite())).getBlatte() == null)
			{ d = Direction.DROITE; }
		else if (cv.getHaut() instanceof Sol 
				&& ((Sol)cv.getHaut()).getBlatte() == null)
			{ d = Direction.HAUT; }
		else if (cv.getGauche() instanceof Sol 
				&& ((Sol)cv.getGauche()).getBlatte() == null)
			{ d = Direction.GAUCHE; }
		else if (cv.getBas() instanceof Sol 
				&& ((Sol)cv.getBas()).getBlatte() == null)
			{ d = Direction.BAS; }	 
		}
	}

	    return d;
	}
}
