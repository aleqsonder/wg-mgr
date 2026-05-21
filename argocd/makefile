SERVICES:=spring react prometheus grafana

up: | create-monitoring.namespace apply-argocd.install $(addprefix up-,$(SERVICES))
	$(MAKE) -C ../postgres/k8s up

down: | delete-argocd.namespace delete-monitoring.namespace
	$(MAKE) -C ../postgres/k8s down

%-argocd.install: | create-argocd.namespace
	kubectl $* -n argocd --server-side --force-conflicts -f custom.install.yml

create-%.namespace:
	-kubectl create namespace $*

delete-%.namespace:
	-kubectl delete namespace $*

up-%: ../%/argocd
	$(MAKE) -C $< up
