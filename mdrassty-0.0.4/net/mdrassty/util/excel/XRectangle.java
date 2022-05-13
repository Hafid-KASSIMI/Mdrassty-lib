/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mdrassty.util.excel;

import net.mdrassty.util.DATA_TYPE;
import net.mdrassty.util.Format;
import net.mdrassty.util.HORIZONTAL_ALIGNMENT;
import net.mdrassty.util.Rectangle;
import net.mdrassty.util.VERTICAL_ALIGNMENT;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 *
 * @author Hafid KASSIMI (@mdrassty.net)
 */
public class XRectangle extends Rectangle<Integer> {
    
    
    private XSSFCellStyle style;
    private int colSpan = 0, rowSpan = 0;
    
    public XRectangle(Integer x, Integer y, float width, float height) {
        super(x, y, width, height);
    }
    
    public XRectangle(String address, float width, float height) {
        CellReference ref = new CellReference(address);
        x = (int) ref.getCol();
        y = ref.getRow();
        this.width = width;
        this.height = height;
    }

    public XRectangle(Integer x, Integer y, float width, float height, Format format) {
        super(x, y, width, height, format);
    }

    public XRectangle(String address, float width, float height, Format format) {
        this(address, width, height);
        this.format = format;
    }

    public XRectangle() {
    }

    public XRectangle(Format format) {
        super(format);
    }

    public XRectangle(Integer x, Integer y, float width, float height, float fontSize, String font) {
        super(x, y, width, height, fontSize, font);
    }

    public XRectangle(String address, float width, float height, float fontSize, String font) {
        this(address, width, height);
        setFormat(fontSize, font);
    }

    public void reset(String address, float width, float height, int fontSize, String font, Boolean isBold) {
        reset(address, width, height);
        setFormat(fontSize, font, isBold);
    }

    public void reset(String address, float width, float height, float fontSize, String font) {
        reset(address, width, height);
        setFormat(fontSize, font);
    }

    public void reset(String address, float width, float height, Format format) {
        reset(address, width, height);
        this.format = format;
    }

    public void reset(String address, float width, float height) {
        CellReference ref = new CellReference(address);
        reset((int) ref.getCol(), ref.getRow(), width, height);
    }

    public void reset(String address) {
        CellReference ref = new CellReference(address);
        x = (int) ref.getCol();
        y = ref.getRow();
    }

    public void reset(String address, Format format) {
        reset(address);
        this.format = format;
    }
    
    public void doMergeCols(Sheet sheet) {
        sheet.addMergedRegion(new CellRangeAddress(y, y, x, x + colSpan));
    }
    
    public void doMerge(Sheet sheet) {
        sheet.addMergedRegion(new CellRangeAddress(y, y + rowSpan, x, x + colSpan));
    }
    
    public void doMergeCols(Sheet sheet, int row) {
        sheet.addMergedRegion(new CellRangeAddress(row, row, x, x + colSpan));
    }
    
    public void cancelStyle() {
        style = null;
    }
    
    public CellStyle getStyle(Workbook wb) {
        return getStyle(wb, FillPatternType.SOLID_FOREGROUND);
    }
    
    public CellStyle getStyle(Workbook wb, FillPatternType fill) {
        if ( style != null )
            return style;
        reloadStyle(wb, fill);
        return style;
    }
    
    public CellStyle reloadStyle(Workbook wb) {
        reloadStyle(wb, FillPatternType.SOLID_FOREGROUND);
        return style;
    }
    
    public void reloadStyle(Workbook wb, FillPatternType fill) {
        XSSFColor color;
        XSSFFont fnt = (XSSFFont) wb.createFont();
        XSSFDataFormat xdf = (XSSFDataFormat) wb.createDataFormat();
        style = (XSSFCellStyle) wb.createCellStyle();
        style.setAlignment(getHAlignment());
        style.setVerticalAlignment(getVAlignment());
        color = new XSSFColor(format.getBorderColor(), null);
        style.setBorderTop( getBorderStyle(0) );
        style.setTopBorderColor(color);
        style.setBorderRight( getBorderStyle(1) );
        style.setRightBorderColor(color);
        style.setBorderBottom( getBorderStyle(2) );
        style.setBottomBorderColor(color);
        style.setBorderLeft( getBorderStyle(3) );
        style.setLeftBorderColor(color);
        if ( format.getForeColor() != null ) {
            style.setFillForegroundColor(new XSSFColor(format.getForeColor(), null));
            style.setFillPattern(fill);
        }
        if ( format.getBackColor() != null ) {
            style.setFillBackgroundColor(new XSSFColor(format.getBackColor(), null));
        }
        style.setWrapText(format.isWrapped());
        style.setShrinkToFit(format.isShrinked());
        style.setRotation((short) format.getRotation());
        style.setDataFormat(getDataFormat(xdf));
        fnt.setBold(format.isBold());
        fnt.setFontName(format.getFontFamily());
        fnt.setFontHeight(format.getFontSize());
        fnt.setColor(new XSSFColor(format.getFontColor(), null));
        style.setFont(fnt);
        style.setWrapText(format.isWrapped());
        style.setShrinkToFit(true);
    }
    
