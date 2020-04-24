package Loghme.PresentationController;

import Loghme.DataSource.CartItemDAO;

import java.util.ArrayList;

public class ShoppingCartDTO {
    private boolean isEmpty;
    private String restaurantId;
    private String restaurantName;
    private int totalPayment;
    private ArrayList<CartItemDAO> items;
    private int userId;

    public ShoppingCartDTO(boolean isEmpty, String restaurantId, String restaurantName, int totalPayment, ArrayList<CartItemDAO> items, int userId) {
        this.restaurantId = restaurantId;
        this.isEmpty = isEmpty;
        this.restaurantName = restaurantName;
        this.totalPayment = totalPayment;
        this.items = items;
        this.userId = userId;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
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

    public ArrayList<CartItemDAO> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItemDAO> items) {
        this.items = items;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
