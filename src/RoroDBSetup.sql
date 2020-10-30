DROP DATABASE IF EXISTS roro;
CREATE DATABASE roro;

USE roro;


CREATE TABLE athlete (
    athlete_id INT AUTO_INCREMENT,
    firstName VARCHAR(250) NOT NULL,
    lastName VARCHAR(250) NOT NULL,
    birth smallint(4) NOT NULL,
    sex ENUM('F', 'M', 'O') NOT NULL,

    CONSTRAINT athlete_PK PRIMARY KEY (athlete_id),
    CONSTRAINT athlete_UN UNIQUE KEY (firstName, lastName, birth)
);


CREATE TABLE user (
    user_id INT AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password varchar(250) NOT NULL,
    userType enum('ADMIN', 'ATHLETE', 'COACH') NOT NULL,

    CONSTRAINT user_PK PRIMARY KEY (user_id)
);

CREATE TABLE userLogin (
    user INT AUTO_INCREMENT,
    created timestamp default CURRENT_TIMESTAMP,
    loginToken VARCHAR(20) NOT NULL,

    CONSTRAINT userLogin_FK FOREIGN KEY (user) REFERENCES user(user_id)
);


CREATE TABLE club_user (
    user INT NOT NULL,
    athlete INT NOT NULL,

    CONSTRAINT club_user_user_FK FOREIGN KEY (user) REFERENCES user(user_id),
    CONSTRAINT club_user_athlete_FK FOREIGN KEY (athlete) REFERENCES athlete(athlete_id)
);


CREATE TABLE club (
    club_id INT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,

    CONSTRAINT club_PK PRIMARY KEY (club_id)
);


CREATE TABLE club_reg (
    athlete INT NOT NULL,
    club INT NOT NULL,

    CONSTRAINT club_reg_athlete_FK FOREIGN KEY (athlete) REFERENCES athlete(athlete_id),
    CONSTRAINT club_reg_club_FK FOREIGN KEY (club) REFERENCES club(club_id),
    CONSTRAINT club_reg_PK PRIMARY KEY (athlete, club));


CREATE TABLE exercise (
    exercise_id INT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(250) NOT NULL,
    unit ENUM('WATT', 'TIME', 'KG', 'PERCENT', 'REPS', 'METER', 'CM'),
    exerciseType ENUM('ALLEX', 'CLUBEX'),

    CONSTRAINT EXERCISE_PK PRIMARY KEY (exercise_id)
);


CREATE TABLE club_exercise (
    exercise INT NOT NULL,
    club INT NOT NULL,

    CONSTRAINT club_exercise_exercise_FK FOREIGN KEY (exercise) REFERENCES exercise(exercise_id),
    CONSTRAINT club_exercise_club_FK FOREIGN KEY (club) REFERENCES club(club_id),
    CONSTRAINT club_exercise_PK PRIMARY KEY (club, exercise)
);


CREATE TABLE class (
    class_id INT AUTO_INCREMENT,
    name varchar(10) NOT NULL,
    ageFrom INT(2) NOT NULL,

    CONSTRAINT class_PK PRIMARY KEY (class_id)
);

CREATE TABLE class_period (
    period_id INT AUTO_INCREMENT,
    start DATE NOT NULL,
    athlete INT NOT NULL ,
    class INT NOT NULL ,

    CONSTRAINT class_period_PK PRIMARY KEY (period_id),
    CONSTRAINT class_period_class_FK FOREIGN KEY (class) REFERENCES class(class_id),
    CONSTRAINT class_period_athlete_FK FOREIGN KEY (athlete) REFERENCES athlete(athlete_id)

);

CREATE TABLE test_set (
    class INT NOT NULL,
    exercise INT NOT NULL,
    weight INT NOT NULL,


    CONSTRAINT test_set_exercise_FK FOREIGN KEY (exercise) REFERENCES exercise(exercise_id),
    CONSTRAINT test_set_Class_FK FOREIGN KEY (class) REFERENCES class(class_id)
    /*CONSTRAINT test_set_PK PRIMARY KEY (exercise, testClass)*/
);


