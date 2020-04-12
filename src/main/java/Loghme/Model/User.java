package Loghme.Model;

import Loghme.Exceptions.*;
import Loghme.Repository.OrderState;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Location getLocation() {
        return location;
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

    public String getId() {
        return id;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public Order getOrder(int orderId) throws Error404 {
        for(Order order: orders){
            if(order.getId() == orderId){
                return order;
            }
        }
        throw new Error404("Error: There is no order with ID: "+orderId+" in system right now!");
    }

    public void setIsFoodParty(boolean state){
        shoppingCart.setIsFoodParty(state);
    }

    public boolean isFoodParty(){
        return shoppingCart.isFoodParty();
    }

    public void setTimeForShoppingCart(LocalDateTime enteredTime){
        shoppingCart.setFirstPartyFoodEnteredTime(enteredTime);
    }

    public LocalDateTime getShoppingCartTime(){
        return shoppingCart.getFirstPartyFoodEnteredTime();
    }

    public void setShoppingCartRestaurant(String restaurantId, String restaurantName){
        shoppingCart.setRestaurantName(restaurantId, restaurantName);
    }

    public String addToCart(Food newFood, boolean isPartyFood){
        return shoppingCart.addToCart(newFood, isPartyFood);
    }

    public String deleteFromCart(String foodName) throws Error404 {
        return shoppingCart.deleteFromCart(foodName);
    }

    public ShoppingCart doGetCart() throws Error404 {
        return this.shoppingCart;
    }

    public Order finalizeOrder(boolean isFoodPartyFinished) throws Error403, Error400 {
        int totalPayment = shoppingCart.getTotalPayment();
        ShoppingCart order = shoppingCart.finalizeOrder(this.credit, isFoodPartyFinished);
        this.credit -= totalPayment;
        orders.ensureCapacity(orders.size()+1);
        int orderId = orders.size();
        Order newOrder = new Order(order.getRestaurantId(),order.getRestaurantName(),order.getTotalPayment(),order.isFoodParty()
                ,order.getItems(),orderId, OrderState.Searching);
        orders.add(newOrder);

        return newOrder;
    }
}