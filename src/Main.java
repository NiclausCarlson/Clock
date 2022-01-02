import Clock.NormalClock;
import EventStatistic.EventStatisticImpl;
import EventStatistic.EventsStatistic;

public class Main {
    public static void main(String[] args) {
        EventsStatistic statistic = new EventStatisticImpl(new NormalClock());
        statistic.incEvent("la");
        statistic.incEvent("lala");
        statistic.incEvent("lala");
        statistic.incEvent("la");
        statistic.incEvent("la");
        statistic.incEvent("la");
        statistic.incEvent("lalala");
        statistic.incEvent("lalala");
        statistic.printStatistic();
        var a = statistic.getAllEventStatistic();
        for(var pair: a.entrySet()){
            System.out.println(pair.getKey() + " : " + pair.getValue());
        }

        var b = statistic.getEventStatisticByName("la");
        System.out.println(b);
    }
}
