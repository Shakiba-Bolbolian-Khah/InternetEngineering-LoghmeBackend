package Loghme.DataSource;

import java.sql.*;

import static java.sql.Connection.TRANSACTION_SERIALIZABLE;

public class FoodPartyRepository {
    private static FoodPartyRepository instance;

    public static FoodPartyRepository getInstance() {
        if (instance == null)
            instance = new FoodPartyRepository();
        return instance;
    }

    public FoodPartyDAO doGetFoodParty() throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        Statement timeStatement = connection.createStatement();
        Statement foodsStatement = connection.createStatement();
        FoodPartyDAO foodPartyDAO = new FoodPartyDAO();
        ResultSet timeResult = timeStatement.executeQuery("select * from foodParty");
        if(!timeResult.next()) {
            timeResult.close();
            return foodPartyDAO;
        }
        foodPartyDAO.setEnteredDate(timeResult.getTimestamp("enteredDate").toLocalDateTime());
        timeResult.close();
        ResultSet foodResult = foodsStatement.executeQuery("select * from partyFoods");
        while (foodResult.next()){
            PartyFoodDAO partyFoodDAO = new PartyFoodDAO();
            partyFoodDAO.setRestaurantId(foodResult.getString("restaurantId"));
            partyFoodDAO.setRestaurantName(foodResult.getString("restaurantName"));
            partyFoodDAO.setName(foodResult.getString("name"));
            partyFoodDAO.setDescription(foodResult.getString("description"));
            partyFoodDAO.setPrice(foodResult.getInt("price"));
            partyFoodDAO.setOldPrice(foodResult.getInt("oldPrice"));
            partyFoodDAO.setPopularity(foodResult.getFloat("popularity"));
            partyFoodDAO.setImage(foodResult.getString("image"));
            partyFoodDAO.setCount(foodResult.getInt("count"));

            foodPartyDAO.addPartyFoodDAO(partyFoodDAO);
        }
        foodResult.close();
        timeStatement.close();
        foodsStatement.close();
        connection.close();
        return foodPartyDAO;

    }

    public void insertFoodParty(FoodPartyDAO foodPartyDAO) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(TRANSACTION_SERIALIZABLE);

        Statement delTimeStatement = connection.createStatement();
        Statement delFoodStatement = connection.createStatement();
        PreparedStatement insertTimeStatement = connection.prepareStatement("insert into foodParty (enteredDate) values (?)");
        PreparedStatement insertFoodStatement = connection.prepareStatement(
                "insert into partyFoods (restaurantId, name, restaurantName, oldPrice, price, count, description, image, popularity) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            delTimeStatement.executeUpdate("delete from foodParty");
            delFoodStatement.executeUpdate("delete from partyFoods");
            insertTimeStatement.setTimestamp(1, Timestamp.valueOf(foodPartyDAO.getEnteredDate()));
            insertTimeStatement.executeUpdate();
            for (PartyFoodDAO partyFoodDAO : foodPartyDAO.getPartyFoods()) {
                insertFoodStatement.setString(1, partyFoodDAO.getRestaurantId());
                insertFoodStatement.setString(2, partyFoodDAO.getName());
                insertFoodStatement.setString(3, partyFoodDAO.getRestaurantName());
                insertFoodStatement.setInt(4, partyFoodDAO.getOldPrice());
                insertFoodStatement.setInt(5, partyFoodDAO.getPrice());
                insertFoodStatement.setInt(6, partyFoodDAO.getCount());
                insertFoodStatement.setString(7, partyFoodDAO.getDescription());
                insertFoodStatement.setString(8, partyFoodDAO.getImage());
                insertFoodStatement.setFloat(9, partyFoodDAO.getPopularity());
                insertFoodStatement.addBatch();
            }
            insertFoodStatement.executeBatch();
            connection.commit();
        } catch(SQLException e){
            if(connection != null){
                connection.rollback();
            }
        }
        delFoodStatement.close();
        delTimeStatement.close();
        insertFoodStatement.close();
        insertTimeStatement.close();
        connection.close();
    }

    public void updateCount(int count, String restaurantId, String foodName) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement pStatement = connection.prepareStatement(
                "update partyFoods set count = count + ? where name = ? and restaurantId = ?");
        pStatement.setInt(1, count);
        pStatement.setString(2, foodName);
        pStatement.setString(3, restaurantId);
        pStatement.executeUpdate();
        pStatement.close();
        connection.close();
    }

    public int doGetCount(String restaurantId, String foodName) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement pStatement = connection.prepareStatement(
                "select count from partyFoods where name = ? and restaurantId = ?");
        pStatement.setString(1, foodName);
        pStatement.setString(2, restaurantId);
        ResultSet resultSet = pStatement.executeQuery();
        resultSet.next();
        pStatement.close();
        connection.close();
        return resultSet.getInt("count");
    }
}