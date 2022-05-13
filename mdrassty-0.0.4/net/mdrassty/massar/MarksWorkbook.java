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
import java.util.function.BiConsumer;
import net.mdrassty.object.Group;
import net.mdrassty.util.SEMESTER;
import net.mdrassty.object.Student;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Surfer
 * @updated by Sicut
 */
public final class MarksWorkbook extends MASSARExcelWorkbook {
    private final String NAMES_COL, CODES_COL, IDS_COL, SHEET_NAME;
    private final String TEST_REF, MATTER_CODE_REF, MATTER_LABEL_REF, GROUP_REF, SEMESTER_REF, YEAR_REF;
    private final String TEACHER_REF, ACADEMY_REF, DIRECTION_REF, SCHOOL_REF;
    private final Integer FIRST_MARKS_COL, FIRST_ROW, TESTS_COUNT = 5;
        
//    private static final CCMattersCheatSheet CHEAT = new CCMattersCheatSheet();
    private boolean valid;
    private int uid;
    private String group, year, direction, academy, school, matterLabel;
    private Integer matter, test;
    private SEMESTER semester;
            
    public MarksWorkbook(){
        FIRST_MARKS_COL = Character.getNumericValue('G') - Character.getNumericValue('A');
        NAMES_COL = "D";
        CODES_COL = "C";
        IDS_COL = "B";
        TEST_REF = "I5";
        MATTER_CODE_REF = "K5";
        MATTER_LABEL_REF = "O11";
        GROUP_REF = "I9";
        SEMESTER_REF = "G5";
        YEAR_REF = "D13";
        TEACHER_REF = "O9";
        ACADEMY_REF = "D7";
        DIRECTION_REF = "I7";
        SCHOOL_REF = "O7";
        FIRST_ROW = 18;
        SHEET_NAME = "NotesCC";
        valid = false;
        sht = null;
    }
    
    @Override
    public boolean setWorkbook(File f){
        boolean res = false;
        try {
            wb = new XSSFWorkbook(f);
            sht = wb.getSheet(SHEET_NAME);
            if ( isItFromMassar() ) {
                group = getStringValue(sht, GROUP_REF);
                direction = getStringValue(sht, DIRECTION_REF);
                school = getStringValue(sht, SCHOOL_REF);
                year = getStringValue(sht, YEAR_REF);
                academy = getStringValue(sht, ACADEMY_REF);
                semester = ( getNumericValue(sht, SEMESTER_REF).intValue() == 1 ) ? 
                        SEMESTER.SEMESTER_1 : SEMESTER.SEMESTER_2;
                matter = Integer.parseInt(getStringValue(sht, MATTER_CODE_REF).replaceAll("#", ""));
                matterLabel = getStringValue(sht, MATTER_LABEL_REF);
                test = getNumericValue(sht, TEST_REF).intValue();
                uid = (academy.trim() + direction.trim() + school.trim() + year.trim()).hashCode();
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
        return ( sht == null ) ? false : (getStringValue(sht, "A2").contains("sgs") && 
                getStringValue(sht, "A1").contains("A") && 
                getStringValue(sht, "Z1").contains("O") && 
                getStringValue(sht, "Z2").contains("sgs"));
    }
    
    @Override
    public void loadGroupInfos(Group grp){
        if ( !isValid() )  return;
//        new Thread(() -> {
            BiConsumer<Student, Integer> cons;
            String code;
            int currentRow = FIRST_ROW;
            grp.setTeacher(matter, getStringValue(sht, TEACHER_REF));
            if ( test == 0 ) {
                cons = (stu, row) -> {
                    Double mark;
                    for ( int i = 0; i < TESTS_COUNT; i++ ) {
                        if ( !isEmpty(sht, row, FIRST_MARKS_COL + 2 * i) ) {
                            mark = getNumericValue(sht, row, FIRST_MARKS_COL + 2 * i);
                            if ( mark != null )
                                stu.setCCMark(matter, semester, i, mark.floatValue());
                        }
                    }
                };
            }
            else {
                cons = (stu, row) -> {
                    if ( !isEmpty(sht, row, FIRST_MARKS_COL) ) {
                        Double mark = getNumericValue(sht, row, FIRST_MARKS_COL);
                        if ( mark != null )
                            stu.setCCMark(matter, semester, test - 1, mark.floatValue());
                    }
                };
            }
            while( !(code = getStringValue(sht, CODES_COL + currentRow)).isEmpty()) {
                Student stu = grp.getStudent(code);
                if ( stu != null ) {
                    cons.accept(stu, currentRow - 1);
                    processedStudents.set(processedStudents.get() + 1);
                }
                currentRow++;
            }
            close();
            processedGroups.set(processedGroups.get() + 1);
//        }).start();
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public boolean isNull(){
        return (wb == null);
    }
    
    @Override
    public int getUid() {
        return uid;
    }

    @Override
    public String getGroup() {
        return group;
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

    public Integer getMatter() {
        return matter;
    }

    public String getMatterLabel() {
        return matterLabel;
    }
    
}
