<%@ page import ="tools.UserAuth" import="models.UserModel" import="tools.htmltools.HtmlConstants" import="enums.User" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
    UserModel currentUser = UserAuth.verifyLogin(request);
%><!DOCTYPE html>
<html lang="no">
<head>
    <title>Roing Webapp</title>
    <%=HtmlConstants.getHtmlHeaders()%>
</head>
<body style="text-align: center;">

<div id="nav-placeholder" style="min-height: 70px;"></div>
<%=UserAuth.navBarLogin(currentUser)%>
<script src="js/menu.js"></script>


<h1>Roing webapp</h1>
<table id="exTable" class="table table-striped table-bordered table-hover">
    <thead>
        <tr>
            <th scope="col">Rank</th>
            <th scope="col">Score</th>
            <th scope="col">Født</th>
            <th scope="col">Fornavn</th>
            <th scope="col">Etternavn</th>
            <th scope="col">Klubb</th>
            <th scope="col">3000 sekunder</th>
            <th scope="col">3000 tid</th>
            <th scope="col">2000 watt</th>
            <th scope="col">2000 tid</th>
            <th scope="col">60 sekunder watt</th>
            <th scope="col">Antall kroppshevinger</th>
            <th scope="col">Sargeant cm</th>
            <th scope="col">Antall bevegelser</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td scope="row">1</td>
            <td>89,1</td>
            <td>2004</td>
            <td><a href="./athlete/Tyra%20Hjemdal">Tyra</a></td>
            <td><a href="./athlete/Tyra%20Hjemdal">Hjemdal</a></td>
            <td><a href="./club/Ormsund">Ormsund</a></td>
            <td>879</td>
            <td>14,39</td>
            <td>216</td>
            <td>7,49,00</td>
            <td>372</td>
            <td>40</td>
            <td>48</td>
            <td>2</td>
        </tr>
        <tr>
            <td scope="row">2</td>
            <td>88,2</td>
            <td>2004</td>
            <td><a href="./athlete/Mia%20Engvik">Mia</a></td>
            <td><a href="./athlete/Mia%20Engvik">Engvik</a></td>
            <td><a href="./club/Aalesunds">Aalesunds</a></td>
            <td>881</td>
            <td>14,41</td>
            <td>198</td>
            <td>8,03,40</td>
            <td>357</td>
            <td>35</td>
            <td>52</td>
            <td>3</td>
        </tr>
    </tbody>
</table>

<script>
    $(document).ready(function () {
        $('#exTable').DataTable();
        $('.dataTables_length').addClass('bs-select');
    });
</script>

</body>
</html>
