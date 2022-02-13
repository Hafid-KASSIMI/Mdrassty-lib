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

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import net.mdrassty.util.Decision;
import net.mdrassty.util.Misc;
import static net.mdrassty.util.Misc.justLetters;

public class Student implements Serializable {
    private Integer num;
    private String firName, secName, fullName, gender, address, code, birthDate;
    private Double s1Mark, s2Mark, average, locExamMark, regExamMark, s13A;
    private Integer groupRank, levelRank;
    private String choice1, choice2;
    private Integer[] years;
    private String dec;
    private Integer RAND_ID;
    private Long age, daysAge;
    private String observationKey, appreciationKey; // To avoid further calculations by getting this ready when ranking
    private Decision councilDecision;
    public enum MARK { S1, S2, AVERAGE, LOCAL_EXAM, REGIONAL_EXAM };

    public Student() {
        num = 0;
        firName = "";
        secName = "";
        fullName = "";
        address = "";
        code = "";
        birthDate = "";
        s1Mark = s2Mark = locExamMark = regExamMark = average = s13A = -1.0;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Integer getGroupRank() {
        return groupRank;
    }

    public void setGroupRank(Integer groupRank) {
        this.groupRank = groupRank;
    }

    public Integer getLevelRank() {
        return levelRank;
    }

    public void setLevelRank(Integer levelRank) {
        this.levelRank = levelRank;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = justLetters(address);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirName() {
        return firName;
    }

    public void setFirName(String firName) {
        this.firName = justLetters(firName);
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = justLetters(secName);
    }

    public Double getS1Mark() {
        return s1Mark;
    }

    public void setS1Mark(Double s1Mark) {
        this.s1Mark = s1Mark;
    }

    public Double getS2Mark() {
        return s2Mark;
    }

    public void setS2Mark(Double s2Mark) {
        this.s2Mark = s2Mark;
    }

    public Double getLocExamMark() {
        return locExamMark;
    }

    public void setLocExamMark(Double locExamMark) {
        this.locExamMark = locExamMark;
    }

    public Double getRegExamMark() {
        return regExamMark;
    }

    public void setRegExamMark(Double regExamMark) {
        this.regExamMark = regExamMark;
    }

    public Double getAverage() {
        return average;
    }

    public Double getLN3LastMark() {
        if ( average > -1 )
                return average;
        return s1Mark;
    }

    public Double getL3LastMark() {
        if ( average > -1 )
                return average;
        return s13A;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = justLetters(gender);
    }
    
    public String getFullName() {
        return "".equals(fullName) ? firName + " " + secName : fullName;
    }
    
    public String getFirstNLastName() {
        return firName + " " + secName;
    }
    
    public String getLastNFirstName() {
        return secName + " " + firName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        LocalDate dt;
        try {
            dt = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd/MM/uuuu"));
            age = ChronoUnit.YEARS.between(dt, LocalDate.now());
            daysAge = ChronoUnit.DAYS.between(dt, LocalDate.now());
        } catch ( DateTimeParseException ex ) {
            dt = LocalDate.now();
            age = 0L;
            daysAge = 0L;
        }
        this.birthDate = dt.format(DateTimeFormatter.ofPattern("uuuu/MM/dd"));
    }
    
    public Boolean isGirl() {
        return gender.matches("(F)|(Female)|(Fille)|(أنثى)");
    }
    
    public Boolean isFemale() {
        return isGirl();
    }

    public Integer[] getYears() {
        return years;
    }

    public void setYears(Integer[] years) {
        this.years = years;
    }

    public String getDecision() {
        return dec;
    }

    public void setDecision(String dec) {
        this.dec = Misc.justLetters(dec);
    }

    public Long getAge() {
        return age;
    }

    public Long getDaysAge() {
        return daysAge;
    }

    public Double getS13A() {
        return s13A;
    }

    public void setS13A(Double s13A) {
        this.s13A = s13A;
    }

    public void calculateS13A() {
        s13A = ( s1Mark + locExamMark * 2 ) / 3;
    }
    
    public Double getL3Mark(int i) {
        switch (i) {
            case 0:
                return s1Mark;
            case 1:
                return s2Mark;
            case 2:
                return locExamMark;
            case 3:
                return regExamMark;
            default:
                return average;
        }
    }
    
    public Double getLN3Mark(int i) {
        switch (i) {
            case 0:
                return s1Mark;
            case 1:
                return s2Mark;
            default:
                return average;
        }
    }
    
    public int getS1L3MarksSize() {
        return 3;
    }
    
    public int getS1LN3MarksSize() {
        return 1;
    }
    
    public Double getS1Mark(int i) {
        switch (i) {
            case 0:
                return s1Mark;
            case 1:
                return locExamMark;
            default:
                return s13A;
        }
    }
    
    public Double getS1L3AvgMark() {
        return s13A;
    }
    
    public Double getS1LN3AvgMark() {
        return s1Mark;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public Integer getRAND_ID() {
        return RAND_ID;
    }

    public void setRAND_ID(Integer RAND_ID) {
        this.RAND_ID = RAND_ID;
    }
    
    public void copyMarks(Student stu) {
        setS1Mark(stu.getS1Mark());
        setS2Mark(stu.getS2Mark());
        setAverage(stu.getAverage());
        setLocExamMark(stu.getLocExamMark());
        setRegExamMark(stu.getRegExamMark());
        setS13A(stu.getS13A());
    }

    public String getAppreciationKey() {
        return appreciationKey;
    }

    public void setAppreciationKey(String appreciationKey) {
        this.appreciationKey = appreciationKey;
    }
    
    public String getObservationKey() {
        return observationKey;
    }

    public void setObservationKey(String observationKey) {
        this.observationKey = observationKey;
    }

    public Decision getCouncilDecision() {
        return councilDecision;
    }
    
    public void setObservationKey(Double mark) {
        String suffix = isFemale()? "_FEMALE" : "_MALE";
        observationKey = "";
        if ( mark >= 0 && mark < 5 )
            observationKey = "0_TO_5" + suffix;
        else if ( mark >= 5 && mark < 8 )
            observationKey = "5_TO_8" + suffix;
        else if ( mark >= 8 && mark < 10 )
            observationKey = "8_TO_10" + suffix;
        else if ( mark >= 10 && mark < 12 )
            observationKey = "10_TO_12" + suffix;
        else if ( mark >= 12 && mark < 14 )
            observationKey = "12_TO_14" + suffix;
        else if ( mark >= 14 && mark < 16 )
            observationKey = "14_TO_16" + suffix;
        else if ( mark >= 16 && mark < 18 )
            observationKey = "16_TO_18" + suffix;
        else if ( mark >= 18 && mark <= 20 )
            observationKey = "18_TO_20" + suffix;
    }
    
    public void setAppreciation(Double mark) {
        appreciationKey = "";
        if ( mark >= 14 && mark < 16 )
            appreciationKey = "GOOD";
        else if ( mark >= 16 && mark < 18 )
            appreciationKey = "VERY_GOOD";
        else if ( mark >= 18 && mark < 20 )
            appreciationKey = "EXCELLENT";
    }
    
    public void setCouncilDecision(Double mark) {
        councilDecision = Decision.NONE;
        if ( mark < 5 )
            councilDecision = Decision.REBUKE;
        else if ( mark >= 5 && mark < 6 )
            councilDecision = Decision.ALERT;
//            else if ( mark >= 6 && mark < 8 )
//                return Decision.WARNING;
        else if ( mark >= 12 && mark < 14 )
            councilDecision = Decision.HONOR;
        else if ( mark >= 14 && mark < 16 )
            councilDecision = Decision.CHEER;
        else if ( mark >= 16 && mark <= 20 )
            councilDecision = Decision.NOTICE;
    }
}
