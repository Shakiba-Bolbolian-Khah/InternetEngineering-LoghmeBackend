package Loghme.DataSource;

import Loghme.Domain.Logic.Location;
import Loghme.Exceptions.Error403;
import Loghme.Exceptions.Error404;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UserRepository {
    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    public UserDAO doGetUser(int userId) throws Error404, SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement userStatement = connection.prepareStatement("select * from Users where id =?");
        userStatement.setInt(1, userId);
        ResultSet userResult = userStatement.executeQuery();
        if (!userResult.next())
            throw new Error404("Error: There is no user with id: " + userId);

        UserDAO userDAO = new UserDAO();
        userDAO.setId(userResult.getInt("id"));
        userDAO.setFirstName(userResult.getString("firstName"));
        userDAO.setLastName(userResult.getString("lastName"));
        userDAO.setPhoneNumber(userResult.getString("phoneNumber"));
        userDAO.setEmail(userResult.getString("email"));
        userDAO.setLocation(new Location(userResult.getInt("x"),userResult.getInt("y")));
        userDAO.setCredit(userResult.getInt("credit"));

        userResult.close();


        PreparedStatement itemStatement = connection.prepareStatement("select * from OrderItems where orderId = ?  and userId = ?");
        PreparedStatement orderStatement = connection.prepareStatement("select * from Orders where userId = ?");
        orderStatement.setInt(1, userId);
        ResultSet orderResult = orderStatement.executeQuery();
        while(orderResult.next()) {
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.setId(orderResult.getInt("id"));
            orderDAO.setRestaurantId(orderResult.getString("restaurantId"));
            orderDAO.setRestaurantName(orderResult.getString("restaurantName"));
            orderDAO.setTotalPayment(orderResult.getInt("totalpayment"));
            orderDAO.setDeliveryId(orderResult.getString("deliveryId"));
            orderDAO.setState(orderResult.getString("state"));
            Timestamp finalizationTime = orderResult.getTimestamp("finalizationTime");
            if (finalizationTime != null)
                orderDAO.setFinalizationTime(finalizationTime.toLocalDateTime());
            Time deliveringTime = orderResult.getTime("deliveringTime");
            if (deliveringTime != null)
                orderDAO.setDeliveringTime(deliveringTime.toLocalTime());

            itemStatement.setInt(1, orderDAO.getId());
            itemStatement.setInt(2, userId);
            ResultSet itemResult = itemStatement.executeQuery();
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
        userDAO.setShoppingCart(doGetCart(userId));

        orderResult.close();
        userStatement.close();
        orderStatement.close();
        itemStatement.close();
        connection.close();
        return userDAO;
    }

    public CartDAO doGetCart(int userId) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement cartStatement = connection.prepareStatement("select * from ShoppingCarts where userId = ? ");
        cartStatement.setInt(1, userId);
        ResultSet cartResult = cartStatement.executeQuery();
        cartResult.next();

        CartDAO cartDAO = new CartDAO();
        cartDAO.setEmpty(cartResult.getBoolean("isEmpty"));
        cartDAO.setRestaurantId(cartResult.getString("restaurantId"));
        cartDAO.setRestaurantName(cartResult.getString("restaurantName"));
        cartDAO.setTotalPayment(cartResult.getInt("totalpayment"));
        cartDAO.setFoodParty(cartResult.getInt("isFoodParty"));

        Timestamp cartTime = cartResult.getTimestamp("firstPartyFoodEnteredTime");
        if (cartTime != null)
            cartDAO.setFirstPartyFoodEnteredTime(cartTime.toLocalDateTime());

        PreparedStatement cartItemStatement = connection.prepareStatement("select * from CartItems where userId = ?");
        cartItemStatement.setInt(1, userId);
        ResultSet cartItemResult = cartItemStatement.executeQuery();
        while (cartItemResult.next()) {
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
        PreparedStatement pStatement = connection.prepareStatement("update Users set credit = credit + ? where id = ?");
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

        PreparedStatement foodStatement = connection.prepareStatement("select * from Foods where restaurantId = ? and name = ?");
        PreparedStatement resStatement = connection.prepareStatement("select name from Restaurants where id = ?");
        PreparedStatement cartUpdate = connection.prepareStatement(
                "update ShoppingCarts set isEmpty = ? , restaurantId = ? , restaurantName = ? , totalPayment = totalPayment + ? where userId = ? ");
        PreparedStatement itemStatement = connection.prepareStatement(
                "insert into CartItems (userId, foodName, price, number, isPartyFood) values (?, ?, ?, ?, ?)");
        try {
            foodStatement.setString(1, restaurantId);
            foodStatement.setString(2, foodName);
            ResultSet foodResult = foodStatement.executeQuery();
            foodResult.next();

            resStatement.setString(1, restaurantId);
            ResultSet restaurantName = resStatement.executeQuery();
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
                "select price from CartItems where userId = ? and foodName = ?");
        PreparedStatement cartUpdate = connection.prepareStatement(
                "update ShoppingCarts set totalPayment = totalPayment + ? where userId = ? ");
        PreparedStatement itemStatement = connection.prepareStatement(
                "update CartItems set number = number + 1 where userId = ? and foodName = ? and isPartyFood = false");
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

        PreparedStatement foodStatement = connection.prepareStatement("select * from PartyFoods where restaurantId = ? and name = ?");
        PreparedStatement cartTime = connection.prepareStatement("select isFoodParty from ShoppingCarts where userId = ?");
        PreparedStatement itemStatement = connection.prepareStatement(
                "insert into CartItems (userId, foodName, price, number, isPartyFood) values (?, ?, ?, ?, ?)");
        foodStatement.setString(1, restaurantId);
        foodStatement.setString(2, foodName);
        ResultSet foodResult = foodStatement.executeQuery();
        foodResult.next();
        if (foodResult.getInt("count") <= 0) {
            foodResult.close();
            foodStatement.close();
            cartTime.close();
            itemStatement.close();
            connection.close();
            throw new Error403("Error: Sorry! "+foodName+" is over!");
        }
        PreparedStatement cartUpdate;

        cartTime.setInt(1, userId);
        ResultSet isTimeSet = cartTime.executeQuery();
        isTimeSet.next();
        if(isTimeSet.getInt("isFoodParty") == 0) {
             cartUpdate = connection.prepareStatement(
                    "update ShoppingCarts set isEmpty = ? , restaurantId = ? , restaurantName = ? , totalPayment = totalPayment + ? , isFoodParty = isFoodParty + 1 , firstPartyFoodEnteredTime = ? where userId = ? ");
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
                    "update ShoppingCarts set isEmpty = ? , restaurantId = ? , restaurantName = ? , totalPayment = totalPayment + ? , isFoodParty = isFoodParty + 1 where userId = ? ");
            cartUpdate.setInt(5, userId);
        }
        try{
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

        PreparedStatement foodStatement = connection.prepareStatement("select * from PartyFoods where restaurantId = ? and name = ?");
        Statement cartTime = connection.createStatement();
        PreparedStatement cartUpdate = connection.prepareStatement(
                "update ShoppingCarts set totalPayment = totalPayment + ? , isFoodParty = isFoodParty + 1 where userId = ? ");
        PreparedStatement itemStatement = connection.prepareStatement(
                "update CartItems set number = number + 1 where userId = ? and foodName = ? and isPartyFood = true");
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

    public boolean deleteFromCart(String foodName, int userId, boolean isOne, int price) throws SQLException {
        boolean isEmpty = false;
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement deleteStatement;
        PreparedStatement updateCart = connection.prepareStatement(
                "update ShoppingCarts set totalPayment = totalPayment - ? where userId = ? ");
        updateCart.setInt(1, price);
        updateCart.setInt(2, userId);

        if(isOne) {
            deleteStatement = connection.prepareStatement("delete from CartItems where userId = ? and foodName = ?");
            PreparedStatement checkCart = connection.prepareStatement("select count(*) from CartItems where userId = ?");
            checkCart.setInt(1, userId);
            ResultSet itemNumber = checkCart.executeQuery();
            itemNumber.next();
            if(itemNumber.getInt("count(*)") == 1)
                isEmpty = true;
            itemNumber.close();
            checkCart.close();
        }
        else
            deleteStatement = connection.prepareStatement(
                    "update CartItems set number = number - 1 where userId = ? and foodName = ? and isPartyFood = false");

        deleteStatement.setInt(1, userId);
        deleteStatement.setString(2, foodName);
        deleteStatement.executeUpdate();
        updateCart.executeUpdate();

        updateCart.close();
        deleteStatement.close();
        connection.close();
        return isEmpty;
    }

    public boolean deletePartyFoodFromCart(String restaurantId, String foodName, int userId, boolean isOne, int price) throws SQLException {
        boolean isEmpty = false;
        Connection connection;
        connection = ConnectionPool.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement deleteStatement;
        PreparedStatement updateCart = connection.prepareStatement(
                "update ShoppingCarts set totalPayment = totalPayment - ? ,  isFoodParty = isFoodParty - 1 where userId = ? ");
        updateCart.setInt(1, price);
        updateCart.setInt(2, userId);

        if (isOne) {
            deleteStatement = connection.prepareStatement(
                    "delete from CartItems where userId = ? and foodName = ? and isPartyFood = true");
            PreparedStatement checkCart = connection.prepareStatement("select count(*) from CartItems where userId = ?");
            checkCart.setInt(1, userId);
            ResultSet itemNumber = checkCart.executeQuery();
            itemNumber.next();
            if (itemNumber.getInt("count(*)") == 1)
                isEmpty = true;
            itemNumber.close();
            checkCart.close();
        }
        else
            deleteStatement = connection.prepareStatement(
                    "update CartItems set number = number - 1 where userId = ? and foodName = ? and isPartyFood = true");
        try {
            deleteStatement.setInt(1, userId);
            deleteStatement.setString(2, foodName);
            deleteStatement.executeUpdate();
            updateCart.executeUpdate();
            FoodPartyRepository.getInstance().updateCount(1, restaurantId, foodName);
            connection.commit();
        } catch (SQLException e){
            if(connection != null){
                connection.rollback();
            }
        }
        updateCart.close();
        deleteStatement.close();
        connection.close();
        return isEmpty;
    }

    public void clearCart(int userId) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement cartDelete = connection.prepareStatement(
                "update ShoppingCarts set totalPayment = 0 , isFoodParty = 0, isEmpty = true where userId = ? ");
        cartDelete.setInt(1,userId);
        PreparedStatement itemDelete = connection.prepareStatement(
                "delete from CartItems where userId = ? ");
        itemDelete.setInt(1, userId);
        cartDelete.executeUpdate();
        itemDelete.executeUpdate();
        cartDelete.close();
        itemDelete.close();
        connection.close();
    }

    public void finalizeOrder(int userId, int orderId) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement insertOrder = connection.prepareStatement(
                "insert into Orders (userId, id, restaurantId, restaurantName, totalPayment, state, finalizationTime) values (?, ?, ?, ?, ?, ?, ?)");
        PreparedStatement insertItem = connection.prepareStatement(
                "insert into OrderItems (userId, orderId, foodName, price, number) values (?, ?, ?, ?, ?)");
        CartDAO cartDAO = doGetCart(userId);

        insertOrder.setInt(1, userId);
        insertOrder.setInt(2, orderId);
        insertOrder.setString(3, cartDAO.getRestaurantId());
        insertOrder.setString(4, cartDAO.getRestaurantName());
        insertOrder.setInt(5, cartDAO.getTotalPayment());
        insertOrder.setString(6, "Searching");
        insertOrder.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        insertOrder.executeUpdate();

        for(CartItemDAO cartItemDAO: cartDAO.getItems()){
            insertItem.setInt(1,userId);
            insertItem.setInt(2, orderId);
            insertItem.setString(3, cartItemDAO.getFoodName());
            insertItem.setInt(4, cartItemDAO.getPrice());
            insertItem.setInt(5, cartItemDAO.getNumber());
            insertItem.executeUpdate();
        }

        PreparedStatement decrease = connection.prepareStatement("update Users set credit = credit - ? where id = ?");
        decrease.setInt(1, cartDAO.getTotalPayment());
        decrease.setInt(2, userId);
        decrease.executeUpdate();
        try {
            connection.commit();
        } catch (SQLException e){
            if(connection != null){
                connection.rollback();
            }
        }
        decrease.close();
        insertOrder.close();
        insertItem.close();
        connection.close();
    }

    public void setDeliveryForOrder(int orderId, int userId, String deliveryId, LocalTime deliveringTime) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement updateOrder = connection.prepareStatement(
                "update Orders set deliveringTime = ? , deliveryId = ? , state = ? where userId = ? and id = ?");
        updateOrder.setTime(1,Time.valueOf(deliveringTime));
        updateOrder.setString(2, deliveryId);
        updateOrder.setString(3, "Delivering");
        updateOrder.setInt(4, userId);
        updateOrder.setInt(5, orderId);
        updateOrder.executeUpdate();
        updateOrder.close();
        connection.close();
    }

    public void setDeliveredState(int orderId, int userId) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement updateOrder = connection.prepareStatement(
                "update Orders set state = ? where userId = ? and id = ?");
        updateOrder.setString(1, "Done");
        updateOrder.setInt(2, userId);
        updateOrder.setInt(3, orderId);
        updateOrder.executeUpdate();
        updateOrder.close();
        connection.close();
    }

    public int signup(UserDAO userDAO, String hashPass) throws SQLException, Error403 {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement checkEmail = connection.prepareStatement("select * from Users where email = ?");
        checkEmail.setString(1, userDAO.getEmail());
        ResultSet userResult = checkEmail.executeQuery();
        if (userResult.next()){
            checkEmail.close();
            connection.close();
            throw new Error403("Error: Email " + userDAO.getEmail() +" has been in system before.");
        }
        Statement findId = connection.createStatement();
        ResultSet id = findId.executeQuery("select count(*) from Users");
        id.next();
        userDAO.setId(id.getInt("count(*)"));

        connection.setAutoCommit(false);
        PreparedStatement insertUser = connection.prepareStatement(
                "insert into Users(id, firstName, lastName, phoneNumber, email, password, x, y, credit) " +
                        " values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        PreparedStatement insertCart = connection.prepareStatement(
                "insert into ShoppingCarts(userId, isEmpty, isFoodParty, totalPayment) " +
                        " values (?, true, 0, 0)");

        insertUser.setInt(1, userDAO.getId());
        insertUser.setString(2, userDAO.getFirstName());
        insertUser.setString(3, userDAO.getLastName());
        insertUser.setString(4, userDAO.getPhoneNumber());
        insertUser.setString(5, userDAO.getEmail());
        insertUser.setString(6, hashPass);
        insertUser.setInt(7, userDAO.getLocation().getX());
        insertUser.setInt(8, userDAO.getLocation().getY());
        insertUser.setInt(9, userDAO.getCredit());
        insertUser.executeUpdate();

        insertCart.setInt(1, userDAO.getId());
        insertCart.executeUpdate();

        try {
            connection.commit();
        } catch (SQLException e){
            if(connection != null){
                connection.rollback();
            }
        }
        insertCart.close();
        insertUser.close();
        connection.close();
        return userDAO.getId();
    }

    public int login(String email, String hashPass) throws SQLException, Error403 {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement findUser;

        findUser = connection.prepareStatement("select * from Users where email = ? and password = ?");
        findUser.setString(1, email);
        findUser.setString(2, hashPass);

        ResultSet userResult = findUser.executeQuery();

        System.out.println("User ID: ");

        if (!userResult.next()){
            findUser.close();
            connection.close();
            throw new Error403("Error: Entered email or password is not correct!");
        }
        System.out.println(userResult.getInt("id"));
        return userResult.getInt("id");
    }

    public int googleLogin(String email) throws SQLException, Error403 {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement findUser;

        findUser = connection.prepareStatement("select * from Users where email = ? ");
        findUser.setString(1, email);

        System.out.println(findUser);
        System.out.println("User ID: ");
        ResultSet userResult = findUser.executeQuery();

        if (!userResult.next()){
            findUser.close();
            connection.close();
            throw new Error403("Error: Entered email is not in system!");
        }
        System.out.println(userResult.getInt("id"));
        return userResult.getInt("id");
    }
}
