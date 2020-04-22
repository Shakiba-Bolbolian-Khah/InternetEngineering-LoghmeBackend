package Loghme.DataSource;

import Loghme.Exceptions.Error404;

import java.sql.*;

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
        Statement cartStatement = connection.createStatement();
        Statement cartItemStatement = connection.createStatement();
        ResultSet userResult = userStatement.executeQuery("select * from users where id =" + userId);
        if (!userResult.next())
            throw new Error404("Error: There is no user with id: " + userId);

        UserDAO userDAO = new UserDAO();
        userDAO.setId(userResult.getInt("id"));
        userDAO.setFirstName(userResult.getString("firstName"));
        userDAO.setLastName(userResult.getString("lastName"));
        userDAO.setLastName(userResult.getString("phoneNumber"));
        userDAO.setLastName(userResult.getString("email"));
        userDAO.setX(userResult.getInt("x"));
        userDAO.setY(userResult.getInt("y"));
        userDAO.setCredit(userResult.getInt("credit"));

        userResult.close();

        ResultSet orderResult = orderStatement.executeQuery("select * from orders where userId =" + userId);
        while(orderResult.next()) {
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.setId(orderResult.getInt("id"));
            orderDAO.setRestaurantId(orderResult.getString("restaurantId"));
            orderDAO.setRestaurantName(orderResult.getString("restaurantName"));
            orderDAO.setTotalPayment(orderResult.getInt("totalpayment"));
            orderDAO.setFoodParty(orderResult.getBoolean("isFoodParty"));
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

        ResultSet cartResult = cartStatement.executeQuery("select * from shoppingCarts where userId =" + userId);
        cartResult.next();
        CartDAO cartDAO = new CartDAO();
        cartDAO.setEmpty(cartResult.getBoolean("isEmpty"));
        cartDAO.setRestaurantId(cartResult.getString("restaurantId"));
        cartDAO.setRestaurantName(cartResult.getString("restaurantName"));
        cartDAO.setTotalPayment(cartResult.getInt("totalpayment"));
        cartDAO.setFoodParty(cartResult.getBoolean("isFoodParty"));
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
        userDAO.setShoppingCart(cartDAO);

        cartItemResult.close();
        cartResult.close();
        orderResult.close();
        userStatement.close();
        orderStatement.close();
        itemStatement.close();
        cartStatement.close();
        cartItemStatement.close();
        connection.close();
        return userDAO;
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

//    public void insertOrder()
//    public void updateOrder()
//    public void addToCart()
//    public void deleteFromCart()
}
