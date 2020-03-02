<%@ page import="Model.Restaurant" %>
<%@ page import="Model.Location" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.util.ArrayList" %>
<%@ page pageEncoding="UTF-8" %>
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
            <th>estimated delivery time</th>
        </tr>
        <% ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) request.getAttribute("restaurants");
            Location userLocation = (Location) request.getAttribute("userLocation"); %>
        <%for (Restaurant restaurant: restaurants){ %>
            <% LocalTime estimatedTime = restaurant.estimateDeliveryTime(userLocation); %>
             <tr>
                 <td><%=restaurant.getId()%></td>
                 <td><img class="logo" src="<%=restaurant.getLogoUrl()%>" alt="logo"></td>
                 <td><%=restaurant.getName()%></td>
                 <td>(<%=restaurant.getLocation().getX()%>, <%=restaurant.getLocation().getY()%>)</td>
                 <td><%=estimatedTime.getHour()%> hour(s) <%=estimatedTime.getMinute()%> min(s) <%=estimatedTime.getSecond()%> sec(s)</td>
             </tr>
        <%}%>
    </table>
</body>
</html>