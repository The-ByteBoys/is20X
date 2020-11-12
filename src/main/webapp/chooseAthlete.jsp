<%@ page import="java.sql.ResultSet" %>
<%@ page import="tools.DbTool" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="tools.htmltools.HtmlConstants" %>
<%@ page import="enums.UserLevel" %>
<%@ page import="tools.UserAuth" %>
<%@ page import="models.UserModel" %>
<%@ page import="enums.User" %>
<%@ page import="models.AthleteModel" %>
<%@ page import="java.util.List" %>
<%@ page import="tools.repository.Athletes" %>
<%@ page import="enums.Athlete" %>
<%@ page import="models.ExerciseModel" %>
<%@ page import="tools.repository.Exercises" %>
<%@ page import="enums.Exercise" %>
<%
    UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);
    if(currentUser == null){ return; }
%><%--
  Created by IntelliJ IDEA.
  User: johan
  Date: 26.10.2020
  Time: 12:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Velg utøvere</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            width:300px;

        }
    </style>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body>
    <div id="nav-placeholder"></div>
    <%=UserAuth.navBarLogin(currentUser)%>
    <script src="js/menu.js"></script>

    <h1>Velg utøvere</h1>
    <br>

    <form action="choose-athlete" method="post">
    <%
        //Make a table for each class that contains all the athletes that belongs to that class.
        String[] classes = {"SENIOR", "A", "B", "C"};
        for (String cl : classes) {
    %>
        <table>
            <tr><th><%=cl%></th></tr>

            <%
                //Checks if the current user belongs to a club, which a coach obviously needs to.
                //Then gets all the athletes in a List.
                try {
                    if (currentUser.get(User.CLUBID) != null) {
                    int clubId = (int) currentUser.get(User.CLUBID);
                    List<AthleteModel> athletes = Athletes.getAthletes(clubId);
                    for (AthleteModel a : athletes) {
                        if (a.get(Athlete.CLASS).toString().equals(cl)) { //The athletes class equals the current String being iterated from the classes Array.
                            String firstName = a.get(Athlete.FNAME).toString();
                            String lastName = a.get(Athlete.LNAME).toString();
                            int athlete_id = Integer.parseInt(a.get(Athlete.ID).toString());


            %>
                            <tr>
                                <td>
                                    <label>
                                        <!--The athlete_id and cl is included in the value because the servlet needs this information-->
                                        <input type="checkbox" name="athletes" value="<%=cl+"-"+firstName + " " + lastName +"-"+ athlete_id%>">
                                        <%=firstName + " " +  lastName%>
                                    </label>
                                </td>
                            </tr>

            <%
                        }
                    }
                    %>
            <tr>
                <td>
                    <label>
                        <select name="<%=cl%>-exercises">
                            <option disabled selected>Velg test</option>
                            <%
                                //Gets all the exercises for the class and holds them in a List. Then iterate the list to have them as options in a select tag.
                                List<ExerciseModel> exercises = Exercises.getExercisesForClass(cl, clubId);
                                for (ExerciseModel ex : exercises) {
                                    String exercise_name = ex.get(Exercise.NAME).toString();
                                    String exercise_unit = ex.get(Exercise.UNIT).toString();
                                    int exercise_id = (int) ex.get(Exercise.ID);
                            %>
                            <!--The exercise_id and exercise_unit is included in the value becuase the servlet needs this information-->
                            <option value="<%=exercise_id + "-" + exercise_name + "-" + exercise_unit%>"><%=exercise_name + " " + exercise_unit%></option>
                            <%
                                }
                            %>
                        </select>
                    </label>
                </td>
            </tr>
            <%
                    }
                } catch (SQLException throwables) {
                throwables.printStackTrace();
                }
            %>


        </table>
        <br>
    <%
        }
    %>
        <input type="submit" value=">">
    </form>
</body>
</html>
