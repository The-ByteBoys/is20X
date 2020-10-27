<%@ page import ="tools.DbTool" %>
<!DOCTYPE html>
<html lang="no">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Register - Roing Webapp</title>

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
<h2>Register</h2>
<p>
    <form method="POST" action="userregistration">
        <input name="userEmail" type="email" placeholder="Email">
        <input name="userPass" type="password" placeholder="Password">
        <select name="userType">
            <option value="ATHLETE">Athlete</option>
            <option value="COACH">Coach</option>
            <option value="ADMIN">Admin</option>
        </select>
        <input name="submit" type="submit" value="Register">
    </form>
</p>

</body>
</html>
