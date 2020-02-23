<%@ page import="java.util.*" %>
<%@ page import = "Model.*" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
    <style>
        li, div, form {
        	padding: 5px
        }
    </style>
</head>
<body>
    <% String restaurantName = (String) request.getAttribute("restaurantName");
       Map<String,Integer> cart = (Map<String,Integer>) request.getAttribute("cart");
    %>
    <div><%=restaurantName%></div>
    <ul>
    <%for (Map.Entry<String,Integer> entry : cart.entrySet()){ %>
        <li><%=entry.getKey()%>:‌ <%=entry.getValue()%></li>
    <%}%>
    </ul>
    <form action="" method="POST">
        <button type="submit">finalize</button>
    </form>
</body>
</html>