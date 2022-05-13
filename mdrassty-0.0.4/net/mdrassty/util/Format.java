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

package net.mdrassty.util;

import java.awt.Color;
import net.mdrassty.util.excel.XAvailableFonts;

public class Format implements Cloneable {
    private final float PADDING = 2.5f;
    private static final Color DEFAULT_BORDER_COLOR = new Color(0x80, 0x80, 0x80);
    private final Color DEFAULT_BACK_COLOR = Color.WHITE;
    private final Color DEFAULT_FONT_COLOR = Color.BLACK;
    private float fontSize = 12;
    private float rotation = 0;
    private String fontFamily = XAvailableFonts.TIMES;
    private boolean bold = false, wrapped = false, shrinked = true;
    private final float[] BORDERS = {0, 0, 0, 0};
    private ALIGNMENT alignment = ALIGNMENT.CENTER;
    private HORIZONTAL_ALIGNMENT hAlignment = HORIZONTAL_ALIGNMENT.CENTER;
    private VERTICAL_ALIGNMENT vAlignment = VERTICAL_ALIGNMENT.MIDDLE;
    private DATA_TYPE dataType = DATA_TYPE.TEXT;
    private Color fontColor = DEFAULT_FONT_COLOR, foreColor, backColor , borderColor = DEFAULT_BORDER_COLOR;

    public Format(float fontSize, String font) {
        this.fontSize = fontSize;
        this.fontFamily = font;
        bold = false;
        fontColor = Color.BLACK;
    }

    public Format(float fontSize, String font, Boolean bold) {
        this.fontSize = fontSize;
        this.fontFamily = font;
        this.bold = bold;
    }

