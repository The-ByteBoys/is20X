
:: START MARIADB
docker run --rm --name mariadb -p 3306:3306/tcp -v database:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=eplepai -d mariadb

:: START PAYARA
docker run --rm --name payara -p 8080:8080 -p 4848:4848 -v ./target:/opt/payara/deployments -d payara/server-full
