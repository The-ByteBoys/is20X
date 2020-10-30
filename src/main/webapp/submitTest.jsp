<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8"%>
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
</head>
<body>

<form action="submit-tests" method="post">
    <%
        String[] classes = {"SENIOR", "A", "B", "C"};
        for (String cl : classes) {


            boolean classNotFound = true;
            List<String> athletes = (List<String>) request.getAttribute("athletes");
            Iterator<String> it = athletes.iterator();
            while (it.hasNext() && classNotFound) {
                String athlete = it.next();
                String[] input = athlete.split("-");
                String acl = input[0];


                if (acl.equals(cl)) {
                    String exerciseClass = acl + "-exercises";
                    String exercises = request.getParameter(exerciseClass);
                    String[] exValue = exercises.split("-");
                    String ex = exValue[1];
                    String ex_id = exValue[0];
    %>
                    <table>
                        <tr>
                            <th colspan="3"><%=acl%></th>
                        </tr>
                        <tr>
                            <th>Utøver</th>
                            <th>Resultat</th>
                        </tr>

                        <%
                            for (String at : athletes) {
                                input = at.split("-");
                                acl = input[0];
                                String at_name = input[1];
                                String at_id = input[2];
                                if (acl.equals(cl)){

                        %>

                                <tr>
                                    <td style="width:200px; text-align:center"><%=at_name%></td>
                                    <td>
                                        <label>
                                            <input type="number" name="<%=at_id%>result" min="0" max="99999.999" placeholder="<%=ex%>">
                                            <% %>
                                            <input type="hidden" name="ids" value="<%=ex_id + "-" + at_id%>">
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
                    classNotFound = false;
                }
            }
    %>
    <%
        }
    %>
    <input type="submit" value="Registrer tester">
</form>
</body>
</html>
