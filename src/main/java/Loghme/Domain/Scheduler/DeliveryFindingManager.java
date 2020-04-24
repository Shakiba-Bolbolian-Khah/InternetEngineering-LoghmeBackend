package Loghme.Domain.Scheduler;

import Loghme.Exceptions.Error403;
import Loghme.Exceptions.Error404;
import Loghme.Domain.Logic.CommandHandler;
import Loghme.Domain.Logic.Order;
import Loghme.DataSource.APIReader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class DeliveryFindingManager {
    private Timer timer;
    private int orderId;
    private int userId;

    public DeliveryFindingManager(int seconds, int orderId, int userId) {
        this.orderId = orderId;
        this.userId = userId;
        timer = new Timer();
        timer.scheduleAtFixedRate(new DeliveryFinder(), 0, seconds*1000);
    }

    public class DeliveryFinder extends TimerTask {
        @Override
        public void run() {
            try {
                String deliveriesInfo = APIReader.getInstance().getDataFromAPI("deliveries");
                if (!deliveriesInfo.equals("[]")) {
                    CommandHandler.getInstance().doSetLoghmeDeliveries(deliveriesInfo);
                    CommandHandler.getInstance().assignDelivery(orderId, userId);
                    cancelTimer();
                }
            } catch (IOException | Error404 | Error403 | SQLException e) {
                System.err.print(e);
            }
        }
    }

    public void cancelTimer() {
        timer.cancel();
    }
}