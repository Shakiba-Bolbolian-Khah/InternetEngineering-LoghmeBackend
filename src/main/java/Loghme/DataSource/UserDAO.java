package Loghme.DataSource;

import Loghme.Domain.Logic.Location;

import java.util.ArrayList;

public class UserDAO {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Location location;
//    private int x;
//    private int y;
    private int credit;
    private CartDAO shoppingCart;
    private ArrayList<OrderDAO> orders = new ArrayList<>();

    public CartDAO getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(CartDAO shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<OrderDAO> getOrders() {
        return orders;
    }

    public void addOrder(OrderDAO order) {
        this.orders.add(order);
    }
}