@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
set "CONFIG_MAP_FILE=%SCRIPT_DIR%.configmap.yml"

if exist "%CONFIG_MAP_FILE%" (
    kubectl apply -f "%CONFIG_MAP_FILE%"
) else (
    kubectl get configmap backend
    if errorlevel 1 (
        >&2 echo backend ConfigMap is unset and %CONFIG_MAP_FILE% has not been found
        exit /b 1
    )
)

kubectl apply ^
    -f %SCRIPT_DIR%deployment.yml ^
    -f %SCRIPT_DIR%hpa.yml ^
    -f %SCRIPT_DIR%service.yml