CREATE TABLE result (
    athlete INT NOT NULL,
    exercise INT NOT NULL,
    result DECIMAL(8,3),
    date_time DATETIME NOT NULL,
    result_Type ENUM('IP', 'NP', 'CT') NOT NULL,
    /* result_Type can be IP = IS PUBLISHED, NP = NOT PUBLISHED and CT = CLUB TEST */

    CONSTRAINT result_athlete_FK FOREIGN KEY (athlete) REFERENCES athlete(athlete_id),
    CONSTRAINT result_exercise_FK FOREIGN KEY (exercise) REFERENCES exercise(exercise_id),
    CONSTRAINT result_PK PRIMARY KEY (athlete, exercise, date_time)
);


INSERT INTO class (class_id, name, ageFrom)
    VALUES
        (1, 'SENIOR', 19),
        (2, 'A', 17),
        (3, 'B', 15),
        (4, 'C', 13);

INSERT INTO user (user_id, email, password, userType)
    VALUES
        (1, 'admin@admin', 'DA4263CC96DD21071FC9C864A74C774D', 'ADMIN');

INSERT INTO exercise (exercise_id, name, description, unit, exerciseType)
    VALUES
        (1, '5000', '', 'WATT', 'ALLEX'),
        (2, '5000', '', 'TIME', 'ALLEX'),
        (3, '3000', '', 'WATT', 'ALLEX'),
        (4, '3000', '', 'TIME', 'ALLEX'),
        (5, '2000', '', 'WATT', 'ALLEX'),
        (6, '2000', '', 'TIME', 'ALLEX'),
        (7, '60', 'WATT produsert på 60 sek', 'WATT', 'ALLEX'),
        (8, 'ligg.ro', '', 'PERCENT', 'ALLEX'),
        (9, 'ligg.ro', '', 'KG', 'ALLEX'),
        (10, 'knebøy', '', 'PERCENT', 'ALLEX'),
        (11, 'knebøy', '', 'KG', 'ALLEX'),
        (12, 'bevegelse', '', 'REPS', 'ALLEX'),
        (13, 'sargeant', '', 'CM', 'ALLEX'),
        (14, 'kroppshevning', '', 'REPS', 'ALLEX'),

        (15, '100Sek', 'lengde på 100 sek', 'METER', 'CLUBEX');

INSERT INTO test_set (class, exercise, weight)
    VALUES
        (1, 1, 45), (1, 2, 0), (1, 5, 30), (1, 6, 0), (1, 7, 10), (1, 8, 5), (1, 9, 0), (1, 10, 5), (1, 11, 0), (1, 12, 5),
        (2, 1, 45), (2, 2, 0), (2, 5, 30), (2, 6, 0), (2, 7, 10), (2, 8, 5), (2, 9, 0), (2, 13, 5), (2, 12, 5),
        (3, 3, 40), (3, 4, 0), (3, 5, 40), (3, 6, 0), (3, 7, 5), (3, 14, 5), (3, 13, 5), (3, 12, 5),
        (4, 4, 0), (4, 7, 0), (4, 14, 0), (4, 13, 0), (4, 12, 0);


# DUMMY DATA BELOW:
INSERT INTO athlete (firstName, lastName, birth, sex)
VALUES ('Johannes', 'Birkeland', 2000, 'M'),
       ('Mona', 'Bond', 1950, 'F'),
       ('Per', 'Olavsen', 2003, 'M'),
       ('Sonja', 'Haraldsen', 2005, 'F'),
       ('Frank', 'Haarfagre', 2007, 'M');

INSERT INTO club_user (user, athlete) VALUES (1, 1);

INSERT INTO club (name) VALUES ('BRK');

INSERT INTO club_reg (athlete, club) VALUES (1, 1);

INSERT INTO club_exercise (exercise, club) VALUES (3, 1);

INSERT INTO class_period (start, athlete, class)
    VALUES
        ('2019-11-19', 1, 1);

INSERT INTO result (athlete, exercise, result, date_time, result_Type)
    VALUES
        (1, 1, 700.999, '2020-08-15 15:41:33', 'NP');

