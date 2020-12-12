<%@ page
        import="java.util.Arrays"
        import="java.util.List"
        import="java.util.Iterator"
        import="tools.htmltools.HtmlConstants"
        import="models.UserModel"
        import="enums.UserLevel"
        import="tools.UserAuth"

        contentType="text/html;charset=UTF-8"
%><%
    UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);
    if(currentUser == null){ return; }

    List<String> athletes = Arrays.asList(request.getParameterValues("athletes"));

    if(athletes.isEmpty()) {
        response.sendRedirect("chooseAthlete.jsp");
    }

%><!DOCTYPE html>
<html>
<head>
    <title>Registrer test</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }

        table {
            width:350px;
        }
    </style>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body>

<div id="nav-placeholder"></div>
<%=UserAuth.navBarLogin(currentUser)%>
<script src="js/menu.js"></script>

<div class="container" style="text-align: center;">

    <!--Makes a table for a class if theres a athlete with that class chosen-->
    <form action="submit-tests" method="post">
        <%
            String[] classes = {"SENIOR", "A", "B", "C"};
            for (String cl : classes) {

                try {
                boolean classHasMembers = true;
                Iterator<String> it = athletes.iterator();
                while (it.hasNext() && classHasMembers) {
                    String athlete_it = it.next();
                    String[] athlete_info = athlete_it.split("-");
                    String athlete_cl = athlete_info[0];


                    if (athlete_cl.equals(cl)) {
                        String exerciseClass = athlete_cl + "-exercises";
                        String exercises = request.getParameter(exerciseClass);
                        String[] exerciseValues = exercises.split("-");
                        String exerciseName = exerciseValues[1];
                        String exerciseUnit = exerciseValues[2];
                        String exercise_id = exerciseValues[0];
        %>
                        <table style="margin: auto;">
                            <tr>
                                <th colspan="3"><%=athlete_cl + " (" + exerciseName + " " + exerciseUnit + ")"%></th>
                            </tr>
                            <tr>
                                <th>Utøver</th>
                                <th>Resultat</th>
                            </tr>

                            <%
                                for (String at : athletes) {
                                    athlete_info = at.split("-");
                                    athlete_cl = athlete_info[0];
                                    String athlete_name = athlete_info[1];
                                    String athlete_id = athlete_info[2];
                                    if (athlete_cl.equals(cl)){

                            %>
                                    <!--Input fields have certain rules to minimize wrong values.
                                    If the exercise unit is time, then the inputs will have other rules-->
                                    <tr>
                                        <td style="width:200px; text-align:center"><%=athlete_name%></td>
                                        <td>
                                            <label>
                                                <%if (exerciseUnit.contains("TIME")) {%>
                                                    <input type="number" name="<%=athlete_id%>resultMin" min="0" max="60" required="required" placeholder="Minutes">
                                                    <input type="number" name="<%=athlete_id%>resultSec" min="0" max="59" step=".001" required="required" placeholder="Seconds">
                                                <%} else {   %>
                                                    <input type="number" name="<%=athlete_id%>result" min="0" max="99999.999" step=".001" required="required" placeholder="<%=exerciseUnit%>">
                                                <%} %>
                                                <input type="hidden" name="ids" value="<%=exercise_id + "-" + athlete_id + "-" + exerciseUnit%>">
                                            </label>
                                        </td>
                                    </tr>
                            <%

                                    }
                                }
                            %>

                        </table>
                        <br>
        <%
                        classHasMembers = false;
                    }
                }
        %>
        <%
                } catch (Exception e) {
                    out.println(":( you can't access here now");

                    //TODO: Make this error feedback better
                }
            }
        %>
        <input type="submit" value="Registrer tester">
    </form>

</div>

</body>
</html>
