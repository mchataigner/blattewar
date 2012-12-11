package blattewar.props;
import java.io.Serializable;
import blattewar.Blatte;

import java.io.Serializable;


public abstract class DescPropriete implements Serializable
{
    private int id;

    protected DescPropriete(int _id)
    {
        this.id = _id;
    }

    public void setId(int _id)
    {
        this.id = _id;
    }

    public int getId()
    {
        return this.id;
    }

    public boolean equals(DescPropriete p)
    {
        return this.id == p.id;
    }

    public abstract void effet(Blatte b);

    public abstract int nombreUtilisationsInitial();
}

