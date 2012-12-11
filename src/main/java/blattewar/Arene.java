package blattewar;

import java.util.ArrayList;
import java.lang.StringBuffer;


public class Arene 
{
    private int largeur, hauteur;
    private ArrayList<Case> cases;
    public Arene(int _hauteur, int _largeur)
    {
        this.largeur = _largeur;
        this.hauteur = _hauteur;
        this.initialiserArene();
    }
    public Arene(int _hauteur, int _largeur, ArrayList<Case> _cases)
    {
        this.largeur = _largeur;
        this.hauteur = _hauteur;
        this.cases = _cases;
    }
    public int getHauteur()
    {
        return hauteur;
    }
    public int getLargeur()
    {
        return largeur;
    }
    public void setCase(int x, int y, Case _case)
    {
        this.cases.add((y-1)*this.largeur+x-1, _case);
    }
    public Case getCase(int x, int y)
    {
        return this.cases.get((y-1)*this.largeur+x-1);
    }
    public int[] getPosition(Case c)
    {
        int i=cases.indexOf(c);
        int pos[]=new int[2];
        pos[0]=i%largeur+1;
        pos[1]=i/largeur+1;
        return pos;
    }
    public int getNbCases()
    {
        return cases.size();
    }
    public ArrayList<Case> getCases()
    {
        return this.cases;
    }
    private void initialiserArene()
    {
        cases=new ArrayList<Case>();
        
        for (int i=0; i<=this.hauteur*this.largeur-1; i++)
        {
            if (i%5==0)
                this.cases.add(new Depart());
//            else if (i%51==0)
//                this.cases.add(new Sortie());
            else this.cases.add(new Sol());
        }
    }
    public ArrayList<Depart> getDeparts()
    {
        ArrayList<Depart> r=new ArrayList<Depart>();
        for(Case c : cases)
            if(c instanceof Depart)
                r.add((Depart)c);
        return r;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        int nbCasesLignesParcourues = 0;
        for(Case c : cases)
        {
            if(c instanceof Mur)
                sb.append('X');
            else if(c instanceof Depart)
                sb.append('D');
            else if(c instanceof Sortie)
                sb.append('S');
            else if(c instanceof Sol)
                sb.append('_');
            
            if(c.getPropriete() != null)
            {
                sb.append(c.getPropriete().getId());
            }

            sb.append(" ");

            nbCasesLignesParcourues++;
            if(nbCasesLignesParcourues >= this.largeur)
            {
                nbCasesLignesParcourues = 0;
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}

