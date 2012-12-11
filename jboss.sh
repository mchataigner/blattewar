#!/bin/bash

if [ x$JBOSS_HOME="x" ];then
echo "export JBOSS_HOME"
fi

ip=`ifconfig eth0 | grep -E "([0-9]{1,3}\.){3}[0-9]{1,3}" -o | head -n 1`

bash $JBOSS_HOME/bin/run.sh -b 0.0.0.0 -Djava.rmi.server.hostname=$ip -Dremoting.bind_by_host=false --host=$ip

