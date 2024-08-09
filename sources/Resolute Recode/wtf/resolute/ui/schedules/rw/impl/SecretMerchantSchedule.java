package wtf.resolute.ui.schedules.rw.impl;


import wtf.resolute.ui.schedules.rw.Schedule;
import wtf.resolute.ui.schedules.rw.TimeType;

public class SecretMerchantSchedule
        extends Schedule {
    @Override
    public String getName() {
        return "������ ��������";
    }

    @Override
    public TimeType[] getTimes() {
        return new TimeType[]{TimeType.FOUR, TimeType.FIVE, TimeType.EIGHT, TimeType.ELEVEN, TimeType.FOURTEEN, TimeType.SEVENTEEN, TimeType.TWENTY, TimeType.TWENTY_THREE};
    }
}
