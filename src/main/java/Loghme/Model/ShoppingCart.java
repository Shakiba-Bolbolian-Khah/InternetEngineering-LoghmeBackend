package Loghme.Model;

import Loghme.Exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.*;

public class ShoppingCart {
    private boolean isEmpty;
    private String restaurantId;
    private String restaurantName;
    private int totalPayment;
    private boolean isFoodParty;
    private LocalDateTime firstPartyFoodEnteredTime;
    private ArrayList<ShoppingCartItem> items;

    public ShoppingCart(boolean isEmpty) {
        this.isEmpty = isEmpty;
        this.items = new ArrayList<>();
        this.totalPayment = 0;
        this.isFoodParty = false;
        this.firstPartyFoodEnteredTime = null;
    }

    public ShoppingCart(boolean isEmpty, String restaurantId, String restaurantName, int totalPayment, boolean isFoodParty, ArrayList<ShoppingCartItem> items) {
        this.isEmpty = isEmpty;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.totalPayment = totalPayment;
        this.isFoodParty = isFoodParty;
        this.items = items;
        this.firstPartyFoodEnteredTime = null;
    }

    public LocalDateTime getFirstPartyFoodEnteredTime() {
        return firstPartyFoodEnteredTime;
    }

    public void setFirstPartyFoodEnteredTime(LocalDateTime firstPartyFoodEnteredTime) {
        this.firstPartyFoodEnteredTime = firstPartyFoodEnteredTime;
    }

    public boolean isFoodParty() {
        return isFoodParty;
    }

    public void setIsFoodParty(boolean foodParty) {
        isFoodParty = foodParty;
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

    public String getRestaurantName() {
        return restaurantName;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setRestaurantName(String restaurantId, String restaurantName) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.isEmpty = false;
    }

    public ArrayList<ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ShoppingCartItem> items) {
        this.items = items;
    }

    public int contain(Food food){
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getFood().equals(food)){
                return i;
            }
        }
        return -1;
    }

    public int contains(String foodName){
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getFood().getName().equals(foodName)){
                return i;
            }
        }
        return -1;
    }

    public String addToCart(Food newFood, boolean isPartyFood){
        int foodIndex = contain(newFood);
        if(foodIndex == -1){
            items.add(new ShoppingCartItem(newFood,1, newFood.getPrice(), isPartyFood));
        }
        else{
            items.get(foodIndex).increaseNumber();
        }
        calculateTotalPayment(newFood.getPrice());
        return "\""+newFood.getName()+"\" has been added to your cart successfully!";
    }

    public String deleteFromCart(String foodName) throws Error404 {
        int foodIndex = contains(foodName);
        if(foodIndex == -1){
            throw new Error404("Error: There is no food with name "+foodName+" in your cart to delete.");
        }
        else {
            items.get(foodIndex).decreaseNumber();
            if (items.get(foodIndex).getNumber() == 0){
                items.remove(foodIndex);
                if(items.size() == 0)
                    isEmpty = true;
                return "\""+foodName+"\" has been deleted from your cart successfully!";
            }
            else{
                return "\""+foodName+"\" number has been decreased in your cart successfully!";
            }
        }
    }

    public ArrayList<ShoppingCartItem> doGetCart() throws Error404 {
        if(isEmpty)
            throw new Error404("Error: There is nothing to show in your cart!");
        return items;
    }

    public void calculateTotalPayment(int newPrice) {
        totalPayment += newPrice;
    }

    public ShoppingCart finalizeOrder(int userCredit, boolean isFoodPartyFinished) throws Error403, Error400 {
        if(isEmpty){
            throw new Error400("Error: There is nothing in your cart to be finalized!");
        }
        if(isFoodParty && isFoodPartyFinished){
            clearCart();
            throw new Error403("Error: You ordered from food party! Food party time is over!!");
        }
        if (userCredit >= totalPayment){
            ShoppingCart order = copyCart(this);
            clearCart();
            return order;
        }
        else
            throw new Error400("Error: Not enough credit!");
    }

    private static ShoppingCart copyCart(ShoppingCart cart) {
        Gson gson = new GsonBuilder().create();
        String orderInStringForm = gson.toJson(cart);
        return gson.fromJson(orderInStringForm, ShoppingCart.class);
    }

    public void clearCart(){
        items.clear();
        totalPayment = 0;
        isEmpty = true;
        isFoodParty = false;
        firstPartyFoodEnteredTime = null;
    }
}
