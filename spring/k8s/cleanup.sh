#!/bin/sh

kubectl delete configmap backend
kubectl delete -f deployment.yml -f service.yml
