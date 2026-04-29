# DB Cluster Setup

## Secrets

StatefulSet requires for `postgres.username` and `postgres.password`
to be defined. [Click here](../../k8s/README.md) to reach the
template for a secret file.

It is suggested to declare the file out of the *postgres* directory
since the same secrets are required by the backend cluster.

## Setup

To set the DB cluster up use the [setup script](setup.bat).

```bat
.\setup.bat
```

## Cleanup

To remove all the entities created by the setup script use the
[cleanup script](cleanup.bat).

```bat
.\cleanup.bat
```
