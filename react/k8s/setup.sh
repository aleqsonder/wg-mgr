#!/bin/sh

set -e

echorun () {
	echo $@
	$@
}

SCRIPT_DIR=$(dirname $0)
ENV_FILE="$SCRIPT_DIR/.env"
CONFIG_MAP_FILE="$SCRIPT_DIR/.configmap.yml"

if [ -e $CONFIG_MAP_FILE ]; then
	echorun kubectl apply -f $CONFIG_MAP_FILE
elif [ -e $ENV_FILE ]; then
	echorun kubectl create configmap frontend --from-env-file="$ENV_FILE"
elif [ -n "${BACKEND_BASE_URL+isset}" ]; then
	echorun kubectl create configmap frontend \
		--from-literal=BACKEND_BASE_URL="$BACKEND_BASE_URL"
else
	echo "BACKEND_BASE_URL is unset" >&2
	exit 1
fi

kubectl apply -f deployment.yml -f ingress.yml -f service.yml
