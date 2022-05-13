

package net.mdrassty.util.pdf;

import net.mdrassty.util.A4_PAPER;
import net.mdrassty.util.Format;
import net.mdrassty.util.HORIZONTAL_ALIGNMENT;
import net.mdrassty.util.Rectangle;
import net.mdrassty.util.VERTICAL_ALIGNMENT;

public class PDFRectangle extends Rectangle<Float> {
    private float userFloat;
    private float pageHeight = A4_PAPER.getPortraitHeight();
    private float inkscapeY;
//    private final float[] PADDING = { 2, 4, 2, 4 }; // Top, Right, Bottom, Left
//    private Boolean transform = true;

    public PDFRectangle(Float x, Float y, float width, float height, float pageHeight) {
        super(x, pageHeight - ( y + height ), width, height);
        inkscapeY = y;
        this.pageHeight = pageHeight;
    }
    
    public PDFRectangle(Float x, Float y, float width, float height) {
        super(x, y, width, height);
        this.y = pageHeight - ( y + height );
        inkscapeY = y;
    }

    public PDFRectangle(Float x, Float y, float width, float height, float pageHeight, Format format) {
        super(x, pageHeight - ( y + height ), width, height, format);
        inkscapeY = y;
        this.pageHeight = pageHeight;
    }

    public PDFRectangle(Float x, Float y, float width, float height, Format format) {
        super(x, y, width, height, format);
        this.y = pageHeight - ( y + height );
        inkscapeY = y;
    }

    public PDFRectangle() {
        
    }

    public PDFRectangle(float pageHeight) {
        this.pageHeight = pageHeight;
    }

    public PDFRectangle(Format format) {
        super(format);
    }

    public PDFRectangle(Float x, Float y, float width, float height, float pageHeight, float fontSize, String font) {
        super(x, pageHeight - ( y + height ), width, height, fontSize, font);
        inkscapeY = y;
        this.pageHeight = pageHeight;
    }

    public PDFRectangle(Float x, Float y, float width, float height, float fontSize, String font) {
        super(x, y, width, height, fontSize, font);
        this.y = pageHeight - ( y + height );
        inkscapeY = y;
    }

    @Override
    public void setYH(Float y, float h) {
        super.setHeight(h); 
        setY(y);
    }

    @Override
    public void setY(Float y) {
        this.y = pageHeight - ( y + height );
        inkscapeY = y;
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        setY(inkscapeY);
    }

    public void setMuteHeight(float height) {
        super.setHeight(height);
    }

    public void setInkscapeY(Float y) {
        super.setY(y);
        inkscapeY = pageHeight - ( y + height );
    }

    public float getInkscapeY() {
        return inkscapeY;
    }

    @Override
    public void reset(Float x, Float y, float width, float height, int fontSize, String font, Boolean isBold) {
        super.reset(x, y, width, height, fontSize, font, isBold);
        this.y = pageHeight - ( y + height );
        inkscapeY = y;
    }

    @Override
    public void reset(Float x, Float y, float width, float height, float fontSize, String font) {
        super.reset(x, y, width, height, fontSize, font);
        this.y = pageHeight - ( y + height );
        inkscapeY = y;
    }

    @Override
    public void reset(Float x, Float y, float width, float height, Format format) {
        super.reset(x, y, width, height, format);
        this.y = pageHeight - ( y + height );
        inkscapeY = y;
    }

    @Override
    public void reset(Float x, Float y, float width, float height) {
        super.reset(x, y, width, height);
        this.y = pageHeight - ( y + height );
        inkscapeY = y;
    }
    
    public float getInnerContentX(float contentWidth) {
        if ( format.getHAlignment() == HORIZONTAL_ALIGNMENT.RIGHT )
            return x + width - contentWidth;
        if ( format.getHAlignment() == HORIZONTAL_ALIGNMENT.CENTER )
            return x + ( width - contentWidth ) / 2;
        return x;
    }
    
    public float getInnerContentY(float contentHeight) {
        if ( format.getVAlignment() == VERTICAL_ALIGNMENT.TOP )
            return y + height - contentHeight;
        if ( format.getVAlignment() == VERTICAL_ALIGNMENT.MIDDLE )
            return y + ( height - contentHeight ) / 2;
        return y;
    }

    public float getUserFloat() {
        return userFloat;
    }

    public void setUserFloat(float userFloat) {
        this.userFloat = userFloat;
    }

    public float getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(float pageHeight) {
        this.pageHeight = pageHeight;
        setY(inkscapeY);
    }
    
    public static float convertInkscapeY(float y, float pageHeight) {
        return pageHeight - y;
    }
    
    public void applyMargin(float left, float right) {
        x += left;
        width -= (left + right);
    }
    
}
