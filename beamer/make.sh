#!/bin/bash

#cd images
#dia -t eps *.dia
#for i in *.eps; do epstopdf $i; done
#cd ..

echo "Compilation 1/3 ..."
pdflatex -file-line-error BlatteWar-presentation.tex 
echo "Compilation 2/3 ..."
pdflatex -file-line-error BlatteWar-presentation.tex
echo "Compilation 3/3 ..."
pdflatex -file-line-error BlatteWar-presentation.tex
echo "Compilation 3/3 terminée"

rm -f *.aux
rm -f *.cb
rm -f *.cb2
rm -f *.log
rm -f *.out
rm -f *.toc
rm -f *~
rm -f *.backup
rm -f images/*.eps
rm -f *.nav
rm -f *.snm
#rm -f images/*.pdf
