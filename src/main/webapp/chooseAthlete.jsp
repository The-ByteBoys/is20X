<%@ page
    import="java.sql.SQLException"
    import="tools.htmltools.HtmlConstants"
    import="enums.UserLevel"
    import="tools.UserAuth"
    import="models.UserModel"
    import="enums.User"
    import="models.AthleteModel"
    import="java.util.List"
    import="tools.repository.Athletes"
    import="enums.Athlete"
    import="models.ExerciseModel"
    import="tools.repository.Exercises"
    import="enums.Exercise"
    contentType="text/html;charset=UTF-8"
%><%
    UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);
    if(currentUser == null){ return; }
%><%--
  Created by IntelliJ IDEA.
  User: johan
  Date: 26.10.2020
  Time: 12:54
--%>
<!DOCTYPE html>
<html lang="no">
<head>
    <title>Velg utøvere</title>
    <style>
        .card-body {
            text-align: left;
        }

        .card {
            min-width: 220px !important;
            margin-bottom: 15px !important;
        }
    </style>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body>

<div id="nav-placeholder"></div>
<%=UserAuth.navBarLogin(currentUser)%>
<script src="js/menu.js"></script>

<div class="container" style="text-align: center;">


    <h1>Velg utøvere</h1>
    <br>

    <form action="submitTest.jsp" method="post">
        <div class="card-deck">
    <%
        String[] classes = {"SENIOR", "A", "B", "C"};
        for (String cl : classes) {
    %>

            <div class="card">
                <div class="card-header"><%=cl%></div>
                <div class="card-body">
                <%
                try {
                    if (currentUser.get(User.CLUBID) != null) {
                    int clubId = (int) currentUser.get(User.CLUBID);
                    List<AthleteModel> athletes = Athletes.getAthletes(clubId);
                    for (AthleteModel a : athletes) {
                        if (a.get(Athlete.CLASS).toString().equals(cl)) {
                            String firstName = a.get(Athlete.FNAME).toString();
                            String lastName = a.get(Athlete.LNAME).toString();
                            int athlete_id = Integer.parseInt(a.get(Athlete.ID).toString());


            %>
                    <label>
                        <input type="checkbox" name="athletes" value="<%=cl+"-"+firstName + " " + lastName +"-"+ athlete_id%>">
                        <%=firstName + " " +  lastName%>
                    </label><br>
                <%
                        }
                    }
                    %>
                </div>
                <div class="card-footer">
                    <label>
                        <select name="<%=cl%>-exercises">
                            <option disabled selected>Velg test</option>
                            <%
                                List<ExerciseModel> exercises = Exercises.getExercisesForClass(cl, clubId);
                                for (ExerciseModel ex : exercises) {
                                    String exercise_name = ex.get(Exercise.NAME).toString();
                                    String exercise_unit = ex.get(Exercise.UNIT).toString();
                                    int exercise_id = (int) ex.get(Exercise.ID);
                            %>
                                <option value="<%=exercise_id + "-" + exercise_name + "-" + exercise_unit%>"><%=exercise_name + " " + exercise_unit%></option>
                            <%
                            }
                            %>
                        </select>
                    </label>
            <%
                    }
                } catch (SQLException throwables) {
                throwables.printStackTrace();
                }
            %>
            </div>
        </div>
        <br>
    <%
        }
    %>
        </div>
        <br>
        <input type="submit" value=">">
    </form>
</div>

</body>
</html>
