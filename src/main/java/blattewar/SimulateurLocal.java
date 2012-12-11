package blattewar;
import javax.ejb.Local;
@Local
public interface SimulateurLocal
{
	public Arene getBlatteArena();
	public void setBlatteArena(Arene a);
	public boolean matchFini();
	public String vainqueur();
}
