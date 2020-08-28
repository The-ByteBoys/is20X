#!/bin/bash

# START MARIADB
docker run --rm --name mariadb -p 3306:3306/tcp -v $(pwd)/database:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=eplepai -d mariadb

# START PAYARA
docker run --rm --name payara -p 8080:8080 -p 4848:4848 -v $(pwd)/target:/opt/payara/deployments -d payara/server-full

