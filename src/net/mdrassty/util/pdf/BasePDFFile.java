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

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;
import net.mdrassty.util.ALIGNMENT;
import net.mdrassty.util.Misc;
import org.apache.pdfbox.text.TextPosition;

public class BasePDFFile {
    protected PDDocument doc;
    protected String tpl, path;
    protected final int grayingGap;
    protected final float lineGap = 6f;
    private final double TWO_PIS = Math.PI * 2;
    private final double HALF_PI = Math.PI / 2, THIRD_PI = Math.PI / 3;
    protected PDFont tahomaBd, times, timesBd, trad, tradBd, tahoma, maghribi, tifinaghe, icomoon;
    protected float pageHeight;
    protected final ArabicShaping AR_SHAPING = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
    
    public BasePDFFile(){
        grayingGap = 24;
        tahomaBd = times = timesBd = trad = tradBd = tahoma = maghribi = tifinaghe = icomoon = null;
    }
    
    public final void reset(){
        try {
            doc = PDDocument.load(getClass().getResource(tpl).openStream());
            pageHeight = doc.getPage(0).getBBox().getHeight();
        } catch (IOException ex) {
            doc = null;
        }
    }
    
    public final void reset(String tpl){
        this.tpl = tpl;
        reset();
    }
    
    public final void load(File f){
        try {
            doc = PDDocument.load(f);
            path = f.getAbsolutePath();
            pageHeight = doc.getPage(0).getBBox().getHeight();
        } catch (IOException ex) {
            doc = null;
        }
    }
    
    public final void reload(){
        close();
        try {
            doc = PDDocument.load(new File(path));
        } catch (IOException ex) {
            doc = null;
        }
    }
    
    public boolean isLoaded() {
        return doc != null;
    }
    
    public void create() {
        doc = new PDDocument();
    }
	
//    private void loadFonts() {
//        try {
//            tahomaBd = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TAHOMA));
//            tahoma = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TAHOMA_NORMAL));
//            tradBd = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TRAD_AR));
//            trad = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TRAD_AR_NORMAL));
//            times = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TIMES_NORMAL));
//            timesBd = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TIMES));
//            maghribi = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.MAGHRIBI));
//            tifinaghe = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TIFINAGH));
//            icomoon = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.ICOMOON));
//        } catch (IOException ex) { }
//    }
    
    public boolean save(String path) {
        try ( FileOutputStream fos = new FileOutputStream(path) ) {
            doc.save(fos);
            doc.close();
            doc = null;
            return true;
        } catch (IOException ex) { 
            return false;
        }
    }
    
    public void close(){
        try {
            doc.close();
            doc = null;
        }
        catch (IOException | NullPointerException e){ }
    }
    
    public String getLocation(){
        return (new File(path)).getParent();
    }
    
    public int getNumberOfPages(){
        return doc.getNumberOfPages();
    }
    
    public PDPage getPage(int page){
        PDPage pg = doc.getPage(page);
        return pg;
    }
    
    public String getPageContent(int page){
        try {
            String cnt;
            PDFTextStripper ps = new PDFTextStripper();
            ps.setStartPage(page + 1);
            ps.setEndPage(page + 1);
            cnt = ps.getText(doc);
            return cnt;
        }
        catch(IOException e){
            return "";
        }
    }
    
    public void addPage(PDPage pg) {
        doc.addPage(pg);
    }
    
    protected void removePage(int index) {
        doc.removePage(index);
    }
    
    public void duplicate() {
        for ( int i = 0, n = doc.getNumberOfPages(); i < n; i++ ) {
            clonePage(getPage(i));
        }
    }
    
    public void duplicate(int startPage) {
        for ( int i = startPage, n = doc.getNumberOfPages(); i < n; i++ ) {
            clonePage(getPage(i));
        }
    }
    
    protected PDPage clonePage(PDPage srcPg){ // The page is added in document
        COSDictionary dict = new COSDictionary(srcPg.getCOSObject());
        dict.removeItem(COSName.ANNOTS);
        PDPage np = new PDPage(dict);
        List<PDStream> x = new ArrayList();
        Iterator<PDStream> y = srcPg.getContentStreams();
        while (y.hasNext()){
            x.add(y.next());
        }
        np.setContents(x);
        doc.addPage(np);
        return np;
    }
    
