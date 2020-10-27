package tools.repository;

import enums.Exercise;
import models.ExerciseModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tools.DbTool;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eirik Svagård
 */
public class Exercises {

    private Exercises() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Query for all exercises
     *
     * @return List<ExerciseModel>
     * @throws SQLException
     */
    public static List<ExerciseModel> getExercises() throws SQLException {
        List<ExerciseModel> toReturn = new ArrayList<>();

        try {
            String query = "SELECT e.exercise_id, e.name, e.description, e.unit, e.exerciseType FROM exercise e";

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while (rs.next()) {
                ExerciseModel exercise = new ExerciseModel(rs.getInt("e.exercise_id"), rs.getString("e.name"), rs.getString("e.description"), rs.getString("e.unit"), rs.getString("e.exerciseType"));
                toReturn.add(exercise);
            }

            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }

        return toReturn;
    }

    /**
     * Query for a single exercise
     *
     * @param name Exercise-name
     * @param unit Exercise-unit
     * @return ExerciseModel
     * @throws SQLException
     */
    public static ExerciseModel getExercise(String name, String unit) throws SQLException {
        String queryWhere = "";
        ExerciseModel exercise = null;

        queryWhere = "name = '"+name+"' AND unit = '"+unit.toUpperCase()+"'";

        try {
            String query = "SELECT e.exercise_id, e.name, e.description, e.unit, e.exerciseType FROM exercise e WHERE "+queryWhere;

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while(rs.next()){
                exercise = new ExerciseModel(rs.getInt("e.exercise_id"), rs.getString("e.name"), rs.getString("e.description"), rs.getString("e.unit"), rs.getString("e.exerciseType"));
            }

            rs.close();
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }

        return exercise;
    }

    public static int getExerciseId(String name, String unit){
        int toReturn = 0;
        try {
            ExerciseModel exercise = getExercise(name, unit);
            if(exercise != null){
                toReturn = Integer.parseInt(exercise.get(Exercise.ID).toString());
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * Method to add exercise to the datasource, if it doesn't already exist
     *
     * @param newExercise <ExerciseModel>
     * @return
     * @throws NamingException
     * @throws SQLException
     */
    public static Integer addExercise(ExerciseModel newExercise) throws NamingException, SQLException {
        ExerciseModel checkIfExistsExercise= getExercise(newExercise.get(Exercise.NAME).toString(), newExercise.get(Exercise.UNIT).toString());

        if(checkIfExistsExercise != null) {
            return (int) checkIfExistsExercise.get(Exercise.ID);
        }
        else {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("name", newExercise.get(Exercise.NAME));
            parameters.put("description", newExercise.get(Exercise.DESCR));
            parameters.put("unit", newExercise.get(Exercise.UNIT));
            parameters.put("exerciseType", newExercise.get(Exercise.TYPE));

            Context ctx = new InitialContext();
            JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) ctx.lookup("roingdb"));

            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("exercise").usingGeneratedKeyColumns("exercise_id");
            return insert.executeAndReturnKey(parameters).intValue();
        }
    }
}