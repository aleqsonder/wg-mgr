# Cluster start-up

## ConfigMap

Create a `ConfigMap` before starting up the cluster.
`ConfigMap` file template is placed below.

```yml
apiVersion: v1
kind: ConfigMap
metadata:
  name: frontend
data:
  BACKEND_BASE_URL: http://<host>:<port>/api
```

### Important

Manual ConfigMap definition is redundant in case the whole application
cluster is set up with the [main setup script](../../k8s/setup.bat).

## Setup script

There also is [batch](setup.bat) setup script.

```bat
.\setup.bat
```

## Cleanup script

To remove all created kubernetes entities execute the
[batch](cleanup.bat) scipt.

```bat
.\cleanup.bat
```
