package blattewar.props;

import blattewar.Blatte;


public class ModifVie extends DescPropriete
{
    private int modif;
    private int utils;

    public ModifVie(int id, int modif, int utils)
    {
        super(id);
        this.modif = modif;
        this.utils = utils;
    }

    public int nombreUtilisationsInitial()
    {
        return this.utils;
    }

    public void effet(Blatte b)
    {
        int v = b.getVies() + this.modif;
        if(v < 0)
            v = 0;
        b.setVies(v);
    }
}

