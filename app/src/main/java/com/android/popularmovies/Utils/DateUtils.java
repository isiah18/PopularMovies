package com.android.popularmovies.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Isingh930 on 9/18/17.
 */

public final class DateUtils {

    public static String stringToDate(String format, String date){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
            Date parsedDate = formatter.parse(date);
            String formattedDate = formatter.format(parsedDate);
            return formattedDate;
        }catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }
}
