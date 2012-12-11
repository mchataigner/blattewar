<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="https://www.w3.org/TR/REC-html40">

<xsl:template match="/">
	<xsl:apply-templates/>
</xsl:template>
<xsl:template match="rapport">
	<div class="rapport">
		<div class="descriptionrapport">Rapport de Match du <xsl:value-of select="./date"/> sur <xsl:value-of select="./mapname"/>. Maximum <xsl:value-of select="nombretoursmax"/> tours.</div>
<div class="blattes"><xsl:value-of select="nombrejoueurs"/> blattes participantes: 
			<table class="listeblatte">
				<tr>
					<th>Nom</th>		
				</tr>
				<xsl:for-each select="./nomblatte">
					<tr>
						<td><xsl:value-of select="."/></td>
					</tr>
				</xsl:for-each>
			</table>
		</div>
		<div class="deroullement">
			<table class="tablederounds">
				<tr>
					<th class="colnum">N</th>
					<th class="colcontent">tour</th>
				</tr>
				<xsl:for-each select="./tour">
					<tr>
						<td class="colnum">
							<xsl:value-of select="./numerotour"/>
						</td>
						<td class="colcontent">
							<xsl:apply-templates select="."/>
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</div>
		<div class="resultat">
			Gagnant: <xsl:value-of select="victoire/nomblatte"/> (<xsl:if test="victoire/dernieresurvivante">Derniere survivante</xsl:if> <xsl:if test="victoire/sortie">Sortie</xsl:if><xsl:if test="victoire/findematch">Fin de match (le plus de vie)</xsl:if>).
		</div>
	</div>
</xsl:template>
<xsl:template match="tour">
	<div class="tour" style="
		width: 100%;
		height: 200px;
		overflow: auto;
	">
	Nombre de blattes en vie: <xsl:value-of select="nombreblatte"/><br/>
	<table class="tour">
		<tr>
			<th class="action">Action</th>
			<th class="nomblattes">Nom Blatte</th>	
			<th class="position">Position</th>
			<th class="direction">Direction</th>
			<th class="resultat">Resultat</th>
		</tr>
		<xsl:for-each select="./attaque">
			<tr>
				<td>attaque</td>
				<td><xsl:value-of select="./nomblatte"/></td>
				<td>x: <xsl:value-of select="./positionblatte/x"/>, y: <xsl:value-of select="positionblatte/y"/></td>
				<td><xsl:value-of select="./direction"/></td>
	<td>	<xsl:if test="./succes">Succes!<xsl:value-of select="./succes/blattegagnante"/> bat <xsl:value-of select="./succes/blatteperdante"/><xsl:if test="./succes/blatteeliminee"> et l'elimine</xsl:if>.</xsl:if>
					<xsl:if test="./echec">Echec!</xsl:if></td>
			</tr>
		</xsl:for-each>
		<xsl:for-each select="./deplacement">
			<tr>
				<td>deplacement</td>
				<td><xsl:value-of select="./nomblatte"/></td>
				<td>x: <xsl:value-of select="./positionblatte/x"/>, y: <xsl:value-of select="positionblatte/y"/></td>
				<td><xsl:value-of select="./direction"/></td>
				<td>	<xsl:if test="./succes">Succes!</xsl:if>
					<xsl:if test="./echec">Echec!</xsl:if></td>
			</tr>
		</xsl:for-each>
		<xsl:for-each select="./attente">
			<tr>
				<td>attente</td>
				<td><xsl:value-of select="./nomblatte"/></td>
				<td>x: <xsl:value-of select="./positionblatte/x"/>, y: <xsl:value-of select="positionblatte/y"/></td>
				<td><xsl:value-of select="./direction"/></td>
				<td></td>
			</tr>
		</xsl:for-each>
	</table>
	</div>
</xsl:template>
</xsl:stylesheet>

