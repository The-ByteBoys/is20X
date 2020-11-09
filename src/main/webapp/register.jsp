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

<div class="container">


<h1>Roing webapp</h1>
<h2>Register</h2>
<%=UserAuth.getSessionNotes(session)%>
<p>
    <form method="POST" action="userregistration" class="form-group">
        Email: <input name="userEmail" type="email" placeholder="Email" class="form-control" required>
        Password: <input name="userPass" type="password" placeholder="Password" class="form-control" required>
        Type: <select name="userType" class="form-control" id="userTypeSelect">
            <option value="ATHLETE">Athlete</option>
            <% if(currentUser.get(User.TYPE).equals(UserLevel.ADMIN.toString())){
            %><option value="COACH">Coach</option>
            <option value="ADMIN">Admin</option><% } %>
        </select>
        <br>
        <div id="userClubDiv">
            Fornavn: <input type="text" name="userFname" class="form-control">
            Etternavn: <input type="text" name="userLname" class="form-control">
            Fødselsår: <input type="date" name="userBirth" class="form-control">
            Klubb: <input type="text" name="userClub" class="form-control" >
            Kjønn: <select class='sexPicker form-control' name="userSex" style='width: initial; display: initial;' required>
                <option>Velg kjønn</option>
                <option value='M'>Mann</option>
                <option value='F'>Kvinne</option>
                <option value='O'>Annet</option>
            </select>
        </div>
        <input name="submit" type="submit" value="Register" class="form-control">
    </form>
</p>
</div>

<script>
    $("#userTypeSelect").on('change', function(e){
        if( $(this).val() === "ADMIN"){
            $("#userClubDiv").hide();
        }
        else {
            $("#userClubDiv").show();
        }
    });
</script>

</body>
</html>
