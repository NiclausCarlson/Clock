package Clock;

import java.util.Date;

public class NormalClock implements Clock {
    @Override
    public long getTime() {
        return (new Date()).getTime();
    }
}
