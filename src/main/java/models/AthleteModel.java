package models;

import enums.*;

public class AthleteModel extends AbstractModel {
    /**
     * 
     * @param name
     * @param birth
     * @param club
     * @param sex
     */
    public AthleteModel(String name, Integer birth, String club, String sex){
        super();

        // field.put(Athlete.ID, null);
        field.put(Athlete.NAME, name);
        field.put(Athlete.BIRTH, birth);
        field.put(Athlete.CLUB, club);
        // field.put(Athlete.CLASS, "");
        field.put(Athlete.SEX, sex);
    }

    public void setAthleteClass(String newClass){
        field.put(Athlete.CLASS, newClass);
    }
}
