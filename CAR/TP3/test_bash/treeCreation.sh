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
java -jar ./CreateTreeLink.jar 1 2 &
sleep 1
java -jar ./CreateTreeLink.jar 1 3 &
sleep 1
java -jar ./CreateTreeLink.jar 2 4 &
sleep 1
java -jar ./CreateTreeLink.jar 3 5 &
sleep 1
java -jar ./CreateTreeLink.jar 3 6 &
sleep 1
