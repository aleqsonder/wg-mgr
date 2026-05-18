# Backend Cluster Start-up

## Secrets

StatefulSet requires for `postgres.username` and `postgres.password`
to be defined. [Click here](../../k8s/README.md) to reach the
template for a secret file.

It is suggested to declare the file out of the *backend* directory
since the same secrets are required by the DB cluster.

## ConfigMap

Before starting up the cluster ensure the **"backend"** config-map
is presented in the cluster; otherwise create the *.configmap.yml* file
within this directory. ConfigMap file template is placed below.

```yml
apiVersion: v1
kind: ConfigMap
metadata:
  name: backend
data:
  conn-str: postgresql://<hostname>:<port>/vpn
```

### Important

Manual ConfigMap definition is redundant in case the whole application
cluster is set up with the [main setup script](../../k8s/setup.bat).

## Setup

To set the cluster up use [batch](setup.bat) script.

```bat
.\setup.bat
```

## Cleanup

To remove all kubernetes entities created with a setup-script use
[batch](cleanup.bat) script.

```bat
.\cleanup.bat
```
