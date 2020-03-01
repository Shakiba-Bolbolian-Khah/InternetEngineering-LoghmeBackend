<%@ page import="java.util.*" %>
<%@ page import = "Model.*" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Food Party</title>
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
        .old-price {
            text-decoration: line-through;
        }
    </style>
</head>
<body>
    <ul>
    <% ArrayList<PartyFood> partyFoods = (ArrayList<PartyFood>) request.getAttribute("partyFoods"); %>
    <%for (PartyFood partyFood: partyFoods){ %>
        <li>menu:
        	<ul>
        		<li>
                    <img src="<%=partyFood.getImageUrl()%>" alt="logo">
                    <div><%=partyFood.getRestaurantName()%></div>
                    <div><%=partyFood.getName()%></div>
                    <div><%=partyFood.getDescription()%></div>
                    <div class="old-price"><%=partyFood.getOldPrice()%> Toman</div>
                    <div><%=partyFood.getPrice()%> Toman</div>
                    <div>remaining count: <%=partyFood.getCount()%></div>
                    <div>popularity: <%=partyFood.getPopularity()%></div>
                    <form action="" method="POST">
                        <input type="hidden" id="foodInfo" name="foodInfo" value="<%=partyFood.getName()%>*<%=partyFood.getRestaurantId()%>">
                        <input type="submit" value="addToCart">
                    </form>
                </li>
        	</ul>
        </li>
    <%}%>
    <% String msg = (String) request.getAttribute("msg");
        if(msg != null){ %>
            <h3><%=msg%></h3>
        <%}%>
    </ul>
</body>
</html>