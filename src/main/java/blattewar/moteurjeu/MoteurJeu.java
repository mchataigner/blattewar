package blattewar.moteurjeu;

import blattewar.*;
import blattewar.props.*;

import java.lang.Exception;
import java.util.HashMap;
public class MoteurJeu
{
    private int viesDeDepart=5;
    private int maxJoueurs=10;
    private int nbToursMax=500;
    private Arene ar=null;
    private HashMap<Joueur,Blatte> joueurs=new HashMap<Joueur,Blatte>();
    public MoteurJeu()
    {          }
    public MoteurJeu(int vies, int _maxJoueurs, int _nbToursMax, Arene _ar)
    {
        viesDeDepart=vies;
        maxJoueurs=_maxJoueurs;
        nbToursMax=_nbToursMax;
        ar=_ar;
    }

    private ProprietesDispo chargerProps()
    {
        ProprietesDispo props = new ProprietesDispo();
        try{
            props.push(new DescProprieteVide());
            props.push(new ModifVie(1, -1, 1));
            props.push(new ModifVie(2, 1, 1));
        }
        catch(Throwable e){}
        return props;
    }

    public Match nouveauMatch(String arene)
    {
        try
        {
            ar=ChargeurArene.charger(arene, this.chargerProps());
        }
        catch(Throwable e){};
        return nouveauMatch();
    }
    public Match nouveauMatch()
    {
        try
        {
            if(ar==null)
                ar = ChargeurArene.charger(System.getProperty("user.home")+"/.blattewar/arenes/demo.ar", this.chargerProps());
        }
        catch(Throwable e){};
        if(ar==null)
            ar=new Arene(25,25);
        Match m=new Match(ar,nbToursMax,joueurs);
        //m.lancer();
        return m;
    }
    public void ajouterJoueur(Joueur j) throws Exception
    {
        if(joueurs.size()>=maxJoueurs)
            throw new Exception("Max Joueurs Atteint");
        Blatte b=new Blatte(j.getNomBlatte(),viesDeDepart);
        joueurs.put(j,b);
    }
    public void enleverJoueur(Joueur j) throws Exception
    {
        if(!joueurs.containsKey(j))
            return;
        joueurs.remove(j);
    }

}

