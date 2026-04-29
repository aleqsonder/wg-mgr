#!/bin/sh

set -e

echorun () {
	echo $@
	$@
}

SCRIPT_DIR=$(dirname $0)
CONFIG_MAP_FILE="$SCRIPT_DIR/.configmap.yml"

if [ -e $CONFIG_MAP_FILE ]; then
	echorun kubectl apply -f $CONFIG_MAP_FILE
else
	echo "$CONFIG_MAP_FILE has not been found" >&2
	exit 1
fi

echorun kubectl apply -f deployment.yml -f ingress.yml -f service.yml
