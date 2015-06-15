package de.oganisyan.paraglidervario.widgets;

import android.widget.*;
import android.content.*;
import android.util.*;
import de.oganisyan.paraglidervario.*;
import android.content.res.*;
import android.graphics.*;
import android.text.*;

public class TextBox extends TextView
{
    private Paint paint;
    private CharSequence value;
    private float valueTextSize;
    
    public TextBox(final Context context) {
        super(context);
        this.value = "";
        this.valueTextSize = 32.0f;
        this.paint = new Paint();
    }
    
    public TextBox(final Context context, final AttributeSet set) {
        super(context, set);
        this.value = "";
        this.valueTextSize = 32.0f;
        this.paint = new Paint();
        this.init(context, set);
    }
    
    public TextBox(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.value = "";
        this.valueTextSize = 32.0f;
        this.paint = new Paint();
        this.init(context, set);
    }
    
    private void init(final Context context, final AttributeSet set) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.TextBox);
        //final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.TextBox);
        final String string = obtainStyledAttributes.getString(0);
        this.valueTextSize = obtainStyledAttributes.getDimension(1, this.valueTextSize);
        if (string != null) {
            this.value = string;
        }
        obtainStyledAttributes.recycle();
    }
    
    public CharSequence getValue() {
        return this.value;
    }
    
    protected void onDraw(final Canvas canvas) {
        final float textSize = this.getTextSize();
        final TextPaint paint = this.getPaint();
        paint.setTextSize(this.valueTextSize);
        super.onDraw(canvas);
        paint.setTextSize(textSize);
        this.paint.setTextAlign(Paint.Align.RIGHT);
        this.paint.setTextSize(this.getTextSize());
        this.paint.setColor(-1);
        canvas.drawText(this.value.toString(), this.getWidth() - paint.getFontMetrics().descent, this.getHeight() - paint.getFontMetrics().bottom, this.paint);
    }
    
    public void setValue(final CharSequence value) {
        this.value = value;
        this.invalidate();
    }
}
