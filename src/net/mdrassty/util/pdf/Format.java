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

package net.mdrassty.util.pdf;

public class Format implements Cloneable {
    private float fontSize;
    private String font;
    private Boolean isBold;

    public Format(float fontSize, String font) {
        this.fontSize = fontSize;
        this.font = font;
        isBold = false;
    }

    public Format(float fontSize, String font, Boolean isBold) {
        this.fontSize = fontSize;
        this.font = font;
        this.isBold = isBold;
    }

    public Format() {
        isBold = false;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public String getFont() {
        return isBold ? font : font.replace("bd", "");
    }

    public void setFont(String font) {
        this.font = font;
    }
    
    public void reset(float fontSize, String font) {
        this.fontSize = fontSize;
        this.font = font;
    }
    
    public void reset(float fontSize, String font, Boolean isBold) {
        this.fontSize = fontSize;
        this.font = font;
        this.isBold = isBold;
    }

    public Boolean getIsBold() {
        return isBold;
    }

    public void setIsBold(Boolean isBold) {
        this.isBold = isBold;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
}
