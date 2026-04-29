setlocal

set "SCRIPT_DIR=%~dp0"
set "CONFIG_MAP_FILE=%SCRIPT_DIR%.configmap.yml"

if exist "%CONFIG_MAP_FILE%" (
    kubectl apply -f "%CONFIG_MAP_FILE%"
) else (
    >&2 echo %CONFIG_MAP_FILE% has not been found
    exit /b 1
)
if errorlevel 1 exit /b %errorlevel%

kubectl apply -f deployment.yml -f service.yml
