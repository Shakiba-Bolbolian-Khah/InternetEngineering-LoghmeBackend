package Loghme.DataSource;

import Loghme.Exceptions.Error404;

import java.sql.*;
import java.util.ArrayList;

public class RestaurantRepository {
    private static RestaurantRepository instance;

    public static RestaurantRepository getInstance() {
        if (instance == null)
            instance = new RestaurantRepository();
        return instance;
    }

    public ArrayList<RestaurantDAO> getRestaurants() throws SQLException {
        ArrayList<RestaurantDAO> restaurantDAOS = new ArrayList<>();
        Connection connection;
        connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select * from restaurants");
        while(result.next()) {
            RestaurantDAO restaurantDAO = new RestaurantDAO();
            restaurantDAO.setId(result.getString("id"));
            restaurantDAO.setName(result.getString("name"));
            restaurantDAO.setX(result.getInt("x"));
            restaurantDAO.setY(result.getInt("y"));
            restaurantDAO.setLogo(result.getString("logo"));
            restaurantDAOS.add(restaurantDAO);
        }
        result.close();
        statement.close();
        connection.close();
        return restaurantDAOS;
    }

    public RestaurantDAO getRestaurant(String id) throws SQLException, Error404 {
        Connection connection;
        connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        Statement innerStatement = connection.createStatement();
        ResultSet result = statement.executeQuery("select * from restaurants where id = \"" + id + "\"");
        if (!result.next())
            throw new Error404("Error: There is no restaurant with id: " + id);

        RestaurantDAO restaurantDAO = new RestaurantDAO();
        restaurantDAO.setId(result.getString("id"));
        restaurantDAO.setName(result.getString("name"));
        restaurantDAO.setX(result.getInt("x"));
        restaurantDAO.setY(result.getInt("y"));
        restaurantDAO.setLogo(result.getString("logo"));

        ResultSet innerResult = innerStatement.executeQuery("select * from foods where restaurantId = \"" + id + "\"");
        while (innerResult.next()) {
            FoodDAO foodDAO = new FoodDAO();
            foodDAO.setName(innerResult.getString("name"));
            foodDAO.setPrice(innerResult.getInt("price"));
            foodDAO.setDescription(innerResult.getString("description"));
            foodDAO.setPopularity(innerResult.getFloat("popularity"));
            foodDAO.setImage(innerResult.getString("image"));
            restaurantDAO.addToMenu(foodDAO);
        }
        result.close();
        innerResult.close();
        statement.close();
        innerStatement.close();
        connection.close();
        return restaurantDAO;
    }

    public void insertRestaurants(ArrayList<RestaurantDAO> restaurantDAOS) throws SQLException {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement searchStatement = connection.prepareStatement("select id from restaurants where id = ?");
        PreparedStatement insertStatement = connection.prepareStatement("into Users(id, firstName, lastName, phoneNumber, email, x, y, credit)\n" +
                "values (0, \"احسان\", \"خامس\u200Cپناه\", \"09123456789\", \"ekhamespanah@yahoo.com\", 0, 0, 100000)")
        pStatement.setInt(1, addedCredit);
        pStatement.setInt(2, userId);
        pStatement.executeUpdate();
        pStatement.close();
        connection.close();
    }
}
