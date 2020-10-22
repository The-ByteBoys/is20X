package models;

import enums.Athlete;
import enums.Exercise;

import java.util.ArrayList;

public class ExerciseModel extends AbstractModel {

    /**
     * Exercise Model
     *
     * @param id
     * @param name          VARCHAR(50)
     * @param description   VARCHAR(250)
     * @param unit          ENUM('WATT', 'TIME', 'KG', 'PERCENT', 'REPS', 'METER', 'CM')
     * @param exerciseType  ENUM('ALLEX', 'CLUBEX')
     */
    public ExerciseModel(int id, String name, String description, String unit, String exerciseType){
        super();

        field.put(Exercise.ID, id);
        field.put(Exercise.NAME, name);
        field.put(Exercise.DESCR, description);
        field.put(Exercise.UNIT, unit);
        field.put(Exercise.TYPE, exerciseType);
    }
}
