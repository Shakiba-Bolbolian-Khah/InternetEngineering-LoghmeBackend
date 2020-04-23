package Loghme.DataSource;

import Loghme.Domain.Logic.FoodParty;
import Loghme.Domain.Logic.Location;
import Loghme.Exceptions.Error403;
import Loghme.Exceptions.Error404;

import java.sql.*;

import static java.sql.Connection.TRANSACTION_SERIALIZABLE;

public class UserRepository {
    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    public UserDAO getUser(int userId) throws Error404, SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        Statement userStatement = connection.createStatement();
        Statement orderStatement = connection.createStatement();
        Statement itemStatement = connection.createStatement();
        ResultSet userResult = userStatement.executeQuery("select * from users where id =" + userId);
        if (!userResult.next())
            throw new Error404("Error: There is no user with id: " + userId);

        UserDAO userDAO = new UserDAO();
        userDAO.setId(userResult.getInt("id"));
        userDAO.setFirstName(userResult.getString("firstName"));
        userDAO.setLastName(userResult.getString("lastName"));
        userDAO.setLastName(userResult.getString("phoneNumber"));
        userDAO.setLastName(userResult.getString("email"));
        userDAO.setLocation(new Location(userResult.getInt("x"),userResult.getInt("y")));
        userDAO.setCredit(userResult.getInt("credit"));

        userResult.close();

        ResultSet orderResult = orderStatement.executeQuery("select * from orders where userId =" + userId);
        while(orderResult.next()) {
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.setId(orderResult.getInt("id"));
            orderDAO.setRestaurantId(orderResult.getString("restaurantId"));
            orderDAO.setRestaurantName(orderResult.getString("restaurantName"));
            orderDAO.setTotalPayment(orderResult.getInt("totalpayment"));
            orderDAO.setDeliveryId(orderResult.getString("deliveryId"));
            orderDAO.setState(orderResult.getString("state"));
            orderDAO.setFinalizationTime(orderResult.getTimestamp("finalizationTime").toLocalDateTime());
            orderDAO.setDeliveringTime(orderResult.getTime("deliveringTime").toLocalTime());

            ResultSet itemResult = itemStatement.executeQuery("select * from orderItems where orderId = " + orderDAO.getId() +
                    " and userId = " + userId);
            while(itemResult.next()) {
                OrderItemDAO orderItemDAO = new OrderItemDAO();
                orderItemDAO.setFoodName(itemResult.getString("foodName"));
                orderItemDAO.setPrice(itemResult.getInt("price"));
                orderItemDAO.setNumber(itemResult.getInt("number"));
                orderDAO.addOrderItem(orderItemDAO);
            }
            itemResult.close();

            userDAO.addOrder(orderDAO);
        }

        userDAO.setShoppingCart(getCart(userId));

