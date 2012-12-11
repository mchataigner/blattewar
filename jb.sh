#!/bin/bash
# récupération et lancement de JBOSS pour les machines de TP

estMachineTP=`hostname | grep "asi2[5-7]-[0-2][0-9]\|ep-[0-4][0-9]\|stpi-[0-4][0-9]" | wc -l`
if [ $estMachineTP -eq 1 ]
then
echo "Machine TP detectée"
	if ! [ -d /tmp/jboss_blattewar ]
	then  
	mkdir /tmp/jboss_blattewar
	fi
	if ! [ -d /tmp/jboss_blattewar/jboss-5.0.0.CR2 ]
	then
	cp /uv/asi/technoweb/jboss-5.0.0.CR2.zip /tmp/jboss_blattewar
	cd /tmp/jboss_blattewar
	unzip jboss-5.0.0.CR2.zip
	echo "Jboss récupéré et dézippé"
	echo "Lancement en cours . . ."
	sleep 5
	rm jboss-5.0.0.CR2.zip
	bash jboss-5.0.0.CR2/bin/run.sh
	fi
else
echo "Machine personnelle detectée => pas de récupération de JBOSS => Demerde toi !"
fi
