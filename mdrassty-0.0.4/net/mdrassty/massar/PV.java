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


import java.io.*;
import java.util.ArrayList;
import java.util.function.BiFunction;
import net.mdrassty.object.Group;
import net.mdrassty.object.Student;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import net.mdrassty.util.Misc;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Surfer
 * @updated by Sicut
 */
public final class PV extends MASSARExcelWorkbook {
    private final String codeClmn, dataSheetName, nameClmn, genderClmn, s1Clmn, s2Clmn, locClmn, regClmn, choice1Clmn, choice2Clmn;
    private String decisionClmn, avgClmn;
    private final String[] yearClmns;
    private final String groupCell, yearCell, directionCell, academyCell, levelCell, schoolCell;
    private final int firstRow;
        
    private boolean valid;
    private String tmpCode;
    private int uid;
    private String group, year, level, direction, academy, school;
    private BiFunction<Sheet, String, Double> examsFunc;
    private BiFunction<Sheet, String, String> orientFunc;
    //private File tmpFile;
            
    public PV(){
        yearClmns = new String[]{"E", "F", "G"};
        codeClmn = "B";
        nameClmn = "C";
        genderClmn = "D";
        s1Clmn = "H";
        s2Clmn = "I";
        locClmn = "J";
        regClmn = "K";
        choice1Clmn = "M";
        choice2Clmn = "N";
        firstRow = 12;
        dataSheetName = "Data";
        groupCell = "J7";
        yearCell = "K5";
        directionCell = "F5";
        academyCell = "C5";
        levelCell = "F7";
        schoolCell = "C7";
        valid = false;
        sht = null;
    }
    
    @Override
    public boolean setWorkbook(File f){
        boolean res = false;
        try {
            wb = new XSSFWorkbook(f);
            //tmpFile = f;
            sht = wb.getSheet(dataSheetName);
            if ( isItFromMassar() ) {
                group = getStringValue(sht, groupCell);
                year = getStringValue(sht, yearCell);
                level = getStringValue(sht, levelCell);
                direction = getStringValue(sht, directionCell);
                academy = getStringValue(sht, academyCell);
                school = getStringValue(sht, schoolCell);
                uid = (academy.trim() + direction.trim() + school.trim() + year.trim()).hashCode();
                if ( group.startsWith("3A") ) {
                    decisionClmn = "O";
                    avgClmn = "L";
                    examsFunc = (sheet, ref) -> getNumericValue(sheet, ref);
                    orientFunc = (sheet, ref) -> getStringValue(sheet, ref);
                }
                else {
                    decisionClmn = "K";
                    avgClmn = "J";
                    examsFunc = (sheet, ref) -> {return -1.0;};
                    orientFunc = (sheet, ref) -> {return "";};
                }
                res = true;
            }
        }
        catch(IOException | InvalidFormatException e){
            wb = null;
        }
        valid = res;
        return res;
    }
    
    @Override
    public boolean isItFromMassar(){
        return ( sht == null ) ? false : (getStringValue(sht, "E2").contains("قرار مجلس القسم") && getStringValue(sht, "B10").contains("رقم التلميذ") && 
                getStringValue(sht, "I7").contains("القسم") && getStringValue(sht, "I5").contains("السنة الدراسية") &&
                getStringValue(sht, "C10").contains("الاسم و النسب"));
    }

    public boolean isValid() {
        return valid;
    }
    
    public boolean isNull(){
        return (wb == null);
    }
    
    public ArrayList<Student> getStudentsMarks() {
        int r = firstRow;
        ArrayList<Student> students = new ArrayList();
        while( true ) {
            try{
                if ( !( tmpCode = getStringValue(sht, codeClmn + r)).isEmpty() ) {
                    Student stu = new Student();
                    stu.setCode(tmpCode);
                    stu.setS1Mark(getNumericValue(sht, s1Clmn + r));
                    stu.setS2Mark(getNumericValue(sht, s2Clmn + r));
                    stu.setAverage(getNumericValue(sht, avgClmn + r));
                    stu.setLocExamMark(examsFunc.apply(sht, locClmn + r));
                    stu.setRegExamMark(examsFunc.apply(sht, regClmn + r));
                    stu.calculateS13A();
                    students.add(stu);
                    r++;
                }
                else {
                    break;
                }
            }
            catch(NullPointerException e){
                break;
            }
        }
        return students;
    }
    
    @Override
    public void loadGroupInfos(Group grp) {
        int r = firstRow;
        while ( !( tmpCode = getStringValue(sht, codeClmn + r)).isEmpty() ) {
            Student stu = grp.getStudent(tmpCode);
            if ( stu != null ) {
                stu.setCode(tmpCode);
                stu.setS1Mark(getNumericValue(sht, s1Clmn + r));
                stu.setS2Mark(getNumericValue(sht, s2Clmn + r));
                stu.setAverage(getNumericValue(sht, avgClmn + r));
                stu.setLocExamMark(examsFunc.apply(sht, locClmn + r));
                stu.setRegExamMark(examsFunc.apply(sht, regClmn + r));
                stu.calculateS13A();
            }
            r++;
            processedStudents.set(processedStudents.get() + 1);
        }
        close();
        processedGroups.set(processedGroups.get() + 1);
    }
    
    public ArrayList<Student> getStudentsInfos() {
        int r = firstRow;
        ArrayList<Student> students = new ArrayList();
        while( true ) {
            try{
                if ( !( tmpCode = getStringValue(sht, codeClmn + r)).isEmpty() ) {
                    Student stu = new Student();
                    stu.setS1Mark(getNumericValue(sht, s1Clmn + r));
                    stu.setS2Mark(getNumericValue(sht, s2Clmn + r));
                    stu.setAverage(getNumericValue(sht, avgClmn + r));
                    stu.setLocExamMark(examsFunc.apply(sht, locClmn + r));
                    stu.setRegExamMark(examsFunc.apply(sht, regClmn + r));
                    stu.calculateS13A();
                    stu.setChoice1(orientFunc.apply(sht, choice1Clmn + r));
                    stu.setChoice2(orientFunc.apply(sht, choice2Clmn + r));
                    stu.setYears(new Integer[] {getNumericValue(sht, yearClmns[0] + r).intValue(), 
                        getNumericValue(sht, yearClmns[1] + r).intValue(),
                        getNumericValue(sht, yearClmns[2] + r).intValue()
                    });
                    stu.setDecision(getStringValue(sht, decisionClmn + r));
                    stu.setFullName(Misc.justLetters(getStringValue(sht, nameClmn + r)));
                    stu.setGender(getStringValue(sht, genderClmn + r));
                    stu.setCode(tmpCode);
                    students.add(stu);
                    r++;
                }
                else {
                    break;
                }
            }
            catch(NullPointerException e){
                break;
            }
        }
        return students;
    }
    
    @Override
    public void close() {
        try {
            wb.close();
        } catch (IOException | NullPointerException ex) {  }
    }
    
    @Override
    public String getGroup() {
        return group;
    }

    public String getLevel() {
        return level;
    }

    public String getYear() {
        return year;
    }

    public String getDirection() {
        return direction;
    }

    public String getAcademy() {
        return academy;
    }

    public String getSchool() {
        return school;
    }

    @Override
    public int getUid() {
        return uid;
    }
    
    
}
