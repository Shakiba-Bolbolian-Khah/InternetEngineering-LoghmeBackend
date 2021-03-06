package Loghme.Domain.Logic;

import Loghme.DataSource.*;
import Loghme.Exceptions.*;
import Loghme.PresentationController.HomeRestaurantDTO;
import Loghme.PresentationController.RestaurantDTO;
import Loghme.PresentationController.ShoppingCartDTO;
import Loghme.PresentationController.UserDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CommandHandler {
    private static CommandHandler instance;
    private Loghme loghme;

    private CommandHandler() throws IOException, SQLException {
        this.loghme = Loghme.getInstance();
        String restaurantsData = APIReader.getInstance().getDataFromAPI("restaurants");
        insertLoghmeRestaurants(restaurantsData);
    }

    public static CommandHandler getInstance() throws IOException, SQLException {
        if(instance == null) {
            instance = new CommandHandler();
        }
        return instance;
    }

    public void insertLoghmeRestaurants(String restaurantsData) throws SQLException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<RestaurantDAO> restaurantDAOS = gson.fromJson(restaurantsData, new TypeToken<ArrayList<RestaurantDAO>>(){}.getType());
        loghme.insertRestaurants(restaurantDAOS);
    }

    public void doSetLoghmeDeliveries(String deliveriesData){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Delivery> deliveries = gson.fromJson(deliveriesData, new TypeToken<ArrayList<Delivery>>(){}.getType());
        loghme.doSetDeliveries(deliveries);
    }

    public void assignDelivery(int orderId, int userId) throws Error404, Error403, IOException, SQLException {
        loghme.assignDelivery(orderId, userId);
    }

    public ArrayList<HomeRestaurantDTO> doGetRestaurants() throws Error404, SQLException {
        return DataConverter.getInstance().restaurantsToDTO(loghme.findNearestRestaurantsForUser());
    }

    public RestaurantDTO doGetRestaurant(String restaurantId) throws Error404, SQLException {
        return loghme.doGetRestaurant(restaurantId);
    }

    public String addToCart(String restaurantId, String foodName, boolean isPartyFood, int userId) throws Error403, SQLException {
        return loghme.addToCart(restaurantId, foodName, isPartyFood, userId);
    }

    public String deleteFromCart(String restaurantId, String foodName, boolean isPartyFood, int userId) throws Error403, Error404, SQLException {
        return loghme.deleteFromCart(restaurantId, foodName, isPartyFood, userId);
    }

    public ShoppingCartDTO doGetCart(int userId) throws Error400, SQLException {
        ShoppingCartDTO cart = DataConverter.getInstance().DAOtoCartDTO(UserRepository.getInstance().doGetCart(userId));
        if(cart.isEmpty())
            throw  new Error400("There is nothing to show in your cart!");
        return cart;
    }

    public String finalizeOrder(int userId) throws Error403, Error400, Error404, SQLException {
        return loghme.finalizeOrder(userId);
    }

    public UserDTO doGetUser(int userId) throws Error404, SQLException {
        return DataConverter.getInstance().DAOtoUserDTO(UserRepository.getInstance().doGetUser(userId));
    }

    public void getRecommendedRestaurants(int userId){
        try {
            System.out.println(loghme.doGetRecommendedRestaurants(userId));
        } catch (ErrorHandler | SQLException errorHandler){
            System.err.print(errorHandler);
        } catch (Error404 error404) {
            error404.printStackTrace();
        }
    }

    public String increaseCredit(int addedCredit, int userId) throws SQLException {
        return loghme.increaseCredit(addedCredit, userId);
    }

    public void doSetFoodParty(String newPartyRestaurants) throws SQLException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<RestaurantDAO> partyRestaurantDAOs = gson.fromJson(newPartyRestaurants, new TypeToken<ArrayList<RestaurantDAO>>(){}.getType());
        doSetPartyRestaurants(partyRestaurantDAOs);
        doSetPartyFoods(newPartyRestaurants);
    }

    public void doSetPartyRestaurants(ArrayList<RestaurantDAO> partyRestaurantDAOs) throws SQLException {
        for(RestaurantDAO restaurantDAO: partyRestaurantDAOs){
            restaurantDAO.clearMenu();
        }
        loghme.insertRestaurants(partyRestaurantDAOs);
    }

    public void doSetPartyFoods(String newPartyRestaurants) throws SQLException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray restaurantsArray = new JsonParser().parse(newPartyRestaurants).getAsJsonArray();
        ArrayList<PartyFoodDAO> partyFoodDAOS = new ArrayList<>();
        FoodPartyDAO foodPartyDAO = new FoodPartyDAO();
        foodPartyDAO.setEnteredDate(LocalDateTime.now());
        for (int i = 0; i < restaurantsArray.size(); i++) {
            JsonArray newMenu = restaurantsArray.get(i).getAsJsonObject().get("menu").getAsJsonArray();
            String newRestaurantId = restaurantsArray.get(i).getAsJsonObject().get("id").getAsString();
            String newRestaurantName = restaurantsArray.get(i).getAsJsonObject().get("name").getAsString();
            ArrayList<PartyFoodDAO> newPartyFoods = gson.fromJson(newMenu, new TypeToken<ArrayList<PartyFoodDAO>>(){}.getType());
            newPartyFoods.forEach((u) -> u.setRestaurantId(newRestaurantId));
            newPartyFoods.forEach((u) -> u.setRestaurantName(newRestaurantName));
            partyFoodDAOS.addAll(newPartyFoods);
        }
        foodPartyDAO.setPartyFoods(partyFoodDAOS);
        loghme.setFoodParty(foodPartyDAO);
    }

    public FoodPartyDAO getFoodParty() throws SQLException {
        return loghme.getFoodParty();
    }

    public ArrayList<HomeRestaurantDTO> search( String restaurantName, String foodName) throws SQLException, Error400 {
        return DataConverter.getInstance().restaurantsToDTO(loghme.search(restaurantName, foodName));
    }

    public int signup( UserDAO userDAO, String password) throws SQLException, Error403 {
        return loghme.signup(userDAO, password);
    }

    public int login( String email, String password) throws SQLException, Error403 {
        System.out.println("Command Handler: "+ email+password);
        return loghme.login(email, password);
    }

    public int googleLogin( String email) throws SQLException, Error403 {
        return loghme.googleLogin(email);
    }
}