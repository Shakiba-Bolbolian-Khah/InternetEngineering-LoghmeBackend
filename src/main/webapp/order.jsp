<%@ page import="Model.Order" %>
<%@ page import="Model.ShoppingCartItem" %>
<%@ page import="Repository.OrderState" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order</title>
    <style>
        li, div, form {
        	padding: 5px
        }
    </style>
</head>
<body>
    <% Order order = (Order) request.getAttribute("order"); %>
    <div>Order ID: <%=order.getId()%></div>
    <div><%=order.getRestaurantName()%></div>
    <ul>
    <%for( ShoppingCartItem item : order.getItems()){%>
        <li><%=item.getFood().getName()%>:‌ <%=item.getNumber()%></li>
    <%}%>
    </ul>
    <% OrderState state = order.getState(); %>
    <div>
        status : <%=state.getStateAsString(state)%>
        <% if(state.equals(OrderState.Delivering)) {
            int hours = order.getRemainingHoursAsInteger();
            int minutes = order.getRemainingMinutesAsInteger();
            int seconds = order.getRemainingSecondsAsInteger();
        %>
        <div>remaining time: <%=hours%> hour(s) <%=minutes%> min(s) <%=seconds%> sec(s)</div>
        <%}%>
    </div>
    <% String msg = (String) request.getAttribute("msg");
        if(msg != null){ %>
        <h4><%=msg%></h4>
    <%}%>
</body>
</html>