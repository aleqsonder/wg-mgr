#!/bin/sh

mkdir /projects
git clone https://github.com/aleqsonder/wg-mgr /projects/wg-mgr

PGUSER=$(tr -dc 'a-z0-9' < /dev/urandom | head -c 12)
PGPASSWD=$(tr -dc 'A-Za-z0-9' < /dev/urandom | head -c 20)
printf 'POSTGRES_USER=%s\nPOSTGRES_PASSWORD=%s\n' $PGUSER $PGPASSWD > /projects/wg-mgr/.env

docker compose -f /projects/wg-mgr/prod.docker-compose.yml up -d
