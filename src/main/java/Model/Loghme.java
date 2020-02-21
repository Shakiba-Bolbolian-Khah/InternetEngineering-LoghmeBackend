package Model;

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
        this.user = new User("1","Ehsan","Khames Paneh","09123456789","ekhamespanah@yahoo.com",new Location(0,0),1000,new ShoppingCart(true));
    }

    public static Loghme getInstance(){
        if(instance == null)
            instance = new Loghme();
        return instance;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void setDeliveries(ArrayList<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public User getUser() {
        return user;
    }

    public String addRestaurant(Restaurant newRestaurant) throws ErrorHandler {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId().equals(newRestaurant.getId())) {
                throw new ErrorHandler("Error: \"" + newRestaurant.getName() + "\" restaurant was added before!");
            }
        }
        restaurants.ensureCapacity(restaurants.size()+1);
        restaurants.add(newRestaurant);
        return "\"" + newRestaurant.getName() + "\" restaurant has been added successfully!";
    }

    public String addFood(Food newFood, String restaurantId) throws ErrorHandler {
        for (Restaurant restaurant : restaurants) {
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

    public ArrayList<Restaurant> findNearestRestaurantsForUser() throws ErrorHandler {
        ArrayList<Restaurant> nearRestaurants = new ArrayList<>();
        if(restaurants.size() == 0){
            throw new ErrorHandler("Error: Sorry there is no restaurant in Loghme at this time!");
        }
        for (Restaurant restaurant : restaurants) {
            Double distance = calculateDistance(restaurant.getLocation(), user.getLocation());
            if (distance <= 170) {
                nearRestaurants.add(restaurant);
            }
        }
        return nearRestaurants;
    }

    public ArrayList<Restaurant> getRestaurants() throws ErrorHandler {
        if(restaurants.size()==0){
            throw new ErrorHandler("Error: Sorry there is no restaurant in Loghme at this time!");
        }
        return this.findNearestRestaurantsForUser();
    }

    public Restaurant getRestaurant(String restaurantId) throws ErrorHandler{
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId().equals(restaurantId)) {
                if (calculateDistance(restaurant.getLocation(), user.getLocation()) <= 170)
                    return restaurant;
                else
                    throw new ErrorHandler("403");
            }
        }
        throw new ErrorHandler("404");
    }

    public boolean hasResraurant(String restaurantId){
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId().equals(restaurantId))
                return true;
        }
        return false;
    }

    public Food getFood(String restaurantId, String foodName) throws ErrorHandler {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId().equals(restaurantId)) {
                return restaurant.getFood(foodName);
            }
        }
        throw new ErrorHandler("Error: No \"" + restaurantId +"\" restaurant exists!");
    }

    public String addToCart(String restaurantId, String foodName) throws ErrorHandler{
        if (!user.getShoppingCart().isEmpty()) {
            if (!(user.getShoppingCart().getRestaurantId().equals(restaurantId))) {
                throw new ErrorHandler("403");
            }
        }
        for (Restaurant restaurant : getRestaurants()) {
            if (restaurant.getId().equals(restaurantId)) {
                user.setShoppingCartRestaurant(restaurantId, restaurant.getName());
                Food orderedFood = restaurant.getOrderedFood(foodName);
                if (orderedFood != null) {
                    return user.addToCart(orderedFood);
                } else {
                    throw new ErrorHandler("403");
                }
            }
        }
        throw new ErrorHandler("403");
    }

    public String addPartyFoodToCart(String restaurantId, String partyFoodName) throws ErrorHandler {
        if (!user.getShoppingCart().isEmpty()) {
            if (!(user.getShoppingCart().getRestaurantId().equals(restaurantId))) {
                throw new ErrorHandler("403");
            }
        }
        for (Restaurant restaurant : getRestaurants()) {
            if (restaurant.getId().equals(restaurantId)) {
                user.setShoppingCartRestaurant(restaurantId, restaurant.getName());
                user.setIsFoodParty(true);
                PartyFood orderedFood = foodParty.getOrderedFood(restaurantId, partyFoodName);
                if (orderedFood != null) {
                    return user.addToCart(orderedFood);
                } else {
                    throw new ErrorHandler("403");
                }
            }
        }
        throw new ErrorHandler("403");
    }

    public Map<String, Integer> getCart() throws ErrorHandler {
        return user.getCart();
    }

    public void finalizeOrder() throws ErrorHandler {
        int orderId = user.finalizeOrder(this.foodParty.isPartyFinished());
        findDelivery(orderId);
    }

    public void findDelivery(int orderId){
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

    public String getRecommendedRestaurants() throws ErrorHandler {
        Map<String,Double> scores = new HashMap<>();
        if(restaurants.size() == 0){
            throw new ErrorHandler("Error: Sorry there is no restaurant in Loghme at this time!");
        }
        for (Restaurant restaurant : restaurants) {
            int xDistance = restaurant.getLocation().getX() - user.getLocation().getX();
            int yDistance = restaurant.getLocation().getY() - user.getLocation().getY();
            Double distance = sqrt(pow(xDistance, 2) + pow(yDistance, 2));
            scores.put(restaurant.getName(), restaurant.getScore() / distance);
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

    public String setFoodParty(ArrayList<PartyFood> partyFoods){
        return foodParty.setFoodParty(partyFoods);
    }

    public ArrayList<PartyFood> getFoodParty(){
        return foodParty.getPartyFoods();
    }
}