    public CellStyle cCopyStyle(Workbook wb, FillPatternType fill) {
        XSSFColor color;
        XSSFFont fnt = (XSSFFont) wb.createFont();
        XSSFDataFormat xdf = (XSSFDataFormat) wb.createDataFormat();
        XSSFCellStyle cStyle = (XSSFCellStyle) wb.createCellStyle();
        cStyle.setAlignment(getHAlignment());
        cStyle.setVerticalAlignment(getVAlignment());
        color = new XSSFColor(format.getBorderColor(), null);
        cStyle.setBorderTop( getBorderStyle(0) );
        cStyle.setTopBorderColor(color);
        cStyle.setBorderRight( getBorderStyle(1) );
        cStyle.setRightBorderColor(color);
        cStyle.setBorderBottom( getBorderStyle(2) );
        cStyle.setBottomBorderColor(color);
        cStyle.setBorderLeft( getBorderStyle(3) );
        cStyle.setLeftBorderColor(color);
        if ( format.getForeColor() != null ) {
            cStyle.setFillForegroundColor(new XSSFColor(format.getForeColor(), null));
            cStyle.setFillPattern(fill);
        }
        if ( format.getBackColor() != null ) {
            cStyle.setFillBackgroundColor(new XSSFColor(format.getBackColor(), null));
        }
        cStyle.setWrapText(format.isWrapped());
        cStyle.setShrinkToFit(format.isShrinked());
        cStyle.setRotation((short) format.getRotation());
        cStyle.setDataFormat(getDataFormat(xdf));
        fnt.setBold(format.isBold());
        fnt.setFontName(format.getFontFamily());
        fnt.setFontHeight(format.getFontSize());
        fnt.setColor(new XSSFColor(format.getFontColor(), null));
        cStyle.setFont(fnt);
        cStyle.setWrapText(format.isWrapped());
        cStyle.setShrinkToFit(true);
        return cStyle;
    }
    
    private HorizontalAlignment getHAlignment() {
        if ( format.getHAlignment() == HORIZONTAL_ALIGNMENT.LEFT )
            return HorizontalAlignment.LEFT;
        if ( format.getHAlignment() == HORIZONTAL_ALIGNMENT.RIGHT )
            return HorizontalAlignment.RIGHT;
        return HorizontalAlignment.CENTER;
    }
    
    private VerticalAlignment getVAlignment() {
        if ( format.getVAlignment() == VERTICAL_ALIGNMENT.TOP )
            return VerticalAlignment.TOP;
        if ( format.getVAlignment() == VERTICAL_ALIGNMENT.BOTTOM )
            return VerticalAlignment.BOTTOM;
        return VerticalAlignment.CENTER;
    }
    
    private BorderStyle getBorderStyle(int index) {
        if ( format.getBorder(index) == 0 )
            return BorderStyle.NONE;
        if ( format.getBorder(index) == 1 )
            return BorderStyle.THIN;
        if ( format.getBorder(index) == 1.5f )
            return BorderStyle.MEDIUM;
        if ( format.getBorder(index) == 2 )
            return BorderStyle.DOUBLE;
        if ( format.getBorder(index) == 3 )
            return BorderStyle.THICK;
        return BorderStyle.DOTTED;
    }
    
    private short getDataFormat(XSSFDataFormat xdf) {
        if ( format.getDataType().equals(DATA_TYPE.INTEGER) )
            return xdf.getFormat("00");
        if ( format.getDataType().equals(DATA_TYPE.DECIMAL) )
            return xdf.getFormat("00.00");
        if ( format.getDataType().equals(DATA_TYPE.DATE) )
            return xdf.getFormat("yyyy/mm/dd");
        return xdf.getFormat("@");
    }

    public int getColSpan() {
        return colSpan + 1;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan - 1;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }
    
    public enum STYLE {
        NORMAL,
        HIGHLIGHTED
    }
    
}
