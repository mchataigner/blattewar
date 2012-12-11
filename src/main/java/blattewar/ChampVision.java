package blattewar;
import java.util.HashMap;
import java.io.Serializable;
@SuppressWarnings("serial")
public class ChampVision implements Serializable
{
    private  HashMap<Direction,Case> champ;
    public ChampVision()
    {
        this.champ=new HashMap<Direction,Case>();
    }
    public HashMap<Direction,Case> getChamp()
    {
        return this.champ;
    }
    public Case getHaut()
    {
        return this.champ.get(Direction.HAUT);
    }
    public Case getGauche()
    {
        return this.champ.get(Direction.GAUCHE);
    }
    public Case getDroite()
    {
        return this.champ.get(Direction.DROITE);
    }
    public Case getBas()
    {
        return this.champ.get(Direction.BAS);
    }
    public void setChamp(HashMap<Direction,Case> _champ)
    {
        this.champ.putAll(_champ);
    }
    public void setHaut(Case _case)
    {
        this.champ.put(Direction.HAUT,_case);
    }
    public void setGauche(Case _case)
    {
        this.champ.put(Direction.GAUCHE,_case);
    }
    public void setDroite(Case _case)
    {
        this.champ.put(Direction.DROITE,_case);
    }
    public void setBas(Case _case)
    {
        this.champ.put(Direction.BAS,_case);
    }
}
