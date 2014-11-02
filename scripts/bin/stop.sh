#!/bin/sh

PIDS=$(pgrep java -lf | grep spa-om-clojure | cut -d" " -f1);

if [ -n "$PIDS" ]
then
  echo "Killing $PIDS";
  echo $PIDS | xargs kill;
else
  echo "No running instances found";
fi
