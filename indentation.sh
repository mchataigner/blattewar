#!/bin/sh
astyleInstalled=`aptitude search astyle | sed 's/^i.*/yes/g' | grep "yes"` 

if [ "blatte $astyleInstalled" = "blatte yes" ]
then
	for i in `find . -name "*.java"`
	do
	sed 's/[[:blank:]]*$//' -i $i
	sed '/^$/d' -i $i
	astyle --style=ansi $i
	done
else
	echo "go installer astyle, ça pwn ta soeur"
fi

	

