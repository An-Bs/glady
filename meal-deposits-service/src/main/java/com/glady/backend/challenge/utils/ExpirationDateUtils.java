package com.glady.backend.challenge.utils;

import java.util.Calendar;
import java.util.Date;

public class ExpirationDateUtils {

    private ExpirationDateUtils(){
    }
    public static Date geExpirationDate(int expirationMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, expirationMonth);
        int lastDate = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DATE, lastDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static  Date makeNewExpirationDate(Date currentYearLastExpirationDate){
        Calendar c = Calendar.getInstance();
        c.setTime(currentYearLastExpirationDate);
        c.add(Calendar.YEAR, 1);
        return c.getTime();
    }
}
