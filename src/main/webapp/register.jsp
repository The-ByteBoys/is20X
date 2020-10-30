<%@ page import ="tools.UserAuth" import="models.UserModel" import="tools.htmltools.HtmlConstants" import="enums.User" import="enums.UserLevel" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
    UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);
    if(currentUser == null){ return; }
%>
<!DOCTYPE html>
<html lang="no">
<head>
    <title>Register - Roing Webapp</title>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body>

<div id="nav-placeholder"></div>
<%=UserAuth.navBarLogin(currentUser)%>
<script src="js/menu.js"></script>

<h1>Roing webapp</h1>
<h2>Register</h2>
<%=UserAuth.getSessionNotes(session)%>
<p>
    <form method="POST" action="userregistration">
        <input name="userEmail" type="email" placeholder="Email">
        <input name="userPass" type="password" placeholder="Password">
        <select name="userType">
            <option value="ATHLETE">Athlete</option>
            <% if(currentUser.get(User.TYPE).equals(UserLevel.ADMIN.toString())){
            %><option value="COACH">Coach</option>
            <option value="ADMIN">Admin</option><% } %>
        </select>
        <input name="submit" type="submit" value="Register">
    </form>
</p>

</body>
</html>
