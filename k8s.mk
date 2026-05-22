MANIFESTS:=$(strip $(MANIFESTS))
NAMESPACES:=$(strip $(NAMESPACES))

.PHONY: up down

up: $(MANIFESTS)
ifneq ($(NAMESPACES),)
	-kubectl create namespace $(NAMESPACES)
endif
ifneq ($(MANIFESTS),)
	kubectl apply $(addprefix -f ,$^)
endif

down: $(MANIFESTS)
ifneq ($(MANIFESTS),)
	kubectl delete $(addprefix -f ,$^)
endif
