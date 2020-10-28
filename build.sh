#!/bin/bash

# Build with maven
docker run --rm -it --name mavenbuild -v maven-repo:/root/.m2 -v $(pwd):/usr/src/mymaven -w /usr/src/mymaven maven:3.6-adoptopenjdk-8 mvn clean install

# Copy .war to payara:
docker cp target/roingwebapp.war payara:/opt/payara/deployments
docker cp src/config.properties payara:/opt/payara

# Copy excel files
#docker cp excel payara:/opt/payara

# Redeploy war
docker exec -it payara asadmin --user=admin --passwordFile /opt/payara/passwordFile undeploy roingwebapp
docker exec -it payara asadmin --user=admin --passwordFile /opt/payara/passwordFile deploy deployments/roingwebapp.war

echo ""
echo "Link: http://localhost:8080/roingwebapp/"
echo ""

#read -n 1 -s -r -p "Press any key to continue..."
echo ""
