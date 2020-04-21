package Loghme.Domain.Scheduler;

import Loghme.Exceptions.Error403;
import Loghme.Exceptions.Error404;
import Loghme.Domain.Logic.CommandHandler;
import Loghme.Domain.Logic.Order;
import Loghme.DataSource.APIReader;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DeliveryFindingManager {
    private Timer timer;
    private Order order;

    public DeliveryFindingManager(int seconds, Order order) {
        this.order = order;
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
                    CommandHandler.getInstance().assignDelivery(order);
                    cancelTimer();
                }
            } catch (IOException | Error404 | Error403 e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelTimer() {
        timer.cancel();
    }
}