<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="no">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>Club Registration - Roforbundet</title>

    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://unpkg.com/bootstrap-darkmode@0.7.0/dist/darktheme.css"/>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="https://unpkg.com/bootstrap-darkmode@0.7.0/dist/theme.js"></script>

</head>
<body>

    <div id="nav-placeholder"></div>
    <script src="js/menu.js"></script>

        <div class="container" style="text-align: center;">
            <h1>Club registration</h1><br><br><br>

            <form action="clubregistration" method="POST">
                <div class="form-group">
                    <label>Club name:
                        <input type="clubName" class="form-control" name="clubname" size="30" />
                    </label>
                </div>
                <div class="form-group">
                    <input type="submit" value="Register" class="btn btn-secondary" />
                </div>
            </form>
        </div>
</body>
</html>
