@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
set "SECRET_FILE=%SCRIPT_DIR%.secret.yml"

if exist %SECRET_FILE% (
	kubectl apply -f %SECRET_FILE%
) else (
	kubectl get secret/grafana -n monitoring
	if errorlevel 1 (
		>&2 echo grafana secret is unset and %SECRET_FILE% has not been found
		exit /b 1
	)
)

kubectl create configmap grafana-dashboards -n monitoring ^
	--from-file=%SCRIPT_DIR%../provisioning/dashboards

kubectl apply ^
	-f %SCRIPT_DIR%datasources.configmap.yml ^
	-f %SCRIPT_DIR%statefulset.yml ^
	-f %SCRIPT_DIR%web.service.yml