        orderResult.close();
        userStatement.close();
        orderStatement.close();
        itemStatement.close();
        connection.close();
        return userDAO;
    }

    public CartDAO getCart(int userId) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        Statement cartStatement = connection.createStatement();
        Statement cartItemStatement = connection.createStatement();
        ResultSet cartResult = cartStatement.executeQuery("select * from shoppingCarts where userId =" + userId);
        cartResult.next();
        CartDAO cartDAO = new CartDAO();
        cartDAO.setEmpty(cartResult.getBoolean("isEmpty"));
        cartDAO.setRestaurantId(cartResult.getString("restaurantId"));
        cartDAO.setRestaurantName(cartResult.getString("restaurantName"));
        cartDAO.setTotalPayment(cartResult.getInt("totalpayment"));
        cartDAO.setFoodParty(cartResult.getInt("isFoodParty"));
        cartDAO.setFirstPartyFoodEnteredTime(cartResult.getTimestamp("firstPartyFoodEnteredTime").toLocalDateTime());

        ResultSet cartItemResult = cartItemStatement.executeQuery("select * from cartItems where userId = " + userId);
        while(cartItemResult.next()) {
            CartItemDAO cartItemDAO = new CartItemDAO();
            cartItemDAO.setFoodName(cartItemResult.getString("foodName"));
            cartItemDAO.setPrice(cartItemResult.getInt("price"));
            cartItemDAO.setNumber(cartItemResult.getInt("number"));
            cartItemDAO.setPartyFood(cartItemResult.getBoolean("isPartyFood"));
            cartDAO.addItem(cartItemDAO);
        }
        cartItemResult.close();
        cartResult.close();
        cartStatement.close();
        cartItemStatement.close();
        connection.close();
        return cartDAO;
    }

    public void increaseCredit(int userId, int addedCredit) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement pStatement = connection.prepareStatement("update users set credit = credit + ? where id = ?");
        pStatement.setInt(1, addedCredit);
        pStatement.setInt(2, userId);
        pStatement.executeUpdate();
        pStatement.close();
        connection.close();
    }

    public void insertInCart(String foodName, String restaurantId, int userId) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement foodStatement = connection.prepareStatement("select * from foods where restaurantId = ? and name = ?");
        Statement resStatement = connection.createStatement();
        PreparedStatement cartUpdate = connection.prepareStatement(
                "update shoppingCarts set isEmpty = ? , restaurantId = ? , restaurantName = ? , totalPayment = totalPayment + ? where userId = ? ");
        PreparedStatement itemStatement = connection.prepareStatement(
                "insert into cartItems (userId, foodName, price, number, isPartyFood) values (?, ?, ?, ?, ?, )");
        try {
            foodStatement.setString(1, restaurantId);
            foodStatement.setString(2, foodName);
            ResultSet foodResult = foodStatement.executeQuery();
            foodResult.next();

            ResultSet restaurantName = resStatement.executeQuery("select name from restaurants where id = \"" + restaurantId + "\"");
            restaurantName.next();

            cartUpdate.setBoolean(1, false);
            cartUpdate.setString(2, restaurantId);
            cartUpdate.setString(3, restaurantName.getString("name"));
            cartUpdate.setInt(4, foodResult.getInt("price"));
            cartUpdate.setInt(5, userId);
            cartUpdate.executeUpdate();

            itemStatement.setInt(1, userId);
            itemStatement.setString(2, foodName);
            itemStatement.setInt(3, foodResult.getInt("price"));
            itemStatement.setInt(4, 1);
            itemStatement.setBoolean(5, false);
            itemStatement.executeUpdate();

            foodResult.close();
            restaurantName.close();
            connection.commit();
        } catch (SQLException e){
            if(connection != null){
                connection.rollback();
            }
        }
        foodStatement.close();
        resStatement.close();
        cartUpdate.close();
        itemStatement.close();
        connection.close();
    }

    public void updateInCart(String foodName, int userId) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement foodStatement = connection.prepareStatement(
                "select price from cartItems where userId = ? and foodName = ?");
        PreparedStatement cartUpdate = connection.prepareStatement(
                "update shoppingCarts set totalPayment = totalPayment + ? where userId = ? ");
        PreparedStatement itemStatement = connection.prepareStatement(
                "update cartItems set number = number + 1 where userId = ? and foodName = ?");
        try {
            foodStatement.setInt(1, userId);
            foodStatement.setString(2, foodName);
            ResultSet foodResult = foodStatement.executeQuery();
            foodResult.next();

            cartUpdate.setInt(1, foodResult.getInt("price"));
            cartUpdate.setInt(2, userId);
            cartUpdate.executeUpdate();

            itemStatement.setInt(1, userId);
            itemStatement.setString(2, foodName);
            itemStatement.executeUpdate();

            foodResult.close();
            connection.commit();
        } catch (SQLException e){
            if(connection != null){
                connection.rollback();
            }
        }
        foodStatement.close();
        cartUpdate.close();
        itemStatement.close();
        connection.close();
    }

    public void insertPartyFoodInCart(String foodName, String restaurantId, int userId) throws SQLException, Error403 {
        Connection connection;
        connection = ConnectionPool.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(TRANSACTION_SERIALIZABLE);

        PreparedStatement foodStatement = connection.prepareStatement("select * from partyFoods where restaurantId = ? and name = ?");
        Statement cartTime = connection.createStatement();
        PreparedStatement cartUpdate = null;
        PreparedStatement itemStatement = connection.prepareStatement(
                "insert into cartItems (userId, foodName, price, number, isPartyFood) values (?, ?, ?, ?, ?, )");
        try {
            foodStatement.setString(1, restaurantId);
            foodStatement.setString(2, foodName);
            ResultSet foodResult = foodStatement.executeQuery();
            foodResult.next();
            if (foodResult.getInt("count") <= 0) {
                foodResult.close();
                foodStatement.close();
                cartTime.close();
                cartUpdate.close();
                itemStatement.close();
                connection.close();
                throw new Error403("Error: Sorry! "+foodName+" is over!");
            }

            ResultSet isTimeSet = cartTime.executeQuery("select isFoodParty from shoppingCarts where id = " + userId);
            isTimeSet.next();
            if(isTimeSet.getInt("isFoodParty") == 0) {
                 cartUpdate = connection.prepareStatement(
                        "update shoppingCarts set isEmpty = ? , restaurantId = ? , restaurantName = ? , totalPayment = totalPayment + ? , isFoodParty = isFoodParty + 1 , firstPartyFoodEnteredTime = ? where userId = ? ");
                 Statement foodPartyTime = connection.createStatement();
                 ResultSet time = foodPartyTime.executeQuery("select * from Foodparty");
                 time.next();
                 cartUpdate.setTimestamp(5, time.getTimestamp("enteredDate"));
                 cartUpdate.setInt(6, userId);
                 time.close();
                 foodPartyTime.close();
            }
            else {
                cartUpdate = connection.prepareStatement(
                        "update shoppingCarts set isEmpty = ? , restaurantId = ? , restaurantName = ? , totalPayment = totalPayment + ? , isFoodParty = isFoodParty + 1 where userId = ? ");
                cartUpdate.setInt(5, userId);
            }
            cartUpdate.setBoolean(1, false);
            cartUpdate.setString(2, restaurantId);
            cartUpdate.setString(3, foodResult.getString("restaurantName"));
            cartUpdate.setInt(4, foodResult.getInt("price"));

            cartUpdate.executeUpdate();

            itemStatement.setInt(1, userId);
            itemStatement.setString(2, foodName);
            itemStatement.setInt(3, foodResult.getInt("price"));
            itemStatement.setInt(4, 1);
            itemStatement.setBoolean(5, true);
            itemStatement.executeUpdate();

            FoodPartyRepository.getInstance().updateCount(-1, restaurantId, foodName);

            foodResult.close();
            isTimeSet.close();
            connection.commit();
        } catch (SQLException e){
            if(connection != null){
                connection.rollback();
            }
        }
        foodStatement.close();
        cartTime.close();
        cartUpdate.close();
        itemStatement.close();
        connection.close();
    }

    public void updatePartyFoodInCart(String foodName, String restaurantId, int userId) throws SQLException, Error403 {
        Connection connection;
        connection = ConnectionPool.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(TRANSACTION_SERIALIZABLE);

        PreparedStatement foodStatement = connection.prepareStatement("select * from partyFoods where restaurantId = ? and name = ?");
        Statement cartTime = connection.createStatement();
        PreparedStatement cartUpdate = connection.prepareStatement(
                "update shoppingCarts set totalPayment = totalPayment + ? , isFoodParty = isFoodParty + 1 where userId = ? ");
        PreparedStatement itemStatement = connection.prepareStatement(
                "update cartItems set number = number + 1 where userId = ? and foodName = ? and isPartyFood = true");
        try {
            foodStatement.setString(1, restaurantId);
            foodStatement.setString(2, foodName);
            ResultSet foodResult = foodStatement.executeQuery();
            foodResult.next();
            if (foodResult.getInt("count") <= 0) {
                foodResult.close();
                foodStatement.close();
                cartTime.close();
                cartUpdate.close();
                itemStatement.close();
                connection.close();
                throw new Error403("Error: Sorry! "+foodName+" is over!");
            }

            cartUpdate.setInt(1, foodResult.getInt("price"));
            cartUpdate.setInt(2, userId);
            cartUpdate.executeUpdate();

            itemStatement.setInt(1, userId);
            itemStatement.setString(2, foodName);
            itemStatement.executeUpdate();

            FoodPartyRepository.getInstance().updateCount(-1, restaurantId, foodName);

            foodResult.close();
            connection.commit();
        } catch (SQLException e){
            if(connection != null){
                connection.rollback();
            }
        }
        foodStatement.close();
        cartUpdate.close();
        itemStatement.close();
        connection.close();
}

//    public void insertOrder()
//    public void updateOrder()
//    public void deleteFromCart()
}
