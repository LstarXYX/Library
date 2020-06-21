package utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author L.star
 * @date 2020/6/4 20:22
 */
public class CountDays {
    public static int daysOfTwo(Date pastDay,Date lastDay){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDay);
        int last = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(pastDay);
        int past = calendar.get(Calendar.DAY_OF_YEAR);
        return past - last;
    }
}
