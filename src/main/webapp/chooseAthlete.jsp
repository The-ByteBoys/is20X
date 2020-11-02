<%@ page import="java.sql.ResultSet" %>
<%@ page import="tools.DbTool" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="tools.htmltools.HtmlConstants" %>
<%@ page import="enums.UserLevel" %>
<%@ page import="tools.UserAuth" %>
<%@ page import="models.UserModel" %>
<%
    UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.ADMIN);
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
        String[] classes = {"SENIOR", "A", "B", "C"};
        for (String cl : classes) {
    %>
        <table>
            <tr><th><%=cl%></th></tr>

            <%
                String query = "SELECT a.firstName, a.lastName, a.athlete_id, (2020 - a.birth) age,\n" +  
                        "        (SELECT c.name FROM class c WHERE ageFrom <= age ORDER BY ageFrom DESC LIMIT 1) class\n" +
                        "FROM athlete a;";


                try {
                    ResultSet rs = DbTool.getINSTANCE().selectQuery(query);
                    while (rs.next()) {
                        if (rs.getString("class").equals(cl)) {
                            String firstName = rs.getString("a.firstName");
                            String lastName = rs.getString("a.lastName");
                            int athlete_id = rs.getInt("a.athlete_id");

            %>
                            <tr>
                                <td>
                                    <label>
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
                                query = "SELECT c.name, e.name, e.unit, e.exercise_id FROM EXERCISE e\n" +
                                        "INNER JOIN test_set ts ON e.exercise_id = ts.exercise\n" +
                                        "INNER JOIN class c ON c.class_id = ts.class\n" +
                                        "WHERE c.name = '" + cl + "'";
                                rs = DbTool.getINSTANCE().selectQuery(query);
                                while (rs.next()) {
                                    String exercise_name = rs.getString("e.name");
                                    String exercise_unit = rs.getString("e.unit");
                                    int exercise_id = rs.getInt("e.exercise_id");
                            %>
                            <option value="<%=exercise_id + "-" + exercise_name + "-" + exercise_unit%>"><%=exercise_name + " " + exercise_unit%></option>
                            <%
                                }
                            %>
                        </select>
                    </label>
                </td>
            </tr>
            <%
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