    protected PDPage addNewPage(){
        PDPage np = new PDPage();
        np.setMediaBox(doc.getPage(0).getMediaBox());
        doc.addPage(np);
        return np;
    }
    
    public PDPage clonePage(int index){
        return clonePage(doc.getPage(index));
    }
    
    protected PDFont getFont(String name) {
        switch ( name ) {
            case AvailableFonts.TAHOMA_BOLD:
                if ( tahomaBd == null )
                    try {
                        tahomaBd = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TAHOMA_BOLD));
                    } catch (IOException ex) { }
                return tahomaBd;
            case AvailableFonts.TAHOMA:
                if ( tahoma == null )
                    try {
                        tahoma = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TAHOMA));
                    } catch (IOException ex) { }
                return tahoma;
            case AvailableFonts.TRAD_AR_BOLD:
                if ( tradBd == null )
                    try {
                        tradBd = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TRAD_AR_BOLD));
                    } catch (IOException ex) { }
                return tradBd;
            case AvailableFonts.TRAD_AR:
                if ( trad == null )
                    try {
                        trad = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TRAD_AR));
                    } catch (IOException ex) { }
                return trad;
            case AvailableFonts.TIMES_BOLD:
                if ( timesBd == null )
                    try {
                        timesBd = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TIMES_BOLD));
                    } catch (IOException ex) { }
                return timesBd;
            case AvailableFonts.MAGHRIBI:
                if ( maghribi == null )
                    try {
                        maghribi = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.MAGHRIBI));
                    } catch (IOException ex) { }
                return maghribi;
            case AvailableFonts.TIFINAGH_BOLD:
                if ( tifinaghe == null )
                    try {
                        tifinaghe = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TIFINAGH_BOLD));
                    } catch (IOException ex) { }
                return tifinaghe;
            case AvailableFonts.ICOMOON:
                if ( icomoon == null )
                    try {
                        icomoon = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.ICOMOON));
                    } catch (IOException ex) { }
                return icomoon;
            default:
                if ( times == null )
                    try {
                        times = PDType0Font.load(doc, getClass().getResourceAsStream(AvailableFonts.TIMES));
                    } catch (IOException ex) { }
                return times;
        }
    }
    
    protected Short getAlignment(String alignment) {
        switch ( alignment ) {
            case "L":
                return ALIGNMENT.LEFT;
            case "R":
                return ALIGNMENT.RIGHT;
            default:
                return ALIGNMENT.CENTER;
        }
    }
    
    public void placeString(LARectangle rectangle, PDPageContentStream pcs, String str) throws IOException, ArabicShapingException {
        float fntSize, coef;
        PDFont fnt;
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        coef = fntSize / 1000f;
        if ( Misc.isArabic(str) )
            str = Misc.cleanReversedAR(new StringBuilder(AR_SHAPING.shape(str)).reverse().toString());
        pcs.setFont(fnt, fntSize);
        pcs.beginText();
        pcs.newLineAtOffset(rectangle.getTransformedX(fnt.getStringWidth(str) * coef), rectangle.getTransformedY(pageHeight, ( fnt.getFontDescriptor().getCapHeight()) * coef));
        pcs.showText( str );
        pcs.endText();
    }
    
    public void placeString(LARectangle rectangle, PDPage pg, String str) {
        float fntSize, coef;
        PDFont fnt;
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        coef = fntSize / 1000f;
        try (PDPageContentStream pcs = new PDPageContentStream(doc, pg, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            if ( Misc.isArabic(str) )
                str = Misc.cleanReversedAR(new StringBuilder(AR_SHAPING.shape(str)).reverse().toString());
            pcs.setFont(fnt, fntSize);
            pcs.beginText();
            pcs.newLineAtOffset(rectangle.getTransformedX(fnt.getStringWidth(str) * coef), rectangle.getTransformedY(pageHeight, ( fnt.getFontDescriptor().getCapHeight()) * coef));
            pcs.showText( str );
            pcs.endText();
        } catch (IOException | ArabicShapingException ex) {  }
    }
    
    public void placeString(LARectangle rectangle, PDPageContentStream pcs, String str, short hAlign) throws IOException, ArabicShapingException {
        float fntSize, coef;
        PDFont fnt;
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        coef = fntSize / 1000f;
        if ( Misc.isArabic(str) )
            str = Misc.cleanReversedAR(new StringBuilder(AR_SHAPING.shape(str)).reverse().toString());
        pcs.setFont(fnt, fntSize);
        pcs.beginText();
        pcs.newLineAtOffset(rectangle.getTransformedX(fnt.getStringWidth(str) * coef, hAlign), rectangle.getTransformedY(pageHeight, ( fnt.getFontDescriptor().getCapHeight()) * coef));
        pcs.showText( str );
        pcs.endText();
    }
    
    public void placeString(LARectangle rectangle, PDPageContentStream pcs, Matrix mx, String str, short hAlign) throws IOException, ArabicShapingException {
        float fntSize, coef;
        PDFont fnt;
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        coef = fntSize / 1000f;
        if ( Misc.isArabic(str) )
            str = Misc.cleanReversedAR(new StringBuilder(AR_SHAPING.shape(str)).reverse().toString());
        pcs.setFont(fnt, fntSize);
        pcs.beginText();
        pcs.setTextMatrix(mx);
        pcs.newLineAtOffset(rectangle.getTransformedX(fnt.getStringWidth(str) * coef, hAlign), rectangle.getTransformedY(pageHeight, ( fnt.getFontDescriptor().getCapHeight()) * coef));
        pcs.showText( str );
        pcs.endText();
    }
    
    public String prepareMutiline(LARectangle rectangle, String str) throws ArabicShapingException, IOException {
        String result = "";
        String line = "";
        float fntSize, coef, w;
        PDFont fnt;
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        w = rectangle.getWidth();
        coef = fntSize / 1000f;
        String[] parts = str.replace("\n", " ").split(" ");
        for ( String part : parts ) {
            String tmp;
            if ( Misc.isArabic(part) ) {
                part = new StringBuilder(AR_SHAPING.shape(part)).reverse().toString();
                tmp = part + " " + line;
            }
            else {
                tmp = line + " " + part;
            }
            if ( fnt.getStringWidth(tmp) * coef > w ) {
                result += line.trim() + "\n";
                line = part;
            }
            else {
                line = tmp;
            }
        }
        if ( !line.isEmpty() )
            result += line;
        return result;
    }
    
    public void placeMultilineString(LARectangle rectangle, PDPageContentStream pcs, String str) throws IOException, ArabicShapingException, CloneNotSupportedException {
        String[] lines = str.split("\n");
        int n = lines.length;
        float h = rectangle.getHeight() / n;
        for ( int i = 0; i < n; i++ ) {
            LARectangle box = (LARectangle) rectangle.clone();
            box.setHeight(box.getHeight() / n);
            box.setY(box.getY() + h * i);
            placeString(box, pcs, lines[i]);
        }
    }
    
    public void placeMultilineString(LARectangle rectangle, PDPage pg, String str) {
        try (PDPageContentStream pcs = new PDPageContentStream(doc, pg, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            String[] lines = str.split("\n");
            int n = lines.length;
            float h = rectangle.getHeight() / n;
            for ( int i = 0; i < n; i++ ) {
                LARectangle box = (LARectangle) rectangle.clone();
                box.setHeight(box.getHeight() / n);
                box.setY(box.getY() + h * i);
                placeString(box, pcs, lines[i]);
            }
        } catch (IOException | ArabicShapingException | CloneNotSupportedException ex) {  }
    }
    
    public void placeNResizeMultilineString(LARectangle rectangle, PDPageContentStream pcs, String str) throws IOException, ArabicShapingException, CloneNotSupportedException {
        String[] lines = str.split("\n");
        int n = lines.length;
        float h = rectangle.getHeight() / n;
        float height;
        height = getFont(rectangle.getFont()).getFontDescriptor().getCapHeight() * rectangle.getFontSize() / 1000f;
        if ( height > ( h - lineGap ) ) {
            float org = rectangle.getFontSize();
            rectangle.setFontSize(rectangle.getFontSize() - 0.2f);
            BasePDFFile.this.placeNResizeMultilineString(rectangle, pcs, prepareMutiline(rectangle, str));
            rectangle.setFontSize(org);
        }
        else {
            for ( int i = 0; i < n; i++ ) {
                LARectangle box = (LARectangle) rectangle.clone();
                box.setHeight(h);
                box.setY(box.getY() + h * i);
                placeString(box, pcs, lines[i]);
            }
        }
    }
    
    public void resizeNplaceString(LARectangle rectangle, PDPageContentStream pcs, String str) throws IOException, ArabicShapingException, CloneNotSupportedException {
        float org = rectangle.getFontSize();
        float w = getFont(rectangle.getFont()).getStringWidth(str) * org / 1000f;
        if ( w > rectangle.getWidth() ) {
            rectangle.setFontSize(rectangle.getFontSize() - 0.2f);
            resizeNplaceString(rectangle, pcs, str);
            rectangle.setFontSize(org);
        }
        else {
            placeString(rectangle, pcs, str);
        }
    }
    
    public void placeNResizeMultilineString(LARectangle rectangle, PDPageContentStream pcs, String str, short hAlign) throws IOException, ArabicShapingException, CloneNotSupportedException {
        String[] lines = prepareMutiline(rectangle, str).split("\n");
        int n = lines.length;
        float h = rectangle.getHeight() / n;
        float height;
        height = getFont(rectangle.getFont()).getFontDescriptor().getCapHeight() * rectangle.getFontSize() / 1000f;
        if ( height > ( h - lineGap ) ) {
            float org = rectangle.getFontSize();
            rectangle.setFontSize(rectangle.getFontSize() - 0.2f);
            placeNResizeMultilineString(rectangle, pcs, str, hAlign);
            rectangle.setFontSize(org);
        }
        else {
            for ( int i = n - 1; i >= 0; i-- ) {
                LARectangle box = (LARectangle) rectangle.clone();
                box.setHeight(h);
                box.setY(box.getY() + h * i);
                placeString(box, pcs, lines[i], hAlign);
            }
        }
    }
    
    public void placeMultilineString(LARectangle rectangle, PDPageContentStream pcs, String str, short hAlign) throws IOException, ArabicShapingException, CloneNotSupportedException {
        String[] lines = str.split("\n");
        int n = lines.length;
        float h = rectangle.getHeight() / n;
        for ( int i = 0; i < n; i++ ) {
            LARectangle box = (LARectangle) rectangle.clone();
            box.setHeight(box.getHeight() / n);
            box.setY(box.getY() + h * i);
            placeString(box, pcs, lines[i], hAlign);
        }
    }
    
    public void placeMultilineString(LARectangle rectangle, PDPageContentStream pcs, Matrix mx, String str, short hAlign) throws IOException, ArabicShapingException, CloneNotSupportedException {
        String[] lines = str.split("\n");
        int n = lines.length;
        float h = rectangle.getHeight() / n;
        for ( int i = 0; i < n; i++ ) {
            LARectangle box = (LARectangle) rectangle.clone();
            box.setHeight(box.getHeight() / n);
            box.setY(box.getY() + h * i);
            placeString(box, pcs, mx, lines[i], hAlign);
        }
    }
        
    public Double getContentLastYCord(int page) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void startPage(PDPage page) throws IOException {
                super.startPage(page);
            }

            @Override
            protected void endPage(PDPage page) throws IOException {
                super.endPage(page);
            }

            @Override
            protected void writeLineSeparator() throws IOException {
                super.writeLineSeparator();
            }

            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                TextPosition firstProsition = textPositions.get(0);
                String str = firstProsition.getYDirAdj() + "";
                if ( !"".equals(str) ) {
                    writeString(str.trim());
                    writeLineSeparator();
                }
            }
        };
        stripper.setStartPage(page);
        stripper.setEndPage(page);
        stripper.setLineSeparator(";");
        return Arrays.asList(stripper.getText(doc).replaceAll(" ", "").split(";")).stream().mapToDouble(str ->  {
            try {
                return Double.parseDouble(str);
            } catch ( NumberFormatException nfe ) {
                return 0;
            }
        }).max().getAsDouble();
    }
        
    public Double getContentLastYCord(int page, float maxX) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void startPage(PDPage page) throws IOException {
                super.startPage(page);
            }

            @Override
            protected void endPage(PDPage page) throws IOException {
                super.endPage(page);
            }

            @Override
            protected void writeLineSeparator() throws IOException {
                super.writeLineSeparator();
            }

            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                TextPosition firstProsition = textPositions.get(0);
                if ( firstProsition.getXDirAdj() > maxX )
                    return;
                String str = firstProsition.getYDirAdj() + "";
                if ( !"".equals(str) ) {
                    writeString(str.trim());
                    writeLineSeparator();
                }
            }
        };
        stripper.setStartPage(page);
        stripper.setEndPage(page);
        stripper.setLineSeparator(";");
        return Arrays.asList(stripper.getText(doc).replaceAll(" ", "").split(";")).stream().mapToDouble(str ->  {
            try {
                return Double.parseDouble(str);
            } catch ( NumberFormatException nfe ) {
                return 0;
            }
        }).max().getAsDouble();
    }
        
    public Double getContentLastYCord(int page, float minX, float maxX) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void startPage(PDPage page) throws IOException {
                super.startPage(page);
            }

            @Override
            protected void endPage(PDPage page) throws IOException {
                super.endPage(page);
            }

            @Override
            protected void writeLineSeparator() throws IOException {
                super.writeLineSeparator();
            }

            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                TextPosition firstProsition = textPositions.get(0);
                float x = firstProsition.getXDirAdj();
                if ( x > maxX || x < minX )
                    return;
                String str = firstProsition.getYDirAdj() + "";
                if ( !"".equals(str) ) {
                    writeString(str.trim());
                    writeLineSeparator();
                }
            }
        };
        stripper.setStartPage(page);
        stripper.setEndPage(page);
        stripper.setLineSeparator(";");
        return Arrays.asList(stripper.getText(doc).replaceAll(" ", "").split(";")).stream().mapToDouble(str ->  {
            try {
                return Double.parseDouble(str);
            } catch ( NumberFormatException nfe ) {
                return 0;
            }
        }).max().getAsDouble();
    }
        
    public String getContentPosistion(int page) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void startPage(PDPage page) throws IOException {
                super.startPage(page);
            }

            @Override
            protected void endPage(PDPage page) throws IOException {
                super.endPage(page);
            }

            @Override
            protected void writeLineSeparator() throws IOException {
                super.writeLineSeparator();
            }

            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                TextPosition firstProsition = textPositions.get(0);
                super.writeString(firstProsition.getYDirAdj() + "");
                writeLineSeparator();
            }
        };
        stripper.setStartPage(page);
        stripper.setEndPage(page);
        stripper.setLineSeparator("\n");
        return stripper.getText(doc);
    }
    
