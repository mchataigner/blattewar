package blattewar;

import blattewar.props.Propriete;


public class Sol extends Case
{
    private Blatte blatte;

    public Sol()
    {
        blatte=null;
    }
    public Sol(Propriete p)
    {
        super(p);
    }
    public Blatte getBlatte()
    {
        return this.blatte;
    }
    public void setBlatte(Blatte b) //throws Exception
    {
        // if (this.blatte!=null)
        //   throw new Exception("Il y a déjà une blatte ici");
        this.blatte=b;
        getPropriete().appliquerPropriete(b);
    }
}

