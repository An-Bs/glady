package com.glady.backend.challenge.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

public class ExpirationDateUtils {

    Predicate<Date> isStillValid = x -> x.before(new Date());

    public static Date geExpirationDate(int expirationMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, expirationMonth);
        int lastDate = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DATE, lastDate);
        return calendar.getTime();
    }

    public static  Date makeNewExpirationDate(Date currentYearLastExpirationDate){
        Calendar c = Calendar.getInstance();
        c.setTime(currentYearLastExpirationDate);
        c.add(Calendar.YEAR, 1);
        return c.getTime();
    }
}
