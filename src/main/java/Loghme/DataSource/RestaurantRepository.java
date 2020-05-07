package Loghme.DataSource;

import Loghme.Domain.Logic.Location;
import Loghme.Exceptions.Error403;
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

    public ArrayList<RestaurantDAO> doGetRestaurants() throws SQLException {
        ArrayList<RestaurantDAO> restaurantDAOS = new ArrayList<>();
        Connection connection;
        connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select * from Restaurants");
        while(result.next()) {
            RestaurantDAO restaurantDAO = new RestaurantDAO();
            restaurantDAO.setId(result.getString("id"));
            restaurantDAO.setName(result.getString("name"));
            restaurantDAO.setLocation(new Location(result.getInt("x"),result.getInt("y")));
            restaurantDAO.setLogo(result.getString("logo"));
            restaurantDAOS.add(restaurantDAO);
        }
        result.close();
        statement.close();
        connection.close();
        return restaurantDAOS;
    }

    public RestaurantDAO doGetRestaurant(String id) throws SQLException, Error404 {
        Connection connection;
        connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from Restaurants where id = ?");
        statement.setString(1, id);
        ResultSet result = statement.executeQuery();
        PreparedStatement innerStatement = connection.prepareStatement("select * from Foods where restaurantId = ?");
        innerStatement.setString(1,id);
        if (!result.next()) {
            statement.close();
            innerStatement.close();
            connection.close();
            throw new Error404("Error: There is no restaurant with id: " + id);
        }
        RestaurantDAO restaurantDAO = new RestaurantDAO();
        restaurantDAO.setId(result.getString("id"));
        restaurantDAO.setName(result.getString("name"));
        restaurantDAO.setLocation(new Location(result.getInt("x"),result.getInt("y")));
        restaurantDAO.setLogo(result.getString("logo"));

        ResultSet innerResult = innerStatement.executeQuery();
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
        PreparedStatement searchStatement = connection.prepareStatement("select id from Restaurants where id = ?");
        PreparedStatement insertResStatement = connection.prepareStatement(
                "insert into Restaurants (id, name, x, y, logo) values (?, ?, ?, ?, ?)");
        PreparedStatement insertFoodStatement = connection.prepareStatement(
                "insert into Foods (restaurantId, name, price, description, image, popularity) values (?, ?, ?, ?, ?, ?)");

        for (RestaurantDAO restaurantDAO : restaurantDAOS) {
            searchStatement.setString(1, restaurantDAO.getId());
            ResultSet searchResult = searchStatement.executeQuery();
            if (searchResult.next()) {
                searchResult.close();
                continue;
            }
            insertResStatement.setString(1, restaurantDAO.getId());
            insertResStatement.setString(2, restaurantDAO.getName());
            insertResStatement.setInt(3, restaurantDAO.getLocation().getX());
            insertResStatement.setInt(4, restaurantDAO.getLocation().getY());
            insertResStatement.setString(5, restaurantDAO.getLogo());
            insertResStatement.executeUpdate();
            for (FoodDAO foodDAO : restaurantDAO.getMenu()) {
                insertFoodStatement.setString(1, restaurantDAO.getId());
                insertFoodStatement.setString(2, foodDAO.getName());
                insertFoodStatement.setInt(3, foodDAO.getPrice());
                insertFoodStatement.setString(4, foodDAO.getDescription());
                insertFoodStatement.setString(5, foodDAO.getImage());
                insertFoodStatement.setFloat(6, foodDAO.getPopularity());
                insertFoodStatement.addBatch();
            }
            insertFoodStatement.executeBatch();
            searchResult.close();
        }
        searchStatement.close();
        insertResStatement.close();
        insertFoodStatement.close();
        connection.close();
    }

    public ArrayList<RestaurantDAO> search(String restaurantName, String foodName) throws SQLException, Error403 {
        Connection connection;
        connection = ConnectionPool.getConnection();
        Statement searchStatement = connection.createStatement();
        ResultSet result;

        //ToDo: Check these with Hamed

        if(!restaurantName.equals("") && !foodName.equals(""))
            result = searchStatement.executeQuery(
                    "select * from Restaurants where id in (select restaurantId from Foods where name like \"%"+ foodName + "%\") and name like \"%"+ restaurantName +"%\"");
        else if(!restaurantName.equals(""))
            result = searchStatement.executeQuery(
                    "select * from Restaurants where name like \"%"+ restaurantName + "%\"");
        else if(!foodName.equals(""))
            result = searchStatement.executeQuery(
                    "select * from Restaurants where id in (select restaurantId from Foods where name like \"%"+ foodName + "%\")");
        else {
            searchStatement.close();
            connection.close();
            throw new Error403("No restaurant name and food name has been entered!");
        }
        ArrayList<RestaurantDAO> restaurantDAOS = new ArrayList<>();
        while(result.next()) {
            RestaurantDAO restaurantDAO = new RestaurantDAO();
            restaurantDAO.setId(result.getString("id"));
            restaurantDAO.setName(result.getString("name"));
            restaurantDAO.setLocation(new Location(result.getInt("x"),result.getInt("y")));
            restaurantDAO.setLogo(result.getString("logo"));
            restaurantDAOS.add(restaurantDAO);
        }
        searchStatement.close();
        result.close();
        connection.close();
        return restaurantDAOS;
    }
}