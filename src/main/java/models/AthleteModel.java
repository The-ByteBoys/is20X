package models;

import enums.*;

import java.sql.Date;
import java.util.ArrayList;

public class AthleteModel extends AbstractModel {
    private ArrayList<String> clubs = new ArrayList<>();

    /**
     * 
     * @param athleteid The ID of the user. May be null!
     * @param fname First name of the athlete
     * @param lname Last name of the athlete
     * @param birth Birthdate of the athlete
     * @param sex Sex of the athlete (F/M/O)
     */
    public AthleteModel(Integer athleteid, String fname, String lname, Date birth, String sex){
        super();

        field.put(Athlete.ID, athleteid);
        field.put(Athlete.FNAME, fname);
        field.put(Athlete.LNAME, lname);
        field.put(Athlete.BIRTH, birth);
        field.put(Athlete.SEX, sex);
    }

    public void setAthleteClass(String newClass){
        field.put(Athlete.CLASS, newClass);
    }

    public void addAthleteClub(String newClub){
        clubs.add(newClub);
    }

    public ArrayList<String> getClubs(){
        return this.clubs;
    }
}
