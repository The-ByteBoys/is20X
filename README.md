# is20X - Roforbund Resultater
This project is developed by a student-group of 6 people. The application is trying to help reporting rowing-testresults, and give a better presentation of the data, that until now is stored in public Excel documents.


<br>

# Setting up
To make this work, you need to have docker installed on your system.


## Prepare
* Run `setup.sh` (or `setup.cmd` in windows) to start your database and cache-volume for maven.

* Edit the `setupDataSource.cmd` file to match your IP and database-user.     
> Run the script some time after the setup script is complete, or it might fail.     

> You may find your IP by doing the following:   
> 
> You can find the IP you need by entering: `docker network inspect bridge` into your terminal.    
> Copy the IP under "Gateway" (Typically: `172.17.0.1`)    
> 
> The port is default at `3306`, but you can change it in `setup.sh` or `setup.cmd` if you need to.    

<br>

## Build

* Run `build.sh` (or `build.cmd` in windows). This will build the .war file and (re)start payara.

* Visit http://localhost:8080/roingwebapp/ to see if it works!
