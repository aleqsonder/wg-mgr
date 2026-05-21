@echo off

set "PROJ_DIR=%~dp0..\"
for %%s in (spring react prometheus grafana) do (
	kubectl delete -f %PROJ_DIR%%%s\argocd\application.yml
)
call %PROJ_DIR%postgres\k8s\cleanup.bat

kubectl delete secret postgres
kubectl delete configmap frontend backend
kubectl delete namespace monitoring argocd
