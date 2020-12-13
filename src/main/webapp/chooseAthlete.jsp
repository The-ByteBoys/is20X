<%@ page
    import="java.sql.SQLException"
    import="java.util.List"
    import="java.util.ArrayList"

    import="enums.Athlete"
    import="enums.Exercise"
    import="enums.User"
    import="enums.UserLevel"

    import="models.AthleteModel"
    import="models.ExerciseModel"
    import="models.UserModel"

    import="tools.htmltools.HtmlConstants"
    import="tools.repository.Athletes"
    import="tools.repository.Exercises"
    import="tools.UserAuth"

    contentType="text/html;charset=UTF-8"
%><%
    UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);
    if(currentUser == null){ return; }
%><%--
  Created by IntelliJ IDEA.
  User: johan
  Date: 26.10.2020
  Time: 12:54
--%><!DOCTYPE html>
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
        //Printing a table or "card"(bootstrap) for each of the classes.
        //If the user logged in has a club then list all the athletes in the club in the class they belong in.
        List<AthleteModel> athletes = null;
        int clubId = 0;
        if (currentUser.get(User.CLUBID) != null) {
            try {
                clubId = (int) currentUser.get(User.CLUBID);
                athletes = Athletes.getAthletes(clubId);
            } catch (SQLException e) {
                out.print(e);
                e.printStackTrace();
            }
        }

        if (athletes != null && clubId != 0) {
            String[] classes = {"SENIOR", "A", "B", "C"};
            for (String cl : classes) {
                    %>
            <div class="card">
                <div class="card-header"><%=cl%></div>
                <div class="card-body">
                    <%
                        for (AthleteModel athlete : athletes) {
                            if ((athlete.get(Athlete.CLASS) != null) && athlete.get(Athlete.CLASS).toString().equals(cl)) {
                                String firstName = athlete.get(Athlete.FNAME).toString();
                                String lastName = athlete.get(Athlete.LNAME).toString();
                                int athlete_id = Integer.parseInt(athlete.get(Athlete.ID).toString());
                    %>
                        <label>
                            <input type="checkbox" name="athletes" value="<%=cl + "-" + firstName + " " + lastName + "-" + athlete_id%>">
                            <%=firstName + " " + lastName%>
                        </label><br>
                    <%
                            }
                        }
                    %>
                </div>
                <div class="card-footer">
                    <label>
                        <select name="<%=cl%>-exercises"><!--Used later in submitTest.jsp for validation-->
                            <option disabled selected>Velg test</option>
                            <%
                                try {
                                    List<ExerciseModel> exercises = Exercises.getExercisesForClass(cl, clubId);
                                    for (ExerciseModel ex : exercises) {
                                        String exercise_name = ex.get(Exercise.NAME).toString();
                                        String exercise_unit = ex.get(Exercise.UNIT).toString();
                                        int exercise_id = (int) ex.get(Exercise.ID);
                            %><option value="<%=exercise_id + "-" + exercise_name + "-" + exercise_unit%>"><%=exercise_name + " " + exercise_unit%></option><%
                                    }
                            %>
                        </select>
                    </label>
                <%
                                } catch (Exception e) {
                                    out.print(e);
                                    e.printStackTrace();
                                }
                    %>
                </div>
            </div>
            <br>
        <%
            }
        }
    %>
        </div>
        <br>
        <input type="submit" value="Velg utøvere">
    </form>
</div>

</body>
</html>
