<!DOCTYPE html>
<html>
<head>
    <link rel = "stylesheet" type = "text/css" href="css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="https://unpkg.com/bootstrap-darkmode@0.7.0/dist/theme.js"></script>
    <meta charset="utf-8">
    <title>Roforbundet</title>
</head>
<body>
<div id="nav-placeholder">

</div>

<script>
    $(function(){
        $("#nav-placeholder").load("nav.html");
    });
    const themeConfig = new ThemeConfig();
    themeConfig.initTheme();
</script>
<div style="position: absolute;top: 5px;right: 5px;margin: 10px;height: 30px;">
    <script> const darkSwitch = writeDarkSwitch(themeConfig);</script>
</div>

<div style="text-align: center">
    <h1>User Login</h1>
    <form action="login" method="post">
        <label>Username:
            <input name="email" size="30" />
        </label>
        <br><br>
        <label>Password:
            <input type="password" name="password" size="30" />
        </label>
        <br><br>
        <button type="submit">Login</button>
    </form>
</div>
</body>
</html>