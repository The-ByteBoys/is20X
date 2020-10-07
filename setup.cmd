
:: Start database
docker run --rm --name mariadb -p 3306:3306/tcp -v "%cd%\database":/var/lib/mysql -e MYSQL_ROOT_PASSWORD=eplepai -d mariadb:10.5.5

:: Start maven-cache
docker volume create --name maven-repo

:: Start payara
docker run --rm --name payara -p 8080:8080 -p 4848:4848 -d nosp/web-app-skeleton

pause