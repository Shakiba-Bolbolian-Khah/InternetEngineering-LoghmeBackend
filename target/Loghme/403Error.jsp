<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://fonts.googleapis.com/css?family=Raleway:500,800" rel="stylesheet">
    <title>Document</title>
</head>
<body>
<h1 align="center">403</h1>
<h1 align="center">Forbidden</h1>
<%String msg = (String) request.getAttribute("errorMsg"); %>
<h2 align="center"><%=msg%></h2>
</body>
</html>