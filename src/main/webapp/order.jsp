<%@ page import="java.util.*" %>
<%@ page import = "Model.*" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User</title>
    <style>
        li, div, form {
        	padding: 5px
        }
    </style>
</head>
<body>
    <% Order order = (Order) request.getAttribute("order");
    %>
    <% if(order.getDeliveryId()!=null){%>
    <div>Order ID: <%=order.getDeliveryId()%></div>
    <%}%>
    <div><%=order.getRestaurantName()%></div>
    <ul>
    <%for( ShoppingCartItem item : order.getItems()){%>
        <li><%=item.getFood().getName()%>:‌ <%=item.getNumber()%></li>
    <%}%>
    </ul>
    <% String state = order.getStateString(); %>
    <div>
        status : <%=state%>
        <% if(state.equals("Delivering")){%>
        <div>remained time : 10 min 12 sec</div>
        <%}%>
    </div>
    <% String msg = (String) request.getAttribute("msg");
        if(msg != null){ %>
        <h4><%=msg%></h4>
    <%}%>
</body>
</html>