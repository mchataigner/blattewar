<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//FR"><!--
<link rel="stylesheet" type="text/css" href="blatte.css">-->
<%@ page import="blattewar.SimulateurLocal, blattewar.*, java.util.ArrayList, java.lang.StringBuffer,
	java.lang.String, java.util.ArrayList, java.util.Vector, 
    javax.naming.InitialContext, java.util.Properties, 
    javax.rmi.PortableRemoteObject, java.io.File" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">

<head>
	<title>Blattosphere - liste des matchs</title>
</head>
<body>

<table class="matchs">
<%	
	File[] files =new File(System.getProperty("user.home")+"/.blattewar/rapport").listFiles(); 
	for(int i=0; i<files.length;i++){
		if(files[i].getName().contains(".xml")){%>
			<tr class="rapport"><th>Rapport:</th><td><form action="./match.jsp">
			
			<%=files[i].getName().replace(".xml","")%>
			<input type="hidden" name="rapport"  value=<%='"'+files[i].getName()+'"'%>/>
			<input type="submit" value="voir"/>
			</form method="get">
				</td></tr>
			<%
		}
	}%>			

</table>
</body>
</html>
