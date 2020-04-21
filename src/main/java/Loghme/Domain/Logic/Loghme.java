package Loghme.Domain.Logic;

import Loghme.Exceptions.*;
import Loghme.Domain.Scheduler.DeliveryFindingManager;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Loghme {
    private static Loghme instance;

    private ArrayList<Restaurant> restaurants;
    private ArrayList<Delivery> deliveries;
    private FoodParty foodParty;
    private User user;

    private Loghme() {
        this.restaurants = new ArrayList<>();
        this.deliveries = new ArrayList<>();
        this.foodParty = new FoodParty();
        this.user = new User("0","احسان","خامس‌پناه","۰۹۱۲۳۴۵۶۷۸۹","ekhamespanah@yahoo.com",new Location(0,0),100000,new ShoppingCart(true));
    }

    public static Loghme getInstance(){
        if(instance == null)
            instance = new Loghme();
        return instance;
    }

    public void doSetRestaurants(ArrayList<Restaurant> restaurants) {
        if (this.restaurants.size() == 0)
            this.restaurants = restaurants;
    }

    public void doSetDeliveries(ArrayList<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public void assignDelivery(Order deliveringOrder) throws Error404, Error403, IOException {
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
    }

    public User getUser() {
        return user;
    }

    public String addRestaurant(Restaurant newRestaurant) throws ErrorHandler {
        for (Restaurant restaurant : this.restaurants) {
            if (restaurant.getId().equals(newRestaurant.getId())) {
                throw new ErrorHandler("Error: \"" + newRestaurant.getName() + "\" restaurant was added before!");
            }
        }
        this.restaurants.ensureCapacity(this.restaurants.size()+1);
        this.restaurants.add(newRestaurant);
        return "\"" + newRestaurant.getName() + "\" restaurant has been added successfully!";
    }

    public String addFood(Food newFood, String restaurantId) throws ErrorHandler {
        for (Restaurant restaurant : this.restaurants) {
            if (restaurant.getId().equals(restaurantId)) {
                return restaurant.addFood(newFood);
            }
        }
        throw new ErrorHandler("Error: No \"" + restaurantId + "\" restaurant exists!");
    }

    public Double calculateDistance(Location restaurantLocation, Location userLocation) {
        int xDistance = restaurantLocation.getX() - userLocation.getX();
        int yDistance = restaurantLocation.getY() - userLocation.getY();
        return sqrt(pow(xDistance, 2) + pow(yDistance, 2));
    }

    public ArrayList<Restaurant> findNearestRestaurantsForUser() throws Error404 {
        ArrayList<Restaurant> nearRestaurants = new ArrayList<>();
        if(this.restaurants.size() == 0){
            throw new Error404("Sorry! There is no restaurant around you in Loghme at this time!");
        }
        for (Restaurant restaurant : this.restaurants) {
            Double distance = calculateDistance(restaurant.getLocation(), user.getLocation());
            if (distance <= 170) {
                nearRestaurants.add(restaurant);
            }
        }
        return nearRestaurants;
    }

    public ArrayList<Restaurant> doGetRestaurants() throws Error404 {
        if(this.restaurants.size()==0){
            throw new Error404("Error: Sorry there is no restaurant in Loghme at this time!");
        }
        return findNearestRestaurantsForUser();
    }

    public Restaurant doGetRestaurant(String restaurantId) throws Error403, Error404 {
        for (Restaurant restaurant : this.restaurants) {
            if (restaurant.getId().equals(restaurantId)) {
                if (calculateDistance(restaurant.getLocation(), user.getLocation()) <= 170)
                    return restaurant;
                else
                    throw new Error403("Error: Restaurant with ID "+restaurantId+" is not close enough to you!");
            }
        }
        throw new Error404("Error: Restaurant with ID "+restaurantId+" does not exist in system!");
    }

    public boolean hasResraurant(String restaurantId){
        for (Restaurant restaurant : this.restaurants) {
            if (restaurant.getId().equals(restaurantId))
                return true;
        }
        return false;
    }

    public Food getFood(String restaurantId, String foodName) throws ErrorHandler {
        for (Restaurant restaurant : this.restaurants) {
            if (restaurant.getId().equals(restaurantId)) {
                return restaurant.getFood(foodName);
            }
        }
        throw new ErrorHandler("Error: No \"" + restaurantId +"\" restaurant exists!");
    }

    public String addToCart(String restaurantId, String foodName) throws Error403, Error404 {
        if (!user.getShoppingCart().isEmpty()) {
            if (!(user.getShoppingCart().getRestaurantId().equals(restaurantId))) {
                throw new Error403("Error: You chose your restaurant before! Choosing 2 restaurants is invalid!");
            }
        }
        for (Restaurant restaurant : doGetRestaurants()) {
            if (restaurant.getId().equals(restaurantId)) {
                Food orderedFood = restaurant.getOrderedFood(foodName);
                if (orderedFood != null) {
                    user.setShoppingCartRestaurant(restaurantId, restaurant.getName());
                    return user.addToCart(orderedFood, false);
                } else {
                    throw new Error404("Error: There is no "+foodName+" in restaurant with name: "+ restaurant.getName());
                }
            }
        }
        throw new Error404("Error: Restaurant with ID "+restaurantId+" does not exist in system!");
    }

    public String deleteFromCart(String restaurantId, String foodName) throws Error403, Error404 {
        if (user.getShoppingCart().isEmpty()) {
            throw new Error403("Error: There is nothing in your cart to delete.");
        }
        if (user.getShoppingCart().getRestaurantId().equals(restaurantId))
            return user.deleteFromCart(foodName);
        else
            throw new Error403("Error: You had not ordered food from this restaurant!");
    }

    public String addPartyFoodToCart(String restaurantId, String partyFoodName) throws Error404, Error403 {
        if (!user.getShoppingCart().isEmpty()) {
            if (!(user.getShoppingCart().getRestaurantId().equals(restaurantId))) {
                throw new Error403("Error: You chose your restaurant before! Choosing 2 restaurants is invalid!");
            }
        }
        for (Restaurant restaurant : this.restaurants) {
            if (restaurant.getId().equals(restaurantId)) {
                PartyFood orderedFood = foodParty.doGetOrderedFood(restaurantId, partyFoodName);
                if (orderedFood != null) {
                    if(!user.isFoodParty()) {
                        user.setTimeForShoppingCart(foodParty.getEnteredDate());
                        user.setIsFoodParty(true);
                    }
                    user.setShoppingCartRestaurant(restaurantId, restaurant.getName());
                    return user.addToCart(orderedFood, true);
                } else {
                    throw new Error404("Error: There is no "+partyFoodName+" in restaurant with name: "+restaurant.getName()+" in Food Party");
                }
            }
        }
        throw new Error404("Error: Restaurant with ID "+restaurantId+" does not exist in system!");
    }

    public String deletePartyFoodFromCart(String restaurantId, String partyFoodName) throws Error403, Error404 {
        if (user.getShoppingCart().isEmpty()) {
            throw new Error403("Error: There is nothing in your cart to delete.");
        }
        if (user.getShoppingCart().getRestaurantId().equals(restaurantId)) {
            if (!user.isFoodParty())
                throw new Error403("Error: You had not selected any food from food party!");
            else {
                if (!this.foodParty.isPartyFinished(user.getShoppingCartTime())) {
                    this.foodParty.increaseFoodCount(restaurantId, partyFoodName);
                    return user.deleteFromCart(partyFoodName);
                }
                else
                    throw new Error403("Error: Food party time is over!");
            }
        }
        else
            throw new Error403("Error: You had not ordered food from this restaurant!");
    }

    public ShoppingCart doGetCart() throws Error404 {
        return user.doGetCart();
    }

    public Order finalizeOrder() throws Error400, Error403 {
        Order order = user.finalizeOrder(this.foodParty.isPartyFinished(user.getShoppingCartTime()));
        findDelivery(order);
        return order;
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

    public String doGetRecommendedRestaurants() throws ErrorHandler {
        Map<String,Double> scores = new HashMap<>();
        if(this.restaurants.size() == 0){
            throw new ErrorHandler("Error: Sorry there is no restaurant in Loghme at this time!");
        }
        for (Restaurant restaurant : this.restaurants) {
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

    public String increaseCredit(int addedCredit) {
        user.setCredit(user.getCredit() + addedCredit);
        return "Credit increased successfully!";
    }

    public Order getOrder(int orderId) throws Error404 {
        return user.getOrder(orderId);
    }

    public String setFoodParty(ArrayList<PartyFood> partyFoods){
        return foodParty.setFoodParty(partyFoods);
    }

    public FoodParty getFoodParty(){
        return foodParty;
    }
}