<%@ page import="java.util.*" %>
<%@ page import = "Model.*" %>
<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Restaurants</title>
    <style>
        table {
            text-align: center;
            margin: auto;
        }
        th, td {
            padding: 5px;
            text-align: center;
        }
        .logo{
            width: 100px;
            height: 100px;
        }
    </style>
</head>
<body>
    <table>
        <tr>
            <th>id</th>
            <th>logo</th>
            <th>name</th>
            <th>location</th>
        </tr>
        <% ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) request.getAttribute("restaurants"); %>
        <%for (Restaurant restaurant: restaurants){ %>
                 <tr>
                     <td><%=restaurant.getId() %></td>
                     <td><img class="logo" src=<%=restaurant.getLogoUrl()%> alt="logo"></td>
                     <td><%=restaurant.getName()%></td>
                     <td>(<%=restaurant.getLocation().getX()%>, <%=restaurant.getLocation().getY()%>)</td>
                 </tr>
        <%}%>
    </table>
</body>
</html>