package Loghme.Model;

import Loghme.Exceptions.Error404;
import Loghme.Repository.OrderState;
import Loghme.Scheduler.DeliveringTimeManager;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

public class Order extends ShoppingCart {
    private int id;
    private String deliveryId;
    private OrderState state;
    private LocalTime remainingTime;

    public Order(String restaurantId, String restaurantName, int totalPayment, boolean isFoodParty, ArrayList<ShoppingCartItem> items, int id, OrderState state) {
        super(true, restaurantId, restaurantName, totalPayment, isFoodParty, items);
        this.id = id;
        this.deliveryId = null;
        this.state = state;
        this.remainingTime = null;
    }

    public void setDeliveryForOrder(String deliveryId, LocalTime remainingTime) throws IOException, Error404 {
        this.deliveryId = deliveryId;
        this.state = OrderState.Delivering;
        this.remainingTime = remainingTime;
        new DeliveringTimeManager(id);
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

    public void decreaseRemainingTime(int seconds) {
        this.remainingTime = this.remainingTime.minusSeconds(seconds);
    }

    public int doGetRemainingTimeInSeconds() {
        return remainingTime.getHour()*3600 + remainingTime.getMinute()*60 + remainingTime.getSecond();
    }

    public LocalTime getRemainingTime() {
        return remainingTime;
    }
}