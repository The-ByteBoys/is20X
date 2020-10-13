# Database Variables
host=172.17.0.1
port=3306
bruker=roing
passord=Passord123
databaseNavn=roro

# Setup database connection
docker exec -it payara asadmin --user=admin --passwordFile /opt/payara/passwordFile create-jdbc-connection-pool --datasourceclassname org.mariadb.jdbc.MySQLDataSource --restype javax.sql.XADataSource --property serverName=$host\:portNumber=$port\:user=$bruker\:password=$passord\:databaseName=$databaseNavn mariapool
docker exec -it payara asadmin --user=admin --passwordFile /opt/payara/passwordFile ping-connection-pool mariapool
docker exec -it payara asadmin --user=admin --passwordFile /opt/payara/passwordFile create-jdbc-resource --connectionpoolid mariapool roingdb

echo ""
echo "- If above is without errors, it succeeded successfully!"
echo ""