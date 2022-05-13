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

package net.mdrassty.util.excel;


import java.io.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

/**
 *
 * @author Surfer
 * @updated by Sicut
 */
public abstract class XLSXWorkbook {
    
    protected Workbook wb;
    protected Sheet sht;
    
    public abstract boolean setWorkbook(File f);
    
    private String getStringValue_(Sheet sht, CellReference cr){
        DataFormatter df;
        df = new DataFormatter();
        try {
            return df.formatCellValue(sht.getRow(cr.getRow()).getCell(cr.getCol()));
        } catch ( NullPointerException npe ) {
            return "";
        }
    }
    
    private Double getNumericValue_(Sheet sht, CellReference cr){
        try {
            return sht.getRow(cr.getRow()).getCell(cr.getCol()).getNumericCellValue();
        } catch ( NullPointerException npe ) {
            return null;
        }
        catch ( IllegalStateException ise ) {
            try {
                return Double.parseDouble(getStringValue_(sht, cr));
            } catch ( NumberFormatException nfe ) {
                return null;
            }
        }
    }
    
    protected boolean isEmpty(Sheet sht, String cellRef){
        return "".equals(getStringValue_(sht, new CellReference(cellRef)));
    }
    
    protected boolean isEmpty(Sheet sht, int row, int col){
        return "".equals(getStringValue_(sht, new CellReference(row, col)));
    }
    
    protected String getStringValue(Sheet sht, String cellRef){
        return getStringValue_(sht, new CellReference(cellRef));
    }
    
    protected String getStringValue(Sheet sht, int row, int col){
        return getStringValue_(sht, new CellReference(row, col));
    }
    
    protected Double getNumericValue(Sheet sht, String cellRef){
        return getNumericValue_(sht, new CellReference(cellRef));
    }
    
    protected Double getNumericValue(Sheet sht, int row, int col){
        return getNumericValue_(sht, new CellReference(row, col));
    }
    
    public void close() {
        try {
            wb.close();
        } catch (IOException | NullPointerException ex) {  }
    }
}
