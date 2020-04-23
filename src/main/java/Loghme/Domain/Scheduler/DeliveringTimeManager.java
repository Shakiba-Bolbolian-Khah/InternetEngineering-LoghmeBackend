package Loghme.Domain.Scheduler;

import Loghme.DataSource.UserRepository;
import Loghme.Exceptions.Error404;
import Loghme.Domain.Logic.Order;
import Loghme.Domain.Logic.OrderState;

import java.io.IOException;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class DeliveringTimeManager {
    private Timer timer;
    private Order order;
    private int userId;

    public DeliveringTimeManager(Order newOrder, int userId) {
        this.order = newOrder;
        this.userId = userId;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimeCountDown(), 0, 1000);
    }

    public class TimeCountDown extends TimerTask {
        @Override
        public void run() {
            if (order.getFinalizationTime().until(order.getDeliveringTime(), ChronoUnit.SECONDS) == order.doGetDeliveringTimeInSeconds()) {
                try {
                    UserRepository.getInstance().setDeliveredState(order.getId(), userId);
                } catch (SQLException e) {
                    System.err.print(e);
                }
                cancelTimer();
            }
        }
    }

    public void cancelTimer() {
        timer.cancel();
    }
}