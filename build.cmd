

:: Build with maven
docker run --rm -it --name mavenbuild -v maven-repo:/root/.m2 -v %cd%:/usr/src/mymaven -w /usr/src/mymaven maven mvn clean install

:: Copy .war to payara:
docker cp target/roingwebapp.war payara:/opt/payara/deployments

:: Redeploy war
docker exec -it payara asadmin --user=admin --passwordFile /opt/payara/passwordFile undeploy roingwebapp
docker exec -it payara asadmin --user=admin --passwordFile /opt/payara/passwordFile deploy deployments/roingwebapp.war

pause