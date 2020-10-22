package models;

import enums.Result;

import java.sql.Date;

/**
 * @author Eirik Svagård
 */
public class ResultModel extends AbstractModel {

    public ResultModel(int athleteid, int exerciseid, double result, Date dateTime, String resultType){
        super();

        field.put(Result.ATHLETEID, athleteid);
        field.put(Result.EXERCISEID, exerciseid);
        field.put(Result.RESULT, result);
        field.put(Result.DATETIME, dateTime);
        field.put(Result.TYPE, resultType);
    }
}
