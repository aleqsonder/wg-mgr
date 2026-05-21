@echo off
setlocal

set "MANIFEST_DIR=%~dp0..\manifests\"

call:%~1
if errorlevel 1 goto:help
goto:eof

:help
	>&2 echo "usage: %0 help|up|down"
	exit /b 1

:up
	:: TODO: Use the 'postgres-export' configmap
	kubectl create configmap backend ^
		--from-literal=conn-str=postgresql://postgres-0.db:5432/vpn
	kubectl apply ^
		-f %MANIFEST_DIR%deployment.yml ^
		-f %MANIFEST_DIR%hpa.yml ^
		-f %MANIFEST_DIR%service.yml ^
		-f %MANIFEST_DIR%metrics-service.yml ^
		-f %MANIFEST_DIR%export.configmap.yml
	goto:eof

:down
	kubectl delete configmap backend
	kubectl delete ^
		-f %MANIFEST_DIR%deployment.yml ^
		-f %MANIFEST_DIR%hpa.yml ^
		-f %MANIFEST_DIR%service.yml ^
		-f %MANIFEST_DIR%metrics-service.yml ^
		-f %MANIFEST_DIR%export.configmap.yml
