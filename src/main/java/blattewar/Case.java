package blattewar;

import blattewar.props.Propriete;
import blattewar.props.DescProprieteVide;

import java.io.Serializable;


public abstract class Case implements Serializable
{
    private Propriete propriete;
    public Case()
    {
        this.setPropriete(new Propriete(DescProprieteVide.instance()));
    }
    public Case(Propriete _p)
    {
        this.setPropriete(_p);
    }
    public Propriete getPropriete()
    {
        return this.propriete;
    }
    public void setPropriete(Propriete _p)
    {
        this.propriete = _p;
    }
}

