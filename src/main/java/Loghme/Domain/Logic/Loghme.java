package Loghme.Domain.Logic;

import Loghme.DataSource.*;
import Loghme.Exceptions.*;
import Loghme.Domain.Scheduler.DeliveryFindingManager;
import Loghme.PresentationController.HomeRestaurantDTO;
import Loghme.PresentationController.RestaurantDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Loghme {
    private static Loghme instance;

    private ArrayList<Delivery> deliveries;

    private Loghme() {
        this.deliveries = new ArrayList<>();
    }

    public static Loghme getInstance(){
        if(instance == null)
            instance = new Loghme();
        return instance;
    }

    public void insertRestaurants(ArrayList<RestaurantDAO> restaurantDAOS) throws SQLException {
        RestaurantRepository.getInstance().insertRestaurants(restaurantDAOS);
    }

    public void doSetDeliveries(ArrayList<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public void assignDelivery(int orderId, int userId) throws Error404, Error403, IOException, SQLException {
        User user = doGetUser();
        Order deliveringOrder = user.getOrder(orderId);
        double deliveringTime = 0;
        String BestDeliveryId = null;
        for (Delivery delivery: deliveries) {
            double timeToDeliver = delivery.findTimeToDeliver(user.getLocation(), doGetRestaurant(deliveringOrder.getRestaurantId()).getLocation());
            if (deliveringTime == 0 || timeToDeliver < deliveringTime) {
                deliveringTime = timeToDeliver;
                BestDeliveryId = delivery.getId();
            }
        }
        int hours = (int) deliveringTime / 3600;
        int minutes = (int) (deliveringTime % 3600) / 60;
        int seconds = (int) deliveringTime % 60;
        LocalTime remainingTime = LocalTime.of(hours, minutes, seconds);
        deliveringOrder.setDeliveryForOrder(userId, BestDeliveryId, remainingTime);
        //ToDo: update order function in DB must be called
    }

    public User doGetUser() throws Error404, SQLException {
        return DataConverter.getInstance().DAOtoUser(UserRepository.getInstance().doGetUser(0));
    }

    public Double calculateDistance(Location restaurantLocation, Location userLocation) {
        int xDistance = restaurantLocation.getX() - userLocation.getX();
        int yDistance = restaurantLocation.getY() - userLocation.getY();
        return sqrt(pow(xDistance, 2) + pow(yDistance, 2));
    }

    public ArrayList<Restaurant> findNearestRestaurantsForUser() throws Error404, SQLException {
        User user = doGetUser();
        ArrayList<Restaurant> restaurants = DataConverter.getInstance().DAOtoRestaurantList(RestaurantRepository.getInstance().doGetRestaurants());
        ArrayList<Restaurant> nearRestaurants = new ArrayList<>();
        if(restaurants.size() == 0){
            throw new Error404("Sorry! There is no restaurant around you in Loghme at this time!");
        }
        for (Restaurant restaurant : restaurants) {
            Double distance = calculateDistance(restaurant.getLocation(), user.getLocation());
            if (distance <= 170) {
                nearRestaurants.add(restaurant);
            }
        }
        return nearRestaurants;
    }

    public RestaurantDTO doGetRestaurant(String restaurantId) throws Error404, SQLException {
        return DataConverter.getInstance().DAOtoRestaurantDTO(RestaurantRepository.getInstance().doGetRestaurant(restaurantId));
    }

    public String addToCart(String restaurantId, String foodName, boolean isPartyFood) throws Error403, SQLException {
        ShoppingCart userCart = doGetCart();
        if (!userCart.isEmpty()) {
            if (!(userCart.getRestaurantId().equals(restaurantId))) {
                throw new Error403("Error: You chose your restaurant before! Choosing 2 restaurants is invalid!");
            }
        }
        return userCart.addToCart(foodName, restaurantId, isPartyFood);
    }

    public String deleteFromCart(String restaurantId, String foodName, boolean isPartyFood) throws Error403, Error404, SQLException {
        ShoppingCart userCart = doGetCart();
        if (userCart.isEmpty()) {
            throw new Error403("Error: There is nothing in your cart to delete.");
        }
        if (userCart.getRestaurantId().equals(restaurantId))
            return userCart.deleteFromCart(restaurantId, foodName, isPartyFood);
        else
            throw new Error403("Error: You had not ordered food from this restaurant!");
    }

    public ShoppingCart doGetCart() throws SQLException {
        return DataConverter.getInstance().DAOtoCart(UserRepository.getInstance().doGetCart(0));
    }

    public String finalizeOrder() throws Error400, Error403, Error404, SQLException {
        User user = doGetUser();
        int orderId = user.finalizeOrder();
        findDelivery(orderId, 0);
        return "Your order finalized successfully!";
        //ToDo: User rep. calling insert and finalize order.
    }

    public void findDelivery(int orderId, int userId) {
        new DeliveryFindingManager(30, orderId, userId);
    }

    public static Map<String, Double> sortByValue(Map<String, Double> hm)
    {
        List<Map.Entry<String, Double> > list = new LinkedList<>(hm.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        HashMap<String, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            temp.put(entry.getKey(), entry.getValue());
        }
        return temp;
    }

    public String doGetRecommendedRestaurants() throws ErrorHandler, SQLException, Error404 {
        User user = doGetUser();
        Map<String,Double> scores = new HashMap<>();
        ArrayList<Restaurant> restaurants = DataConverter.getInstance().DAOtoRestaurantList(RestaurantRepository.getInstance().doGetRestaurants());
        if(restaurants.size() == 0){
            throw new ErrorHandler("Error: Sorry there is no restaurant in Loghme at this time!");
        }
        for (Restaurant restaurant : restaurants) {
            int xDistance = restaurant.getLocation().getX() - user.getLocation().getX();
            int yDistance = restaurant.getLocation().getY() - user.getLocation().getY();
            Double distance = sqrt(pow(xDistance, 2) + pow(yDistance, 2));
            scores.put(restaurant.getName(), restaurant.calculateScore() / distance);
        }
        String recommended = "Recommended restaurant(s) based on your location:\n";
        scores = sortByValue(scores);
        int i = 1;
        for (Map.Entry<String,Double> entry : scores.entrySet()){
            if(i < 4)
                recommended += i + ". " + entry.getKey() + "\n";
            i++;
        }
        return recommended.substring(0, recommended.length()-1);
    }

    public String increaseCredit(int addedCredit) throws SQLException {
        UserRepository.getInstance().increaseCredit(0, addedCredit);
        return "Credit increased successfully!";
    }

    public void setFoodParty(FoodPartyDAO foodPartyDAO) throws SQLException {
        FoodPartyRepository.getInstance().insertFoodParty(foodPartyDAO);
    }

    public FoodPartyDAO getFoodParty() throws SQLException {
        return FoodPartyRepository.getInstance().doGetFoodParty();
    }

    public ArrayList<Restaurant> search(String restaurantName, String foodName){
        return DataConverter.getInstance().DAOtoRestaurantList(RestaurantRepository.getInstance().search(restaurantName, foodName));
    }


}