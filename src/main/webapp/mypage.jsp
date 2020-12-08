<%@ page import ="tools.UserAuth" import="models.UserModel" import="enums.User" import="tools.htmltools.HtmlConstants" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
    UserModel currentUser = UserAuth.requireLogin(request, response);
    if(currentUser == null){ return; }
%><!DOCTYPE html>
<html lang="no">
<head>
    <title>Min side - Roing Webapp</title>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body>

<div id="nav-placeholder"></div>
<%=UserAuth.navBarLogin(currentUser)%>
<script src="js/menu.js"></script>

<div class="container" style="text-align: center;">
    <%=UserAuth.getSessionNotes(session)%>

    <h1>Min Side</h1>
    <p>Velkommen, <%=currentUser.get(User.EMAIL)%></p>

    <% if(currentUser.get(User.TYPE).equals("ATHLETE")){ %>
    <p>Det er foreløpig ingenting du kan gjøre her...</p>
    <% } %>

    <% if(currentUser.get(User.TYPE).equals("COACH") || currentUser.get(User.TYPE).equals("ADMIN")){ %>
    <br>
    <p>Trener-funksjoner</p>
    <p><a href="register.jsp">Registrer ny utøver-bruker</a></p>
    <p><a href="massinsert">Sett inn masse data</a></p>
    <p><a href="uploadExcel.jsp">Last opp excel-fil</a></p>
    <p><a href="chooseAthlete.jsp">Registrer resultater</a></p>

    <% } %>

    <% if(currentUser.get(User.TYPE).equals("ADMIN")){ %>
    <br>
    <p>Superbruker-funksjoner</p>
    <p><a href="clubregister.jsp">Registrer ny klubb</a></p>
    <% } %>

    <br>
    <p><a href="logout">Logg ut</a></p>
</div>

</body>
</html>
