<%@ page import="java.sql.ResultSet" %>
<%@ page import="tools.DbTool" %>
<%@ page import="java.sql.SQLException" %><%--
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

        .floatedTable {
            margin-right: 10px;
            float: left;
        }
    </style>
</head>
<body>
    <h1>Velg utøvere</h1>
    <br>

    <form action="choose-athlete" method="post">
    <%
        String[] classes = {"SENIOR", "A", "B", "C"};

        for (String cl : classes) {
    %>
        <table class="floatedTable">
            <tr><th><%=cl%></th></tr>

            <%
                String query = "SELECT a.firstName, a.lastName, (2020 - a.birth) age,\n" +
                        "        (SELECT c.name FROM class c WHERE ageFrom <= age ORDER BY ageFrom DESC LIMIT 1) class\n" +
                        "FROM athlete a;";


                try {
                    ResultSet rs = DbTool.getINSTANCE().selectQuery(query);
                    while (rs.next()) {
                        if (rs.getString("class").equals(cl)) {
                            String f = rs.getString("a.firstName");
                            String l = rs.getString("a.lastName");

            %>
                            <tr>
                                <td>
                                    <label>
                                        <input type="checkbox" name="athletes" value="<%=cl+"-"+f + " " + l%>">
                                        <%=f + " " +  l%>
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
                        <select name="exercises">
                            <option disabled selected>Velg test</option>
                            <%
                                query = "SELECT c.name, e.name, e.unit FROM EXERCISE e\n" +
                                        "INNER JOIN test_set ts ON e.exercise_id = ts.exercise\n" +
                                        "INNER JOIN class c ON c.class_id = ts.class\n" +
                                        "WHERE c.name = '" + cl + "'";
                                rs = DbTool.getINSTANCE().selectQuery(query);
                                while (rs.next()) {
                                    String name = rs.getString("e.name");
                                    String unit = rs.getString("e.unit");
                            %>
                            <option name="exercises" value="<%=cl + "-" + name+unit%>"><%=name + " " + unit%></option>
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
    <%
        }
    %>
        <input type="submit" value=">">
    </form>
</body>
</html>