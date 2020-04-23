package Loghme.Domain.Logic;

import Loghme.DataSource.RestaurantDAO;
import Loghme.DataSource.RestaurantRepository;
import Loghme.DataSource.UserRepository;
import Loghme.Exceptions.*;
import Loghme.Domain.Scheduler.DeliveryFindingManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Loghme {
    private static Loghme instance;

    private ArrayList<Delivery> deliveries;
    private FoodParty foodParty;

    private Loghme() {
        this.deliveries = new ArrayList<>();
        this.foodParty = new FoodParty(LocalDateTime.now(),new ArrayList<>());
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

    public void assignDelivery(Order deliveringOrder) throws Error404, Error403, IOException, SQLException {
        User user = getUser();
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
        deliveringOrder.setDeliveryForOrder(BestDeliveryId, remainingTime);
        //ToDo: update order function in DB must be called
    }

    public User getUser() throws Error404, SQLException {
        return DataConverter.getInstance().DAOtoUser(UserRepository.getInstance().getUser(0));
    }

    public Double calculateDistance(Location restaurantLocation, Location userLocation) {
        int xDistance = restaurantLocation.getX() - userLocation.getX();
        int yDistance = restaurantLocation.getY() - userLocation.getY();
        return sqrt(pow(xDistance, 2) + pow(yDistance, 2));
    }

    public ArrayList<Restaurant> findNearestRestaurantsForUser() throws Error404, SQLException {
        User user = getUser();
        ArrayList<Restaurant> restaurants = DataConverter.getInstance().DAOtoRestaurantList(RestaurantRepository.getInstance().getRestaurants());
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

    public Restaurant doGetRestaurant(String restaurantId) throws Error404, SQLException {
        return DataConverter.getInstance().DAOtoRestaurant(RestaurantRepository.getInstance().getRestaurant(restaurantId));
    }

    public Food getFood(String restaurantId, String foodName) throws ErrorHandler, SQLException, Error404 {
       Restaurant restaurant = doGetRestaurant(restaurantId);
        return restaurant.getFood(foodName);
    }

    public String addToCart(String restaurantId, String foodName) throws Error403, Error404, SQLException {
        ShoppingCart userCart = doGetCart();
        if (!userCart.isEmpty()) {
            if (!(userCart.getRestaurantId().equals(restaurantId))) {
                throw new Error403("Error: You chose your restaurant before! Choosing 2 restaurants is invalid!");
            }
        }
        return userCart.addToCart(foodName, restaurantId, false);
    }

    public String deleteFromCart(String restaurantId, String foodName) throws Error403, Error404 {
//        if (user.getShoppingCart().isEmpty()) {
//            throw new Error403("Error: There is nothing in your cart to delete.");
//        }
//        if (user.getShoppingCart().getRestaurantId().equals(restaurantId))
//            return user.deleteFromCart(foodName);
//        else
//            throw new Error403("Error: You had not ordered food from this restaurant!");

//        ToDo:User repository calling delete from cart
        return "ok"; //Just in order to avoid error -_-

    }

    public String addPartyFoodToCart(String restaurantId, String partyFoodName) throws Error404, Error403, SQLException {
        ShoppingCart userCart = doGetCart();
        if (!userCart.isEmpty()) {
            if (!(userCart.getRestaurantId().equals(restaurantId))) {
                throw new Error403("Error: You chose your restaurant before! Choosing 2 restaurants is invalid!");
            }
        }
//        ArrayList<Restaurant> restaurants = DataConverter.getInstance().DAOtoRestaurantList(RestaurantRepository.getInstance().getRestaurants());
//        for (Restaurant restaurant : restaurants) {
//            if (restaurant.getId().equals(restaurantId)) {
//                PartyFood orderedFood = foodParty.doGetOrderedFood(restaurantId, partyFoodName);
//                if (orderedFood != null) {
//                    if(!user.isFoodParty()) {
//                        user.setTimeForShoppingCart(foodParty.getEnteredDate());
//                        user.setIsFoodParty(true);
//                    }
//                    user.setShoppingCartRestaurant(restaurantId, restaurant.getName());
//                    return user.addToCart(orderedFood, true);
//                } else {
//                    throw new Error404("Error: There is no "+partyFoodName+" in restaurant with name: "+restaurant.getName()+" in Food Party");
//                }
//            }
//        }
//        throw new Error404("Error: Restaurant with ID "+restaurantId+" does not exist in system!");
        //        ToDo:User repository calling insert foodparty to cart

        return userCart.addToCart(partyFoodName, restaurantId, true);
    }

    public String deletePartyFoodFromCart(String restaurantId, String partyFoodName) throws Error403, Error404 {
//        if (user.getShoppingCart().isEmpty()) {
//            throw new Error403("Error: There is nothing in your cart to delete.");
//        }
//        if (user.getShoppingCart().getRestaurantId().equals(restaurantId)) {
//            if (!user.isFoodParty())
//                throw new Error403("Error: You had not selected any food from food party!");
//            else {
//                if (!this.foodParty.isPartyFinished(user.getShoppingCartTime())) {
//                    this.foodParty.increaseFoodCount(restaurantId, partyFoodName);
//                    return user.deleteFromCart(partyFoodName);
//                }
//                else
//                    throw new Error403("Error: Food party time is over!");
//            }
//        }
//        else
//            throw new Error403("Error: You had not ordered food from this restaurant!");
        //        ToDo:User repository calling delete foodParty from cart
        return "ok"; //Just in order to avoid error -_-
    }

    public ShoppingCart doGetCart() throws SQLException {
        return DataConverter.getInstance().DAOtoCart(UserRepository.getInstance().getCart(0));
    }

    public Order finalizeOrder() throws Error400, Error403, Error404, SQLException {
        User user = DataConverter.getInstance().DAOtoUser(UserRepository.getInstance().getUser(0));
        Order order = user.finalizeOrder(this.foodParty.isPartyFinished(user.getShoppingCartTime()));
        findDelivery(order);
        return order;
        //ToDo: User rep. calling insert and finalize order. I just added user here to avoid errors.
    }

    public void findDelivery(Order order) {
        new DeliveryFindingManager(30, order);
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
        User user = getUser();
        Map<String,Double> scores = new HashMap<>();
        ArrayList<Restaurant> restaurants = DataConverter.getInstance().DAOtoRestaurantList(RestaurantRepository.getInstance().getRestaurants());
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

    public String setFoodParty(ArrayList<PartyFood> partyFoods){
        return foodParty.setFoodParty(partyFoods);
    }

    public FoodParty getFoodParty(){
        return foodParty;
    }
}