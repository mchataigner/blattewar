<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//FR">

<%@ page import="blattewar.SimulateurLocal, blattewar.*, java.util.ArrayList, java.lang.StringBuffer,
	java.lang.String, java.util.ArrayList, java.util.Vector, 
	java.util.HashMap,java.lang.*,
    javax.naming.InitialContext, java.util.Properties, 
    javax.rmi.PortableRemoteObject" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
<%!
public static SimulateurLocal sim = null;
public Object ref;
public Arene a=null;
public void jspInit() {
	try {		
		InitialContext ctx = new InitialContext();
		Object ref=ctx.lookup("BlatteWar/Simulateur/local");
		sim = (SimulateurLocal)PortableRemoteObject.narrow(ref,SimulateurLocal.class);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

%>
<head>
	<title>Blattosphere</title>
</head>
<body>

<% 
try
{
a=sim.getBlatteArena();
}
catch(Throwable e){sim = (SimulateurLocal)PortableRemoteObject.narrow(ref,SimulateurLocal.class);
}
if(a==null){ %>
Il n'y a pas de partie en cours :
<%}
else{%><div class="NomsBlattes">
<% try
{
if(sim.matchFini()){ %>
<h2>Fin de partie.</h2>
<%}
 else{
	 Integer compteur=1;
	 Vector<Blatte> lesBlattes=new Vector<Blatte>();
 	for(Case c : a.getCases()){
		if(c instanceof Sol){
			if(((Sol)c).getBlatte()!=null){
				Blatte b=(Blatte)((Sol)c).getBlatte();
				if(session.getAttribute(b.getNom())==null) {
					session.setAttribute(b.getNom(),compteur);
				}
				lesBlattes.addElement(b);
				compteur++;
			}
		}
	}%>
	<h3><%=compteur%> survivantes : <h3><%
	for(int j=0;j<lesBlattes.size();j++){%>
		<h3><%=(lesBlattes.elementAt(j)).getNom()%> avec <%=(lesBlattes.elementAt(j)).getVies()%> vies</h3>
	<%}
	}}catch(Throwable e){sim = (SimulateurLocal)PortableRemoteObject.narrow(ref,SimulateurLocal.class);}%>
</div><table class="arene"><table class="arene">
<%	int compteurCase=0;
	int hauteur=a.getHauteur();
	int largeur=a.getLargeur();
	int i=1;
	for(Case c : a.getCases())
	    { if(compteurCase==0){%><tr><%}
	    if((c.getPropriete()).toujoursActive()){
			%><td class="objet" /><%}
		else{
		if(c instanceof Mur){%><td class="mur"/><%}
		else if(c instanceof Sol)
	    {   
			Blatte bli=((Sol)c).getBlatte();
		    if(bli!=null)
		    {
				if(session.getAttribute((String)bli.getNom())!=null ){
					int codeCSS=((Integer)session.getAttribute((String)bli.getNom())).intValue();
					%><td class="joueur" id="j<%=codeCSS%>"/><%
				}
				else{%><td class="joueur" id="j1"/><%i++; }
		    }
		    else if(c instanceof Depart){%><td class="depart"/><%}
    		else if(c instanceof Sortie){%><td class="sortie"/><%}
		    else
		    {
		        %><td class="sol"/><%
		    }
	    }
		}
		    
		compteurCase++;
		if(compteurCase>=largeur){
			compteurCase=0;
			%></tr><%} 
	 }%>
</table>

<%}%>
<a href="listematch.jsp" >Rapports</href> 
</body>
</html>
