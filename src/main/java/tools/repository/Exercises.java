package tools.repository;

import enums.Exercise;
import enums.Result;
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
     * Query for all exercises connected to a class, plus the club-specific ones
     *
     * @return List<ExerciseModel>
     * @param exClass Exercise class to check for. Currently either "Senior", "A", "B", "C"
     * @throws SQLException if sql fails
     */
    public static List<ExerciseModel> getExercisesForClass(String exClass) throws SQLException {
        return getExercisesForClass(exClass, 0);
    }

    public static List<ExerciseModel> getExercisesForClass(String exClass, int clubId) throws SQLException {
        List<ExerciseModel> toReturn = new ArrayList<>();

        String query = "SELECT e.exercise_id, e.name, e.description, e.unit, e.exerciseType FROM exercise e" +
                " LEFT OUTER JOIN test_set ts ON e.exercise_id = ts.exercise" +
                " LEFT OUTER JOIN class c ON c.class_id = ts.class" +
                " LEFT OUTER JOIN club_exercise ce ON e.exercise_id = ce.exercise" +
                " WHERE c.name = ? OR (exerciseType = 'CLUBEX' AND ce.club = ?)";

        ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, exClass, clubId);
        while (rs.next()) {
            ExerciseModel exercise = new ExerciseModel(rs.getInt("e.exercise_id"), rs.getString("e.name"), rs.getString("e.description"), rs.getString("e.unit"), rs.getString("e.exerciseType"));
            toReturn.add(exercise);
        }

        return toReturn;
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

    public static ExerciseModel getExerciseFromId(int exid) throws SQLException {
        return getExerciseFromId(exid, true);
    }
    public static ExerciseModel getExerciseFromId(int exid, boolean onlyPublished) throws SQLException {
        String query = "SELECT exercise_id, name, description, unit, exerciseType FROM exercise e WHERE exercise_id = ?"+(onlyPublished?" AND exerciseType = 'ALLEX'":"");

        try(ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, exid)){
            while (rs.next()){
                return new ExerciseModel(rs.getInt("exercise_id"), rs.getString("name"), rs.getString("description"), rs.getString("unit"), rs.getString("exerciseType") );
            }
        }
        return null;
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
     * @return Integer of the new exercise ID
     * @throws NamingException if datasource is not found
     * @throws SQLException -
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

            JdbcTemplate jdbcTemplate = new JdbcTemplate(DbTool.getINSTANCE().getDataSource());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("exercise").usingGeneratedKeyColumns("exercise_id");
            return insert.executeAndReturnKey(parameters).intValue();
        }
    }
}