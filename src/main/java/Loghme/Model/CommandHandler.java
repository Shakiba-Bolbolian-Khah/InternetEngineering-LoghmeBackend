package Loghme.Model;

import Loghme.Exceptions.*;
import Loghme.Repository.APIReader;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CommandHandler {
    private static CommandHandler instance;
    private Loghme loghme;

    private CommandHandler() throws IOException {
        this.loghme = Loghme.getInstance();
        String restaurantsData = APIReader.getInstance().getDataFromAPI("restaurants");
        setLoghmeRestaurants(restaurantsData);
    }

    public static CommandHandler getInstance() throws IOException {
        if(instance == null) {
            instance = new CommandHandler();
        }
        return instance;
    }

    public void setLoghmeRestaurants(String restaurantsData){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Restaurant> restaurants = gson.fromJson(restaurantsData, new TypeToken<ArrayList<Restaurant>>(){}.getType());
        loghme.setRestaurants(restaurants);
    }

    public void setLoghmeDeliveries(String deliveriesData){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Delivery> deliveries = gson.fromJson(deliveriesData, new TypeToken<ArrayList<Delivery>>(){}.getType());
        loghme.setDeliveries(deliveries);
    }

    public void assignDelivery(int orderId) throws Error404, Error403, IOException {
        loghme.assignDelivery(orderId);
    }

    public void addRestaurant(String newRestaurantInfo){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Restaurant newRestaurant;
        try {
            newRestaurant = gson.fromJson(newRestaurantInfo, Restaurant.class);
            System.out.println(loghme.addRestaurant(newRestaurant));

        } catch (JsonSyntaxException e) {
            System.out.println("Error Wrong IO Command: Wrong JSON input.");
        } catch (ErrorHandler errorHandler){
            System.err.print(errorHandler);
        }
    }

    public void addFood(String newFoodInfo){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Food newFood = null;
        String restaurantName = "";
        try {
            newFood = gson.fromJson(newFoodInfo, Food.class);
            restaurantName = new JsonParser().parse(newFoodInfo).getAsJsonObject().get("restaurantName").getAsString();
        } catch (JsonSyntaxException e) {
            System.out.println("Error Wrong IO Command: Wrong JSON input.");
        }
        try {
            System.out.println(loghme.addFood(newFood, restaurantName));
        } catch (ErrorHandler errorHandler) {
            System.err.print(errorHandler);
        }
    }

    public ArrayList<Restaurant> getRestaurants() throws Error404 {
        return loghme.getRestaurants();
    }

    public Restaurant getRestaurant(String restaurantId) throws Error404, Error403 {
        return loghme.getRestaurant(restaurantId);
    }

    public void getFood(String newFoodInfo){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String restaurantName, foodName;
        try {
            restaurantName = new JsonParser().parse(newFoodInfo).getAsJsonObject().get("restaurantName").getAsString();
            foodName = new JsonParser().parse(newFoodInfo).getAsJsonObject().get("foodName").getAsString();
            System.out.println(gson.toJson(loghme.getFood(restaurantName, foodName)));
        } catch (JsonSyntaxException e) {
            System.out.println("Error Wrong IO Command: Wrong JSON input.");
        } catch (ErrorHandler errorHandler){
            System.err.print(errorHandler);
        }
    }

    public String addToCart(String restaurantId, String foodName) throws Error404, Error403 {
        return loghme.addToCart(restaurantId, foodName);
    }

    public String addPartyFoodToCart(String restaurantId, String partyFoodName) throws Error404, Error403 {
        return loghme.addPartyFoodToCart(restaurantId, partyFoodName);
    }

    public Map<String,Integer> getCart() throws Error404 {
        return loghme.getCart();
    }

    public Order finalizeOrder() throws Error403, Error400 {
        return loghme.finalizeOrder();
    }

    public String getCartRestaurant(){
        return loghme.getUser().getShoppingCart().getRestaurantName();
    }

    public User getUser(){
        return loghme.getUser();
    }

    public void getRecommendedRestaurants(){
        try {
            System.out.println(loghme.getRecommendedRestaurants());
        } catch (ErrorHandler errorHandler){
            System.err.print(errorHandler);
        }
    }

    public String increaseCredit(int addedCredit){
        return loghme.increaseCredit(addedCredit);
    }

    public Order getOrder(int orderId) throws Error404 {
        return loghme.getOrder(orderId);
    }

    public void setFoodParty(String newPartyRestaurants){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Restaurant> partyRestaurants = gson.fromJson(newPartyRestaurants, new TypeToken<ArrayList<Restaurant>>(){}.getType());
        setPartyRestaurants(partyRestaurants);
        setPartyFoods(newPartyRestaurants);
    }

    public void setPartyRestaurants(ArrayList<Restaurant> partyRestaurants){
        for(Restaurant restaurant: partyRestaurants){
            if(!loghme.hasResraurant(restaurant.getId())){
                restaurant.clearMenu();
                try {
                    loghme.addRestaurant(restaurant);
                } catch (ErrorHandler errorHandler) {
                    System.err.print(errorHandler);
                }
            }
        }
    }

    public void setPartyFoods(String newPartyRestaurants){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray restaurantsArray = new JsonParser().parse(newPartyRestaurants).getAsJsonArray();
        ArrayList<PartyFood> partyFoods = new ArrayList<>();

        for (int i = 0; i < restaurantsArray.size(); i++) {
            JsonArray newMenu = restaurantsArray.get(i).getAsJsonObject().get("menu").getAsJsonArray();
            String newRestaurantId = restaurantsArray.get(i).getAsJsonObject().get("id").getAsString();
            String newRestaurantName = restaurantsArray.get(i).getAsJsonObject().get("name").getAsString();
            ArrayList<PartyFood> newPartyFoods = gson.fromJson(newMenu, new TypeToken<ArrayList<PartyFood>>(){}.getType());
            newPartyFoods.forEach((u) -> u.setRestaurantId(newRestaurantId));
            newPartyFoods.forEach((u) -> u.setRestaurantName(newRestaurantName));
            partyFoods.addAll(newPartyFoods);
        }

        loghme.setFoodParty(partyFoods);
    }

    public ArrayList<PartyFood> getFoodParty(){
        return loghme.getFoodParty();
    }
}