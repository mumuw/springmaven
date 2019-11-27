package com.lin.demo.mvc.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class UUIDTest {

    public static void main(String[] args) {
        DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        format.setTimeZone( TimeZone.getTimeZone("UTC") );
        Date date = parseDate( "1601-01-01 00:00:00", format);
        System.out.println( date.getTime() );

        Date date1 = new Date();

        FileTime fileTime = FileTime.date2FileTime(date1);
        System.out.println(fileTime.toString());

        FileTime fileTile = new FileTime( fileTime.getHigh(), fileTime.getLow() );
        Date date2 = fileTile.toDate();
        System.out.printf( "%tF %<tT%n", date2 );
    }

    public static Date parseDate(String str, DateFormat format) {
        Date date = null;
        try {
            date = format.parse( str );
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
