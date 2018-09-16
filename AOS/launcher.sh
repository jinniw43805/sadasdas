#!/bin/bash

# Change this to your netid
netid=yxk171930

# Root directory of your project
PROJDIR=/home/012/y/yx/yxk171930/AOS

# Directory where the config file is located on your local system
CONFIGLOCAL=$HOME/git/UTD/AOS/scripts-fall18/AOS/config.txt

CONFIGSERVER=$PROJDIR/config.txt
# Directory your java classes are in
BINDIR=$PROJDIR

# Your main project class
PROG=Server2

n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    while [[ $n -lt $i ]]
    do
        read line
        p=$( echo $line | awk '{ print $1 }' )
        host=$( echo $line | awk '{ print $2 }' )
        port=$( echo $line | awk '{ print $3 }' ) # port

    gnome-terminal -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host java -cp $BINDIR $PROG $port $n $CONFIGSERVER; exec bash" &

        n=$(( n + 1 ))
    done
)

