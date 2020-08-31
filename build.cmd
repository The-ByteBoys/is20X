
:: Stop payara
docker stop payara

:: Build with maven
docker run --rm -it --name mavenbuild -v maven-repo:/root/.m2 -v %cd%:/usr/src/mymaven -w /usr/src/mymaven maven mvn clean install

:: Start Payara
docker run --rm --name payara -p 8080:8080 -p 4848:4848 -v %cd%/target:/opt/payara/deployments -d payara/server-full

pause