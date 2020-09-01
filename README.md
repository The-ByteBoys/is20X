# is20X
Roforbund Resultater


<br>

# Setting up
To make this work, you need to have docker installed on your system.


## Prepare
* Run `setup.sh` (or `setup.cmd` in windows) to start your database and cache-volume for maven.

* Edit the `src/config.properties` file to match your IP and database-user.
```
username=roro
password=passord123
URL=jdbc:mariadb://172.17.0.1:3306
```

> You find your IP by doing the following:   
> 
> You can find the IP you need by entering: `docker network inspect bridge` into your terminal.    
> Copy the IP under "Gateway" (Typically: `172.17.0.1`)    
> 
> The port is default at `3306`, but you can change it in `setup.sh` or `setup.cmd` if you need to.    

<br>

## Build

* Run `build.sh` (or `build.cmd` in windows). This will build the .war file and (re)start payara.

* Visit http://localhost:8080/roingwebapp/ to see if it works!
