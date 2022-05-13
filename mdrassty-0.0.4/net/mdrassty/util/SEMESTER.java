/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mdrassty.util;

/**
 *
 * @author Hafid KASSIMI (@mdrassty.net)
 */
public enum SEMESTER {
    ALL,
    SEMESTER_1,
    SEMESTER_2;
    
    public int intValue() {
        if ( this.equals(SEMESTER_1) )
                return 1;
        if ( this.equals(SEMESTER_2) )
                return 2;
        return 0;
    }
    
    public static SEMESTER get(int value) {
        if ( value == 1 )
            return SEMESTER_1;
        if ( value == 2 )
            return SEMESTER_2;
        return ALL;
    }
}
