package Loghme.Model;

import Loghme.Exceptions.Error403;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class FoodParty {
    ArrayList<PartyFood> partyFoods;
    LocalDateTime enteredDate;

    public FoodParty() {
        this.partyFoods = new ArrayList<>();
        this.enteredDate = null;
    }

    public String setFoodParty(ArrayList<PartyFood> newPartyFoods){
        partyFoods.clear();
        partyFoods = newPartyFoods;
        enteredDate = LocalDateTime.now();
        return "addPartyFood: 200";
    }

    public ArrayList<PartyFood> getPartyFoods() {
        return partyFoods;
    }

    public LocalDateTime getEnteredDate() {
        return enteredDate;
    }

    public boolean isPartyFinished(LocalDateTime shoppingCartTime){
        LocalDateTime now = LocalDateTime.now();
        if(enteredDate != null && shoppingCartTime != null)
            return (shoppingCartTime.until( now, ChronoUnit.SECONDS) > 60);
        return false;
    }

    public PartyFood getOrderedFood(String restaurantId, String foodName) throws Error403 {
        for (PartyFood partyFood : partyFoods) {
            if ((partyFood.getRestaurantId().equals(restaurantId)) && (partyFood.getName().equals(foodName))) {
                if(partyFood.getCount() > 0){
                    partyFood.decreaseCount();
                    return partyFood;
                }
                else {
                    throw new Error403("Error: Sorry! "+foodName+" is over!");
                }
            }
        }
        return null;
    }
}