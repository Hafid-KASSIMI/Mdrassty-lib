/*
 * Copyright (C) 2020 Sicut
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.mdrassty.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sicut
 */
public class CustomDate {
    public static String ERROR = "##";
    public static enum DATE_FIELD {DAY, WEEK, MONTH, YEAR};
    // yyyy-MM-dd
    public static Pattern getIsoDatePattern() {
        return Pattern.compile("^(\\d{2,4})-(\\d{1,2})-(\\d{1,2})$");
    }
    
    // yyyy-MM-dd : yyyy -> 0...9999, MM -> 1...12, dd -> 1...30
    public static Pattern getPreciseHijrahDatePattern() {
        return Pattern.compile("^(\\d{1,4})-(([0]?[1-9]{1})|(1[0-2]{1}))-(([0]?[1-9]{1})|([1-2]{1}\\d{1})|(30))$");
    }
    
    // yyyy-MM-dd
    public static String getIsoDateFormat() {
        return "yyyy-MM-dd";
    }
    
    // dd-MM-yyyy
    public static Pattern getChoosenDatePattern() {
        return Pattern.compile("^(\\d{1,2})-(\\d{1,2})-(\\d{2,4})$");
    }
    
    // dd-MM-yyyy
    public static String getChoosenDateFormat() {
        return "dd-MM-yyyy";
    }
    
    // convert choosenDateFormat to isoDateFormat and vice-versa
    public static String toggleDateFormat(String dt) {
        Matcher m = getIsoDatePattern().matcher(dt);
        if ( m.find() )
            return m.group(3) + "-" + m.group(2) + "-" + m.group(1);
        else {
            m = getChoosenDatePattern().matcher(dt);
            return m.find() ? m.group(3) + "-" + m.group(2) + "-" + m.group(1) : "##";
        }
    }
    
    // convert HijrahDate to isoDate and vice-versa
    public static String getHijrahDate(String isoDate) {
        try {
            LocalDate dt = LocalDate.parse(isoDate, DateTimeFormatter.ofPattern(getIsoDateFormat()));
            HijrahDate hd = HijrahDate.from(dt);
            return hd.format(DateTimeFormatter.ofPattern(getIsoDateFormat()));
        }
        catch( DateTimeException | IllegalArgumentException e ) {
            return ERROR;
        }
    }
    
    private static boolean isInHijrahCalendar(int year, int month, int day) {
        try {
            LocalDate ld = LocalDate.from(HijrahDate.of(year, month, day));
            return true;
        }
        catch( DateTimeException | IllegalArgumentException e ) {
            return false;
        }
    }
    
    // convert isoDate to HijrahDate and vice-versa
    public static String getIsoDate(String hijDate) {
        Matcher m = getIsoDatePattern().matcher(hijDate);
        if ( m.find() ) {
            try {
                int year = Integer.parseInt(m.group(1));
                int month = Integer.parseInt(m.group(2));
                int day = Integer.parseInt(m.group(3));
                if ( isInHijrahCalendar(year, month, day) )
                    return LocalDate.from(HijrahDate.of(year, month, day)).format(DateTimeFormatter.ofPattern(getIsoDateFormat()));
                else
                    return LocalDate.from(HijrahDate.of(year, month, --day)).format(DateTimeFormatter.ofPattern(getIsoDateFormat()));
            }
            catch( DateTimeException | IllegalArgumentException e ) {
                
            }
        }
        return ERROR;
    }
    
    // validate HijrahDate
    public static boolean validateHijrahDate(String hijDate) {
        Matcher m = getPreciseHijrahDatePattern().matcher(hijDate);
        return m.matches();
    }
    
    // validate IsoDate
    public static boolean validateIsoDate(String isoDate) {
        Matcher m = getIsoDatePattern().matcher(isoDate);
        if ( m.find() ) {
            try {
                LocalDate tmp = LocalDate.of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
                return true;
            }
            catch( DateTimeException | IllegalArgumentException e ) {
            }
        }
        return false;
    }
    
    // compare 2 HijrahDates
    // Returns endDate if startDate is inferior
    // Returns startDate else
    public static String compareHijrahDates(String startDate, String endDate) {
        Matcher m;
        int stDtSum = 0;
        int endDtSum = 0;
        m = getPreciseHijrahDatePattern().matcher(startDate);
        if ( m.find() ) {
            try {
                stDtSum = Integer.parseInt(m.group(1)) * 355 + Integer.parseInt(m.group(2)) * 30 + Integer.parseInt(m.group(5));
            }
            catch ( NumberFormatException e ) {}
        }
        
        m = getPreciseHijrahDatePattern().matcher(endDate);
        if ( m.find() ) {
            try {
                endDtSum = Integer.parseInt(m.group(1)) * 355 + Integer.parseInt(m.group(2)) * 30 + Integer.parseInt(m.group(5));
            }
            catch ( NumberFormatException e ) {}
        }            
        return ( ( endDtSum >= stDtSum ) ? endDate : startDate );
    }
    
