CREATE DATABASE roro;

CREATE TABLE roro.user (
    user_id INT auto_increment,
    fName varchar(255),
    lName varchar(255) NOT NULL,
    email varchar(255) UNIQUE,
    pass varchar(255),
    CONSTRAINT user_PK PRIMARY KEY (user_id)
);

INSERT INTO roro.user VALUES (null, "Admin", "Admin", "admin@roro", "Passord123");

CREATE TABLE roro.club (
    club_id INT auto_increment, 
    name varchar(100) NOT NULL, 
    owner INT,
    CONSTRAINT club_PK PRIMARY KEY (club_id),
    CONSTRAINT owner_FK FOREIGN KEY (owner) REFERENCES roro.user(user_id)
);

CREATE TABLE roro.athlete (
    athlete_id INT auto_increment, 
    name varchar(100) NOT NULL, 
    birth smallint(4), 
    club INT,
    CONSTRAINT athlete_PK PRIMARY KEY (athlete_id), 
    CONSTRAINT club_FK FOREIGN KEY (club) REFERENCES roro.club(club_id)
);

CREATE TABLE roro.exercise (
    exercise_id INT auto_increment,
    name varchar(100) NOT NULL,
    descr varchar(255) NULL,
    unit varchar(10) NULL,
    CONSTRAINT exercise_PK PRIMARY KEY (exercise_id)
);

CREATE TABLE roro.result (
    result_id INT auto_increment,
    athlete INT,
    exercise INT,
    `datetime` DATETIME DEFAULT CURRENT_TIMESTAMP,
    result INT NOT NULL,
    note varchar(255),
    CONSTRAINT result_PK PRIMARY KEY (result_id),
    CONSTRAINT athlete_FK FOREIGN KEY (athlete) REFERENCES roro.athlete(athlete_id),
    CONSTRAINT exercise_FK FOREIGN KEY (exercise) REFERENCES roro.exercise(exercise_id)
);

CREATE TABLE roro.class (
	class_id INT auto_increment,
	description varchar(50) NOT NULL,
	CONSTRAINT class_PK PRIMARY KEY (class_id)
);

CREATE TABLE roro.classPeriod (
	period_id INT auto_increment,
	athlete INT,
	class INT NOT NULL,
	start date DEFAULT CURRENT_DATE,
	CONSTRAINT period_PK PRIMARY KEY (period_id),
	CONSTRAINT periodAthlete_FK FOREIGN KEY (athlete) REFERENCES roro.athlete(athlete_id),
	CONSTRAINT periodClass_FK FOREIGN KEY (class) REFERENCES roro.class(class_id)
);

INSERT INTO roro.club VALUES (1, "Azerbajan Roklubb", 1);
INSERT INTO roro.athlete VALUES (1, "Jon Angvik", 1982, 1);
INSERT INTO roro.class VALUES (1, "Master 1");
INSERT INTO roro.classPeriod VALUES (1, 1, 1, null);
INSERT INTO roro.exercise VALUES (1, "Hopp", "Utøveren hopper!", "stk");
INSERT INTO roro.`result` VALUES (1, 1, 1, null, 10, "Utøveren er flink!");
