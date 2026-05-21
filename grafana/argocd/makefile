MANIFESTS:=../k8s/.secret.yml application.yml
NAMESPACES:=argocd
include ../../k8s.mk

up: | create-grafana-dashboards.configmap
down: | delete-grafana-dashboards.configmap

create-grafana-dashboards.configmap: | monitoring.namespace
	kubectl create configmap grafana-dashboards -n monitoring \
		--from-file=../provisioning/dashboards

delete-grafana-dashboards.configmap:
	-kubectl delete configmap grafana-dashboards -n monitoring

%.namespace:
	-kubectl create namespace $*
