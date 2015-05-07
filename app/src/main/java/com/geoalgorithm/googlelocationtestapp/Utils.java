package com.geoalgorithm.googlelocationtestapp;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alan Poggetti on 07/05/2015.
 */
public class Utils {

    public static String dateFormatter(Date date){

        long dif = daysBetween(date);

        if(dif>=1){

            if(dif>=365){
                Format sdf = new SimpleDateFormat("H:mm:ss', 'yyyy/MM/dd");

                String formattedDate = sdf.format(date);

                return formattedDate;

            }else
            if(dif>=2){

                Format sdf = new SimpleDateFormat("H:mm:ss', 'yyyy/MM/dd");

                String formattedDate = sdf.format(date);

                return formattedDate.toLowerCase();

            }else{

                Format sdf = new SimpleDateFormat("H:mm:ss', '");

                String formattedDate = sdf.format(date);

                return formattedDate+"Yesterday";
            }

        }else{

            Format sdf = new SimpleDateFormat("H:mm:ss', '");

            String formattedDate = sdf.format(date);

            return formattedDate+"Today";

        }

    }

    private static long daysBetween(Date date){

        Calendar currentDate = Calendar.getInstance();

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        Calendar targetDate = Calendar.getInstance();

        targetDate.setTime(date);

        targetDate.set(Calendar.HOUR_OF_DAY, 0);
        targetDate.set(Calendar.MINUTE, 0);
        targetDate.set(Calendar.SECOND, 0);
        targetDate.set(Calendar.MILLISECOND, 0);

        long dif = currentDate.getTimeInMillis() - targetDate.getTimeInMillis();

        long day = 24 * 60 * 60 * 1000; // hs * min * seg * mil

        long findalDif = dif / day;


        return findalDif;

    }
}
