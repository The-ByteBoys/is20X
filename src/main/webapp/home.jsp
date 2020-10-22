<%@ page import ="tools.UserAuth" %>
<% UserAuth.verifyLogin(request, response); %>
<!DOCTYPE html>
<html>
<head>
    <link rel = "stylesheet" type = "text/css" href="css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <title>Home</title>
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
<h2>Min Side</h2>
</body>
</html>
