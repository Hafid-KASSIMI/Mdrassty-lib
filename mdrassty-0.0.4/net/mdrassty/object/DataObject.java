/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mdrassty.object;

import java.io.Serializable;
import net.mdrassty.util.DATA_TYPE;

/**
 *
 * @author Hafid KASSIMI (@mdrassty.net)
 */
public class DataObject implements Serializable {
    
    protected DATA_TYPE dataType;
    protected int uid;

    public DataObject(DATA_TYPE dataType) {
        this.dataType = dataType;
    }

    public DATA_TYPE getDataType() {
        return dataType;
    }
    
    public Boolean isSchool() {
        return dataType == DATA_TYPE.SCHOOL;
    }
    
    public Boolean isLevel() {
        return dataType == DATA_TYPE.LEVEL;
    }
    
    public Boolean isGroup() {
        return dataType == DATA_TYPE.GROUP;
    }

    public int getUId() {
        return uid;
    }

    public void setUId(int uid) {
        this.uid = uid;
    }
    
}
