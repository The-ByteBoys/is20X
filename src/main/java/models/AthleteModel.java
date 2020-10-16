package models;

import enums.*;

import java.util.ArrayList;

public class AthleteModel extends AbstractModel {
    private ArrayList<String> clubs = new ArrayList<>();

    /**
     * 
     * @param fname First name of the athlete
     * @param lname Last name of the athlete
     * @param birth Birth year of the athlete
     * @param sex Sex of the athlete (F/M/O)
     */
    public AthleteModel(String fname, String lname, Integer birth, String sex){
        super();

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
