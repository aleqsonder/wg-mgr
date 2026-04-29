@echo off

kubectl delete secret postgres
kubectl delete configmap frontend backend

set "PROJ_DIR=%~dp0..\"
for %%s in (postgres spring react) do (
	%PROJ_DIR%%%s\k8s\cleanup.bat
)
