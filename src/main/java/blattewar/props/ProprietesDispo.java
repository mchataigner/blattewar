package blattewar.props;

import java.util.HashMap;
import java.util.Collection;


public class ProprietesDispo
{
    private HashMap<Integer, DescPropriete> descs;

    public ProprietesDispo()
    {
        this.descs = new HashMap<Integer, DescPropriete>();
    }

    public ProprietesDispo(Collection<DescPropriete> descs) throws Exception
    {
        this();
        for(DescPropriete p : descs)
        {
            this.push(p);
        }
    }

    public void push(DescPropriete p) throws Exception
    {
        if(this.descs.get(p.getId()) == null)
            this.descs.put(p.getId(), p);
        else
            throw new Exception("DescPropriete avec id "+p.getId()+" existe déjà");
    }

    public DescPropriete get(int id) throws Exception
    {
        DescPropriete res;
        if((res = this.descs.get(id)) != null)
            return res;
        else
            throw new Exception("DescPropriete avec id "+id+" n'existe pas");
    }

    public DescPropriete pop(int id) throws Exception
    {
        DescPropriete res;
        if((res = this.descs.remove(id)) != null)
            return res;
        else
            throw new Exception("DescPropriete avec id "+id+" n'existe pas");
    }
}

