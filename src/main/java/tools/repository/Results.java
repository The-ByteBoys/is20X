package tools.repository;

import enums.Exercise;
import enums.Result;
import models.ExerciseModel;
import models.ResultModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tools.DbTool;
import tools.PointCalculator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author Eirik Svagård
 */
public class Results {

    private Results() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * Queries database for results with the given options:
     * @param year      query year
     * @param week      query week
     * @param classSex  query sex (M, F, O)
     * @param className query class-name (SENIOR, A, B, C)
     * @return Map<athleteID,  Map<exerciseID,  ResultModel>>
     * @throws SQLException if query fails
     */
    public static Map<Integer, Map<Integer, ResultModel>> getResultsByFilter(int year, int week, String classSex, String className) throws SQLException {
        Calendar cal = Calendar.getInstance();
        cal.setWeekDate(year, week, Calendar.MONDAY);

        Calendar beforeCal = Calendar.getInstance();
        Calendar afterCal = Calendar.getInstance();

        afterCal.setTimeInMillis(cal.getTimeInMillis()-7*24*60*60*1000);
        beforeCal.setTimeInMillis(cal.getTimeInMillis()+24*60*60*1000);

        String afterDate = (afterCal.get(Calendar.YEAR))+ "-"+ (afterCal.get(Calendar.MONTH)+1) + "-"+ afterCal.get(Calendar.DATE);
        String beforeDate = (beforeCal.get(Calendar.YEAR))+ "-"+ (beforeCal.get(Calendar.MONTH)+1) + "-"+ beforeCal.get(Calendar.DATE);


        Map<Integer, Map<Integer, ResultModel>> resultsByAthleteExercise = new HashMap<>();

        String query = "SELECT * FROM resultData WHERE DATE_FORMAT(date_time, '%Y-%m-%d') > ? AND DATE_FORMAT(date_time, '%Y-%m-%d') < ? AND sex = ? AND className = ? AND result_Type = 'IP'";
        try(ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, afterDate, beforeDate, classSex, className)) {

            if (rs == null) {
                return null;
            }

            while (rs.next()) {
                int athleteId = rs.getInt("athlete");
                int exerciseId = rs.getInt("exercise");

                ResultModel resultModel = new ResultModel(athleteId, exerciseId, rs.getDouble("result"), rs.getTimestamp("date_time"), rs.getString("result_Type"));

                resultsByAthleteExercise.computeIfAbsent(athleteId, k -> new HashMap<>());
                resultsByAthleteExercise.get(athleteId).put(exerciseId, resultModel);
            }

            return resultsByAthleteExercise;
        }
    }


    /**
     * Calculate scores from a map and put them as exercise -1 in a new map
     * @param resultsByAthleteExercise Map<athleteID, Map<exerciseID, ResultModel>>
     * @param exercises                List<ExerciseModel>
     * @return Map<athleteID, Map<exerciseID, Result>>
     */
    public static Map<Integer, Map<Integer, Double>> calculateScores(Map<Integer, Map<Integer, ResultModel>> resultsByAthleteExercise, List<ExerciseModel> exercises){
        HashMap<Integer, ArrayList<Double>> exerciseNums = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, ResultModel>> resultsByAthleteExerciseMapEntry : resultsByAthleteExercise.entrySet()) {
            Map<Integer, ResultModel> athleteResult = resultsByAthleteExerciseMapEntry.getValue();
            for(ExerciseModel ex : exercises){
                Integer exerciseId = Integer.parseInt(ex.get(Exercise.ID).toString());

                Double result = athleteResult.get(exerciseId) != null
                        ? Double.parseDouble(athleteResult.get(exerciseId).get(Result.RESULT).toString())
                        : 0.0;

                if(!exerciseNums.containsKey(exerciseId)){
                    exerciseNums.put(exerciseId, new ArrayList<>());
                }
                exerciseNums.get(exerciseId).add(result);
            }
        }

        HashMap<Integer, PointCalculator> pointCalcPerExercise = new HashMap<>();
        for (Map.Entry<Integer, ArrayList<Double>> exerciseMapEntry : exerciseNums.entrySet()) {
            ArrayList<Double> numbers = exerciseMapEntry.getValue();

            PointCalculator pointCalc = new PointCalculator(numbers);
            pointCalcPerExercise.put(exerciseMapEntry.getKey(), pointCalc);
        }

        Map<Integer, Map<Integer, Double>> returnMap = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, ResultModel>> resultsByAthleteExerciseMapEntry : resultsByAthleteExercise.entrySet()) {
            Map<Integer, ResultModel> athleteResult = resultsByAthleteExerciseMapEntry.getValue();
            int athleteId = resultsByAthleteExerciseMapEntry.getKey();
            returnMap.put(athleteId, new HashMap<>());

            double totalPoints = 0;
            for (ExerciseModel ex : exercises) {
                int exerciseId = Integer.parseInt(ex.get(Exercise.ID).toString());

                PointCalculator pointCalc = pointCalcPerExercise.get(exerciseId);

                double result = 0.0;
                if(athleteResult.get(exerciseId) != null){
                    result = Double.parseDouble(athleteResult.get(exerciseId).get(Result.RESULT).toString());
                }
                totalPoints += pointCalc.getPoints(result, Integer.parseInt(ex.get(Exercise.WEIGHT).toString()));

                returnMap.get(athleteId).put(exerciseId, result);
            }

            returnMap.get(athleteId).put(-1, totalPoints*10+80);
        }

        return returnMap;
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
                    "WHERE cr.club = ? ORDER BY DATE_FORMAT(date_time, '%Y-%m-%d %H %i %s') DESC";

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


    public static void publishResult(int athlete, int exercise, String date_time) {
        try {
            String query = "UPDATE result r\n" +
                    "SET result_Type = 'IP'\n" +
                    "WHERE (athlete, exercise, date_time) = (?, ?, ?)";
            ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, athlete, exercise, date_time);
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<ResultModel> getBestResultsInTestBattery() {
        List<ResultModel> toReturn = new ArrayList<>();

        try {
            String query = "SELECT r.athlete, r.exercise, max(r.result) result, max(r.date_time) dateTime, r.result_Type\n" +
                    "FROM result r\n" +
                    "WHERE r.result_Type = 'NP' AND r.date_time BETWEEN NOW() - INTERVAL 14 DAY AND NOW()\n" +
                    "GROUP BY athlete, exercise";

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);
            while (rs.next()) {
                ResultModel result = new ResultModel(rs.getInt("r.athlete"), rs.getInt("r.exercise"), rs.getDouble("result"), rs.getTimestamp("dateTime"), rs.getString("result_Type"));
                toReturn.add(result);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
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
            String query = "SELECT r.athlete, CONCAT(a.firstName, ' ', a.lastName) athleteName, r.exercise, r.`result`, r.date_time, r.result_Type FROM `result` r " +
                    "INNER JOIN athlete a ON r.athlete = a.athlete_id ORDER BY DATE_FORMAT(date_time, '%Y-%m-%d') DESC";

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while (rs.next()) {
                ResultModel result = new ResultModel(rs.getInt("athlete"), rs.getInt("exercise"), rs.getDouble("result"), rs.getTimestamp("date_time"), rs.getString("result_Type"));
                result.set(Result.ATHLETENAME, rs.getString("athleteName"));
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

    /**
     * Inserts a batch of results
     * @param listOfAttributes contains lists with attributes
     */
    public static void addResultBatch(ArrayList<ArrayList<Object>> listOfAttributes) {

        String query = "INSERT INTO result (athlete, exercise, result, date_time, result_Type)\n" +
                "VALUES (?, ?, ?, ?, 'NP')";
        try {
            Connection db = DbTool.getINSTANCE().dbLoggIn();
            PreparedStatement pstm = db.prepareStatement(query);

            for (ArrayList<Object> attributes : listOfAttributes) {
                pstm.setInt(1, (int) attributes.get(0));
                pstm.setInt(2, (int) attributes.get(1));
                pstm.setDouble(3, (double) attributes.get(2));
                pstm.setString(4, attributes.get(3).toString());

                pstm.addBatch();
            }

            pstm.executeBatch();

            pstm.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}