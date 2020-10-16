DROP DATABASE IF EXISTS roro;
CREATE DATABASE roro;

USE roro;


CREATE TABLE athlete (
    athlete_id INT AUTO_INCREMENT,
    firstName VARCHAR(250) NOT NULL,
    lastName VARCHAR(250) NOT NULL,
    birth DATE NOT NULL,
    sex ENUM('F', 'M', 'O') NOT NULL,

    CONSTRAINT athlete_PK PRIMARY KEY (athlete_id)
);


CREATE TABLE user (
    user_id INT AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password varchar(250) NOT NULL,

    CONSTRAINT user_PK PRIMARY KEY (user_id)
);


CREATE TABLE club_user (
    user INT NOT NULL,
    athlete INT NOT NULL,

    CONSTRAINT club_user_user_FK FOREIGN KEY (user) REFERENCES user(user_id),
    CONSTRAINT club_user_club_athlete_FK FOREIGN KEY (athlete) REFERENCES athlete(athlete_id)
);


CREATE TABLE club (
    club_id INT AUTO_INCREMENT,
    name VARCHAR(250) NOT NULL UNIQUE,

    CONSTRAINT club_PK PRIMARY KEY (club_id)
);


CREATE TABLE club_reg (
    athlete INT NOT NULL,
    club INT NOT NULL,

    CONSTRAINT club_reg_athlete_FK FOREIGN KEY (athlete) REFERENCES athlete(athlete_id),
    CONSTRAINT club_reg_club_FK FOREIGN KEY (club) REFERENCES club(club_id)
);


CREATE TABLE exercise (
    exercise_id INT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(250) NOT NULL,
    unit VARCHAR(10),

    CONSTRAINT EXERCISE_PK PRIMARY KEY (exercise_id)
);


CREATE TABLE club_exercise (
    exercise INT NOT NULL,
    club INT NOT NULL,

    CONSTRAINT club_exercise_exercise_FK FOREIGN KEY (exercise) REFERENCES exercise(exercise_id),
    CONSTRAINT club_exercise_club_FK FOREIGN KEY (club) REFERENCES club(club_id)
);


CREATE TABLE test_class (
    testClass_id INT AUTO_INCREMENT,
    name varchar(10) NOT NULL ,

    CONSTRAINT test_class_PK PRIMARY KEY (testClass_id)
);


CREATE TABLE class (
    class_id INT AUTO_INCREMENT,
    class_sex ENUM('F', 'M', 'O') NOT NULL,
    age_From INT(2) NOT NULL,
    testClass INT NOT NULL,

    CONSTRAINT class_PK PRIMARY KEY (class_id),
    CONSTRAINT class_testClass_FK FOREIGN KEY (testClass) REFERENCES test_class(testClass_id)
);


CREATE TABLE class_period (
    period_id INT AUTO_INCREMENT,
    start DATE NOT NULL,
    athlete INT NOT NULL ,
    class INT NOT NULL ,

    CONSTRAINT class_period_PK PRIMARY KEY (period_id),
    CONSTRAINT class_period_athlete_FK FOREIGN KEY (athlete) REFERENCES athlete(athlete_id)
);

CREATE TABLE test_set (
    exercise INT NOT NULL,
    testClass INT NOT NULL,

    CONSTRAINT test_set_exercise_FK FOREIGN KEY (exercise) REFERENCES exercise(exercise_id),
    CONSTRAINT test_set_testClass_FK FOREIGN KEY (testClass) REFERENCES test_class(testClass_id)
);


CREATE TABLE result (
    athlete INT NOT NULL,
    exercise INT NOT NULL,
    result DECIMAL(5,3),
    date_time DATETIME NOT NULL,
    result_Type ENUM('IP', 'NP', 'CT') NOT NULL,
    /* result_Type can be IP = IS PUBLISHED, NP = NOT PUBLISHED and CT = CLUB TEST */

    CONSTRAINT result_athlete_FK FOREIGN KEY (athlete) REFERENCES athlete(athlete_id),
    CONSTRAINT result_exercise_FK FOREIGN KEY (exercise) REFERENCES exercise(exercise_id)
)


