package Loghme.Scheduler;

import Loghme.Exceptions.Error404;
import Loghme.Model.CommandHandler;
import Loghme.Model.Order;
import Loghme.Repository.OrderState;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DeliveringTimeManager {
    private Timer timer;
    private Order order;

    public DeliveringTimeManager(int orderId) throws IOException, Error404 {
        this.order = CommandHandler.getInstance().getOrder(orderId);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimeCountDown(), 0, 1000);
    }

    public class TimeCountDown extends TimerTask {
        @Override
        public void run() {
            if (order.getRemainingTimeInSeconds() == 0) {
                order.setState(OrderState.Done);
                cancelTimer();
            }
            order.decreaseRemainingTime(1);
        }
    }

    public void cancelTimer() {
        timer.cancel();
    }
}