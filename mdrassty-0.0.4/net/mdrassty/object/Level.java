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

package net.mdrassty.object;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.mdrassty.util.DATA_TYPE;
import net.mdrassty.util.Mark;
import net.mdrassty.util.Misc;

public class Level extends DataObject {
    private final ArrayList<Group> groups;
    private String name, shortName;
    private Integer weight;
    private School school;
    private Boolean ACL3;
    
    public Level(String name) {
        super(DATA_TYPE.LEVEL);
        groups = new ArrayList();
        this.name = name;
        weight = null;
        ACL3 = false;
        shortName = "";
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Group addGroup(String groupName){
        Group grp = new Group(groupName);
        grp.setLevel(this);
        groups.add(grp);
        if ( weight == null ) {
            weight = Misc.getLevelWeight(groupName);
            ACL3 = weight == 2;
        }
        if ( shortName.equals("") )
            shortName = Misc.getLevelAbrev(groupName);
        return grp;
    }
    
    public String getName() {
        return this.name;
    }

    public String getShortName() {
        return shortName;
    }
    
    public int getGroupsCount() {
        return groups.size();
    }
    
    public ArrayList<Group> getGroups() {
        return new ArrayList(groups.stream().sorted((g1, g2) -> g1.getIndex().compareTo(g2.getIndex())).collect(Collectors.toList()));
    }
    
    public Group getGroup(String groupName) {
        Optional<Group> grp = groups.stream().filter(g -> groupName.equalsIgnoreCase(g.getName())).findFirst();
        return grp.isPresent() ? grp.get() : null;
    }
    
    public Group getGroup(int index) {
        if ( index >= groups.size() ) {
            return null;
        }
        return groups.get(index);
    }
    
    public Integer getWeight() {
        return weight;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
        uid = school.getUId();
    }

    public String getGroupsPattern() {
        return groups.stream().sorted((g1, g2) -> Integer.compare(g2.getWeight(), g1.getWeight())).map(grp -> "(" + grp.getName() + ")").collect(Collectors.joining("|"));
    }
    
    public void calculateRanks(Mark mark) {
        Iterator<Student> it;
        ArrayList<Student> students = new ArrayList();
        Function<Student, Double> func = Misc.getMarkFunction(mark, ACL3);
        Student stu;
        int rank = 0;
        Double dMark = -100.0;
        groups.forEach(g -> {
            students.addAll(g.getStudents());
        });
        it = students.stream().sorted((s1, s2) -> func.apply(s2).compareTo(func.apply(s1))).iterator();
        while ( it.hasNext() ) {
            stu = it.next();
            if ( !dMark.equals(func.apply(stu)) )
                rank++;
            stu.setLevelRank(rank);
        }
    }

    public Boolean isACL3() {
        return ACL3;
    }
    
    public int getGroupsMaxSize() {
        return groups.stream().map(g -> g.getStudentsCount()).max(Comparator.naturalOrder()).get();
    }

    @Override
    public String toString() {
        return name;
    }
}