//    protected void placeAndRotateString(LARectangle rectangle, PDPageContentStream pcs, String str, double theta) throws IOException, ArabicShapingException {
//        float fntSize, coef;
//        PDFont fnt;
//        Matrix mx;
//        float x, y;
//        fntSize = rectangle.getFontSize();
//        fnt = getFont(rectangle.getFont());
//        coef = fntSize / 1000f;
//        coef /= 2;
//        x = rectangle.getTransformedX();
//        y = rectangle.getTransformedY(pageHeight);
//        if ( Misc.isArabic(str) )
//            str = Misc.cleanReversedAR(new StringBuilder(AR_SHAPING.shape(str)).reverse().toString());
//        pcs.setFont(fnt, fntSize);
//        pcs.beginText();
//        mx = new Matrix();
//        mx.translate(x + 100, 0);
//        mx.rotate(theta);
////        mx.translate(rectangle.getHeight() / 2 - fnt.getStringWidth(str) * coef, rectangle.getWidth() / 2 - (fnt.getFontDescriptor().getCapHeight() - fnt.getFontDescriptor().getDescent()) * coef);
//        pcs.setTextMatrix(mx);
//        pcs.showText(str);
//        pcs.endText();
//    }
    
    public void placeAndMinusPiRotateString(LARectangle rectangle, PDPageContentStream pcs, String str) throws IOException, ArabicShapingException {
        float fntSize, coef;
        PDFont fnt;
        Matrix mx;
        float x, y;
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        coef = fntSize / 1000f;
        coef /= 2;
        x = rectangle.getTransformedX();
        y = rectangle.getTransformedY(pageHeight);
        if ( Misc.isArabic(str) )
            str = Misc.cleanReversedAR(new StringBuilder(AR_SHAPING.shape(str)).reverse().toString());
        pcs.setFont(fnt, fntSize);
        pcs.beginText();
        mx = new Matrix();
        mx.translate(x, y);
        mx.rotate(-Math.PI / 2);
        mx.translate(rectangle.getHeight() / 2 - fnt.getStringWidth(str) * coef, rectangle.getWidth() / 2 - (fnt.getFontDescriptor().getCapHeight() - fnt.getFontDescriptor().getDescent()) * coef);
        pcs.setTextMatrix(mx);
        pcs.showText(str);
        pcs.endText();
    }
    
    public void placeAndRotateString(LARectangle rectangle, PDPageContentStream pcs, String str, double theta) throws IOException, ArabicShapingException {
        float fntSize, coef;
        PDFont fnt;
        Matrix mx;
        float x, y;
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        coef = fntSize / 1000f;
        coef /= 2;
        x = rectangle.getTransformedX();
        y = rectangle.getTransformedY(pageHeight);
        if ( Misc.isArabic(str) )
            str = Misc.cleanReversedAR(new StringBuilder(AR_SHAPING.shape(str)).reverse().toString());
        pcs.setFont(fnt, fntSize);
        pcs.beginText();
        mx = new Matrix();
        mx.translate(x, y);
        mx.rotate(theta);
//        mx.translate(0, 20);
        mx.translate(rectangle.getHeight() / 2 - fnt.getStringWidth(str) * coef, -rectangle.getWidth() / 2 + (fnt.getFontDescriptor().getCapHeight() - fnt.getFontDescriptor().getDescent()) * coef);
        pcs.setTextMatrix(mx);
        pcs.showText(str);
        pcs.endText();
    }
    
    public void placeBidirectionalString(LARectangle rectangle, PDPageContentStream pcs, String string1, String string2) throws IOException, ArabicShapingException {
        float fntSize, coef;
        PDFont fnt;
        String str;
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        coef = fntSize / 1000f;
        if ( Misc.isArabic(string1) )
            string1 = new StringBuilder(AR_SHAPING.shape(string1)).reverse().toString();
        if ( Misc.isArabic(string2) )
            string2 = new StringBuilder(AR_SHAPING.shape(string2)).reverse().toString();
        str = string1 + " " + string2;
        pcs.setFont(fnt, fntSize);
        pcs.beginText();
        pcs.newLineAtOffset(rectangle.getTransformedX(fnt.getStringWidth(str) * coef), rectangle.getTransformedY(pageHeight, ( fnt.getFontDescriptor().getCapHeight()) * coef));
        pcs.showText( str );
        pcs.endText();
    }

    public void placeBidirectionalString(LARectangle rectangle, PDPageContentStream pcs, String string) throws IOException, ArabicShapingException {
        float fntSize, coef;
        PDFont fnt;
        String str = "";
        String parts[] = string.split(" ");
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        coef = fntSize / 1000f;
        for ( String part : parts ) {
            str = ( ( Misc.isArabic(part) ) ? new StringBuilder(AR_SHAPING.shape(part)).reverse().toString() : part ) + " " + str;
        }
        str = str.trim();
        pcs.setFont(fnt, fntSize);
        pcs.beginText();
        pcs.newLineAtOffset(rectangle.getTransformedX(fnt.getStringWidth(str) * coef), rectangle.getTransformedY(pageHeight, ( fnt.getFontDescriptor().getCapHeight()) * coef));
        pcs.showText( str );
        pcs.endText();
    }

    public void placeBidirectionalString(LARectangle rectangle, PDPageContentStream pcs, String string, short hAlign) throws IOException, ArabicShapingException {
        float fntSize, coef;
        PDFont fnt;
        String str = "";
        String parts[] = string.split(" ");
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        coef = fntSize / 1000f;
        for ( String part : parts ) {
            str = ( ( Misc.isArabic(part) ) ? new StringBuilder(AR_SHAPING.shape(part)).reverse().toString() : part ) + " " + str;
        }
        str = str.trim();
        pcs.setFont(fnt, fntSize);
        pcs.beginText();
        pcs.newLineAtOffset(rectangle.getTransformedX(fnt.getStringWidth(str) * coef, hAlign), rectangle.getTransformedY(pageHeight, ( fnt.getFontDescriptor().getCapHeight()) * coef));
        pcs.showText( str );
        pcs.endText();
    }

    public void placeBidirectionalString(LARectangle rectangle, PDPage page, String string, short hAlign) {
        float fntSize, coef;
        PDFont fnt;
        String str = "";
        String parts[] = string.split(" ");
        fntSize = rectangle.getFontSize();
        fnt = getFont(rectangle.getFont());
        coef = fntSize / 1000f;
        try (PDPageContentStream pcs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false)) {
            for ( String part : parts ) {
                str = ( ( Misc.isArabic(part) ) ? new StringBuilder(AR_SHAPING.shape(part)).reverse().toString() : part ) + " " + str;
            }
            str = str.trim();
            pcs.setFont(fnt, fntSize);
            pcs.beginText();
            pcs.newLineAtOffset(rectangle.getTransformedX(fnt.getStringWidth(str) * coef, hAlign), rectangle.getTransformedY(pageHeight, ( fnt.getFontDescriptor().getCapHeight()) * coef));
            pcs.showText( str );
            pcs.endText();
        } catch (IOException | ArabicShapingException ex) {  }
    }

    public void gray(PDPageContentStream pcs, LARectangle rect, Boolean whiten) throws IOException {
        float startX, endX, startY, endY, x, y, wX, hY, tg;
        x = startX = rect.getX();
        y = startY = rect.getTransformedY(pageHeight);
        wX = rect.getWidth();
        hY = rect.getHeight();
        if ( whiten ) {
            pcs.setNonStrokingColor(Color.WHITE);
            pcs.addRect(x, y, wX, -hY);
            pcs.fill();
        }
        endX = x + wX + hY + 0.001f;
        endY = y - hY - wX + 0.001f;
        tg = (float) Math.tan(Math.PI / 4);
        hY = startY - hY;
        wX = startX + wX;
        pcs.setStrokingColor(new Color(128, 128, 128));
        while ( (x += grayingGap) < endX && (y -= grayingGap) > endY ) {
            if ( y < hY ) {
                pcs.moveTo(startX + (hY - y) * tg, hY);
            }
            else {
                pcs.moveTo(startX, y);
            }
            if ( x > wX ) {
                pcs.lineTo(wX, startY - (x - wX) * tg);
            }
            else {
                pcs.lineTo(x, startY);
            }
        }
        pcs.closeAndStroke();
        pcs.setNonStrokingColor(Color.BLACK);
    }

    public void drawPieChart(PDPageContentStream pcs, LARectangle rect, ArrayList<Long> arr, ArrayList<Color> cols) throws IOException {
        float rad =  rect.getWidth() / 2;
        float cx = rect.getTransformedX() + rad;
        float cy = rect.getTransformedY(pageHeight) - rad;
        double startAngle = HALF_PI, endAngle, tmp;
        double sum = arr.stream().collect(Collectors.summingDouble(a -> a));
        double coef = TWO_PIS / sum;
        pcs.saveGraphicsState();
        pcs.transform(Matrix.getTranslateInstance(cx, cy));
        for ( int i = 0, n = arr.size(); i < n; i++ ) {
            endAngle = arr.get(i) * coef + startAngle;
            while ( endAngle - startAngle > THIRD_PI ) {
                tmp = THIRD_PI + startAngle;
                drawSlice(pcs, cols.get(i), rad, startAngle, tmp);
                startAngle = tmp;
            }
            if ( startAngle != endAngle )
                drawSlice(pcs, cols.get(i), rad, startAngle, endAngle);
            startAngle = endAngle;
        }
        pcs.restoreGraphicsState();
        strokeCircle(pcs, cx, cy, rad, Color.BLACK);
    }

    public void drawSlice(PDPageContentStream pcs, Color color, float radius, double startAngle, double endAngle) throws IOException {
        ArrayList<Float> smallArc;
        pcs.moveTo(0, 0);
        smallArc = createSmallArc(radius, startAngle, endAngle);
        pcs.setNonStrokingColor(color);
        pcs.setStrokingColor(color);
        pcs.lineTo(smallArc.get(0), smallArc.get(1));
        pcs.curveTo(smallArc.get(2), smallArc.get(3), smallArc.get(4), smallArc.get(5), smallArc.get(6), smallArc.get(7));
        pcs.fillAndStroke();
        pcs.closePath();
        
    }
    
    public void strokeCircle(PDPageContentStream pcs, float cx, float cy, float radius, Color color) throws IOException {
        final float k = 0.552284749831f;
        pcs.moveTo(cx - radius, cy);
        pcs.curveTo(cx - radius, cy + k * radius, cx - k * radius, cy + radius, cx, cy + radius);
        pcs.curveTo(cx + k * radius, cy + radius, cx + radius, cy + k * radius, cx + radius, cy);
        pcs.curveTo(cx + radius, cy - k * radius, cx + k * radius, cy - radius, cx, cy - radius);
        pcs.curveTo(cx - k * radius, cy - radius, cx - radius, cy - k * radius, cx - radius, cy);
        pcs.setStrokingColor(color);
        pcs.setLineWidth(0.5f);
        pcs.stroke();
    }
    
    public boolean placeNRotateString(PDPage page, String text, float degree, float x, float y, int fntSize){
        Matrix mx;
        double angle, cosAngle, sinAngle;
        try (PDPageContentStream pdpcss = new PDPageContentStream(doc, page, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcss.beginText();
            pdpcss.setFont(getFont(AvailableFonts.TIMES), fntSize);
            mx = new Matrix();
            angle = degree * Math.PI / 180;
            cosAngle = Math.cos(angle);
            sinAngle = Math.sin(angle);
            mx.rotate(angle);
            mx.translate((float) (x * cosAngle + y * sinAngle), (float) (y * cosAngle - x * sinAngle));
            pdpcss.setTextMatrix(mx);
            try {
                    pdpcss.showText(text);
                }
                catch ( IllegalArgumentException iae ) {}
            pdpcss.endText();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
    public boolean placeImage(PDPage page, PDImageXObject img, float x, float y){
        try (PDPageContentStream pdpcs = new PDPageContentStream(doc, page, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcs.drawImage(img, x, y);
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
    public boolean placeImage(PDPage page, PDImageXObject img, float x, float y, float width, float height){
        try (PDPageContentStream pdpcs = new PDPageContentStream(doc, page, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcs.drawImage(img, x, y, width, height);
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
    private ArrayList<Float> createSmallArc(double r, double a1, double a2) {
        double a = (a2 - a1) / 2;
        double x4 = r * Math.cos(a);
        double y4 = r * Math.sin(a);
        double x1 = x4;
        double y1 = -y4;
        double q1 = x1*x1 + y1*y1;
        
        double q2 = q1 + x1*x4 + y1*y4;
        double k2 = 4/3d * (Math.sqrt(2 * q1 * q2) - q2) / (x1 * y4 - y1 * x4);
        double x2 = x1 - k2 * y1;
        double y2 = y1 + k2 * x1;
        double x3 = x2; 
        double y3 = -y2;
        
        double ar = a + a1;
        double cos_ar = Math.cos(ar);
        double sin_ar = Math.sin(ar);
        
        ArrayList<Float> list = new ArrayList();
        list.add((float) (r * Math.cos(a1)));
        list.add((float) (r * Math.sin(a1))); 
        list.add((float) (x2 * cos_ar - y2 * sin_ar)); 
        list.add((float) (x2 * sin_ar + y2 * cos_ar)); 
        list.add((float) (x3 * cos_ar - y3 * sin_ar)); 
        list.add((float) (x3 * sin_ar + y3 * cos_ar)); 
        list.add((float) (r * Math.cos(a2))); 
        list.add((float) (r * Math.sin(a2)));
        return list;
    }

    public void drawNFill(LARectangle rect, PDPageContentStream pcs) throws IOException {
        pcs.setNonStrokingColor(rect.getColor());
        pcs.addRect(rect.getTransformedX(), rect.getTransformedY(pageHeight), rect.getWidth(), rect.getHeight());
        pcs.fill();
        pcs.setNonStrokingColor(Color.BLACK);
    }

    public void drawNFill(LARectangle rect, PDPage page) throws IOException {
        try (PDPageContentStream pcs = new PDPageContentStream(doc, page, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pcs.setNonStrokingColor(rect.getColor());
            pcs.addRect(rect.getTransformedX(), rect.getTransformedY(pageHeight), rect.getWidth(), rect.getHeight());
            pcs.fill();
            pcs.setNonStrokingColor(Color.BLACK);
        }
        catch (IOException e){ }
    }
    
    public String generateFileName(String name, String extension) {
        return "/" + name + " " + DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss").format(LocalDateTime.now()) + "." + extension;
    }

    public String getTpl() {
        return tpl;
    }

    public void setTpl(String tpl) {
        this.tpl = tpl;
    }

    public PDDocument getDoc() {
        return doc;
    }

    public void setPageHeight(float pageHeight) {
        this.pageHeight = pageHeight;
    }
    
}
