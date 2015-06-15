package de.oganisyan.paraglidervario.widgets;

import android.content.*;
import android.util.*;
import de.oganisyan.paraglidervario.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;

public class HeaderView extends LinearLayout implements View.OnClickListener
{
    private HeaderTextView header;
    private boolean showItems;
    
    public HeaderView(final Context context) {
        super(context);
        this.showItems = false;
        this.header = null;
        super.setOrientation(LinearLayout.VERTICAL);
        (this.header = new HeaderTextView(context)).setOnClickListener(this);
        this.addView(this.header, new LayoutParams(-2, -2));
    }
    
    public HeaderView(final Context context, final AttributeSet set) {
        super(context, set);
        this.showItems = false;
        this.header = null;
        super.setOrientation(LinearLayout.VERTICAL);
        (this.header = new HeaderTextView(context, set)).setOnClickListener(this);
        this.header.setBackgroundColor(context.obtainStyledAttributes(set, R.styleable.HeaderView).getColor(0, -16777216));
        this.addView(this.header, new LayoutParams(-1, -2));
    }
    
    public HeaderView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.showItems = false;
        this.header = null;
        super.setOrientation(LinearLayout.VERTICAL);
        (this.header = new HeaderTextView(context, set, n)).setOnClickListener(this);
        this.header.setBackgroundColor(context.obtainStyledAttributes(set, R.styleable.HeaderView).getColor(0, -16777216));
        this.addView(this.header, new LayoutParams(-1, -2));
    }
    
    private void initItems(final View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup)view).getChildCount(); ++i) {
                this.initItems(((ViewGroup)view).getChildAt(i));
            }
        }
        else if (view != null && !view.equals(this.header)) {
            int visibility;
            if (this.showItems) {
                visibility = View.VISIBLE;
            }
            else {
                visibility = View.INVISIBLE;
            }
            view.setVisibility(visibility);
        }
    }
    
    public void onClick(final View view) {
        this.showItems = !this.showItems;
        this.initItems(this);
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.initItems(this);
        super.onMeasure(n, n2);
    }
    
    public void setOnClickListener(final View.OnClickListener view$OnClickListener) {
        throw new RuntimeException("MethodNotSupportedException use add OnClickListener");
    }
    
    class HeaderTextView extends TextView
    {
    	//TODO comentado o paint
        //private Paint paint;
        private String titelOff;
        private String titelOn;

        public HeaderTextView(final Context context) {
            super(context);
            this.init(context, null);
        }
        
        public HeaderTextView(final Context context, final AttributeSet set) {
            super(context, set);
            this.init(context, set);
        }
        
        public HeaderTextView(final Context context, final AttributeSet set, final int n) {
            super(context, set, n);
            this.init(context, set);
        }
        
        private void init(final Context context, final AttributeSet set) {
            Object text = null;
            if (context != null) {
                text = null;
                if (set != null) {
                    text = context.obtainStyledAttributes(set, R.styleable.HeaderView).getText(1);
                }
            }
            this.titelOff = " + " + text + " ...";
            this.titelOn = " - " + text;
            //(this.paint = new Paint()).setTextAlign(Paint.Align.LEFT);
        }

        protected void onDraw(Canvas canvas) {
            String text;
            if (HeaderView.this.showItems) {
                text = this.titelOn;
            }
            else {
                text = this.titelOff;
            }
            this.setText(text);
            super.onDraw(canvas);
        }
    }
}
