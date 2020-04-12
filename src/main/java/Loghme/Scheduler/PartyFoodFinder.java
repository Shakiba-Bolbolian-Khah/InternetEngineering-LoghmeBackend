package Loghme.Scheduler;

import Loghme.Model.CommandHandler;
import Loghme.Repository.APIReader;

import java.io.IOException;

public class PartyFoodFinder implements Runnable {
    @Override
    public void run() {
        try {
            String foodPartyInfo = APIReader.getInstance().getDataFromAPI("foodparty");
            CommandHandler.getInstance().doSetFoodParty(foodPartyInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}