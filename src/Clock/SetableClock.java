package Clock;

import java.util.Date;

public class SetableClock implements Clock {
    private Date now;
    private long MINUTE = 60000;
    private long HOUR = 3600000;

    public SetableClock() {
        this.now = new Date();
    }

    public SetableClock(int year, int month, int day, int hour, int minutes) {
        this.now = new Date(year, month, day, hour, minutes, 0);
    }

    public void addMillisecond(long ms) {
        assert ms > 0;
        now = new Date(now.getTime() + ms);
    }

    public void addMinute() {
        now = new Date(now.getTime() + MINUTE);
    }

    public void addHour() {
        now = new Date(now.getTime() + HOUR);
    }

    public void setNow() {
        now = new Date();
    }

    @Override
    public long getTime() {
        return now.getTime();
    }
}
