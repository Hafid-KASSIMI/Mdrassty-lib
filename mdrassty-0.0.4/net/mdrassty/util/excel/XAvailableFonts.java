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

package net.mdrassty.util.excel;

import java.util.Arrays;

public class XAvailableFonts {
    public static final String TIMES = "Times New Roman";
    public static final String TAHOMA = "Tahoma";
    public static final String TRAD_AR = "Traditional Arabic";
    public static final String TIFINAGH = "Tinifaghe-IRCAMprime";
    public static final String MAGHRIBI = "arabswell_2";
    public static final String ICOMOON = "IcoMoon-Free";
    
    public static final String[] LIST = new String[] {
        "Times New Roman", "Tahoma", "Traditional Arabic", "Arabswell 2", "Tifinagh IRCAM"
    };
    
    public static String getFont(String listItem) {
        return getFont(Arrays.asList(LIST).indexOf(listItem));
    }
    
    public static String getFont(int listIndex) {
        switch ( listIndex ) {
            case 1:
                return TAHOMA;
            case 2:
                return TRAD_AR;
            case 3:
                return MAGHRIBI;
            case 4:
                return TIFINAGH;
            default:
                return TIMES;
        }
    }
}
