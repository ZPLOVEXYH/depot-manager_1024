package cn.samples.depot.common.utils;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yhllo on 2018/5/8.
 */
public class TimerTaskUtil {
    private static List<Timer> timers = Lists.newArrayList();

    /**
     * 执行一个定时任务
     *
     * @param task      任务
     * @param firstTime 首次执行时间
     * @param period    执行间隔
     */
    public static void startTimerTask(TimerTask task, Date firstTime, long period) {
        Timer timer = new Timer();
        timer.schedule(task, firstTime, period);
        timers.add(timer);
    }

    public static void cancelTimer() {
        if (!CollectionUtils.isEmpty(timers)) {
            for (Timer timer : timers) {
                timer.cancel();
            }
            timers.clear();
        }
    }
}
