@echo off

kubectl delete configmap initdb
kubectl delete service db
kubectl delete statefulset postgres
kubectl delete persistentvolumeclaim postgres
