package EventStatistic;

import Clock.SetableClock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventStatisticImplTest {
    static private final double MIN_IN_ONE_HOUR = 60;
    static private final double MS_IN_ONE_HOUR = 3600000;

    private static double msDeltaToMinutes(long delta) {
        if (delta < MS_IN_ONE_HOUR) {
            return 1;
        }
        return (delta / MS_IN_ONE_HOUR) * MIN_IN_ONE_HOUR;
    }

    EventsStatistic statistic;
    SetableClock setableClock;

    @Before
    public void init() {
        setableClock = new SetableClock(2022, 1, 1, 1, 0);
        statistic = new EventStatisticImpl(setableClock);
    }

    @After
    public void clear() {
        statistic = null;
        setableClock = null;
    }

    @Test
    public void simpleStat() {
        long start = setableClock.getTime();
        statistic.incEvent("A");
        setableClock.addMinute();
        statistic.incEvent("A");
        setableClock.addMinute();
        statistic.incEvent("A");
        setableClock.addMinute();
        setableClock.addMinute();

        assertEquals(3. / MIN_IN_ONE_HOUR, statistic.getEventStatisticByName("A"));
        var stat = statistic.getAllEventStatistic();
        assertEquals(1, stat.size());
        assertEquals(3. / MIN_IN_ONE_HOUR, stat.get("A"));

        var allStat = ((EventStatisticImpl) statistic).getAllRpmStat();
        assertEquals(3. / msDeltaToMinutes(setableClock.getTime() - start), allStat.get("A"));

        setableClock.addHour();
        assertEquals(0., statistic.getEventStatisticByName("A"));
        stat = statistic.getAllEventStatistic();
        assertEquals(0, stat.size());
        assertNull(stat.get("A"));

        allStat = ((EventStatisticImpl) statistic).getAllRpmStat();
        assertEquals(3. / msDeltaToMinutes(setableClock.getTime() - start), allStat.get("A"));

    }

    @Test
    public void setTwoEvents() {
        long start = setableClock.getTime();
        statistic.incEvent("A");
        setableClock.addMinute();
        statistic.incEvent("B");
        setableClock.addMinute();
        setableClock.addMinute();
        statistic.incEvent("A");
        setableClock.addMinute();
        setableClock.addMinute();
        statistic.incEvent("A");
        setableClock.addMinute();
        statistic.incEvent("B");
        statistic.incEvent("B");
        setableClock.addMinute();
        statistic.incEvent("B");
        setableClock.addMinute();

        assertEquals(3. / MIN_IN_ONE_HOUR, statistic.getEventStatisticByName("A"));
        assertEquals(4. / MIN_IN_ONE_HOUR, statistic.getEventStatisticByName("B"));
        var stat = statistic.getAllEventStatistic();
        assertEquals(2, stat.size());
        assertEquals(3. / MIN_IN_ONE_HOUR, stat.get("A"));
        assertEquals(4. / MIN_IN_ONE_HOUR, stat.get("B"));

        var allStat = ((EventStatisticImpl) statistic).getAllRpmStat();
        assertEquals(3. / msDeltaToMinutes(setableClock.getTime() - start), allStat.get("A"));
        assertEquals(4. / msDeltaToMinutes(setableClock.getTime() - start), allStat.get("B"));

    }

    @Test
    public void setEventNearEndOfHour() {
        long start = setableClock.getTime();
        statistic.incEvent("A");
        for (int i = 0; i < 58; ++i) {
            setableClock.addMinute();
        }
        assertEquals(1. / MIN_IN_ONE_HOUR, statistic.getEventStatisticByName("A"));

        statistic.incEvent("A");
        setableClock.addMinute();
        statistic.incEvent("A");
        setableClock.addMinute();
        statistic.incEvent("A");
        setableClock.addMinute();
        assertEquals(3. / MIN_IN_ONE_HOUR, statistic.getEventStatisticByName("A"));

        var allStat = ((EventStatisticImpl) statistic).getAllRpmStat();
        assertEquals(4. / msDeltaToMinutes(setableClock.getTime() - start), allStat.get("A"));
    }

}