package blattewar;
import blattewar.moteurjeu.*;
import javax.ejb.Stateful;
import java.lang.Exception;
import java.io.*;
import java.rmi.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

@Stateful
public class Simulateur implements SimulateurLocal, SimulateurRemote
{
    private Arene blatteArena;
    private static Match m;
    private static MoteurJeu mj;
    private static int nbJoueurs;
	private static boolean fileChecked=false;
    public Simulateur()
    {
	System.out.println("init");/*
	Arene ar=new Arene(25,25);
	MoteurJeu mj=new MoteurJeu(2,50,5000,ar);
	try{
		for(int i=0;i<10;i++)
			mj.ajouterJoueur(new JoueurIALocal());
		for(int i=0;i<40;i++)
			mj.ajouterJoueur(new JoueurIALocalMoyen());
		Match m=mj.nouveauMatch();
		m.lancer();
		BufferedWriter out=new BufferedWriter(new FileWriter("rapport.xml"));
		out.write(m.getRapport());
		out.close();
	}
	catch(Exception e){ System.out.println("problème mj");
		e.printStackTrace();
	}*/

    }
    public Simulateur(Arene _arene, int _nbJoueurs)
    {
        setBlatteArena(_arene);
        nbJoueurs=_nbJoueurs;
    }
    public int getNbJoueurs()
    {
        return nbJoueurs;
    }
    public void setNbJoueurs(int nbJoueurs)
    {
        this.nbJoueurs = nbJoueurs;
    }
    public void setBlatteArena(Arene blatteArena)
    {
        this.blatteArena = blatteArena;
    }
    public Arene getBlatteArena()
    {
        //System.out.println(m.estFini());
        
        if(m!=null )//&& !m.estFini())
            return m.getArene();
        else
            return null;
    }
    public boolean matchFini()
    {
        return m.estFini();
    }
    public String vainqueur()
    {
        return m.getVainqueur();
    }
    public String helloSimulation()
    {
        System.out.println("hello");
        
        return "hello ";
    }
    public boolean partieEnCours()
    {
        return m!=null&&!m.estFini();
    }
    private class MatchThread extends Thread
    {
        private Match m;
        public MatchThread(Match m)
        {
            this.m=m;
        }
        public void run()
        {
            m.lancer();
            try
            {
		String date=(new SimpleDateFormat("yy_MM_dd__hh_mm_ss")).format(new Date());
		date=date.replaceAll("[/ :]","");
                BufferedWriter out=new BufferedWriter(
			new FileWriter(System.getProperty("user.home")+"/.blattewar/rapport/rapport_"+date+".xml")
		);
	        out.write(m.getRapport());
                out.close();
            }
            catch(Throwable e){e.printStackTrace();}
        }
    }
    
    public String rejoindrePartie(Remote j)throws RemoteException
    {
        if(m!=null && m.estFini())
        {
            m=null;
            mj=null;
            nbJoueurs=0;
        }
        if(mj==null)
        {
            mj=new MoteurJeu();
        }
        if(nbJoueurs<10 && m==null)
        {
            try
            {
                mj.ajouterJoueur((Joueur)j);
                nbJoueurs++;
            }
            catch(Throwable e){return e.getStackTrace().toString();}
            if(nbJoueurs ==10)
            {
                m=mj.nouveauMatch();
                Thread lematch=new MatchThread(m);
                lematch.start();
		        
            }
            return "Partie rejointe";
        }
        
        
        return "partie déjà en cours";
    }
    public String helloSimulation(Remote j)throws RemoteException
    {
        return "hello "+((Joueur)j).getNomBlatte();
    }
}
