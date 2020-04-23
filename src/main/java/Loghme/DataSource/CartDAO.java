package Loghme.DataSource;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CartDAO {
    private boolean isEmpty;
    private String restaurantId;
    private String restaurantName;
    private int totalPayment;
    private int isFoodParty;
    private LocalDateTime firstPartyFoodEnteredTime;
    private ArrayList<CartItemDAO> items = new ArrayList<>();

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

    public int isFoodParty() {
        return isFoodParty;
    }

    public void setFoodParty(int foodParty) {
        isFoodParty = foodParty;
    }

    public LocalDateTime getFirstPartyFoodEnteredTime() {
        return firstPartyFoodEnteredTime;
    }

    public void setFirstPartyFoodEnteredTime(LocalDateTime firstPartyFoodEnteredTime) {
        this.firstPartyFoodEnteredTime = firstPartyFoodEnteredTime;
    }

    public ArrayList<CartItemDAO> getItems() {
        return items;
    }

    public void addItem(CartItemDAO item) {
        this.items.add(item);
    }
}
