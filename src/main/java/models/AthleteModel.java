package models;

import enums.*;

public class AthleteModel extends AbstractModel {
    public AthleteModel(Integer id, String name, Integer birth, String club){
        super();

        field.put(Athlete.ID, id);
        field.put(Athlete.NAME, name);
        field.put(Athlete.BIRTH, birth);
        field.put(Athlete.CLUB, club);
    }
}
