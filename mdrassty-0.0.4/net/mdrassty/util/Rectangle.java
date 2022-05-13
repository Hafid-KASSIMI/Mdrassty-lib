

package net.mdrassty.util;

public class Rectangle<T> implements Cloneable {
    protected float width, height;
    protected T x, y;
    protected Format format;

    public Rectangle(T x, T y, float width, float height) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(T x, T y, float width, float height, Format format) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.format = format;
    }

    public Rectangle() {
        
    }

    public Rectangle(Format format) {
        this.format = format;
    }
    
    public Rectangle(T x, T y, float width, float height, float fontSize, String font) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.format = new Format(fontSize, font);
    }
    
    public void reset(T x, T y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public void reset(T x, T y, float width, float height, Format format) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.format = format;
    }
    
    public void reset(T x, T y, float width, float height, float fontSize, String font) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.format = new Format(fontSize, font);
    }
    
    public void reset(T x, T y, float width, float height, int fontSize, String font, Boolean isBold) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.format = new Format(fontSize, font, isBold);
    }
    
    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
    
    public void setPosition(T x, T y) {
        this.x = x;
        this.y = y;
    }
    
    public void setSize(float h, float w) {
        this.height = h;
        this.width = w;
    }
    
    public void setFormat(float fontSize, String font) {
        this.format = new Format(fontSize, font);
    }
    
    public void setFormat(float fontSize, String font, Boolean isBold) {
        this.format = new Format(fontSize, font, isBold);
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        if ( format == null )
            format = new Format();
        return format;
    }
    
    public void setYH(T y, float h) {
        this.height = h;
        this.y = y;
    }
    
    public void setXW(T x, float w) {
        this.x = x;
        this.width = w;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "[ x : " + x + ", y : " + y + ", w : " + width + ", h : " + height + " ]";
    }
    
    public Rectangle transpose() {
        float tmp = width;
        width = height;
        height = tmp;
        return this;
    }
    
    public Rectangle getTransposed() {
        try {
            Rectangle rect = (Rectangle) this.clone();
            rect.setWidth(height);
            rect.setHeight(width);
            return rect;
        } catch ( CloneNotSupportedException ex ) {
            return null;
        }
    }
    
}