    public Format() {
        bold = false;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
    
    public void reset(float fontSize, String font) {
        this.fontSize = fontSize;
        this.fontFamily = font;
    }
    
    public void reset(float fontSize, String font, Boolean bold) {
        this.fontSize = fontSize;
        this.fontFamily = font;
        this.bold = bold;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public Color getForeColor() {
        return foreColor;
    }

    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }
    
    public Color getBackColor() {
        return backColor;
    }

    public void setForeColor(Color foreColor) {
        this.foreColor = foreColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public ALIGNMENT getAlignment() {
        return alignment;
    }

    public HORIZONTAL_ALIGNMENT getHAlignment() {
        return hAlignment;
    }

    public void setHAlignment(HORIZONTAL_ALIGNMENT hAlignment) {
        this.hAlignment = hAlignment;
        switch (hAlignment) {
            case CENTER:
                switch (vAlignment) {
                    case TOP:
                        alignment = ALIGNMENT.TOP_CENTER;
                        break;
                    case BOTTOM:
                        alignment = ALIGNMENT.BOTTOM_CENTER;
                        break;
                    default:
                        alignment = ALIGNMENT.CENTER;
                        break;
                }   break;
            case LEFT:
                switch (vAlignment) {
                    case TOP:
                        alignment = ALIGNMENT.TOP_LEFT;
                        break;
                    case BOTTOM:
                        alignment = ALIGNMENT.BOTTOM_LEFT;
                        break;
                    default:
                        alignment = ALIGNMENT.MIDDLE_LEFT;
                        break;
                }   break;
            default:
                switch (vAlignment) {
                    case TOP:
                        alignment = ALIGNMENT.TOP_RIGHT;
                        break;
                    case BOTTOM:
                        alignment = ALIGNMENT.BOTTOM_RIGHT;
                        break;
                    default:
                        alignment = ALIGNMENT.MIDDLE_RIGHT;
                        break;
                }   break;
        }
    }

    public VERTICAL_ALIGNMENT getvAlignment() {
        return vAlignment;
    }

    public void setVAlignment(VERTICAL_ALIGNMENT vAlignment) {
        this.vAlignment = vAlignment;
        switch (vAlignment) {
            case MIDDLE:
                switch (hAlignment) {
                    case LEFT:
                        alignment = ALIGNMENT.MIDDLE_LEFT;
                        break;
                    case RIGHT:
                        alignment = ALIGNMENT.MIDDLE_RIGHT;
                        break;
                    default:
                        alignment = ALIGNMENT.CENTER;
                        break;
                }   break;
            case TOP:
                switch (hAlignment) {
                    case LEFT:
                        alignment = ALIGNMENT.TOP_LEFT;
                        break;
                    case RIGHT:
                        alignment = ALIGNMENT.TOP_RIGHT;
                        break;
                    default:
                        alignment = ALIGNMENT.TOP_CENTER;
                        break;
                }   break;
            default:
                switch (hAlignment) {
                    case LEFT:
                        alignment = ALIGNMENT.BOTTOM_LEFT;
                        break;
                    case RIGHT:
                        alignment = ALIGNMENT.BOTTOM_RIGHT;
                        break;
                    default:
                        alignment = ALIGNMENT.BOTTOM_CENTER;
                        break;
                }   break;
        }
    }

    public VERTICAL_ALIGNMENT getVAlignment() {
        return vAlignment;
    }

    public void setAlignment(ALIGNMENT alignment) {
        this.alignment = alignment;
        switch ( alignment ) {
            case TOP_LEFT:
                hAlignment = HORIZONTAL_ALIGNMENT.LEFT;
                vAlignment = VERTICAL_ALIGNMENT.TOP;
                break;
            case MIDDLE_LEFT:
                hAlignment = HORIZONTAL_ALIGNMENT.LEFT;
                vAlignment = VERTICAL_ALIGNMENT.MIDDLE;
                break;
            case BOTTOM_LEFT:
                hAlignment = HORIZONTAL_ALIGNMENT.LEFT;
                vAlignment = VERTICAL_ALIGNMENT.BOTTOM;
                break;
            case TOP_RIGHT:
                hAlignment = HORIZONTAL_ALIGNMENT.RIGHT;
                vAlignment = VERTICAL_ALIGNMENT.TOP;
                break;
            case MIDDLE_RIGHT:
                hAlignment = HORIZONTAL_ALIGNMENT.RIGHT;
                vAlignment = VERTICAL_ALIGNMENT.MIDDLE;
                break;
            case BOTTOM_RIGHT:
                hAlignment = HORIZONTAL_ALIGNMENT.RIGHT;
                vAlignment = VERTICAL_ALIGNMENT.BOTTOM;
                break;
            case TOP_CENTER:
                hAlignment = HORIZONTAL_ALIGNMENT.CENTER;
                vAlignment = VERTICAL_ALIGNMENT.TOP;
                break;
            case CENTER:
                hAlignment = HORIZONTAL_ALIGNMENT.CENTER;
                vAlignment = VERTICAL_ALIGNMENT.MIDDLE;
                break;
            case BOTTOM_CENTER:
                hAlignment = HORIZONTAL_ALIGNMENT.CENTER;
                vAlignment = VERTICAL_ALIGNMENT.BOTTOM;
                break;
        }
    }

    public DATA_TYPE getDataType() {
        return dataType;
    }

    public void setDataType(DATA_TYPE dataType) {
        this.dataType = dataType;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setBorder(int index, float border) {
        BORDERS[index] = border;
    }

    public void setBorders(float top, float right, float bottom, float left) {
        BORDERS[0] = top;
        BORDERS[1] = right;
        BORDERS[2] = bottom;
        BORDERS[3] = left;
    }

    public float getBorder(int index) {
        return BORDERS[index];
    }

    public boolean isWrapped() {
        return wrapped;
    }

    public void setWrapped(boolean wrapped) {
        this.wrapped = wrapped;
    }

    public static Color getDefaultBorderColor() {
        return DEFAULT_BORDER_COLOR;
    }

    public boolean isShrinked() {
        return shrinked;
    }

    public void setShrinked(boolean shrinked) {
        this.shrinked = shrinked;
    }
    
}
