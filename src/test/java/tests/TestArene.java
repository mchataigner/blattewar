package tests;

import blattewar.Arene;
import blattewar.props.*;
import blattewar.Blatte;
import blattewar.moteurjeu.ChargeurArene;


public class TestArene
{
    public static void main(String[] args) throws Exception
    {
        ProprietesDispo props = new ProprietesDispo();
        props.push(new DescProprieteVide());
        props.push(new ModifVie(1, -1, 1));
        props.push(new ModifVie(2, 1, 1));
        Arene ar = ChargeurArene.charger(args[0], props);
        System.out.println(ar.toString());
    }
}

