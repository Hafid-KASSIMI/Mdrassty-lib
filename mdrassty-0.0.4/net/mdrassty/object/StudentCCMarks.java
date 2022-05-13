
package net.mdrassty.object;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Hafid KASSIMI (@mdrassty.net)
 */
public class StudentCCMarks implements Serializable {

    private final HashMap<Integer, Float> ccMarks = new HashMap();
    private Float aiMark;

    public StudentCCMarks(int testsCount) {
        aiMark = null;
    }

    public StudentCCMarks() {
        this(4);
    }
    
    public Float getAiMark() {
        return aiMark;
    }

    public void setAiMark(Float aiMark) {
        this.aiMark = aiMark;
    }

    public float getCcMark(int index) {
        return this.ccMarks.get(index);
    }

    public void setCcMarks(int index, Float mark) {
        ccMarks.put(index, mark);
    }

}
