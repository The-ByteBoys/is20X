<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"

         import="enums.*"
         import="models.*"
         import="tools.UserAuth"
         import="tools.htmltools.HtmlConstants"
         import="tools.htmltools.HtmlTableUtil"
         import="tools.repository.Athletes"
         import="tools.repository.Exercises"
         import="tools.repository.Results"

         import="java.util.*"
         import="java.sql.SQLException"
         import="java.text.DecimalFormat"
%><%
   UserModel currentUser = UserAuth.verifyLogin(request);
%><!DOCTYPE html>
<html lang="no">
<head>
    <title>Roing Webapp</title>
    <%=HtmlConstants.getHtmlHeaders()%>
    <style>
        #exTable {
            margin: auto;
            /*width: initial;*/
        }

        #exTable a {
            color: inherit !important;
            text-decoration: underline;
        }
        #exTable a:hover {
            color: #f4511e !important;
            text-decoration: underline;
        }
    </style>
</head>
<body style="text-align: center;">

<div id="nav-placeholder" style="min-height: 70px;"></div>
<%=UserAuth.navBarLogin(currentUser)%>
<script src="js/menu.js"></script>


<h1>Roing webapp</h1>
<%
    Calendar cal = Calendar.getInstance();
    int year = request.getParameter("year") != null
            ? Integer.parseInt(request.getParameter("year"))
            : cal.get(Calendar.YEAR);

    int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);

    int week = request.getParameter("week")!= null
            ? Integer.parseInt(request.getParameter("week"))
            : currentWeek>=44?44: currentWeek>=11?11: 2;

    String className = request.getParameter("class")!= null?request.getParameter("class"):"SENIOR";
    String classSex = request.getParameter("sex")!=null?request.getParameter("sex"):"M";




%>
<div class="container-sm" style="max-width: 500px; margin: auto;">
    <form action="./">
        <div class="form-row">
            <div class="col">
                <label>
                    <select name="year" class="form-control" onchange="this.form.submit();">
                    <%
                        for (int i = Calendar.getInstance().get(Calendar.YEAR); i >= 2016; i--) {
                            out.print("<option"+(Integer.toString(year).equals(Integer.toString(i))?" selected":"")+">"+i+"</option>");
                        }
                    %>
                    </select>
                </label>
            </div>
            <div class="col">
                <label>
                    <select name="week" class="form-control" onchange="this.form.submit();">
                        <option value="2"<%=Integer.toString(week).equals("2")?" selected":""%>>Uke 2</option>
                        <option value="11"<%=Integer.toString(week).equals("11")?" selected":""%>>Uke 11</option>
                        <option value="44"<%=Integer.toString(week).equals("44")?" selected":""%>>Uke 44</option>
                    </select>
                </label>
            </div>
            <div class="col">
                <label>
                    <select name="class" class="form-control" onchange="this.form.submit();">
                        <option value="SENIOR"<%=className.equals("SENIOR")?" selected":""%>>Senior</option>
                        <option value="A"<%=className.equals("A")?" selected":""%>>Junior A</option>
                        <option value="B"<%=className.equals("B")?" selected":""%>>Junior B</option>
                        <option value="C"<%=className.equals("C")?" selected":""%>>Junior C</option>
                    </select>
                </label>
            </div><div class="col">
                <label>
                    <select name="sex" class="form-control" onchange="this.form.submit();">
                        <option value="M"<%=classSex.equals("M")?" selected":""%>>Menn</option>
                        <option value="F"<%=classSex.equals("F")?" selected":""%>>Kvinner</option>
                        <option value="O"<%=classSex.equals("O")?" selected":""%>>Andre</option>
                    </select>
                </label>
            </div>
        </div>
    </form>
</div>

<div class="container-fluid" style="max-width: 1350px; text-align: left;">
<%
    DecimalFormat df = new DecimalFormat("00.##");

    List<ExerciseModel> exercises;
    try {
        exercises = Exercises.getExercisesForClass(className);
    } catch (SQLException throwables) {
        throwables.printStackTrace();
        out.print("CLASS NOT VALID");
        return;
    }

    Map<Integer, Map<Integer, ResultModel>> resultsByAthleteExercise;
    try {
        resultsByAthleteExercise = Results.getResultsByFilter(year, week, classSex, className);
    } catch (SQLException e) {
        e.printStackTrace();
        out.print("Something didn't work: "+e);
        return;
    }

    if(resultsByAthleteExercise == null || resultsByAthleteExercise.isEmpty()){
        out.print("<br><h4>Ingen data funnet for valgte parametre.</h4>");
        return;
    }


    Map<Integer, Map<Integer, Double>> resultsByAthleteExercisePoints = Results.calculateScores(resultsByAthleteExercise, exercises);

    HtmlTableUtil htmlTable = new HtmlTableUtil();
    htmlTable.setTableCaption("Testresultater for "+className+" "+classSex.replace("F", "Kvinner").replace("M", "Menn").replace("O", "Andre")+", "+year+" uke "+week);
    htmlTable.setTableID("exTable");

    if(!className.equals("C")) {
        htmlTable.addHeader("Poeng");
    }
    htmlTable.addHeader("Fødselsår");
    htmlTable.addHeader("Fullt navn");
    htmlTable.addHeader("Klubb(er)");

    for (Map.Entry<Integer, Map<Integer, Double>> resultsMapEntry : resultsByAthleteExercisePoints.entrySet()) {
        Integer athleteId = resultsMapEntry.getKey();
        Map<Integer, Double> athleteResult = resultsMapEntry.getValue();

        AthleteModel athlete;
        try {
            athlete = Athletes.getAthlete(athleteId.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            out.print("Athlete not found: "+e);
            return;
        }

        if(!className.equals("C")){
            htmlTable.addCell(df.format(athleteResult.get(-1)));
        }

        htmlTable.addCell(athlete.get(Athlete.BIRTH).toString().substring(0,4));

        String fullName = athlete.get(Athlete.FNAME)+" "+athlete.get(Athlete.LNAME);
        htmlTable.addCell("<a href='athlete/"+fullName.replace(" ","%20")+"' class='text-secondary'>"+fullName+"</a>");

        htmlTable.addCell(athlete.get(Athlete.CLUBS).toString());

//        double totalPoints = 0.0;

        for(ExerciseModel ex : exercises){
            Integer exerciseId = Integer.parseInt(ex.get(Exercise.ID).toString());
            htmlTable.addHeader(ex.get(Exercise.NAME)+" "+ex.get(Exercise.UNIT));
//            PointCalculator pointCalc = pointCalcPerExercise.get(exerciseId);

            double result = athleteResult.get(exerciseId) != null ? athleteResult.get(exerciseId) : 0.0;

            if(ex.get(Exercise.UNIT).toString().equals("TIME")){
                int newMinutes = (int) Math.floor(result/60);
                double newSecs = result - newMinutes*60;
                htmlTable.addCell(result>0?newMinutes+":"+df.format(newSecs):"");
            }
            else {
//                double newPoints = pointCalc.getPoints(result, Integer.parseInt(ex.get(Exercise.WEIGHT).toString()));
//                totalPoints += newPoints;

                htmlTable.addCell((result > 0? Double.toString(result) :""));
            }
        }

//        totalPoints = totalPoints*10+80;
//
//        htmlTable.setCell(1, df.format(totalPoints));
        htmlTable.newRow();
    }

    out.print(htmlTable);

%>
</div>

<script>
    $(document).ready(function () {
        $('#exTable').DataTable({ "pageLength": 25 }).column(0).order("desc").draw();
        $('.dataTables_length').addClass('bs-select');
    });
</script>

</body>
</html>
