package blattewar.moteurjeu;

import java.io.*;
import blattewar.*;
import blattewar.props.*;

import java.util.ArrayList;


public class ChargeurArene
{
    public static Arene charger(String fichier, ProprietesDispo props) throws Exception
    {
        return charger(new BufferedReader(
                         new InputStreamReader(
                           new FileInputStream(fichier))), props);
    }

    public static Arene charger(BufferedReader br, ProprietesDispo props) throws Exception
    {
        ArrayList<Case> cases = new ArrayList<Case>(); 
        String ligne;
        int hauteur = 0, largeur = 0;
        while((ligne = br.readLine()) != null)
        {
            ligne = ligne.trim().toLowerCase();
            if(ligne.equals(""))
                continue;
            String[] elems = ligne.split("\\s+");
            largeur = elems.length;
            for(int i=0; i<elems.length; i++)
            {
                Case c;
                switch(elems[i].charAt(0))
                {
                    case 'x': c = new Mur();
                              break;
                    case 'd': c = new Depart();
                              break;
                    case 's': c = new Sortie();
                              break;
                    case '_': c = new Sol();
                              break;
                    default:
                        throw new IOException("N'est pas un format d'arène valide");
                }
                String idProp = elems[i].substring(1);
                if(!idProp.equals("")){
			try{
	                    c.setPropriete(new Propriete(props.get(Integer.parseInt(idProp))));
			}
			catch(Exception e ){
				System.out.println("Soucis setPropriete(int)");
			}
			
		}
                cases.add(c);
            }
            hauteur++;
        }

        return new Arene(hauteur, largeur, cases);
    }
}

