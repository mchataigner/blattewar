package blattewar;
import java.io.Serializable;
//@SuppressWarnings("serial")
public class Blatte implements Serializable
{
    private static final int defaultvie=1;
    private int vies;
    private String nom;
    private int[] position=new int[2];//x & y
    public Blatte()
    {
        vies=defaultvie;
        nom="Noname";
    }
    public Blatte(String _nom)
    {
        nom=_nom;
        vies=defaultvie;
    }
    public Blatte(String _nom, int _vies)
    {
        nom=_nom;
        vies=_vies;
    }
    public void setVies(int _vies)
    {
        vies=_vies; /* plutot seulement decrementer vie non ?*/
    }
    public void setNom(String _nom)
    {
        nom=_nom;
    }
    public int getVies()
    {
        return vies;
    }
    public String getNom()
    {
        return nom;
    }
    public int[] getPosition()
    {
        int[] pos=new int[2];
        pos[0]=position[0];
        pos[1]=position[1];
        return pos;
    }
    public void setPosition(int x,int y)
    {
        position[0]=x;
        position[1]=y;
    }
}

