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

package net.mdrassty.object;

import net.mdrassty.util.Misc;

/**
 *
 * @author Sicut
 */
public final class SchoolInfos {

    private String academy, direction, school, year;

    public SchoolInfos() {
        
    }

    public SchoolInfos(String academy, String direction, String school, String year) {
        setInfos(academy, direction, school, year);
    }
    
    public final void setInfos(String academy, String direction, String school, String year) {
        this.academy = Misc.justLetters(academy);
        this.direction = Misc.justLetters(direction);
        this.school = Misc.justLetters(school);
        this.year = year;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = Misc.justLetters(academy);
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = Misc.justLetters(direction);
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = Misc.justLetters(school);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

        
}
