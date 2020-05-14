package ba.unsa.etf.si.local_server.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

@Service
public class TaskScheduleService {
    private final TaskScheduler taskScheduler;

    private ScheduledFuture<?> mainSync;
    private ScheduledFuture<?> cashRegisterOpen;
    private ScheduledFuture<?> cashRegisterClose;

    public TaskScheduleService(@Qualifier("MyTaskScheduler") TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void scheduleSync(String time, Runnable task) {
        cancelTask(mainSync);
        mainSync = scheduleTask(timeToCron(time), task);
    }

    public void scheduleCashRegisterOpen(String time, Runnable task) {
        cancelTask(cashRegisterOpen);
        cashRegisterOpen = scheduleTask(timeToCron(time), task);
    }

    public void scheduleCashRegisterClose(String time, Runnable task) {
        cancelTask(cashRegisterClose);
        cashRegisterClose = scheduleTask(timeToCron(time), task);
    }

    private String timeToCron(String time) {
        String[] data = time.split(":");
        return String.format("0 %s %s * * *", data[1], data[0]);
    }

    private ScheduledFuture<?> scheduleTask(String cron, Runnable task) {
        CronTrigger cronTrigger = new CronTrigger(cron);
        return taskScheduler.schedule(task, cronTrigger);
    }

    private void cancelTask(ScheduledFuture<?> task) {
        if(task != null) {
            task.cancel(true);
        }
    }

}
