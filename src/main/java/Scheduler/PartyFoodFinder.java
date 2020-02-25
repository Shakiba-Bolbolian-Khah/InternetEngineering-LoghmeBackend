package Scheduler;

import Model.CommandHandler;
import Repository.APIReader;

import java.io.IOException;

public class PartyFoodFinder implements Runnable {
    @Override
    public void run() {
        try {
            String foodPartyInfo = APIReader.getInstance().getDataFromAPI("foodparty");
            CommandHandler.getInstance().setFoodParty(foodPartyInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}