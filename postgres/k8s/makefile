MANIFESTS:=.secret.yml pvc.yml statefulset.yml service.yml
include ../../k8s.mk

up: | create-initdb.configmap
down: | delete-initdb.configmap

create-initdb.configmap:
	kubectl create configmap initdb \
		--from-file=../initdb.d/create-tables.sql

delete-initdb.configmap:
	-kubectl delete configmap initdb
