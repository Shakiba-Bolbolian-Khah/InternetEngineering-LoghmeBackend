package Scheduler;

import Exceptions.Error403;
import Exceptions.Error404;
import Model.CommandHandler;
import Repository.APIReader;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DeliveryManager {
    private Timer timer;
    private int orderId;

    public DeliveryManager(int seconds, int orderId) {
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