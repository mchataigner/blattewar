<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//FR"><!--
<link rel="stylesheet" type="text/css" href="blatte.css">-->
<%@ page import="blattewar.SimulateurLocal, blattewar.*, java.util.ArrayList, java.lang.StringBuffer,
	java.lang.String, java.util.ArrayList, java.util.Vector, 
    javax.naming.InitialContext, java.util.Properties, 
    javax.rmi.PortableRemoteObject, java.io.File, javax.xml.transform.Transformer, javax.xml.transform.TransformerFactory, javax.xml.transform.stream.StreamResult, java.io.StringWriter, javax.xml.transform.stream.StreamSource" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">

<head>
	<title>Blattosphere - rapport de match</title>
</head>
<% 
	String rapport="";
	String xslt=System.getProperty("user.home")+"/.blattewar/rapport/rapport.xsl";
	String xml=System.getProperty("user.home")+"/.blattewar/rapport/"+(String)request.getParameter("rapport");//System.getProperty("user.home")+"/.blattewar/rapport/rapport_10_06_15__07_59_39.xml";
        try
        {
            	TransformerFactory tFactory = TransformerFactory.newInstance();
		StringWriter writer = new StringWriter();
		Transformer transformer = tFactory.newTransformer(new StreamSource(xslt));
		StreamResult result = new StreamResult(writer);
      		transformer.transform(new StreamSource(xml), result);
		rapport=writer.toString();
        }
        catch (Exception e)
        {
            rapport="Écriture du rapport échouée";
        }
	finally{
   



%>
<body>
			<%=rapport%>
<%}%>
</body>
</html>
