<!DOCTYPE html>
<html>

<head>
    <title>Roing Webapp</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel = "stylesheet" type = "text/css" href="css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</head>
<body style="text-align: center;">

<div id="nav-placeholder">

</div>

<script>
    $(function(){
        $("#nav-placeholder").load("nav.html");
    });
</script>


<h1>Roing webapp</h1>
<table class="table table-bordered">
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
        <th scope="row">1</th>
        <td>89,1</td>
        <td>2004</td>
        <td>Tyra</td>
        <td>Hjemdal</td>
        <td>Ormsund</td>
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
        <th scope="row">2</th>
        <td>88,2</td>
        <td>2004</td>
        <td>Mia</td>
        <td>Engvik</td>
        <td>Aalesunds</td>
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
</body>
</html>
