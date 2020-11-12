<%@ page import="tools.DbTool"
         import="tools.UserAuth"
         import="tools.htmltools.HtmlConstants"
         import="models.UserModel"
         import="models.ResultModel"
         import="java.sql.ResultSet"
         import="java.sql.SQLException"
         import="java.util.HashMap"
         import="java.util.ArrayList"
         import="enums.User"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
%>
<%@ page import="enums.Result" %>
<%@ page import="java.util.Map" %>
<%@ page import="tools.PointCalculator" %>
<%@ page import="models.AthleteModel" %>
<%@ page import="tools.repository.Athletes" %>
<%@ page import="enums.Athlete" %>
<%
    UserModel currentUser = UserAuth.verifyLogin(request);
%><!DOCTYPE html>
<html lang="no">
<head>
    <title>Roing Webapp</title>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body style="text-align: center;">

<div id="nav-placeholder" style="min-height: 70px;"></div>
<%=UserAuth.navBarLogin(currentUser)%>
<script src="js/menu.js"></script>


<h1>Roing webapp</h1>
<table id="exTable" class="table table-striped table-bordered table-hover">
    <%
        // Initialiser hashmap(s)
        HashMap<Integer, HashMap<Integer, ResultModel>> resultsByAthleteExercise = new HashMap<>(); // Hashmap for å sortere resultatene
        HashMap<Integer, ArrayList<Double>> exerciseNums = new HashMap<>(); // Hashmap for å holde alle resultat-verdiene, så vi kan regne ut poengsum

        // Try-catch med ressurs (stenger rs automatisk)
        try(ResultSet rs = DbTool.getINSTANCE().selectQuery("SELECT * FROM publiserData WHERE DATE_FORMAT(date_time, '%Y-%m-%d') > '2019-10-20' AND DATE_FORMAT(date_time, '%Y-%m-%d') < '2019-10-30' AND className = 'SENIOR' AND sex = 'M'")){

            // Løkke for å gå igjennom resultatet
            while(rs.next()) {
                HashMap<Integer, ResultModel> athleteResults = new HashMap<>();  // hashmap for løkka
                ArrayList<Double> exerciseResults = new ArrayList<>();      // hashmap for løkka

                int athleteId = rs.getInt("athlete_id");
                int exerciseId = rs.getInt("exercise_id");

                // Hvis utøveren alt er i results-hashmappet så hent det hashmappet
                if (resultsByAthleteExercise.get(athleteId) != null) {
                    athleteResults = resultsByAthleteExercise.get(athleteId);
                }

                // Samme med øvelse-hashmappet
                if(exerciseNums.get(exerciseId) != null){
                    exerciseResults = exerciseNums.get(exerciseId);
                }

                // Lage result-model
                ResultModel resultModel = new ResultModel(athleteId, exerciseId, rs.getDouble("result"), rs.getTimestamp("date_time"), null);
                resultModel.set(Result.ATHLETENAME, rs.getString("firstName")+" "+rs.getString("lastName"));


                // Lagre verdiene i hvert sitt hashmap
                athleteResults.put(exerciseId, resultModel);
                exerciseResults.add(rs.getDouble("result"));

                // Lagre de nye verdiene i sine 'foreldre'-hashmaps
                exerciseNums.put(exerciseId, exerciseResults);
                resultsByAthleteExercise.put(athleteId, athleteResults);
            }

            out.print("<thead>");
            out.print("<tr>");
            out.print("<th>Fullt navn</th>");
            out.print("<th>Fødselsår</th>");
            out.print("<th>Klubb</th>");

            HashMap<Integer, PointCalculator> pointCalcPerExercise = new HashMap<>();

            for (Map.Entry<Integer, ArrayList<Double>> exerciseMapEntry : exerciseNums.entrySet()) {

                ArrayList<Double> numbers = exerciseMapEntry.getValue();

                PointCalculator pointCalc = new PointCalculator(numbers, 50);

                pointCalcPerExercise.put(exerciseMapEntry.getKey(), pointCalc);

                out.print("<th>"+exerciseMapEntry.getKey()+"<span style='font-size: 8px;'><br>MEAN: "+pointCalc.getMean()+"<br>St.dev: "+pointCalc.getStdev()+"</span></th>");
            }

            out.print("<th>Poeng?</th>");
            out.print("</tr>");
            out.print("</thead>");



            // Loop current Athletes
            out.print("<tbody>");
            for (Map.Entry<Integer, HashMap<Integer, ResultModel>> me : resultsByAthleteExercise.entrySet()) {
                Integer athleteId = me.getKey();
                HashMap<Integer, ResultModel> resultHash = me.getValue();
                AthleteModel athlete = Athletes.getAthlete(athleteId.toString());

                out.print("<tr>");
                out.print("<td>"+athlete.get(Athlete.FNAME)+" "+athlete.get(Athlete.LNAME)+"</td>");
                out.print("<td>"+athlete.get(Athlete.BIRTH)+"</td>");
                out.print("<td>"+athlete.get(Athlete.CLUBS)+"</td>");

                double totalPoints = 0.0;

                // EXERCISE LOOP
                for (Map.Entry<Integer, PointCalculator> pointCalcMapEntry : pointCalcPerExercise.entrySet()) {
                    int exerciseId = pointCalcMapEntry.getKey();
                    PointCalculator pointCalc = pointCalcMapEntry.getValue();




                    out.print("<td>");
                    if(resultHash.containsKey(exerciseId)){
                        ResultModel result = resultHash.get(exerciseId);
                        int weight = 0;

                        // IF SENIOR CLASS
                        if(exerciseId == 1){
                            weight = 45;
                        }
                        else if(exerciseId == 5){
                            weight = 30;
                        }
                        else if(exerciseId == 7){
                            weight = 10;
                        }
                        else if(exerciseId == 8 || exerciseId == 10 || exerciseId == 12){
                            weight = 5;
                        }

                        totalPoints += pointCalc.getPoints((double) result.get(Result.RESULT), weight);

                        out.print(result.get(Result.RESULT));
                    }
                    out.print("</td>");
                }

                out.print("<td>"+(totalPoints*10+80)+"</td>");
                out.print("</tr>");
            }
            out.print("</tbody>");

        }
        catch (SQLException e){
            out.print("SQL error: "+e);
        }
    %>
</table>

<script>
    $(document).ready(function () {
        $('#exTable').DataTable();
        $('.dataTables_length').addClass('bs-select');
    });
</script>

</body>
</html>
