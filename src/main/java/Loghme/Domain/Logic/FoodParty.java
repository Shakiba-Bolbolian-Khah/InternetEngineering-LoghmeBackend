package Loghme.Domain.Logic;

import Loghme.Exceptions.Error403;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class FoodParty {
    ArrayList<PartyFood> partyFoods;
    LocalDateTime enteredDate;

    public FoodParty(LocalDateTime enteredDate, ArrayList<PartyFood> partyFoods) {
        this.partyFoods = partyFoods;
        this.enteredDate = enteredDate;
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
            return (shoppingCartTime.until(now, ChronoUnit.MINUTES) > 30);
        return false;
    }

    public void increaseFoodCount(String restaurantId, String foodName) {
        for (PartyFood partyFood : partyFoods) {
            if((partyFood.getRestaurantId().equals(restaurantId))&&(partyFood.getName().equals(foodName))) {
                partyFood.increaseCount();
            }
        }
    }

    public PartyFood doGetOrderedFood(String restaurantId, String foodName) throws Error403 {
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