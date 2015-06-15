package de.oganisyan.paraglidervario.widgets;

import android.widget.*;
import android.content.*;
import android.util.*;
import de.oganisyan.paraglidervario.*;
import android.content.res.*;
import android.graphics.drawable.*;
import android.graphics.*;

public class ImageButton extends ToggleButton
{
    Bitmap drawableDown;
    Bitmap drawableOff;
    Bitmap drawableOn;
    Paint paint;
    boolean shutdown;
    
    public ImageButton(final Context context) {
        super(context);
        this.drawableOff = null;
        this.drawableOn = null;
        this.drawableDown = null;
        this.paint = new Paint();
        this.shutdown = false;
    }
    
    public ImageButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.drawableOff = null;
        this.drawableOn = null;
        this.drawableDown = null;
        this.paint = new Paint();
        this.shutdown = false;
        this.initDrawable(context, set);
    }
    
    public ImageButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.drawableOff = null;
        this.drawableOn = null;
        this.drawableDown = null;
        this.paint = new Paint();
        this.shutdown = false;
        this.initDrawable(context, set);
    }
    
    private void initDrawable(Context context, AttributeSet set) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ImageButton);
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        if (drawable instanceof BitmapDrawable) {
            this.drawableOff = ((BitmapDrawable)drawable).getBitmap();
        }
        Drawable drawable2 = obtainStyledAttributes.getDrawable(1);
        if (drawable2 instanceof BitmapDrawable) {
            this.drawableOn = ((BitmapDrawable)drawable2).getBitmap();
        }
        obtainStyledAttributes.recycle();
    }
    
    public void destroy() {
        if (this.drawableOn != null) {
            this.drawableOn.recycle();
        }
        if (this.drawableOff != null) {
            this.drawableOff.recycle();
        }
        if (this.drawableDown != null) {
            this.drawableDown.recycle();
        }
    }
    
    RectF getBouns() {
        final float n = Math.min(this.getWidth(), this.getHeight());
        return new RectF(0.0f, 0.0f, n, n);
    }
    
    protected void onDraw(Canvas canvas) {
        this.paint.setColor(-16777216);
        canvas.drawRect(0.0f, 0.0f, (float)this.getWidth(), (float)this.getHeight(), this.paint);
        if (this.shutdown && this.drawableDown != null) {
            canvas.drawBitmap(this.drawableDown, null, this.getBouns(), this.paint);
        }
        else if (this.drawableOff != null && this.drawableOn != null) {
            if (this.isChecked()) {
                canvas.drawBitmap(this.drawableOn, null, this.getBouns(), this.paint);
            }
            else {
                canvas.drawBitmap(this.drawableOff, null, this.getBouns(), this.paint);
            }
        }
    }
    
/*    public void setDrawableDown(BitmapDrawable bitmapDrawable) {
        Bitmap bitmap;
        if (bitmapDrawable != null) {
            bitmap = bitmapDrawable.getBitmap();
        }
        else {
            bitmap = null;
        }
        this.drawableDown = bitmap;
    }*/
    
/*    public void setDrawableOn(BitmapDrawable bitmapDrawable) {
        Bitmap bitmap;
        if (bitmapDrawable != null) {
            bitmap = bitmapDrawable.getBitmap();
        }
        else {
            bitmap = null;
        }
        this.drawableOn = bitmap;
    }*/
    
/*    public void setDrawableoff(final BitmapDrawable bitmapDrawable) {
        Bitmap bitmap;
        if (bitmapDrawable != null) {
            bitmap = bitmapDrawable.getBitmap();
        }
        else {
            bitmap = null;
        }
        this.drawableOff = bitmap;
    }*/
    
    public void setShutdown(final boolean shutdown) {
        this.shutdown = shutdown;
    }
    
/*    public void updateView() {
        this.invalidate();
    }*/
}
