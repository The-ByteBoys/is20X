# is20X - Roforbund Resultater
This project is developed by a student-group of 6 people. The application is trying to help reporting rowing-testresults, and give a better presentation of the data, that until now is stored in publicly available Excel documents.

There *might* be a live version on [this link](https://roing.readflow.io/roingwebapp/).

<br>

# Setting up
To make this work, you only need to have [docker](https://www.docker.com/) installed on your system.    


## First time setup
1. Clone this repository into a local folder.


2. Run the `setup` script for your system.
> The script for your system is `setup.cmd` for Windows, and `setup.sh` for Unix and Unix-like systems like Linux or Mac OS.    
> Note: On Unix and Unix-like systems you might need to run the commands with `sudo` to make them work.


3. Run the command `docker exec -it mariadb mysql -p` and enter the password found in [setup](setup.sh) under `mysqlRootPassword`. (Default: `eplepai`)


4. To create a database user for this webapp do the following commands (change the password as you please):     
```sql
CREATE USER 'roing'@'%' IDENTIFIED BY 'Passord123';
CREATE DATABASE roing;
GRANT ALL PRIVILEGES ON roing.* TO 'roing'@'%';
FLUSH PRIVILEGES;
```


5. Edit the `setup`-script to match your IP and database-user.      
> You'll find the IP you need by doing the following:      
> 
> You can find the IP you need by entering: `docker network inspect bridge` into your terminal.    
> Copy the IP under "Gateway" (Typically: `172.17.0.1`)    
> 
> The port is default at `3306`, but you can change it in the `setup` if you need to.    


6. If you edited your `setup`-script, restart your docker-daemon and then run the `setup`-script again.


7. Run the [RoroDBSetup.sql](src/RoroDBSetup.sql) sql-script in the database to setup the database with some default data.
> (Default login is `admin@admin` with pw `eplepai`)   


<br>

When returning to the project, you simply need to run the `setup`-script once to set everything up again.


## Build

* To rebuild the webapp you simply need to run the `build`-script. This will build the project and redeploy it in the payara container.

* Visit http://localhost:8080/roingwebapp/ to see if it works!
