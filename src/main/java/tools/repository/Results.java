package tools.repository;

import enums.Exercise;
import enums.Result;
import models.ResultModel;
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
public class Results {

    private Results() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Query for all results
     *
     * @return List<ResultModel>
     * @throws SQLException
     */
    public static List<ResultModel> getResultsForAthlete(int AthleteId) throws SQLException {
        List<ResultModel> toReturn = new ArrayList<>();

        try {
            String query = "SELECT athlete, exercise, `result`, date_time, result_Type FROM `result` r WHERE r.athlete = ? ORDER BY DATE_FORMAT(date_time, '%Y-%m-%d') DESC";

            ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, AthleteId);

            while (rs.next()) {
                ResultModel exercise = new ResultModel(rs.getInt("athlete"), rs.getInt("exercise"), rs.getDouble("result"), rs.getTimestamp("date_time"), rs.getString("result_Type"));
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
     * Query for all results in a club
     *
     * @return List<ResultModel>
     * @throws SQLException
     */
    public static List<ResultModel> getResultsFromClub(int ClubId) throws SQLException {
        List<ResultModel> toReturn = new ArrayList<>();

        try {
            String query = "SELECT r.athlete, exercise, CONCAT(a.firstName, ' ', a.lastName) athleteName, e.name, e.unit, result, date_time, result_Type\n" +
                    "FROM result r\n" +
                    "INNER JOIN club_reg cr on r.athlete = cr.athlete\n" +
                    "INNER JOIN exercise e on r.exercise = e.exercise_id\n" +
                    "INNER JOIN athlete a on cr.athlete = a.athlete_id\n" +
                    "WHERE cr.club = ? ORDER BY DATE_FORMAT(date_time, '%Y-%m-%d %HH-%MM-%SS') DESC";

            ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, ClubId);

            while (rs.next()) {
                ResultModel result = new ResultModel(rs.getInt("r.athlete"), rs.getInt("exercise"), rs.getDouble("result"), rs.getTimestamp("date_time"), rs.getString("result_Type"));
                result.set(Result.ATHLETENAME, rs.getString("athleteName"));
                result.set(Result.EXERCISENAME, rs.getString("e.name"));
                result.set(Result.EXERCISEUNIT, rs.getString("e.unit"));
                toReturn.add(result);
            }

            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }

        return toReturn;
    }

    /**
     * Query for all results
     *
     * @return List<ResultModel>
     * @throws SQLException
     */
    public static List<ResultModel> getResults() throws SQLException {
        List<ResultModel> toReturn = new ArrayList<>();

        try {
            String query = "SELECT e.exercise_id, e.name, e.description, e.unit, e.exerciseType FROM exercise e";

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while (rs.next()) {
//                ResultModel exercise = new ResultModel(rs.getInt("e.exercise_id"), rs.getString("e.name"), rs.getString("e.description"), rs.getString("e.unit"), rs.getString("e.exerciseType"));
//                toReturn.add(exercise);
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
     * @return ResultModel
     * @throws SQLException
     */
    public static ResultModel getResult(String name, String unit) throws SQLException {
        String queryWhere = "";
        ResultModel exercise = null;

        queryWhere = "name = '"+name+"' AND unit = '"+unit.toUpperCase()+"'";

        try {
            String query = "SELECT e.exercise_id, e.name, e.description, e.unit, e.exerciseType FROM exercise e WHERE "+queryWhere;

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while(rs.next()){
//                exercise = new ResultModel(rs.getInt("e.exercise_id"), rs.getString("e.name"), rs.getString("e.description"), rs.getString("e.unit"), rs.getString("e.exerciseType"));
            }

            rs.close();
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }

        return exercise;
    }

//    public static int getExerciseId(String name, String unit){
//        int toReturn = 0;
//        try {
//            ResultModel exercise = getExercise(name, unit);
//            if(exercise != null){
//                toReturn = Integer.parseInt(exercise.get(Exercise.ID).toString());
//            }
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//        return toReturn;
//    }

    /**
     * Method to add exercise to the datasource, if it doesn't already exist
     *
     * @param newResult <ResultModel>
     * @return
     * @throws NamingException
     * @throws SQLException
     */
    public static Integer addResult(ResultModel newResult) throws Exception {

//        ResultModel checkIfExistsExercise= getResult(newExercise.get(Exercise.NAME).toString(), newExercise.get(Exercise.UNIT).toString());

//        if(checkIfExistsExercise != null) {
//            return (int) checkIfExistsExercise.get(Exercise.ID);
//        }
//        else {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("athlete",       newResult.get(Result.ATHLETEID));
            parameters.put("exercise",      newResult.get(Result.EXERCISEID));
            parameters.put("result",        newResult.get(Result.RESULT));
            parameters.put("date_time",     newResult.get(Result.DATETIME));
            parameters.put("result_Type",   newResult.get(Result.TYPE));

            Context ctx = new InitialContext();
            JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) ctx.lookup("roingdb"));

            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("result").usingGeneratedKeyColumns("exercise_id");
            return insert.execute(parameters);
//        }
    }
}