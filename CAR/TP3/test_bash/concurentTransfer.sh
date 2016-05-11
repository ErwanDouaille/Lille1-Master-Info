#!/bin/sh

java -jar ./Node.jar -s 1 &
sleep 1
java -jar ./Node.jar 2 &
sleep 1
java -jar ./Node.jar 3 &
sleep 1
java -jar ./Node.jar 4 &
sleep 1
java -jar ./Node.jar 5 &
sleep 1
java -jar ./Node.jar 6 &
sleep 1
java -jar ./CreateGraphLink.jar 1 2 &
sleep 1
java -jar ./CreateGraphLink.jar 2 3 &
sleep 1
java -jar ./CreateGraphLink.jar 3 4 &
sleep 1
java -jar ./CreateGraphLink.jar 4 5 &
sleep 1
java -jar ./CreateGraphLink.jar 5 6 &
sleep 1
java -jar ./CreateGraphLink.jar 6 1 &
sleep 1
java -jar ./SendMessage.jar 1 Test graph &
sleep 1

