package Loghme.Domain.Scheduler;

import Loghme.Exceptions.Error404;
import Loghme.Domain.Logic.Order;
import Loghme.Domain.Logic.OrderState;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class DeliveringTimeManager {
    private Timer timer;
    private Order order;

    public DeliveringTimeManager(Order newOrder) throws IOException, Error404 {
        this.order = newOrder;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimeCountDown(), 0, 1000);
    }

    public class TimeCountDown extends TimerTask {
        @Override
        public void run() {
            if (order.getFinalizationTime().until(order.getDeliveringTime(), ChronoUnit.SECONDS) == order.doGetDeliveringTimeInSeconds()) {
                order.setState(OrderState.Done);
                cancelTimer();
            }
        }
    }

    public void cancelTimer() {
        timer.cancel();
    }
}