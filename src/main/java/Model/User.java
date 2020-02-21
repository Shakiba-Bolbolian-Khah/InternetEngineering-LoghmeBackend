package Model;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Location location;
    private int credit;
    private ShoppingCart shoppingCart;
    private ArrayList<Order> orders;

    public User(String id, String firstName, String lastName, String phoneNumber, String email, Location location, int credit, ShoppingCart shoppingCart) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.location = location;
        this.credit = credit;
        this.shoppingCart = shoppingCart;
        this.orders = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setShoppingCartRestaurant(String restaurantId, String restaurantName){
        shoppingCart.setRestaurantName(restaurantId, restaurantName);
    }

    public String addToCart(Food newFood){
        return shoppingCart.addToCart(newFood);
    }

    public Map<String, Integer> getCart() throws ErrorHandler {
        return shoppingCart.getCart();
    }

    public int finalizeOrder(boolean isFoodPartyFinished) throws ErrorHandler {
        int totalPayment = shoppingCart.getTotalPayment();
        Map<String, Integer> result = getCart();
        ShoppingCart order = shoppingCart.finalizeOrder(this.credit, isFoodPartyFinished);
        this.credit -= totalPayment;
        orders.ensureCapacity(orders.size()+1);
        int orderId = orders.size();
        orders.add(new Order(order.getRestaurantId(),order.getRestaurantName(),order.getTotalPayment(),order.isFoodParty()
                ,order.getItems(),orderId, State.Searching));

        return orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsFoodParty(boolean state){
        shoppingCart.setIsFoodParty(state);
    }
}