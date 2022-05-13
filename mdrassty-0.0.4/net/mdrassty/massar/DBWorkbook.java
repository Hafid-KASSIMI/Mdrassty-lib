/*
 * Copyright (C) 2021 H. KASSIMI
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
import java.util.HashMap;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import net.mdrassty.object.Group;
import net.mdrassty.object.Level;
import net.mdrassty.object.School;
import net.mdrassty.object.Student;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class DBWorkbook extends MASSARExcelWorkbook {
    private int firstRow;
    private int sheetsNumber;
    private String classRef, dirRef, acadRef, schoolRef, yearRef, levRef;
    private String numCol, codeCol, fNameCol, sNameCol, genderCol, bDateCol, addressCol;
    private final HashMap<String, Integer> validSheets = new HashMap();
    private SimpleBooleanProperty status;
    private SimpleDoubleProperty loadPercentage;
    
    private int currentRow, currentSheet, currentStudent, uid;
    private final School school;
            
    public DBWorkbook(){
        firstRow = 16;
        codeCol = "X";
        numCol = "AA";
        fNameCol = "M";
        sNameCol = "Q";
        genderCol = "L";
        bDateCol = "F";
        addressCol = "C";
        classRef = "I11";
        dirRef = "U8";
        acadRef = "T6";
        schoolRef = "H8";
        yearRef = "C6";
        levRef = "T10";
        status = new SimpleBooleanProperty(false);
        loadPercentage = new SimpleDoubleProperty(0);
        school = new School();
    }
    
    public DBWorkbook(File f){
        this();
        try {
            wb = new HSSFWorkbook(new FileInputStream(f));
        } catch (IOException ex) {
            wb = null;
        }
    }
    
    @Override
    public final boolean setWorkbook(File f){
        validSheets.clear();
        try {
            wb = new HSSFWorkbook(new FileInputStream(f));
        } catch (IOException ex) {
            wb = null;
            return false;
        }
        return isItFromMassar();
    }
    
    private void loadValidSheets(){
        sheetsNumber = wb.getNumberOfSheets();
        for ( int i = 0; i < sheetsNumber; i++ ) {
            sht = wb.getSheetAt(i);
            if ( getStringValue(sht, "E6").contains("السنة الدراسية") && getStringValue(sht, "K3").contains("لائحة التلاميذ ") ) {
                validSheets.put(getStringValue(sht, classRef), i);
            }
        }  
    }
    
    @Override
    public boolean isItFromMassar(){
        loadValidSheets();
        return ( validSheets.size() > 0 );
    }
    
    public void loadDB(){
        if ( validSheets.isEmpty() )  return;
        sht = wb.getSheetAt(validSheets.values().stream().findAny().get());
        school.reset();
        school.setAcademy(getStringValue(sht, acadRef));
        school.setDirection(getStringValue(sht, dirRef));
        school.setSchool(getStringValue(sht, schoolRef));
        school.setYear(getStringValue(sht, yearRef));
        uid = (school.getAcademy().trim() + school.getDirection().trim() + 
                school.getSchool().trim() + school.getYear().trim()).hashCode();
        school.setUId(uid);
        
        new Thread(() -> {
            String code;
            int num;
            status.set(false);
            currentStudent = 0;
            currentSheet = 0;
            processedGroups.set(0);
            for( int i : validSheets.values() ) {
                Group grp;
                Level lev;
                currentRow = firstRow;
                String cls, levName;
                sht = wb.getSheetAt(i);
                cls = getStringValue(sht, classRef);
                levName = getStringValue(sht, levRef);
                lev = school.addLevel(levName);
                grp = lev.addGroup(cls);
                lev.setSchool(school);
                grp.setLevel(lev);
                num = 1;
                while( !(code = getStringValue(sht, codeCol + currentRow)).isEmpty()) {
                    Student stu = new Student();
                    try {
                        stu.setNum(Integer.parseInt(getStringValue(sht, numCol + currentRow)));
                    }
                    catch(NumberFormatException e) {
                        stu.setNum(num++);
                    }
                    stu.setCode(code);
                    stu.setAddress(getStringValue(sht, addressCol + currentRow));
                    stu.setSecName(getStringValue(sht, sNameCol + currentRow));
                    stu.setFirName(getStringValue(sht, fNameCol + currentRow));
                    stu.setGender(getStringValue(sht, genderCol + currentRow));
                    stu.setBirthDate(getStringValue(sht, bDateCol + currentRow));
                    grp.addStudent(stu);
                    currentRow++;
                    currentStudent++;
                    processedStudents.set(processedStudents.get() + 1);
                }
                processedGroups.set(processedGroups.get() + 1);
                loadPercentage.set((double) ++currentSheet / sheetsNumber);
            }
            school.setStudentsCount(currentStudent);
            status.set(true);
            close();
        }).start();
    }
    
    public SimpleBooleanProperty getStatus() {
        return status;
    }

    public SimpleDoubleProperty getLoadPercentage() {
        return loadPercentage;
    }
    
    public int getSheetsNumber() {
        return sheetsNumber;
    }
    
    public boolean isNull(){
        return (wb == null);
    }

    public School getSchool() {
        return school;
    }

    public int getCurrentStudent() {
        return currentStudent;
    }
    
    @Override
    public void close() {
        try {
            wb.close();
        } catch (IOException | NullPointerException ex) {  }
    }

    @Override
    public void loadGroupInfos(Group grp) { }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public int getUid() {
        return uid;
    }
    
}
