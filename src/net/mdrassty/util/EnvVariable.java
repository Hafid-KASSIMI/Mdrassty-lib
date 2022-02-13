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

/**
 *
 * @author Sicut
 */
public class EnvVariable {
    
    public static String APPDATADirectory(){
        String workingDirectory = "";
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")) {
            workingDirectory = System.getenv("APPDATA");
        }
        else if (OS.contains("MAC")) {
            workingDirectory = System.getProperty("user.home");
            workingDirectory += "/Library/Application Support";
        }
        else if (OS.contains("NUX")) {
            workingDirectory = System.getProperty("user.home") + "/.config";
        }
        return workingDirectory;
    }
    
    public static String HOMEDirectory(){
        return System.getProperty("user.home");
    }

}
