package pl.Guzooo.KurozwekiAnywhere;

import java.util.Calendar;

public class UtilsCalendar {

    public static String getTodayWithTime(){ //TODO:zera dopisywać jak jest jedna liczba; miesiąc plus jeden

        Calendar c = Calendar.getInstance();
        String date = "";
        date += c.get(Calendar.DAY_OF_MONTH) + ".";
        date += c.get(Calendar.MONTH) + ".";
        date += c.get(Calendar.YEAR) + " ";
        date += c.get(Calendar.HOUR_OF_DAY) + ":";
        date += c.get(Calendar.MINUTE);
        return date;
    }
}
