#!/bin/bash


if [ -z "$JBOSS_HOME" ]; then
    echo "JBOSS_HOME non défini"
    exit 1
fi

CLASSPATH=.:`$JBOSS_HOME/bin/classpath.sh -b`:./lib/jboss-ejb-api_3.1.jar:./classes
javac -cp $CLASSPATH -d ./classes -sourcepath ./src \
./src/blattewar/*.java \
./src/blattewar/moteurjeu/*.java \
./src/blattewar/props/*.java \
./src/blatteclient/*.java

