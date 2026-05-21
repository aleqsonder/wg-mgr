@echo off
setlocal

set "SCRIPT_DIR=%~dp0"

set "PG_SECRET=%SCRIPT_DIR%.postgres.secret.yml"
if exist %PG_SECRET% (
	kubectl apply -f %PG_SECRET%
) else (
	echo %PG_SECRET% has not been found
	exit /b 1
)
if errorlevel 1 exit /b %errorlevel%

kubectl apply ^
	-f monitoring.namespace.yml ^
	-f backend.configmap.yml ^
	-f frontend.configmap.yml

for %%s in (postgres spring react prometheus grafana) do (
	%SCRIPT_DIR%..\%%s\k8s\setup.bat
)
