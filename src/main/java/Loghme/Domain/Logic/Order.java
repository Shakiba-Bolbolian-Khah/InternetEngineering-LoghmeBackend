package Loghme.Domain.Logic;

import Loghme.Exceptions.Error404;
import Loghme.Domain.Scheduler.DeliveringTimeManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Order extends ShoppingCart {
    private int id;
    private String deliveryId;
    private OrderState state;
    private LocalDateTime finalizationTime;
    private LocalTime deliveringTime;

//    public Order(String restaurantId, String restaurantName, int totalPayment, boolean isFoodParty, ArrayList<ShoppingCartItem> items, int id, OrderState state) {
//        super(false, restaurantId, restaurantName, totalPayment, isFoodParty, items);
//        this.id = id;
//        this.deliveryId = null;
//        this.state = state;
//        this.finalizationTime = LocalDateTime.now();
//        this.deliveringTime = null;
//    }

    public Order(String restaurantId, String restaurantName, int totalPayment, int isFoodParty, ArrayList<ShoppingCartItem> items, int id, String deliveryId, OrderState state, LocalDateTime finalizationTime, LocalTime deliveringTime) {
        super(false, restaurantId, restaurantName, totalPayment, isFoodParty, items);
        this.id = id;
        this.deliveryId = deliveryId;
        this.state = state;
        this.finalizationTime = finalizationTime;
        this.deliveringTime = deliveringTime;
    }

    public void setDeliveryForOrder(String deliveryId, LocalTime deliveringTime) throws IOException, Error404 {
        this.deliveryId = deliveryId;
        this.state = OrderState.Delivering;
        this.deliveringTime = deliveringTime;
        new DeliveringTimeManager(this);
    }

    public int getId() {
        return id;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public int doGetDeliveringTimeInSeconds() {
        return deliveringTime.getHour()*3600 + deliveringTime.getMinute()*60 + deliveringTime.getSecond();
    }

    public LocalDateTime getFinalizationTime() {
        return finalizationTime;
    }

    public LocalTime getDeliveringTime() {
        return deliveringTime;
    }
}