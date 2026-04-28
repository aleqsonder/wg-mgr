# Cluster start-up

## ConfigMap

Create a `ConfigMap` before starting up the cluster.
`ConfigMap` example is placed below.

```yml
apiVersion: v1
kind: ConfigMap
metadata:
  name: frontend
data:
  BACKEND_BASE_URL: http://<host>:<port>/api
```

## Setup script

There also are [sh](setup.sh) and [batch](setup.bat) setup scripts.
Usage prerequisites (any of):
- There is a file named *.configmap.yml* in the same directory as the
  script.
- There is a file named *.env* in the same directory as the script
- There is an env variable `BACKEND_BASE_URL` exported

Linux:
```sh
sh setup.sh
```

Windows:
```bat
.\setup.bat
```

## Cleanup script

To remove all created kubernetes entities execute the
[sh](cleanup.sh) or [batch](cleanup.bat) scipt.

Linux:
```sh
sh cleanup.sh
```

Windows:
```bat
.\cleanup.bat
```
