package Loghme.PresentationController;

import Loghme.DataSource.PartyFoodDAO;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FoodPartyDTO {
    private ArrayList<PartyFoodDAO> partyFoods;
    private LocalDateTime enteredDate;

    public ArrayList<PartyFoodDAO> getPartyFoods() {
        return partyFoods;
    }

    public void setPartyFoods(ArrayList<PartyFoodDAO> partyFoods) {
        this.partyFoods = partyFoods;
    }

    public LocalDateTime getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(LocalDateTime enteredDate) {
        this.enteredDate = enteredDate;
    }
}
