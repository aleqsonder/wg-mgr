SERVICES:=postgres prometheus grafana
APPLICATIONS:=spring react

up: | monitoring.namespace argocd.install $(SERVICES) $(APPLICATIONS)
down: | $(addprefix down-,$(APPLICATIONS) $(SERVICES)) del-monitoring.namespace del-argocd.namespace

argocd.install: | argocd.namespace
	kubectl apply -n argocd --server-side --force-conflicts -f custom.install.yml

%.namespace:
	-kubectl create namespace $*

del-%.namespace:
	-kubectl delete namespace $*

$(SERVICES): %: ../%/k8s
	$(MAKE) -C $< up

$(APPLICATIONS): %: ../%/argocd
	$(MAKE) -C $< up

$(addprefix down-,$(SERVICES)): down-%: ../%/k8s
	$(MAKE) -C $< down

$(addprefix down-,$(APPLICATIONS)): down-%: ../%/argocd
	$(MAKE) -C $< down
