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
/* 
    Author     : H. KASSIMI
*/

package net.mdrassty.util.pdf;

import java.util.Arrays;

public class AvailableFonts {
    private static final String FONTS_DIR = "/net/mdrassty/resources/fonts/";
    public static final String TIMES = FONTS_DIR + "times.ttf";
    public static final String TIMES_BOLD = FONTS_DIR + "timesbd.ttf";
    public static final String TAHOMA = FONTS_DIR + "tahoma.ttf";
    public static final String TAHOMA_BOLD = FONTS_DIR + "tahomabd.ttf";
    public static final String TRAD_AR = FONTS_DIR + "trad.ttf";
    public static final String TRAD_AR_BOLD = FONTS_DIR + "tradbd.ttf";
    public static final String TIFINAGH = FONTS_DIR + "tifinaghe.ttf";
    public static final String TIFINAGH_BOLD = FONTS_DIR + "tifinaghebd.ttf";
    public static final String MAGHRIBI = FONTS_DIR + "maghribi.ttf";
    public static final String ICOMOON = FONTS_DIR + "icomoon.ttf";
    
    public static final String[] LIST = new String[] {
                "Times New Roman", "Times New Roman [bold]", "Tahoma", "Tahoma [bold]",
                "Traditional Arabic", "Traditional Arabic [bold]", 
                "Arabswell 2", "Tifinagh IRCAM", "Tifinagh IRCAM [bold]"};
    
    public static String getFont(String listItem) {
        return getFont(Arrays.asList(LIST).indexOf(listItem));
    }
    
    public static String getFont(int listIndex) {
        switch ( listIndex ) {
            case 1:
                return TIMES_BOLD;
            case 2:
                return TAHOMA;
            case 3:
                return TAHOMA_BOLD;
            case 4:
                return TRAD_AR;
            case 5:
                return TRAD_AR_BOLD;
            case 6:
                return MAGHRIBI;
            case 7:
                return TIFINAGH;
            case 8:
                return TIFINAGH_BOLD;
            default:
                return TIMES;
        }
    }
}
