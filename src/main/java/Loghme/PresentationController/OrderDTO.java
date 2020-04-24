package Loghme.PresentationController;

import Loghme.DataSource.CartDAO;
import Loghme.DataSource.CartItemDAO;
import Loghme.DataSource.OrderItemDAO;

import java.util.ArrayList;

public class OrderDTO {
    private String state;
    private int id;
    private String restaurantId;
    private String restaurantName;
    private int totalPayment;
    private int userId;
    private ArrayList<OrderItemDAO> items;

    public OrderDTO(String state, int id, String restaurantId, String restaurantName, int totalPayment, int userId, ArrayList<OrderItemDAO> items) {
        this.state = state;
        this.id = id;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.totalPayment = totalPayment;
        this.userId = userId;
        this.items = items;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public ArrayList<OrderItemDAO> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderItemDAO> items) {
        this.items = items;
    }
}
