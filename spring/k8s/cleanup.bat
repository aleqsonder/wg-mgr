@echo off
setlocal

set "SCRIPT_DIR=%~dp0"

for %%t in (service hpa deployment) do (
	kubectl delete %%t spring
)
kubectl delete service spring-metrics

set "CONFIG_MAP_FILE=%SCRIPT_DIR%.configmap.yml"
if exist "%CONFIG_MAP_FILE%" (
	kubectl delete -f "%CONFIG_MAP_FILE%"
)
