#!/bin/bash

# Variables
mysqlRootPassword=eplepai

# Database Variables
host=172.17.0.1
port=3306
bruker=roing
passord=Passord123
databaseNavn=roro


# Start database
docker run --rm --name mariadb -p $port\:3306/tcp -v "$(pwd)/database":/var/lib/mysql -e MYSQL_ROOT_PASSWORD=$mysqlRootPassword -d mariadb:10.5.5

# Start maven-cache
docker volume create --name maven-repo

# Start payara
docker run --rm --name payara -p 8080:8080 -p 4848:4848 -d nosp/web-app-skeleton

sleep 5
echo ""
./build.sh

# Setup database connection
docker exec -it payara asadmin --user=admin --passwordFile /opt/payara/passwordFile create-jdbc-connection-pool --datasourceclassname org.mariadb.jdbc.MySQLDataSource --restype javax.sql.XADataSource --property serverName=$host\:portNumber=$port\:user=$bruker\:password=$passord\:databaseName=$databaseNavn mariapool
docker exec -it payara asadmin --user=admin --passwordFile /opt/payara/passwordFile ping-connection-pool mariapool
docker exec -it payara asadmin --user=admin --passwordFile /opt/payara/passwordFile create-jdbc-resource --connectionpoolid mariapool roingdb

echo ""
echo "- If above is without errors, it succeeded successfully!"
echo ""

read -n 1 -s -r -p "Press any key to continue..."
echo ""
