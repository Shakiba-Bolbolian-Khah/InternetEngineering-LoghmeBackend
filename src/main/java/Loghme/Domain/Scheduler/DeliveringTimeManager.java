package Loghme.Domain.Scheduler;

import Loghme.DataSource.UserRepository;
import Loghme.Exceptions.Error404;
import Loghme.Domain.Logic.Order;
import Loghme.Domain.Logic.OrderState;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class DeliveringTimeManager {
    private Timer timer;
    private int orderId;
    private int deliveringTime;
    private int userId;

    public DeliveringTimeManager(int orderId, int userId, LocalTime deliveringTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.deliveringTime = deliveringTime.getHour()*3600 + deliveringTime.getMinute()*60 + deliveringTime.getSecond();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimeCountDown(), 0, 1000);
    }

    public class TimeCountDown extends TimerTask {
        @Override
        public void run() {
            try {
                Thread.sleep(deliveringTime * 1000);
                UserRepository.getInstance().setDeliveredState(orderId, userId);
            } catch (SQLException | InterruptedException e) {
                System.err.print(e);
            }
            cancelTimer();
        }
    }

    public void cancelTimer() {
        timer.cancel();
    }
}