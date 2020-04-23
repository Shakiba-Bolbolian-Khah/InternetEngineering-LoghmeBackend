package Loghme.Domain.Logic;

import Loghme.Exceptions.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Location location;
    private int credit;
    private ShoppingCart shoppingCart;
    private ArrayList<Order> orders;

    public User(int id, String firstName, String lastName, String phoneNumber, String email, Location location, int credit, ShoppingCart shoppingCart, ArrayList<Order> orders) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.location = location;
        this.credit = credit;
        this.shoppingCart = shoppingCart;
        this.orders = orders;
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

    public int getId() {
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

    public void setIsFoodParty(int state){
        shoppingCart.setIsFoodParty(state);
    }

    public int isFoodParty(){
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

    public ShoppingCart doGetCart() throws Error404 {
        return this.shoppingCart;
    }

    public int finalizeOrder() throws Error403, Error400, SQLException {
        int totalPayment = shoppingCart.getTotalPayment();
        int orderId = shoppingCart.finalizeOrder(this.credit, orders.size());
        return orderId;
    }
}