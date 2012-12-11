package blattewar.props;

import blattewar.Blatte;

import java.io.Serializable;


public class DescProprieteVide extends DescPropriete implements Serializable
{
    private static DescProprieteVide singleton = null;

    public DescProprieteVide()
    {
        super(0);
    }

    public DescProprieteVide(int _id)
    {
        super(_id);
    }

    public DescProprieteVide(DescPropriete prop)
    {
        super(prop.getId());
    }
    
    public static DescProprieteVide instance()
    {
        if(singleton == null)
            singleton = new DescProprieteVide();
        return singleton;
    }

    public void effet(Blatte b)
    {}

    public int nombreUtilisationsInitial()
    {
        return 0;  // Aucune utilisation
    }
}

