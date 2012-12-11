package blattewar.props;

import blattewar.Blatte;

import java.io.Serializable;


public class Propriete implements Serializable
{
    private int nbUtils;
    private DescPropriete desc;

    public Propriete()
    {
        this.desc = DescProprieteVide.instance();
    }

    public Propriete(DescPropriete _desc)
    {
        this.desc = _desc;
        this.nbUtils = this.desc.nombreUtilisationsInitial();
        if(this.nbUtils < 0)
            this.nbUtils = -1;
    }

    public int getId()
    {
        return this.desc.getId();
    }

    public boolean toujoursActive()
    {
        return this.nbUtils > 0 || this.nbUtils == -1;
    }

    public final void appliquerPropriete(Blatte b)
    {
        if(this.toujoursActive())
        {
            this.desc.effet(b);
            if(this.nbUtils > 0)
                this.nbUtils--;
        }
    }
}

