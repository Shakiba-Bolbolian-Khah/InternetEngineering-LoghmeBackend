package Loghme.Domain.Scheduler;

import Loghme.Domain.Logic.CommandHandler;
import Loghme.DataSource.APIReader;

import java.io.IOException;
import java.sql.SQLException;

public class PartyFoodFinder implements Runnable {
    @Override
    public void run() {
        try {
            String foodPartyInfo = APIReader.getInstance().getDataFromAPI("foodparty");
            CommandHandler.getInstance().doSetFoodParty(foodPartyInfo);
        } catch (IOException | SQLException e) {
            System.err.print(e);
        }
    }
}