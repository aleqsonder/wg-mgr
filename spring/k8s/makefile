MANIFESTS:=deployment.yml hpa.yml service.yml metrics-service.yml export.configmap.yml
include ../../k8s.mk

up: | create-backend.configmap
down: | delete-backend.configmap

create-backend.configmap:
	kubectl create configmap backend \
		--from-literal=conn-str=postgresql://postgres-0.db:5432/vpn

delete-backend.configmap:
	kubectl delete configmap backend
