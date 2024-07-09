package com.example.myapplication.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    public static String displayDate(String originalDateString) {
        // Define the original format
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Define the desired format
        DateFormat desiredFormat = DateFormat.getDateInstance();

        String formattedDate = "";
        try {
            // Parse the original date string
            Date date = originalFormat.parse(originalDateString);
            // Format the date into the desired format
            formattedDate = desiredFormat.format(date);
        } catch (ParseException e) {
            Log.d("DATEFORMATERROR", e.getMessage());
        }

        return formattedDate;
    }
}
