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

import net.mdrassty.util.SEMESTER;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.mdrassty.util.DATA_TYPE;
import net.mdrassty.util.Mark;
import net.mdrassty.util.Misc;

public class Group extends DataObject {
    protected final ArrayList<Student> students;
    protected final HashMap<Integer, String> teachers = new HashMap();
    protected String name;
    protected Level level;
    private int girls;
    private final int weight;
    
    public Group(String name) {
        super(DATA_TYPE.GROUP);
        students = new ArrayList();
        this.name = "".equals(name) ? "##-#" : name;
        girls = 0;
        weight = Misc.getGroupWeight(name);
    }
    
    public void addStudent(Student stu){
        students.add(stu);
        girls += stu.isFemale() ? 1 : 0;
    }
    
    public void sort() {
        students.sort((s1, s2) -> {
            double n1, n2;
            try {
                n1 = s1.getNum();
                n2 = s2.getNum();
            }
            catch( NumberFormatException ex) {
                return 0;
            }
            return (n1 > n2) ? 1 : -1;
        });
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getInitials() {
        return this.name.substring(0, 2);
    }
    
    public String getLevelName() {
        return level.getName();
    }

    public int getFemaleStudentsCount() {
        return girls;
    }

    public int getGirlStudentsCount() {
        return girls;
    }
    
    public int getStudentsCount() {
        return students.size();
    }
    
    public Student getStudent(int index) {
        if ( index >= students.size() ) return null;
        return students.get(index);
    }
    
    public Student getStudent(String code) {
        Optional<Student> op = students.stream().filter(stu -> Objects.equals(stu.getCode(), code)).findFirst();
        return op.isPresent() ? op.get() : null;
    }
    
    public Integer getIndex() {
        return Misc.getIntGroupIndex(name);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        uid = level.getUId();
    }
    
    public String getCodesPattern() {
        return students.stream().map(stu -> "(" + stu.getCode() + ")").collect(Collectors.joining("|"));
    }
    
    public ArrayList<String> getCodes() {
        return students.stream().map(stu -> stu.getCode()).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
    
    public void calculateRanks(int semester) {
        Iterator<Student> it;
        switch ( semester ) {
            case 0:
                it = students.stream().sorted((s1, s2) -> s2.getS1Mark().compareTo(s1.getS1Mark())).iterator();
                break;
            case 1:
                it = students.stream().sorted((s1, s2) -> s2.getS2Mark().compareTo(s1.getS2Mark())).iterator();
                break;
            case 2:
                if ( level.isACL3() ) {
                    it = students.stream().sorted((s1, s2) -> s2.getS13A().compareTo(s1.getS13A())).iterator();
                }
                else {
                    it = students.stream().sorted((s1, s2) -> s2.getS1Mark().compareTo(s1.getS1Mark())).iterator();
                }
                break;
            default:
                it = students.stream().sorted((s1, s2) -> s2.getAverage().compareTo(s1.getAverage())).iterator();
                break;
        }
        int rank = 1;
        while ( it.hasNext() ) {
            it.next().setGroupRank(rank++);
        }
    }
    
    public void calculateRanks(Mark mark) {
        Iterator<Student> it;
        Function<Student, Double> func = Misc.getMarkFunction(mark, level.isACL3());
        Student stu;
        Double dMark = -100.0;
        int rank = 0;
        it = students.stream().sorted((s1, s2) -> func.apply(s2).compareTo(func.apply(s1))).iterator();
        while ( it.hasNext() ) {
            stu = it.next();
            if ( !dMark.equals(func.apply(stu)) )
                rank++;
            stu.setGroupRank(rank);
        }
    }
    
    public void setObservations(Mark mark) {
        Function<Student, Double> func = Misc.getMarkFunction(mark, level.isACL3());
        students.forEach(stu -> {
            stu.setObservationKey(func.apply(stu));
        });
    }
    
    public void setAppreciations(Mark mark) {
        Function<Student, Double> func = Misc.getMarkFunction(mark, level.isACL3());
        students.forEach(stu -> {
            stu.setAppreciation(func.apply(stu));
        });
    }
    
    public void setDecisions(Mark mark) {
        Function<Student, Double> func = Misc.getMarkFunction(mark, level.isACL3());
        students.forEach(stu -> {
            stu.setCouncilDecision(func.apply(stu));
        });
    }
    
    @Override
    public String toString() {
        return name;
    }

    public int getWeight() {
        return weight;
    }
    
    public String getTeacher(int matter) {
        return teachers.get(matter);
    }
    
    public void setTeacher(int matter, String teacher) {
        teachers.put(matter, teacher);
    }
    
    public ArrayList<Integer> getAssociatedMatters() {
        return new ArrayList(teachers.keySet());
    }
    
    public ArrayList<SEMESTER> getAssociatedSemesters() {
        if ( students.isEmpty() )
            return null;
        return students.get(0).getAssociatedSemesters();
    }
}
