package Scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class FoodPartyManager implements ServletContextListener {
    private static FoodPartyManager instance;
    private ScheduledExecutorService scheduler;

    public static FoodPartyManager getInstance() {
        if(instance == null)
            instance = new FoodPartyManager();
        return instance;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new PartyFoodFinder(), 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}