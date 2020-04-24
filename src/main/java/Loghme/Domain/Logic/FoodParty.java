package Loghme.Domain.Logic;

import Loghme.Exceptions.Error403;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class FoodParty {
    private ArrayList<PartyFood> partyFoods;
    private LocalDateTime enteredDate;

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
}