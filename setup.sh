#!/bin/bash

# Start database
docker run --rm --name mariadb -p 3306:3306/tcp -v $(pwd)/database:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=eplepai -d mariadb

# Start maven-cache
docker volume create --name maven-repo
