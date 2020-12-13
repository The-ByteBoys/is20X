<%@ page import="enums.UserLevel" %>
<%@ page import="tools.UserAuth" %>
<%@ page import="models.UserModel" %>
<%@ page import="tools.htmltools.HtmlConstants" %>
<%@ page import="models.ResultModel" %>
<%@ page import="java.util.List" %>
<%@ page import="tools.repository.Results" %>
<%@ page import="enums.User" %>
<%@ page import="enums.Result" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);
    if(currentUser == null){ return; }
%>
<%--
  Created by IntelliJ IDEA.
  User: johannes
  Date: 11/10/20
  Time: 7:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Title</title>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body>
    <div id="nav-placeholder"></div>
    <%=UserAuth.navBarLogin(currentUser)%>
    <script src="js/menu.js"></script>

    <%

        try{
            if (currentUser.get(User.CLUBID) != null) {
                int ClubId = (int) currentUser.get(User.CLUBID);
                List<ResultModel> results = Results.getResultsFromClub(ClubId);
                DecimalFormat df2 = new DecimalFormat("#.##");
                for (ResultModel rm : results) {
                    String athleteName = rm.get(Result.ATHLETENAME).toString();
                    String testDate = rm.get(Result.DATETIME).toString().substring(0, 16);
                    String exerciseName = rm.get(Result.EXERCISENAME).toString();
                    String exerciseUnit = rm.get(Result.EXERCISEUNIT).toString();
                    String resultType = rm.get(Result.TYPE).toString();
                    double result = (double) rm.get(Result.RESULT);

                    if (resultType.equals("NP")) {
    %>
                        <label>
                            <input type="checkbox">
                        </label>
    <%
                    } else {
    %>
                        <%=resultType%>
    <%
                    }
    %>
    <%
                    if (exerciseUnit.equals("TIME")) {
                        double ms = result * 1000;
                        int minutes = (int) ((ms / 1000) / 60);
                        double seconds = ((ms / 1000) % 60);
    %>
                        <%=testDate + " " + athleteName + " " + minutes + ":" + df2.format(seconds) + " " + exerciseUnit + " " + exerciseName%>
                        <br>
    <%
                    } else {
    %>
                        <%=testDate + " " + athleteName + " " + (int)result + " " + exerciseUnit + " " + exerciseName%>
                        <br>
    <%
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    %>
</body>
</html>
