<%@ page import="tools.UserAuth" %>
<%@ page import="tools.htmltools.HtmlConstants" %>
<%@ page import="enums.UserLevel" %>
<%@ page import="models.UserModel" %>
<%@ page import="models.ResultModel" %>
<%@ page import="java.util.List" %>
<%@ page import="tools.repository.Results" %>
<%@ page import="enums.Result" %>
<%
    UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.ADMIN);
    if(currentUser == null){ return; }
%>
<%--
  Created by IntelliJ IDEA.
  User: johan
  Date: 17.11.2020
  Time: 12:35
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
        List<ResultModel> results = Results.getBestResultsInTestBattery();
        for (ResultModel rm : results) {
            int athleteId = (int) rm.get(Result.ATHLETEID);
            int exerciseId = (int) rm.get(Result.EXERCISEID);
            double result = (double) rm.get(Result.RESULT);
            String testDate = rm.get(Result.DATETIME).toString().substring(0, 19);

%>
            <%=athleteId + " " + exerciseId + " " + result + " " + testDate + "\n"%>
            <br>
<%
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
</body>
</html>
