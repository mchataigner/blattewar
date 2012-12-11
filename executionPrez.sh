#!/bin/bash
if [ x$JBOSS_HOME = "x" ];then
echo "export JBOSS_HOME noob \!\!\!"
exit 0;
fi
echo $#
echo "$1"
if [ $# -ne 2 ];then
echo "Usage : execution.sh <ip-server> <ip-client>"
exit 0;
fi
CLASSPATH=`$JBOSS_HOME/bin/classpath.sh -c`
java -Djava.rmi.server.hostname=$2 -cp $CLASSPATH:./classes blatteclient.ClientPrez "$1"

