package Loghme.DataSource;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class OrderDAO {
    private int id;
    private String restaurantId;
    private String restaurantName;
    private int totalPayment;
    private boolean isFoodParty;
    private String deliveryId;
    private String state;
    private LocalDateTime finalizationTime;
    private LocalTime deliveringTime;
    private ArrayList<OrderItemDAO> orderItems = new ArrayList<>();

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

    public boolean isFoodParty() {
        return isFoodParty;
    }

    public void setFoodParty(boolean foodParty) {
        isFoodParty = foodParty;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getFinalizationTime() {
        return finalizationTime;
    }

    public void setFinalizationTime(LocalDateTime finalizationTime) {
        this.finalizationTime = finalizationTime;
    }

    public LocalTime getDeliveringTime() {
        return deliveringTime;
    }

    public void setDeliveringTime(LocalTime deliveringTime) {
        this.deliveringTime = deliveringTime;
    }

    public ArrayList<OrderItemDAO> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem(OrderItemDAO orderItem) {
        this.orderItems.add(orderItem);
    }
}