<%@ page import ="tools.UserAuth" import="models.UserModel" import="enums.User" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% UserModel currentUser = UserAuth.requireLogin(request, response);
if(currentUser == null){ return; } %>
<!DOCTYPE html>
<html lang="no">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Min side - Roing Webapp</title>

    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://unpkg.com/bootstrap-darkmode@0.7.0/dist/darktheme.css"/>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="https://unpkg.com/bootstrap-darkmode@0.7.0/dist/theme.js"></script>
</head>
<body>

<div id="nav-placeholder"></div>
<script src="js/menu.js"></script>

<h1>Roing webapp</h1>
<h2>Min Side</h2>
<p>Welcome back, <%=currentUser.get(User.EMAIL)%></p>
</body>
</html>
