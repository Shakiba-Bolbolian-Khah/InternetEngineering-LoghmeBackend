<%@ page import="java.util.*" %>
<%@ page import = "Model.*" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Restaurant</title>
    <style>
        img {
        	width: 50px;
        	height: 50px;
        }
        li {
            display: flex;
            flex-direction: row;
        	padding: 0 0 5px;
        }
        div, form {
            padding: 0 5px
        }
    </style>
</head>
<body>
    <ul>
    <% Restaurant restaurant = (Restaurant) request.getAttribute("restaurant"); %>
        <li>id: <%=restaurant.getId()%></li>
        <li>name: <%=restaurant.getName()%></li>
        <li>location: (<%=restaurant.getLocation().getX()%>, <%=restaurant.getLocation().getY()%>)</li>
        <li>logo: <img src="<%=restaurant.getLogoUrl()%>" alt="logo"></li>

        <!-- IN CASE YOU WANT SOME BONUS : -->
        <!-- <li>estimated delivery time: 10 min 2 sec </li> -->

        <li>menu:
        	<ul>
                <%for (Food food: restaurant.getMenu()){ %>
                     <li>
                         <img src="<%=food.getImageUrl()%>" alt="logo">
                         <div><%=food.getName()%></div>
                         <div><%=food.getPrice()%> Toman</div>
                         <form action="" method="POST">
                            <input type="hidden" id="foodName" name="foodName" value="<%=food.getName()%>">
                            <input type="submit" value="addToCart">
                        </form>
                     </li>
                <%}%>
        	</ul>
        </li>
        <% String msg = (String) request.getAttribute("msg");
        if(msg != null){ %>
        <h3><%=msg%></h3>
        <%}%>
    </ul>
</body>
</html>