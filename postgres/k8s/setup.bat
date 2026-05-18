@echo off
setlocal

set "SCRIPT_DIR=%~dp0"

kubectl create configmap initdb ^
	--from-file=%SCRIPT_DIR%..\initdb.d\create-tables.sql

kubectl apply ^
	-f %SCRIPT_DIR%pvc.yml ^
	-f %SCRIPT_DIR%statefulset.yml ^
	-f %SCRIPT_DIR%service.yml
