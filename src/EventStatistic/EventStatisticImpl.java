package EventStatistic;

import Clock.Clock;

import java.util.*;

public class EventStatisticImpl implements EventsStatistic {
    static private final double MS_IN_ONE_HOUR = 3600000;
    static private final double MIN_IN_ONE_HOUR = 60;
    private final Clock clock;

    private static class EventTimeDescriptor {
        long time;

        public EventTimeDescriptor(long time) {
            this.time = time;
        }

        public long getTime() {
            return time;
        }
    }

    private static class EventRpmDescriptor {
        long counter;
        long firstEventTime;

        public EventRpmDescriptor(long firstEventTime) {
            this.counter = 0;
            this.firstEventTime = firstEventTime;
        }

        public EventRpmDescriptor(Date firstEventTime) {
            this(firstEventTime.getTime());
        }

        void inc() {
            ++counter;
        }

        private static double msDeltaToMinutes(long delta) {
            if (delta < MS_IN_ONE_HOUR) {
                return 1;
            }
            return (delta / MS_IN_ONE_HOUR) * MIN_IN_ONE_HOUR;
        }

        public double getRpm(long now) {
            if (now != firstEventTime) {
                double minutes = msDeltaToMinutes(now - firstEventTime);
                return (double) counter / minutes;
            }
            return counter;
        }

        public double getRpm(Date now) {
            return getRpm(now.getTime());
        }
    }

    private final Map<String, EventRpmDescriptor> allRpmStat;
    private final Map<String, List<EventTimeDescriptor>> stat;

    public EventStatisticImpl(Clock clock) {
        this.stat = new HashMap<>();
        this.allRpmStat = new HashMap<>();
        this.clock = clock;
    }

    private void filter(String name) {
        long curDate = clock.getTime();
        stat.getOrDefault(name, new ArrayList<>()).removeIf(o -> curDate - o.getTime() > MS_IN_ONE_HOUR);
    }

    private Double countRpm(List<EventTimeDescriptor> descriptors) {
        return descriptors.size() / MIN_IN_ONE_HOUR;
    }

    @Override
    public void incEvent(String name) {
        var list = stat.getOrDefault(name, new ArrayList<>());
        long curTime = clock.getTime();
        list.add(new EventTimeDescriptor(curTime));
        stat.put(name, list);
        var allStatDescriptor = allRpmStat.getOrDefault(name, new EventRpmDescriptor(curTime));
        allStatDescriptor.inc();
        allRpmStat.put(name, allStatDescriptor);
    }

    @Override
    public void printStatistic() {
        long curTime = clock.getTime();
        for (var event : allRpmStat.entrySet()) {
            System.out.println("Event: " + event.getKey() + " has rpm: " + event.getValue().getRpm(curTime));
        }
    }

    @Override
    public Double getEventStatisticByName(String name) {
        filter(name);
        return countRpm(stat.getOrDefault(name, new ArrayList<>()));
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        Map<String, Double> ans = new HashMap<>();
        for (var pair : stat.entrySet()) {
            filter(pair.getKey());
            if (!stat.getOrDefault(pair.getKey(), new ArrayList<>()).isEmpty()) {
                ans.put(pair.getKey(), countRpm(pair.getValue()));
            }
        }
        return ans;
    }

    public Map<String, Double> getAllRpmStat() {
        Map<String, Double> rmps = new HashMap<>();
        long now = clock.getTime();
        for (var pair : allRpmStat.entrySet()) {
            rmps.put(pair.getKey(), pair.getValue().getRpm(now));
        }
        return rmps;
    }
}
