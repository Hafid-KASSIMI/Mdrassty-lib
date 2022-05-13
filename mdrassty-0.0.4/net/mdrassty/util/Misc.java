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

package net.mdrassty.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import java.util.function.Function;
import net.mdrassty.object.Student;

public final class Misc {
    
    public static String getLevelAbrev(String groupName) {
        return ( "".equals(groupName) || groupName == null ) ? 
                "" : groupName.substring(0, groupName.lastIndexOf("-"));
    }
    
    public static String getGroupIndex(String groupName) {
        return ( "".equals(groupName) || groupName == null ) ? 
                "" : groupName.substring(groupName.lastIndexOf("-") + 1);
    }
    
    public static Integer getGroupWeight(String groupName) {
        if ( "".equals(groupName) || groupName == null )
            return 0;
        return getLevelWeight(groupName) * 1000000 + getBranchWeight(groupName) + 
                getIntGroupIndex(groupName);
    }
    
    public static Integer getBranchWeight(String groupName) {
        if ( "".equals(groupName) || groupName == null )
            return 0;
        String branches = "APIC-ASCG-LSHF-TCSF-ACSH-ACSE-CSEF-CSPF-ACSP-CSVT-";
        int i = groupName.lastIndexOf("-");
        String portion;
        try {
            portion = groupName.substring(i - 4, i);
        } catch ( IndexOutOfBoundsException ex  ) {
            return 0;
        }
        i = branches.indexOf(portion);
        return  i > 0 ? i  * 100 : 0;
    }
    
    public static Integer getLevelWeight(String groupName) {
        if ( "".equals(groupName) || groupName == null )
            return 0;
        String levels = "1A2A3ATC1B2B";
        return levels.indexOf(groupName.substring(0, 2)) / 2;
    }
    
    public static Integer getIntGroupIndex(String groupName) {
        try {
            return Integer.parseInt(getGroupIndex(groupName));
        } catch ( NumberFormatException nfe ) {
            return 0;
        }
    }
    
    public static Boolean isArabic(String text) {
        return text.matches("[\u0600-\u077F]+");
    }
    
    public static String justLetters(String arText) {
        return arText.replaceAll("[\u0640\u064B-\u065F\u06D6-\u06ED]", "");
    }
    
    public static Function<Student, Double> getMarkFunction(Mark mark, Boolean isACL3) {
        switch ( mark ) {
            case FIRST_SEMESTER:
                return (s) -> s.getS1Mark();
            case SECOND_SEMESTER:
                return (s) -> s.getS2Mark();
            case FIRST_SEMESTER_AVERAGE:
                return isACL3 ? (s) -> s.getS13A() : (s) -> s.getS1Mark();
            default:
                return (s) -> s.getAverage();
        }
    }
    
    public static int generateLocalUId() {
        Properties p = System.getProperties();
        return Math.abs((p.getProperty("os.name") + p.getProperty("os.arch") + 
        p.getProperty("os.version") + p.getProperty("user.name")).hashCode());
    }
    
    public static String generateLocalUId(String suffix) {
        return generateLocalUId() + suffix;
    }
    
    public static Boolean serialize(Object o, String path) {
        try {
            (new ObjectOutputStream(new FileOutputStream(path))).writeObject(o);
            return true;
        } catch (IOException ex) { }
        return false;
    }
    
    public static Object eval(String path) {
        Object o = null;
        try {
            return (new ObjectInputStream(new FileInputStream(path))).readObject();
        } catch (IOException | ClassNotFoundException ex) { }
        return o;
    }
    
    public static HORIZONTAL_ALIGNMENT parseHAlignment(String s) {
        switch ( s ) {
            case "L":
                return HORIZONTAL_ALIGNMENT.LEFT;
            case "R":
                return HORIZONTAL_ALIGNMENT.RIGHT;
            default:
                return HORIZONTAL_ALIGNMENT.CENTER;
        }
    }
    
    public static boolean isDark(int red, int green, int blue) {
        return Math.sqrt(0.299 * (red * red) + 0.587 * (green * green) + 0.114 * (blue * blue)) < 127.5;
    }
}
