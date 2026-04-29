# Backend Cluster Start-up

## ConfigMap

Before starting up the cluster ensure the *.configmap.yml* file is
present. ConfigMap contents example is placed below.

```yml
apiVersion: v1
kind: ConfigMap
metadata:
  name: backend
data:
  POSTGRES_USER: <username>
  POSTGRES_PASSWORD: <password>
  WGMGR_DB_CONN_STR: postgresql://<hostname>:<port>/vpn
```

## Setup

To set the cluster up use [sh](setup.sh) or [batch](setup.bat) script.

**Linux**:
```sh
sh setup.sh
```

**Windows**:
```bat
.\setup.bat
```

## Cleanup

To remove all kubernetes entities created with a setup-script use
[sh](cleanup.sh) or [batch](cleanup.bat) script.

**Linux**:
```sh
sh cleanup.sh
```

**Windows**:
```bat
.\cleanup.bat
```
