package EventStatistic;

import java.util.Map;

public interface EventsStatistic {
    void incEvent(String name);

    void printStatistic();

    Double getEventStatisticByName(String name);

    Map<String, Double> getAllEventStatistic();
}
