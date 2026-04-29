# Cluster Start-up

## Secrets

First of all ensure the *.postgres.secret.yml* file is
presented. File content template is placed below.

```yml
apiVersion: v1
kind: Secret
type: Opaque
metadata:
  name: postgres
stringData:
  username: <username>
  password: <password>
```

## Setup

To set the cluster up use the [setup script](setup.bat).

```bat
.\setup.bat
```

## Cleanup

To remove all entities created with the setup script use the
[cleanup script](cleanup.bat).

```bat
.\cleanup.bat
```
