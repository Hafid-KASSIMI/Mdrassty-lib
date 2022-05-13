/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mdrassty.object;

import net.mdrassty.util.SEMESTER;

/**
 *
 * @author Hafid KASSIMI (@mdrassty.net)
 */
public class Semester {

    private SEMESTER s;
    private String label;

    public Semester(SEMESTER s, String label) {
        this.s = s;
        this.label = label;
    }

    public void set(SEMESTER s) {
        this.s = s;
    }

    public SEMESTER get() {
        return s;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

}
