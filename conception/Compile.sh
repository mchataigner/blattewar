#!/bin/bash
#cd Images; make all 
#cd ../ ;
TEXINPUTS=$TEXINPUTS:./lib/
export TEXINPUTS

dia --export=UML_Blatte.eps --filter=eps-pango UML_Blatte.dia 
epstopdf UML_Blatte.eps

pdflatex ./Dossier.tex
pdflatex ./Dossier.tex

	rm -f *.aux
	rm -f *.cb
	rm -f *.cb2
	rm -f *.log
	rm -f *.out
	rm -f *.idx
	rm -f *.toc
	rm -f *~
	rm -f *.backup
	rm -f ./parties/*.aux
	rm -f ./parties/*.cb
	rm -f ./parties/*.cb2
	rm -f ./parties/*.log
	rm -f ./parties/*.out
	rm -f ./parties/*.toc
	rm -f ./parties/*~
	rm -f ./parties/*.backup
	rm -f *.eps

