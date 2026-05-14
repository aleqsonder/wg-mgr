@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
set "SECRET_FILE=%SCRIPT_DIR%.secret.yml"

kubectl delete -n monitoring service grafana-headless grafana-web
kubectl delete -n monitoring statefulset grafana
kubectl delete -n monitoring configmap grafana-datasources grafana-dashboards

if exist %SECRET_FILE% (
	kubectl delete -f %SECRET_FILE%
)
