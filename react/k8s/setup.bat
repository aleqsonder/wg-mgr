setlocal

set "SCRIPT_DIR=%~dp0"
set "ENV_FILE=%SCRIPT_DIR%.env"
set "CONFIG_MAP_FILE=%SCRIPT_DIR%.configmap.yml"

if exist "%CONFIG_MAP_FILE%" (
    kubectl apply -f "%CONFIG_MAP_FILE%"
) else if exist "%ENV_FILE%" (
    kubectl create configmap frontend --from-env-file="%ENV_FILE%"
) else if defined BACKEND_BASE_URL (
    kubectl create configmap frontend --from-literal=BACKEND_BASE_URL="%BACKEND_BASE_URL%"
) else (
    >&2 echo BACKEND_BASE_URL is unset
    exit /b 1
)
if errorlevel 1 exit /b %errorlevel%

kubectl apply -f deployment.yml -f ingress.yml -f service.yml
