#!/bin/bash

echo "Creation des répertoires nécéssaire dans le HOME . . ."
mkdir ~/.blattewar 2>/dev/null
mkdir ~/.blattewar/arenes ~/.blattewar/rapport 2>/dev/null
echo "Copie des arenes et xsl. . ."
cp -R arenes/*.ar ~/.blattewar/arenes 2>/dev/null
cp -R rapport.xsl ~/.blattewar/rapport 2>/dev/null
cd ./classes
jar cvf BlatteWar.jar ./blattewar/*.class ../arenes/*.ar ./blattewar/moteurjeu/*.class ./blattewar/props/*.class
mv BlatteWar.jar ../
cd ../jsp
jar cvf BlatteView.war images/* *.jsp *.html *.css WEB-INF/web.xml
mv BlatteView.war ../
cd ../
jar cvf BlatteWar.ear BlatteWar.jar BlatteView.war
echo "Deploiement en cours . . ."
if [ -e $JBOSS_HOME/server/default/deploy/BlatteWar.ear ]
then
	rm $JBOSS_HOME/server/default/deploy/BlatteWar.ear
	sleep 5
fi
mv BlatteWar.ear $JBOSS_HOME/server/default/deploy/
rm BlatteWar.jar BlatteView.war
echo "Deploiement OK"

