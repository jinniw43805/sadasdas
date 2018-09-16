#!/bin/sh
# This is a comment!
# if javac Server.java ; then
# 	java Server
# 	echo "Compile Server.java OK"
# else
#     echo "Compile failed"
# fi
javac Node.java;
mv Node.class ./bin/;

javac Parseconfig.java;
mv Parseconfig.class ./bin/;

javac RoutingTable.java;
mv RoutingTable.class ./bin/;

javac Message.java;
mv Message.class ./bin/;

javac Server2.java;
# java Server2;