#!/bin/bash


javac -cp ./classes:./lib/jboss-ejb-api_3.1.jar:./lib/jbossall-client.jar -d ./classes -sourcepath ./src ./src/blatteclient/*.java

