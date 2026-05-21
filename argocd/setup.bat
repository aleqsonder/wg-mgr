@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
set "PROJ_DIR=%SCRIPT_DIR%..\"
set "K8S_DIR=%PROJ_DIR%k8s\"

set "PG_SECRET=%SCRIPT_DIR%.postgres.secret.yml"
if exist %PG_SECRET% (
	kubectl apply -f %PG_SECRET%
) else (
	echo %PG_SECRET% has not been found
	exit /b 1
)
if errorlevel 1 exit /b %errorlevel%

kubectl create namespace argocd
kubectl apply -n argocd --server-side --force-conflicts ^
	-f %SCRIPT_DIR%custom.install.yml

kubectl apply ^
	-f %K8S_DIR%monitoring.namespace.yml ^
	-f %K8S_DIR%backend.configmap.yml ^
	-f %K8S_DIR%frontend.configmap.yml

call %PROJ_DIR%postgres\k8s\setup.bat

for %%s in (spring react prometheus grafana) do (
	kubectl apply -f %PROJ_DIR%%%s\argocd\application.yml
)