    public static String compareIsoDates(String startDate, String endDate) {
        Matcher m = getIsoDatePattern().matcher(startDate);
        LocalDate stDt, endDt;
        stDt = endDt = LocalDate.now();
        if ( m.find() ) {
            try {
                stDt = LocalDate.of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
            }
            catch( DateTimeException | IllegalArgumentException e ) {
            }
        }
        m = getIsoDatePattern().matcher(endDate);
        if ( m.find() ) {
            try {
                endDt = LocalDate.of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
            }
            catch( DateTimeException | IllegalArgumentException e ) {
            }
        }
        return ( ( endDt.isAfter(stDt) ) ? endDate : startDate );
    }
    
    public static String procHijrahDate(String hijDate, int days) {
        Matcher m;
        m = getPreciseHijrahDatePattern().matcher(hijDate);
        if ( m.find() ) {
            try {
                int day = Integer.parseInt(m.group(5)) + days;
                int month = Integer.parseInt(m.group(2));
                int year = Integer.parseInt(m.group(1));
                if ( day > 30 ) {
                    month += day / 30;
                    day = day % 30;
                }
                if ( month > 12 ) {
                    year += month / 12;
                    month = month % 12;
                }
                return year + "-" + ( ( month < 10 ) ? "0" : "" ) + month + "-" + ( ( day < 10 ) ? "0" : "" ) + day;
            }
            catch ( NumberFormatException e ) {}
        }
        return hijDate;
    }
    
    public static String procHijrahDate(String hijDate, int count, DATE_FIELD df) {
        switch ( df ) {
            case DAY:
                return procHijrahDate(hijDate, count);
            case WEEK:
                return procHijrahDate(hijDate, count * 7);
            case MONTH:
                return procHijrahDate(hijDate, count * 30);
            case YEAR:
                return procHijrahDate(hijDate, count * 360);
        }
        return hijDate;
    }
    
    public static String advanceHijrahDate(String hijDate, int days) {
        Matcher m;
        m = getPreciseHijrahDatePattern().matcher(hijDate);
        if ( m.find() ) {
            try {
                int day = Integer.parseInt(m.group(5)) - days;
                int month = Integer.parseInt(m.group(2));
                int year = Integer.parseInt(m.group(1));
                if ( day < 1 ) {
                    month += day / 30 - 1;
                    day = 30 + day % 30;
                }
                if ( month < 1 ) {
                    year += month / 12 - 1;
                    month = 12 + month % 12;
                    year = ( year < 0 ) ? 0 : year;
                }
                return year + "-" + ( ( month < 10 ) ? "0" : "" ) + month + "-" + ( ( day < 10 ) ? "0" : "" ) + day;
            }
            catch ( NumberFormatException e ) {}
        }
        return hijDate;
    }
    
    public static String procIsoDate(String isoDate, int days) {
        Matcher m = getIsoDatePattern().matcher(isoDate);
        if ( m.find() ) {
            try {
                return LocalDate
                        .of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)))
                        .plusDays(days)
                        .format(DateTimeFormatter.ofPattern(getIsoDateFormat()));
            }
            catch( DateTimeException | IllegalArgumentException e ) {
            }
        }
        return isoDate;
    }
    
    public static String procIsoDate(String isoDate, int count, DATE_FIELD df) {
        Matcher m = getIsoDatePattern().matcher(isoDate);
        if ( m.find() ) {
            try {
                switch( df ) {
                    case DAY:
                        return LocalDate
                            .of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)))
                            .plusDays(count)
                            .format(DateTimeFormatter.ofPattern(getIsoDateFormat()));
                    case WEEK:
                        return LocalDate
                            .of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)))
                            .plusWeeks(count)
                            .format(DateTimeFormatter.ofPattern(getIsoDateFormat()));
                    case MONTH:
                        return LocalDate
                            .of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)))
                            .plusMonths(count)
                            .format(DateTimeFormatter.ofPattern(getIsoDateFormat()));
                    case YEAR:
                        return LocalDate
                            .of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)))
                            .plusYears(count)
                            .format(DateTimeFormatter.ofPattern(getIsoDateFormat()));
                }
            }
            catch( DateTimeException | IllegalArgumentException e ) {
            }
        }
        return isoDate;
    }
    
    public static String advanceIsoDate(String isoDate, int days) {
        Matcher m = getIsoDatePattern().matcher(isoDate);
        if ( m.find() ) {
            try {
                return LocalDate
                        .of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)))
                        .minusDays(days)
                        .format(DateTimeFormatter.ofPattern(getIsoDateFormat()));
            }
            catch( DateTimeException | IllegalArgumentException e ) {
            }
        }
        return isoDate;
    }
    
}
