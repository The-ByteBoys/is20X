<%@ page import ="tools.DbTool" %>
<!DOCTYPE html>
<html>
<head>
    <link rel = "stylesheet" type = "text/css" href="css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <title>Start Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<div id="nav-placeholder">

</div>

<script>
    $(function(){
        $("#nav-placeholder").load("nav.html");
    });
</script>
<h1>Roing webapp</h1>
<h2>Register</h2>
<p>
    <form method="POST" action="userregistration">
<%--        <input name="userFname" type="text" placeholder="Firstname">--%>
<%--        <input name="userLname" type="text" placeholder="Lastname">--%>
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
