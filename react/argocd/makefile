MANIFESTS:=application.yml
NAMESPACES:=argocd
include ../../k8s.mk

up: | create-frontend.configmap
down: | delete-frontend.configmap

create-frontend.configmap:
	# TODO: Use the 'backend-export' configmap
	kubectl create configmap frontend \
		--from-literal=BACKEND_BASE_URL=http://spring:8177/api

delete-frontend.configmap:
	kubectl delete configmap frontend
