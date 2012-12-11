package blattewar.moteurjeu;
import blattewar.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.swing.Timer;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.StringWriter;
import javax.xml.transform.stream.StreamResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Match
{
    /*
    * Réunis un Joueur, sa blatte, son champVision et son action
    */
    private class JoueurThread extends Thread
    {
        private Joueur j;
        private Action a;
        private ChampVision cv;
        private Blatte b;
        private boolean bool;
        private boolean vivant=true;
        public JoueurThread(Joueur joueur,Blatte bl)
        {
            j=joueur;
            b=bl;
            a=new Attente();
        }
        /// Determiner l'action, si trop long alors attente.
        private void determinerAction()
        {
            //System.out.println("demande action");
            a=new Attente();
            bool=false;
            //try{wait(1001);}catch(Exception e){}	/// for testing purpose
            try
            {
                //System.out.println(j==null);
                a=j.determinerAction(b,cv);
                //System.out.println("action déterminé");
            }
            catch (java.rmi.RemoteException e)
            {
                System.out.println("connection perdue");
                e.printStackTrace();
            }
	    bool=true;
        }
        public boolean aDetermineAction()
        {
            return bool;
        }
        /// Boucle principale du Thread
        /// il faudrait ajouter une condition pour fermer le thread a la fin du match
        synchronized public void run()
        {
            while (vivant)
            {
                try
                {
                    wait();
                    if (b.getVies()==0)
                    {
                        vivant=false;
                    }
                    else
                    {
                        determinerAction();
                        j.message("partie lancée");
                        //Thread.sleep(250);
                    }//notifyAll();
                }
                catch (Exception e)
                {
                    System.out.println("connection perdue");
                    e.printStackTrace();
			return;
                }
            }
        }
        public Action getAction()
        {
            return a;
        }
        public ChampVision getChampVision()
        {
            return cv;
        }
        /// notifie le thread qu'il doit demander l'action.
        synchronized public void demanderAction()
        {
            notifyAll();
        }
        public void setChampVision(ChampVision c)
        {
            cv=c;
        }
        public Blatte getBlatte()
        {
            return b;
        }
        public void sendMessage(String message)
        {
            try
            {
                j.message(message);
            }
            catch (Throwable  e) {};
        }
    }
    private static float SEUILVICTOIRE=(float)0.8;
    private Arene arene;
    private int nbTours;
    private int nbToursMax;
    private int nbJoueursVivants;
    private Document rapport;
    private Element racine,lastRound=null;
    private ArrayList<JoueurThread> joueurs=new ArrayList<JoueurThread>();
    private boolean fini=false;
    private String vainqueur="";
    ///constructeur, initialise les variables, ajoute les joueurs.
    protected Match(Arene _arene, int _nbToursMax, HashMap<Joueur,Blatte> _joueurs)
    {
        this.arene = _arene;
        this.nbToursMax = _nbToursMax;
        this.nbTours = 0;
        this.nbJoueursVivants=_joueurs.size();
for (Map.Entry<Joueur,Blatte> j: _joueurs.entrySet())
            joueurs.add(new JoueurThread(j.getKey(),j.getValue()));
        initialiserRapport();
        initialiserBlattes();
    }
    public boolean estFini()
    {
        return fini;
    }
    public Arene getArene()
    {
        return this.arene;
    }
    ///Lance le match jusqu'a la fin.
    public void lancer()
    {
        boolean victoire=false;
for (JoueurThread j: joueurs)
            j.start();
        ///Boucle executant n tours ou fin de match
        while (nbTours<nbToursMax && !(victoire=jouerTour()));
        ///Si victoire est faux, alors on attribue la victoire a la première blatte ayant le plus de vie.
        if (!victoire)
        {
            Blatte b=null;
for (JoueurThread j:joueurs)
            {
                if (b==null)
                {
                    if (j.getBlatte().getVies()>0)
                        b=j.getBlatte();
                }
                else if (b.getVies() < j.getBlatte().getVies())
                    b=j.getBlatte();
            }
            Element findematch=rapport.createElement("findematch");
            Element v=rapport.createElement("victoire");
            v.appendChild(findematch);
            Element nom=rapport.createElement("nomblatte");
            nom.setTextContent(b.getNom());
            vainqueur=b.getNom();
            v.appendChild(nom);
            racine.appendChild(v);
        }
        //for (JoueurThread j:joueurs)
        //    j.sendMessage("partie finie");
        fini=true;
        System.out.println("Partie finie");
    }
    public String getVainqueur()
    {
        return vainqueur;
    }
    
    /// Executer UN tour.
    synchronized public boolean  jouerTour()
    {
        System.out.println("new tour");
        boolean vic=false;
        boolean actionsDeterminees=false;
        int delay = 1000;
        Timer timer = new Timer(delay,null);
	timer.setRepeats(false);
        Element tourDom=rapport.createElement("tour");
        racine.appendChild(tourDom);
        nbTours++;
        Element numeroTour=rapport.createElement("numerotour");
        numeroTour.setTextContent(""+nbTours);
        tourDom.appendChild(numeroTour);
        Element nombreBlatte=rapport.createElement("nombreblatte");
        nombreBlatte.setTextContent(""+nbJoueursVivants);
        for (JoueurThread j: joueurs)
        {
            j.setChampVision(determinerChampVision(j.getBlatte()));
            try
            {
                j.demanderAction();
            }
            catch (Throwable e)
            {
                System.out.println("connexion perdue");
            }
        }
        try
        {
            timer.start();
            //Thread.sleep(delay);
            while (!actionsDeterminees && timer.isRunning() )
            {
                actionsDeterminees=true;
                for (JoueurThread j: joueurs)
                    actionsDeterminees=j.aDetermineAction() && actionsDeterminees;
                //System.out.println(actionsDeterminees);
                //Thread.sleep(150);
            }
            //wait(500);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ArrayList<JoueurThread> jtemp=(ArrayList<JoueurThread>)(joueurs.clone());
        JoueurThread j2;
        for (int k=joueurs.size(); k>0; k--)
        {
            j2=jtemp.remove((int)(k*Math.random()));
            if (j2.getBlatte().getVies()>0)
            {
                if (j2.getAction() instanceof Attaque)
                    vic=executerAttaque(j2.getBlatte(), (Attaque)j2.getAction(),tourDom) ||vic;
                if (j2.getAction() instanceof Deplacement)
                    vic=executerDeplacement(j2.getBlatte(), (Deplacement)j2.getAction(), tourDom) || vic;
                if (j2.getAction() instanceof Attente)
                    vic=executerAttente(j2.getBlatte(),tourDom) || vic;
            }
        }
        tourDom.appendChild(nombreBlatte);
        if (nbJoueursVivants==1)
        {
            vic=true;
            Element derniere=rapport.createElement("dernieresurvivante");
            Element nom=rapport.createElement("nomblatte");
for (JoueurThread j: joueurs)
                if (j.getBlatte().getVies()!=0)
                {
                    nom.setTextContent(j.getBlatte().getNom());
                    break;
                }
            Element victoire=rapport.createElement("victoire");
            victoire.appendChild(derniere);
            victoire.appendChild(nom);
            racine.appendChild(victoire);
        }
        lastRound=(Element)(tourDom.cloneNode(true));
        /// TODO ajouter les positions de toutes les blattes et la map ? lastRound.
        return vic;
    }
    ///execute une action pour une blatte donnee.
    ///ajoute egalement le résultat au DOM du tour actuel.
    private boolean executerAttaque(Blatte b, Attaque a,Element tourDom)
    {
        boolean vic=false;
        Element e=rapport.createElement("attaque");
        Element nomBlatte=rapport.createElement("nomblatte");
        Element posBlatte=rapport.createElement("positionblatte");
        Element posX=rapport.createElement("x");
        Element posY=rapport.createElement("y");
        nomBlatte.setTextContent(b.getNom());
        posX.setTextContent(""+b.getPosition()[0]);
        posY.setTextContent(""+b.getPosition()[1]);
        posBlatte.appendChild(posX);
        posBlatte.appendChild(posY);
        e.appendChild(nomBlatte);
        e.appendChild(posBlatte);
        tourDom.appendChild(e);
        Element d=rapport.createElement("direction");
        d.setTextContent(""+(a).getDirection());
        e.appendChild(d);
        int[] pos=b.getPosition();
        Element r=null; ///Element représentant le resultat
        ///On recupere la direction, on test l'existence de la case, si la case est un sol, si la case contient une blatte, si oui on fight sinon echec
        switch ((a).getDirection())
        {
        case HAUT:
            if (arene.getHauteur()>pos[1])
                if (arene.getCase(pos[0],pos[1]+1) instanceof Sol)
                    if (((Sol)arene.getCase(pos[0],pos[1]+1)).getBlatte()!=null)
                    {
                        if (Math.random()>SEUILVICTOIRE)
                            r=victoireBlatte(b,((Sol)arene.getCase(pos[0],pos[1]+1)).getBlatte());
                        else
                            r=victoireBlatte(((Sol)arene.getCase(pos[0],pos[1]+1)).getBlatte(),b);
                    }
            break;
        case BAS:
            if (1<pos[1])
                if (arene.getCase(pos[0],pos[1]-1) instanceof Sol)
                    if (((Sol)arene.getCase(pos[0],pos[1]-1)).getBlatte()!=null)
                    {
                        if (Math.random()>SEUILVICTOIRE)
                            r=victoireBlatte(b,((Sol)arene.getCase(pos[0],pos[1]-1)).getBlatte());
                        else
                            r=victoireBlatte(((Sol)arene.getCase(pos[0],pos[1]-1)).getBlatte(),b);
                    }
            break;
        case GAUCHE:
            if (1<pos[0])
                if (arene.getCase(pos[0]-1,pos[1]) instanceof Sol)
                    if (((Sol)arene.getCase(pos[0]-1,pos[1])).getBlatte()!=null)
                    {
                        if (Math.random()>SEUILVICTOIRE)
                            r=victoireBlatte(b,((Sol)arene.getCase(pos[0]-1,pos[1])).getBlatte());
                        else
                            r=victoireBlatte(((Sol)arene.getCase(pos[0]-1,pos[1])).getBlatte(),b);
                    }
            break;
        case DROITE:
            if (arene.getLargeur()>pos[0])
                if (arene.getCase(pos[0]+1,pos[1]) instanceof Sol)
                    if (((Sol)arene.getCase(pos[0]+1,pos[1])).getBlatte()!=null)
                    {
                        System.out.println(""+((Sol)arene.getCase(pos[0]+1,pos[1])).getBlatte()+" "+b);
                        if (Math.random()>SEUILVICTOIRE)
                            r=victoireBlatte(b,((Sol)arene.getCase(pos[0]+1,pos[1])).getBlatte());
                        else
                            r=victoireBlatte(((Sol)arene.getCase(pos[0]+1,pos[1])).getBlatte(),b);
                    }
            break;
        }
        if (r==null)
            r=rapport.createElement("echec");
        e.appendChild(r);
        return vic;
    }
    private boolean executerDeplacement(Blatte b, Deplacement a,Element tourDom)
    {
        boolean vic=false;
        Element e=rapport.createElement("deplacement");
        Element nomBlatte=rapport.createElement("nomblatte");
        Element posBlatte=rapport.createElement("positionblatte");
        Element posX=rapport.createElement("x");
        Element posY=rapport.createElement("y");
        nomBlatte.setTextContent(b.getNom());
        posX.setTextContent(""+b.getPosition()[0]);
        posY.setTextContent(""+b.getPosition()[1]);
        posBlatte.appendChild(posX);
        posBlatte.appendChild(posY);
        e.appendChild(nomBlatte);
        e.appendChild(posBlatte);
        tourDom.appendChild(e);
        Element d=rapport.createElement("direction");
        d.setTextContent(""+(a).getDirection());
        e.appendChild(d);
        int[] pos=b.getPosition();
        Element r=null;/// Represente le resultat (echec ou succes
        ///On recupere la direction, puis on verfie l'existence de la case, que c'est un sol et l'absence de blatte
	int viePre=b.getVies();
        switch ((a).getDirection())
        {
        case HAUT:
            if (arene.getHauteur()>pos[1])
                if (arene.getCase(pos[0],pos[1]+1) instanceof Sol)
                    if (((Sol)arene.getCase(pos[0],pos[1]+1)).getBlatte()==null)
                    {
                        r=rapport.createElement("succes");
                        ((Sol)arene.getCase(pos[0],pos[1])).setBlatte(null);
                        b.setPosition(pos[0],pos[1]+1);
                        ((Sol)arene.getCase(pos[0],pos[1]+1)).setBlatte(b);
                    }
            break;
        case BAS:
            if (1<pos[1])
                if (arene.getCase(pos[0],pos[1]-1) instanceof Sol)
                    if (((Sol)arene.getCase(pos[0],pos[1]-1)).getBlatte()==null)
                    {
                        r=rapport.createElement("succes");
                        ((Sol)arene.getCase(pos[0],pos[1])).setBlatte(null);
                        b.setPosition(pos[0],pos[1]-1);
                        ((Sol)arene.getCase(pos[0],pos[1]-1)).setBlatte(b);
                    }
            break;
        case GAUCHE:
            if (1<pos[0])
                if (arene.getCase(pos[0]-1,pos[1]) instanceof Sol)
                    if (((Sol)arene.getCase(pos[0]-1,pos[1])).getBlatte()==null)
                    {
                        r=rapport.createElement("succes");
                        ((Sol)arene.getCase(pos[0],pos[1])).setBlatte(null);
                        b.setPosition(pos[0]-1,pos[1]);
                        ((Sol)arene.getCase(pos[0]-1,pos[1])).setBlatte(b);
                    }
            break;
        case DROITE:
            if (arene.getLargeur()>pos[0])
            {
                if (arene.getCase(pos[0]+1,pos[1]) instanceof Sol)
                {
                    if (((Sol)arene.getCase(pos[0]+1,pos[1])).getBlatte()==null)
                    {
                        r=rapport.createElement("succes");
                        ((Sol)arene.getCase(pos[0],pos[1])).setBlatte(null);
                        b.setPosition(pos[0]+1,pos[1]);
                        ((Sol)arene.getCase(pos[0]+1,pos[1])).setBlatte(b);
                    }
                }
            }
            break;
        }
	if(viePre>b.getVies())
		initialiserBlatte(b);
	if (b.getVies()==0)
	{
            	blatteEliminee(b,e);        	
		((Sol)arene.getCase(b.getPosition()[0],b.getPosition()[1])).setBlatte(null);
	}
	if (r==null)
            r=rapport.createElement("echec");
        e.appendChild(r);
        ///Si jamais on est sur une case Sortie, alors on gagne
        if (arene.getCase(b.getPosition()[0],b.getPosition()[1]) instanceof Sortie)
        {
            Element sortietrouvee=rapport.createElement("sortie");
            Element victoire=rapport.createElement("victoire");
            victoire.appendChild(sortietrouvee);
            Element nom=rapport.createElement("nomblatte");
            nom.setTextContent(b.getNom());
            victoire.appendChild(nom);
            racine.appendChild(victoire);
            vic=true;
        }
        return vic;
    }
    private boolean executerAttente(Blatte b,Element tourDom)
    {
        boolean vic=false;
        Element e=rapport.createElement("attente");
        Element nomBlatte=rapport.createElement("nomblatte");
        Element posBlatte=rapport.createElement("positionblatte");
        Element posX=rapport.createElement("x");
        Element posY=rapport.createElement("y");
        nomBlatte.setTextContent(b.getNom());
        posX.setTextContent(""+b.getPosition()[0]);
        posY.setTextContent(""+b.getPosition()[1]);
        posBlatte.appendChild(posX);
        posBlatte.appendChild(posY);
        e.appendChild(nomBlatte);
        e.appendChild(posBlatte);
        tourDom.appendChild(e);
        return vic;
    }
    /*
    private boolean executerAction(Blatte b, Action a,Element tourDom)
    {
    	Element e;
    	boolean vic=false;
    	Element nomBlatte=rapport.createElement("nomblatte");
    	nomBlatte.setTextContent(b.getNom());
    	Element posBlatte=rapport.createElement("positionblatte");
    	Element posX=rapport.createElement("x");
    	Element posY=rapport.createElement("y");
    	posX.setTextContent(""+b.getPosition()[0]);
    	posY.setTextContent(""+b.getPosition()[1]);
    	posBlatte.appendChild(posX);
    	posBlatte.appendChild(posY);
    	if(a instanceof Attaque)
    	{
    		e=rapport.createElement("attaque");
    		Element d=rapport.createElement("direction");
    		d.setTextContent(""+((Attaque)a).getDirection());
    		e.appendChild(d);
    		int[] pos=b.getPosition();
    		Element r=null; ///Element représentant le resultat
    		///On recupere la direction, on test l'existence de la case, si la case est un sol, si la case contient une blatte, si oui on fight sinon echec
    		switch((Direction)(((Attaque)a).getDirection()))
    		{
    			case HAUT:
    				if(arene.getHauteur()>pos[1])
    					if(arene.getCase(pos[0],pos[1]+1) instanceof Sol)
    						if(((Sol)arene.getCase(pos[0],pos[1]+1)).getBlatte()!=null)
    						{
    							if(Math.random()>0.5)
    								victoireBlatte(r,b,((Sol)arene.getCase(pos[0],pos[1]+1)).getBlatte());
    							else
    								victoireBlatte(r,((Sol)arene.getCase(pos[0],pos[1]+1)).getBlatte(),b);
    						}
    				break;
    			case BAS:
    				if(1<pos[1])
    					if(arene.getCase(pos[0],pos[1]-1) instanceof Sol)
    						if(((Sol)arene.getCase(pos[0],pos[1]-1)).getBlatte()!=null)
    						{
    							if(Math.random()>0.5)
    								victoireBlatte(r,b,((Sol)arene.getCase(pos[0],pos[1]-1)).getBlatte());
    							else
    								victoireBlatte(r,((Sol)arene.getCase(pos[0],pos[1]-1)).getBlatte(),b);
    						}
    				break;
    			case GAUCHE:
    				if(1<pos[0])
    					if(arene.getCase(pos[0]-1,pos[1]) instanceof Sol)
    						if(((Sol)arene.getCase(pos[0]-1,pos[1])).getBlatte()!=null)
    						{
    							if(Math.random()>0.5)
    								victoireBlatte(r,b,((Sol)arene.getCase(pos[0]-1,pos[1])).getBlatte());
    							else
    								victoireBlatte(r,((Sol)arene.getCase(pos[0]-1,pos[1])).getBlatte(),b);
    						}
    				break;
    			case DROITE:
    				if(arene.getLargeur()>pos[0])
    					if(arene.getCase(pos[0]+1,pos[1]) instanceof Sol)
    						if(((Sol)arene.getCase(pos[0]+1,pos[1])).getBlatte()!=null)
    						{
    						System.out.println("Long bacon");
    							if(Math.random()>0.5)
    								victoireBlatte(r,b,((Sol)arene.getCase(pos[0]+1,pos[1])).getBlatte());
    							else
    								victoireBlatte(r,((Sol)arene.getCase(pos[0]+1,pos[1])).getBlatte(),b);
    						}
    				break;
    		}
    		if(r==null)
    			r=rapport.createElement("echec");
    		e.appendChild(r);
    	}
    	else if(a instanceof Deplacement)
    	{
    		e=rapport.createElement("deplacement");
    		Element d=rapport.createElement("direction");
    		d.setTextContent(""+((Deplacement)a).getDirection());
    		e.appendChild(d);
    		int[] pos=b.getPosition();
    		Element r=null;/// Represente le resultat (echec ou succes
    		///On recupere la direction, puis on verfie l'existence de la case, que c'est un sol et l'absence de blatte
    		switch((Direction)((Deplacement)a).getDirection())
    		{
    			case HAUT:
    				if(arene.getHauteur()>pos[1])
    					if(arene.getCase(pos[0],pos[1]+1) instanceof Sol)
    						if(((Sol)arene.getCase(pos[0],pos[1]+1)).getBlatte()!=null)
    						{
    							r=rapport.createElement("succes");
    							((Sol)arene.getCase(pos[0],pos[1])).setBlatte(null);
    							b.setPosition(pos[0],pos[1]+1);
    							((Sol)arene.getCase(pos[0],pos[1]+1)).setBlatte(b);
    						}
    				break;
    			case BAS:
    				if(1<pos[1])
    					if(arene.getCase(pos[0],pos[1]-1) instanceof Sol)
    						if(((Sol)arene.getCase(pos[0],pos[1]-1)).getBlatte()!=null)
    						{
    							r=rapport.createElement("succes");
    							((Sol)arene.getCase(pos[0],pos[1])).setBlatte(null);
    							b.setPosition(pos[0],pos[1]-1);
    							((Sol)arene.getCase(pos[0],pos[1]-1)).setBlatte(b);
    						}
    				break;
    			case GAUCHE:
    				if(1<pos[0])
    					if(arene.getCase(pos[0]-1,pos[1]) instanceof Sol)
    						if(((Sol)arene.getCase(pos[0]-1,pos[1])).getBlatte()!=null)
    						{
    							r=rapport.createElement("succes");
    							((Sol)arene.getCase(pos[0],pos[1])).setBlatte(null);
    							b.setPosition(pos[0]-1,pos[1]);
    							((Sol)arene.getCase(pos[0]-1,pos[1])).setBlatte(b);
    						}
    				break;
    			case DROITE:
    				if(arene.getLargeur()>pos[0]){
    					if(arene.getCase(pos[0]+1,pos[1]) instanceof Sol){
    						if(((Sol)arene.getCase(pos[0]+1,pos[1])).getBlatte()==null)
    						{
    							r=rapport.createElement("succes");
    							((Sol)arene.getCase(pos[0],pos[1])).setBlatte(null);
    							b.setPosition(pos[0]+1,pos[1]);
    							((Sol)arene.getCase(pos[0]+1,pos[1])).setBlatte(b);
    						}
    					}
    				}
    				break;
    		}
    		if(r==null)
    			r=rapport.createElement("echec");
    		e.appendChild(r);
    		///Si jamais on est sur une case Sortie, alors on gagne
    		if(arene.getCase(b.getPosition()[0],b.getPosition()[1]) instanceof Sortie)
    		{
    			Element sortietrouvee=rapport.createElement("sortie");
    			Element victoire=rapport.createElement("victoire");
    			victoire.appendChild(sortietrouvee);
    			Element nom=rapport.createElement("nomblatte");
    			nom.setTextContent(b.getNom());
    			victoire.appendChild(nom);
    			racine.appendChild(victoire);
    			vic=false;
    		}
    	}
    	else if(a instanceof Attente)
    	{
    		e=rapport.createElement("attente");
    		/// Attente= glander
    	}
    	else
    	{
    		e=rapport.createElement("erreuraction");
    	}
    	e.appendChild(nomBlatte);
    	e.appendChild(posBlatte);
    	tourDom.appendChild(e);
    	return vic;
    }*/
    ///Victoire de g sur p dans un duel, Element e pour ajouter au DOM
    private Element victoireBlatte(Blatte g,Blatte p)
    {
        Element e=rapport.createElement("succes");
        Element blattegagnante=rapport.createElement("blattegagnante");
        blattegagnante.setTextContent(g.getNom());
        e.appendChild(blattegagnante);
        Element blatteperdante=rapport.createElement("blatteperdante");
        blatteperdante.setTextContent(p.getNom());
        e.appendChild(blatteperdante);
        ((Sol)arene.getCase(p.getPosition()[0],p.getPosition()[1])).setBlatte(null);
        p.setVies(p.getVies()-1);
        if (p.getVies()==0)
            blatteEliminee(p,e);
        else
            initialiserBlatte(p);
        return e;
    }
    /// si une blatte n'a plus de vie il faut effectuer les actions suivantes:
    private void blatteEliminee(Blatte b, Element e)
    {
        Element eliminee=rapport.createElement("blatteeliminee");
        eliminee.setTextContent(b.getNom());
        e.appendChild(eliminee);
        nbJoueursVivants--;
    }
    /// Obvious
    private ChampVision determinerChampVision(Blatte b)
    {
        int[] pos=b.getPosition();
        ChampVision res=new ChampVision();
        if (arene.getHauteur()>pos[1])
            res.setHaut(arene.getCase(pos[0],pos[1]+1));
        if (1<pos[1])
            res.setBas(arene.getCase(pos[0],pos[1]-1));
        if (1<pos[0])
            res.setGauche(arene.getCase(pos[0]-1,pos[1]));
        if (arene.getLargeur()>pos[0])
            res.setDroite(arene.getCase(pos[0]+1,pos[1]));
        return res;
    }
    ///prend un Depart au hasard et cole une blatte
    //TODO Ajouter une verification de l'absence de blatte?
    private void initialiserBlatte(Blatte b)
    {
        ArrayList<Depart> d=arene.getDeparts();
        int i=(int)(((float)d.size()-1)*Math.random());
        b.setPosition(arene.getPosition(d.get(i))[0],arene.getPosition(d.get(i))[1]);
        d.get(i).setBlatte(b);
    }
    /// initialiser toutes les blattes des joueurs
    private void initialiserBlattes()
    {
        ArrayList<Depart> d=arene.getDeparts();
        int i=0;
for (JoueurThread j: joueurs)
        {
            j.getBlatte().setPosition(arene.getPosition(d.get(i))[0],arene.getPosition(d.get(i))[1]);
            d.get(i).setBlatte(j.getBlatte());
            i++;
            i=i%d.size();
        }
    }
    /// retourne le rapport du match depuis le debut jusqu'au dernier etat (normalement appele pour obtenir le rapport en fin de partie)
    public String getRapport()
    {
        try
        {
            rapport.appendChild(racine);
            DOMSource domSource = new DOMSource(rapport);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            String StringResult = writer.toString();
            rapport.removeChild(racine);
            return StringResult;
        }
        catch (Exception e)
        {
            return "<rapport><erreur>Écriture du rapport échouée</erreur></rapport>";
        }
    }
    /// Retourne le xml pour le dernier tour
    public String getRapportDernierTour()
    {
        if (lastRound==null)
            return "<tour><erreur>Match non commencé</erreur></tour>";
        try
        {
            rapport.appendChild(lastRound);//TODO Replace this with last round
            DOMSource domSource = new DOMSource(rapport);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            String StringResult = writer.toString();
            rapport.removeChild(lastRound);//TODO same
            return StringResult;
        }
        catch (Exception e)
        {
            return "<tour><erreur>Écriture du rapport échouée</erreur></tour>";
        }
    }
    ///creer le document rapport et la racine.
    private void initialiserRapport()
    {
        try
        {
            DocumentBuilderFactory fact= DocumentBuilderFactory.newInstance();
            DocumentBuilder db=fact.newDocumentBuilder();
            rapport=db.newDocument();
            rapport.setXmlVersion("1.0");
            rapport.setXmlStandalone(true);
            racine=rapport.createElement("rapport");
            racine.appendChild(rapport.createComment("Rapport de match"));
            Element nombreJoueurs=rapport.createElement("nombrejoueurs");
            nombreJoueurs.setTextContent(""+joueurs.size());
            racine.appendChild(nombreJoueurs);
            Element nombreToursMax=rapport.createElement("nombretoursmax");
            nombreToursMax.setTextContent(""+nbToursMax);
            racine.appendChild(nombreToursMax);
        }
        catch (Exception e)
        {
            System.err.println("Problème dans l'initialisation du rapport");
        }
    }
}

