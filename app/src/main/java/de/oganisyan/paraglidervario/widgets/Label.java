package de.oganisyan.paraglidervario.widgets;

import de.oganisyan.paraglidervario.model.*;
import android.graphics.*;

class Label
{
    static final float STD_HEIGHT = 800.0f;
    static final float STD_WIDTH = 480.0f;
    Paint.Align align;
    RectF bounds;
    private VarioModel.LabelModel labelModel;
    private RectF lastBounds;
    private float lastHeight;
    private float lastWidth;
    private float xPos;
    private float yPosText;
    private float yPosTitle;
    
    Label(final RectF rectF, final Paint.Align align) {
        super();
        this.lastWidth = 0.0f;
        this.lastHeight = 0.0f;
        this.xPos = 0.0f;
        this.yPosTitle = 0.0f;
        this.yPosText = 0.0f;
        this.labelModel = null;
        this.bounds = rectF;
        this.lastBounds = rectF;
        this.align = align;
    }
    
    private void recalc(final float lastWidth, final float lastHeight) {
        if (lastWidth != this.lastWidth || lastHeight != this.lastHeight) {
            final float n = lastWidth / STD_WIDTH ;
            final float n2 = lastHeight / STD_HEIGHT;
            this.lastWidth = lastWidth;
            this.lastHeight = lastHeight;
            this.lastBounds = new RectF(n * this.bounds.left, n2 * this.bounds.top, n * this.bounds.right, n2 * this.bounds.bottom);
            float xPos;
            if (this.align == Paint.Align.CENTER) {
                xPos = (this.lastBounds.left + this.lastBounds.right) / 2.0f;
            }
            else if (this.align == Paint.Align.LEFT) {
                xPos = this.lastBounds.left;
            }
            else {
                xPos = this.lastBounds.right;
            }
            this.xPos = xPos;
            this.yPosTitle = this.lastBounds.top + (this.lastBounds.bottom - this.lastBounds.top) / 3.0f;
            this.yPosText = this.lastBounds.bottom;
        }
    }
    
    void draw(final Canvas canvas, final Paint paint, final float n, final float n2) {
        if (this.labelModel != null) {
            this.recalc(n, n2);
            paint.setTextAlign(this.align);
            paint.setTextSize(this.lastHeight / 40.0f);
            canvas.drawText(this.labelModel.getTitle(), this.xPos, this.yPosTitle, paint);
            paint.setTextSize(this.lastHeight / 15.0f);
            canvas.drawText(this.labelModel.getValueString(), this.xPos, this.yPosText, paint);
        }
    }
    
    RectF getBounds(final float n, final float n2) {
        this.recalc(n, n2);
        return this.lastBounds;
    }
    
    void setLabelModel(final VarioModel.LabelModel labelModel) {
        this.labelModel = labelModel;
    }
}
