kubectl delete configmap frontend
kubectl delete -f deployment.yml -f ingress.yml -f service.yml
