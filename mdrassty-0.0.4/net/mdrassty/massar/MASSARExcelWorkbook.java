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

package net.mdrassty.massar;


import javafx.beans.property.SimpleIntegerProperty;
import net.mdrassty.object.Group;
import net.mdrassty.util.excel.XLSXWorkbook;

/**
 *
 * @author Surfer
 * @updated by Sicut
 */
public abstract class MASSARExcelWorkbook extends XLSXWorkbook {
    
    protected final SimpleIntegerProperty processedStudents = new SimpleIntegerProperty(0);
    protected final SimpleIntegerProperty processedGroups = new SimpleIntegerProperty(0);
    
    public abstract boolean isItFromMassar();
    
    public abstract void loadGroupInfos(Group grp);
    
    public abstract String getGroup();
    
    public abstract int getUid();

    public SimpleIntegerProperty getProcessedStudents() {
        return processedStudents;
    }

    public SimpleIntegerProperty getProcessedGroups() {
        return processedGroups;
    }
    
    public void passGroup() {
        processedGroups.set(processedGroups.get() + 1);
    }
}
