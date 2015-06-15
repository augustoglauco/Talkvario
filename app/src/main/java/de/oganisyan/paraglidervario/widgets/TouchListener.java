package de.oganisyan.paraglidervario.widgets;

import android.content.*;
import android.view.*;

abstract class TouchListener implements View.OnTouchListener
{
    boolean deligateEventToScroll;
    boolean moveMode;
    //float p2X;
    //float p2Y;
    float pX;
    float pY;
    private ScaleGestureDetector scaleDetector;
    
    public TouchListener(final Context context) {
        super();
        this.moveMode = false;
        this.deligateEventToScroll = true;
        this.scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            public boolean onScale(final ScaleGestureDetector scaleGestureDetector) {
                TouchListener.this.zoom(scaleGestureDetector.getScaleFactor());
                return false;
            }
            
            public void onScaleEnd(final ScaleGestureDetector scaleGestureDetector) {
                TouchListener.this.changeScale(scaleGestureDetector.getScaleFactor());
            }
        });
    }
    
    public abstract void changeScale(final float p0);
    
    public abstract boolean isInBoubs(final MotionEvent p0);
    
    public abstract void moveLocation(final float p0, final float p1);
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        this.scaleDetector.onTouchEvent(motionEvent);
        this.deligateEventToScroll = !this.isInBoubs(motionEvent);
        if (motionEvent.getAction() == 2) {
            if (motionEvent.getPointerCount() == 1) {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                if (this.moveMode) {
                    this.moveLocation(x - this.pX, y - this.pY);
                }
                this.pX = x;
                this.pY = y;
            }
        }
        else if (motionEvent.getAction() == 0) {
            this.moveMode = true;
            this.pX = motionEvent.getX();
            this.pY = motionEvent.getY();
        }
        else {
            this.moveMode = false;
        }
        return true;
    }
    
    public abstract void zoom(final float p0);
}
