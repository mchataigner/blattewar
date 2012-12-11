package blatteclient;
import blattewar.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.lang.Exception;
import java.rmi.RMISecurityManager;
import java.util.Scanner;
import java.util.ArrayList;
public class Client
{
    public static void main(String[] args)
    {
        try
        {
            String address=null;
            if(args.length>0)
                address=args[0];
            else
                System.exit(0);
            //System.setProperty("java.security.policy","client.policy");
            //System.setSecurityManager(new RMISecurityManager());
            Properties prop = new Properties();
            prop.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");  
            prop.setProperty(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");      
            prop.setProperty(Context.PROVIDER_URL, "jnp://"+address+":1099");
            Context ctx = new InitialContext(prop);

            Object ref=ctx.lookup("BlatteWar/Simulateur/remote");
            SimulateurRemote service = (SimulateurRemote)PortableRemoteObject.narrow(ref,SimulateurRemote.class);
            //System.out.println(service.helloSimulation());
            //Joueur j= service.newJoueur();
            //Joueur j2= service.newJoueur("pouik");
            Joueur j;
            
            ArrayList<Joueur> lesJoueurs = new ArrayList<Joueur>();
            //System.out.println(service.helloSimulation(UnicastRemoteObject.toStub(j)));
            for(int i=0;i<=5;i++)
            {
                j=new Moot("moot "+(int)(10000.*Math.random()));
                lesJoueurs.add(j);
                System.out.println(service.rejoindrePartie(UnicastRemoteObject.toStub(j)));
            }
            System.out.println(service.partieEnCours());
            while(!service.partieEnCours())
            {
                
                Thread.sleep(1000);
            }
            while(service.partieEnCours())
            {
                Thread.sleep(1000);
            }
            for(Joueur i:lesJoueurs)
                UnicastRemoteObject.unexportObject(i,true);
            
            //UnicastRemoteObject.unexportObject(j,true);
            //Thread.sleep(10000);
            //Scanner s=new Scanner(System.in);
			//s.nextInt();
			
        }
        catch (Throwable e)
        {
            //System.out.println("Erreur:"+e.getMessage());
            e.printStackTrace();
        }
    }
}
