<%@ page import="java.util.*" %>
<%@ page import = "Model.*" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>User</title>
    <style>
        li {
            padding: 5px
        }
    </style>
</head>

<body>
    <ul>
    <% User user = (User) request.getAttribute("user"); %>
        <li>id: <%=user.getId()%></li>
        <li>full name: <%=user.getFirstName()%> <%=user.getLastName()%></li>
        <li>phone number: <%=user.getPhoneNumber()%></li>
        <li>email: <%=user.getEmail()%></li>
        <li>credit: <%=user.getCredit()%> Toman</li>
        <form action="" method="POST">
            <button type="submit">increase</button>
            <input type="text" name="credit" value="" />
        </form>
        <li>
            Orders :
            <ul>
                <%for( Order order : user.getOrders()){%>
                        <li><a href="./order/<%=order.getId()%>">order id : <%=order.getId()%></a></li>
                <%}%>
            </ul>
        </li>
        <% String msg = (String) request.getAttribute("msg");
                if(msg != null){ %>
                <h4><%=msg%></h4>
        <%}%>
    </ul>
</body>

</html>