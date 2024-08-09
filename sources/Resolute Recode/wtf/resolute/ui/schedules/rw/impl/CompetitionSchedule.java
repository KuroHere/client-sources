package wtf.resolute.ui.schedules.rw.impl;

import wtf.resolute.ui.schedules.rw.Schedule;
import wtf.resolute.ui.schedules.rw.TimeType;

public class CompetitionSchedule extends Schedule {
    @Override
    public String getName() {
        return "����������";
    }

    @Override
    public TimeType[] getTimes() {
        return new TimeType[]{TimeType.SEVEN_THIRTY_FIVE, TimeType.FIVE, TimeType.TEN_THIRTY_FIVE, TimeType.THIRTEEN_THIRTY_FIVE, TimeType.SIXTEEN_THIRTY_FIVE, TimeType.NINETEEN_THIRTY_FIVE, TimeType.TWENTY_TWO_THIRTY_FIVE, TimeType.ONE_FORTY_FIVE};
    }
}
