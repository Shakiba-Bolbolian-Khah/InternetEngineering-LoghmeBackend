package Loghme.Scheduler;

import Loghme.Exceptions.Error403;
import Loghme.Exceptions.Error404;
import Loghme.Model.CommandHandler;
import Loghme.Repository.APIReader;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DeliveryFindingManager {
    private Timer timer;
    private int orderId;

    public DeliveryFindingManager(int seconds, int orderId) {
        this.orderId = orderId;
        timer = new Timer();
        timer.scheduleAtFixedRate(new DeliveryFinder(), 0, seconds*1000);
    }

    public class DeliveryFinder extends TimerTask {
        @Override
        public void run() {
            try {
                String deliveriesInfo = APIReader.getInstance().getDataFromAPI("deliveries");
                if (!deliveriesInfo.equals("[]")) {
                    CommandHandler.getInstance().setLoghmeDeliveries(deliveriesInfo);
                    CommandHandler.getInstance().assignDelivery(orderId);
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