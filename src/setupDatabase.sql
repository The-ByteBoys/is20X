CREATE TABLE IF NOT EXISTS roro.users (
    id INT UNIQUE auto_increment,
    fName varchar(255),
    lName varchar(255) NOT NULL,
    email varchar(255) UNIQUE,
    pass varchar(255),
    CONSTRAINT users_PK PRIMARY KEY (id)
);

INSERT INTO roro.users VALUES (null, "Admin", "Admin", "admin@roro", "Passord123");

CREATE TABLE IF NOT EXISTS roro.clubs (
    id INT auto_increment, 
    name varchar(100) NOT NULL, 
    owner INT, 
    CONSTRAINT clubs_PK PRIMARY KEY (id),
    CONSTRAINT clubs_FK FOREIGN KEY (owner) REFERENCES roro.users(id)
);

CREATE TABLE IF NOT EXISTS roro.athletes (
    id INT UNIQUE auto_increment, 
    name varchar(100) NOT NULL, 
    birth varchar(100), 
    club INT NULL,
    CONSTRAINT athletes_PK PRIMARY KEY (id), 
    CONSTRAINT athletes_FK FOREIGN KEY (club) REFERENCES roro.clubs(id)
);

CREATE TABLE IF NOT EXISTS roro.exercises (
    id INT UNIQUE AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    descr varchar(255) NULL,
    enhet1 varchar(10) NULL,
    enhet2 varchar(10) NULL,
    CONSTRAINT exercises_PK PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS roro.results (
    id INT UNIQUE auto_increment NOT NULL,
    athlete INT NOT NULL,
    exercise INT NOT NULL,
    `datetime` DATETIME DEFAULT CURRENT_TIMESTAMP,
    result1 INT NULL,
    result2 INT NULL,
    note TEXT NULL,
    CONSTRAINT results_PK PRIMARY KEY (id),
    CONSTRAINT results_FK1 FOREIGN KEY (athlete) REFERENCES roro.athletes(id),
    CONSTRAINT results_FK2 FOREIGN KEY (exercise) REFERENCES roro.exercises(id)
);

