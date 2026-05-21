SERVICES:=postgres spring react prometheus grafana

up: | create-monitoring.namespace $(addprefix up-,$(SERVICES))
down: | $(addprefix down-,$(SERVICES)) delete-monitoring.namespace

up-%: ../%/k8s
	$(MAKE) -C $< up

down-%: ../%/k8s
	$(MAKE) -C $< down

create-%.namespace:
	-kubectl create namespace $*

delete-%.namespace:
	-kubectl delete namespace $*
