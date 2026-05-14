@echo off
setlocal

set "SCRIPT_DIR=%~dp0"

kubectl delete ^
	-f %SCRIPT_DIR%dev.configmap.yml ^
	-f %SCRIPT_DIR%rbac.yml ^
	-f %SCRIPT_DIR%statefulset.yml ^
	-f %SCRIPT_DIR%web.service.yml
