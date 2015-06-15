package de.oganisyan.paraglidervario.widgets;

import de.oganisyan.paraglidervario.R;
import de.oganisyan.paraglidervario.model.*;
import android.util.*;
import android.app.*;
import android.view.*;
import android.graphics.*;
import de.oganisyan.paraglidervario.util.*;
import android.content.*;

public class VarioView extends View
{
    Bitmap bitmapAirSock;
    Bitmap bitmapCompass;
    Bitmap bitmapScale;
    Bitmap bitmapThermalLift;
    int boarder;
    int colorBackground;
    int colorFirstScale;
    int colorForeground;
    int colorSecondScale;
    private Label[] labels;
    private int[] maping;
    VarioModel model;
    Paint paint;
    private boolean settingMode;
    
    public VarioView(final Context context) {
        super(context);
        this.maping = VarioIfc.DEFAULT_VIEW_SETTINGS.clone();
        this.settingMode = false;
        this.labels = new Label[] { new Label(new RectF(10.0f, 70.0f, 160.0f, 140.0f), Paint.Align.LEFT),
        		                    new Label(new RectF(160.0f, 70.0f, 320.0f, 140.0f), Paint.Align.CENTER), 
        		                    new Label(new RectF(320.0f, 70.0f, 470.0f, 140.0f), Paint.Align.RIGHT),
        		                    new Label(new RectF(10.0f, 140.0f, 160.0f, 210.0f), Paint.Align.LEFT), 
        		                    new Label(new RectF(320.0f, 140.0f, 470.0f, 210.0f), Paint.Align.RIGHT),
        		                    new Label(new RectF(320.0f, 325.0f, 470.0f, 395.0f), Paint.Align.RIGHT), 
        		                    new Label(new RectF(320.0f, 395.0f, 470.0f, 465.0f), Paint.Align.RIGHT), 
        		                    new Label(new RectF(10.0f, 580.0f, 160.0f, 650.0f), Paint.Align.LEFT), 
        		                    new Label(new RectF(320.0f, 580.0f, 470.0f, 650.0f), Paint.Align.RIGHT), 
        		                    new Label(new RectF(10.0f, 650.0f, 160.0f, 720.0f), Paint.Align.LEFT),
        		                    new Label(new RectF(160.0f, 650.0f, 320.0f, 720.0f), Paint.Align.CENTER),
        		                    new Label(new RectF(320.0f, 650.0f, 470.0f, 720.0f), Paint.Align.RIGHT), 
        		                    new Label(new RectF(10.0f, 720.0f, 160.0f, 790.0f), Paint.Align.LEFT), 
        		                    new Label(new RectF(160.0f, 720.0f, 320.0f, 790.0f), Paint.Align.CENTER),
        		                    new Label(new RectF(320.0f, 720.0f, 470.0f, 790.0f), Paint.Align.RIGHT) };
        this.init();
    }
    
    public VarioView(final Context context, final AttributeSet set) {
        super(context, set);
        this.maping = VarioIfc.DEFAULT_VIEW_SETTINGS.clone();
        this.settingMode = false;
        this.labels = new Label[] { new Label(new RectF(10.0f, 70.0f, 160.0f, 140.0f), Paint.Align.LEFT), 
        							new Label(new RectF(160.0f, 70.0f, 320.0f, 140.0f), Paint.Align.CENTER), 
        							new Label(new RectF(320.0f, 70.0f, 470.0f, 140.0f), Paint.Align.RIGHT),
        							new Label(new RectF(10.0f, 140.0f, 160.0f, 210.0f), Paint.Align.LEFT),
        							new Label(new RectF(320.0f, 140.0f, 470.0f, 210.0f), Paint.Align.RIGHT),
        							new Label(new RectF(320.0f, 325.0f, 470.0f, 395.0f), Paint.Align.RIGHT), 
        							new Label(new RectF(320.0f, 395.0f, 470.0f, 465.0f), Paint.Align.RIGHT), 
        							new Label(new RectF(10.0f, 580.0f, 160.0f, 650.0f), Paint.Align.LEFT),
        							new Label(new RectF(320.0f, 580.0f, 470.0f, 650.0f), Paint.Align.RIGHT),
        							new Label(new RectF(10.0f, 650.0f, 160.0f, 720.0f), Paint.Align.LEFT), 
        							new Label(new RectF(160.0f, 650.0f, 320.0f, 720.0f), Paint.Align.CENTER), 
        							new Label(new RectF(320.0f, 650.0f, 470.0f, 720.0f), Paint.Align.RIGHT), 
        							new Label(new RectF(10.0f, 720.0f, 160.0f, 790.0f), Paint.Align.LEFT), 
        							new Label(new RectF(160.0f, 720.0f, 320.0f, 790.0f), Paint.Align.CENTER),
        							new Label(new RectF(320.0f, 720.0f, 470.0f, 790.0f), Paint.Align.RIGHT) };
        this.init();
    }
    
    public VarioView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.maping = VarioIfc.DEFAULT_VIEW_SETTINGS.clone();
        this.settingMode = false;
        this.labels = new Label[] { new Label(new RectF(10.0f, 70.0f, 160.0f, 140.0f), Paint.Align.LEFT), 
        							new Label(new RectF(160.0f, 70.0f, 320.0f, 140.0f), Paint.Align.CENTER),
        							new Label(new RectF(320.0f, 70.0f, 470.0f, 140.0f), Paint.Align.RIGHT), 
        							new Label(new RectF(10.0f, 140.0f, 160.0f, 210.0f), Paint.Align.LEFT), 
        							new Label(new RectF(320.0f, 140.0f, 470.0f, 210.0f), Paint.Align.RIGHT),
        							new Label(new RectF(320.0f, 325.0f, 470.0f, 395.0f), Paint.Align.RIGHT),
        							new Label(new RectF(320.0f, 395.0f, 470.0f, 465.0f), Paint.Align.RIGHT),
        							new Label(new RectF(10.0f, 580.0f, 160.0f, 650.0f), Paint.Align.LEFT),
        							new Label(new RectF(320.0f, 580.0f, 470.0f, 650.0f), Paint.Align.RIGHT),
        							new Label(new RectF(10.0f, 650.0f, 160.0f, 720.0f), Paint.Align.LEFT),
        							new Label(new RectF(160.0f, 650.0f, 320.0f, 720.0f), Paint.Align.CENTER),
        							new Label(new RectF(320.0f, 650.0f, 470.0f, 720.0f), Paint.Align.RIGHT),
        							new Label(new RectF(10.0f, 720.0f, 160.0f, 790.0f), Paint.Align.LEFT),
        							new Label(new RectF(160.0f, 720.0f, 320.0f, 790.0f), Paint.Align.CENTER),
        							new Label(new RectF(320.0f, 720.0f, 470.0f, 790.0f), Paint.Align.RIGHT) };
        this.init();
    }
    
    private void createPopUP(final int n) {
        final AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(this.getContext());
        final String[] array = new String[this.model.labelModels.length];
        for (int i = 0; i < this.model.labelModels.length; ++i) {
            if (this.model.labelModels[i] != null) {
                array[i] = this.model.labelModels[i].getTitle();
            }
            else {
                array[i] = "disable";
            }
        }
        alertDialog_Builder.setTitle("title").setSingleChoiceItems(array, this.maping[n], new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, int n1) {
                VarioView.this.maping[n] = n1;
                VarioView.this.initLabelModels();
                dialogInterface.cancel();
                VarioView.this.invalidate();
            }
        });
        alertDialog_Builder.create().show();
    }
    
    private void drawSkale(final Canvas canvas) {
        final float n = this.getWidth() / 2 - this.boarder / 2;
        final float n2 = this.getHeight() / 2 - this.boarder / 2;
        final float n3 = -70 + Math.min(this.getWidth(), this.getHeight()) / 2;
        this.paint.setColor(this.colorForeground);
        this.paint.setStrokeWidth(6.0f);
        for (int i = 1; i <= 4; ++i) {
            final float textSize = Math.min(this.getWidth(), this.getHeight()) / 20;
            final float n4 = 180.0f + 36.0f * i;
            final float n5 = n3 * (float)Math.sin(3.1415 * (n4 / 180.0f));
            final float n6 = n3 * (float)Math.cos(3.1415 * (n4 / 180.0f));
            this.paint.setTextAlign(Paint.Align.CENTER);
            this.paint.setTextSize(textSize);
            int n7;
            if (Math.abs(this.model.getVerticalSpeed()) > 4.0f) {
                n7 = i + 4;
            }
            else {
                n7 = i;
            }
            canvas.drawText(String.valueOf(n7), n + n6, n2 + n5 + textSize / 3.0f, this.paint);
            canvas.drawText(String.valueOf(n7), n + n6, n2 - n5 + textSize / 2.0f, this.paint);
        }
    }
    
    private RectF getAirSockBounds() {
        float n;
        if (this.model == null) {
            n = 30.0f;
        }
        else {
            n = this.model.getWindDirection() + this.model.getOrentation();
        }
        final float n2 = Math.min(this.getWidth(), this.getHeight()) / 20;
        final float n3 = Math.min(this.getWidth(), this.getHeight()) / 6;
        final float n4 = this.getWidth() / 2 + n3 * (float)Math.sin(3.1415 * (n / 180.0f));
        final float n5 = this.getHeight() / 2 - n3 * (float)Math.cos(3.1415 * (n / 180.0f));
        return new RectF(n4 - n2, n5 - n2, n4 + n2, n5 + n2);
    }
    
    private RectF getBmpBounds() {
        final float n = Math.abs((this.getWidth() - this.getHeight()) / 2);
        RectF rectF;
        if (this.getWidth() > this.getHeight()) {
            rectF = new RectF(n + this.boarder, (float)this.boarder, this.getWidth() - 2 * this.boarder - n, (float)(this.getHeight() - 2 * this.boarder));
        }
        else {
            rectF = new RectF((float)this.boarder, n + this.boarder, (float)(this.getWidth() - 2 * this.boarder), this.getHeight() - 2 * this.boarder - n);
        }
        return rectF;
    }
    
    private float getFirstSweepAngle() {
        float n;
        if (this.model != null) {
            n = 36.0f * this.model.getVerticalSpeed();
        }
        else {
            n = 0.0f;
        }
        if (n < -180.0f) {
            n = -180.0f;
        }
        else if (n > 180.0f) {
            n = 180.0f;
        }
        return n;
    }
    
    private Matrix getRotationMatrix() {
        float orentation;
        if (this.model == null) {
            orentation = 0.0f;
        }
        else {
            orentation = this.model.getOrentation();
        }
        final Matrix matrix = new Matrix();
        final RectF bmpBounds = this.getBmpBounds();
        matrix.setRectToRect(new RectF(0.0f, 0.0f, (float)this.bitmapCompass.getWidth(), (float)this.bitmapCompass.getHeight()), bmpBounds, Matrix.ScaleToFit.CENTER);
        matrix.postRotate(orentation, (bmpBounds.right + bmpBounds.left) / 2.0f, (bmpBounds.bottom + bmpBounds.top) / 2.0f);
        return matrix;
    }
    
    private float getSecondSweepAngle() {
        VarioModel var1 = this.model;
        float var2 = 0.0F;
        if(var1 != null) {
           float var4;
           int var3 = (var4 = Math.abs(this.model.getVerticalSpeed()) - 4.0F) == 0.0F?0:(var4 < 0.0F?-1:1);
           var2 = 0.0F;
           if(var3 > 0) {
              if(this.model.getVerticalSpeed() > 0.0F) {
                 var2 = 36.0F * (this.model.getVerticalSpeed() - 4.0F);
              } else {
                 var2 = 36.0F * (4.0F + this.model.getVerticalSpeed());
              }

              if(var2 < -180.0F) {
                 return -180.0F;
              }

              if(var2 > 180.0F) {
                 return 180.0F;
              }
           }
        }

        return var2;
     }
    
    private RectF getThermalLiftBounds() {
        float n;
        if (this.model == null) {
            n = -30.0f;
        }
        else {
            n = this.model.getThermalLiftDirection() + this.model.getOrentation();
        }
        final float n2 = Math.min(this.getWidth(), this.getHeight()) / 20;
        final float n3 = Math.min(this.getWidth(), this.getHeight()) / 6;
        final float n4 = this.getWidth() / 2 + n3 * (float)Math.sin(3.1415 * (n / 180.0f));
        final float n5 = this.getHeight() / 2 - n3 * (float)Math.cos(3.1415 * (n / 180.0f));
        return new RectF(n4 - n2, n5 - n2, n4 + n2, n5 + n2);
    }
    
    //TODO para que isto?
   /* private RectF getTrackingBounds() {
        final float n = Math.min(this.getWidth(), this.getHeight()) / 8;
        return new RectF((float)this.boarder, 0.0f, n + this.boarder, 0.0f + n);
    }*/
    
    public void cancelSettings() {
        this.readPersistentLabels();
    }
    
    public void destroy() {
        if (this.bitmapScale != null) {
            this.bitmapScale.recycle();
        }
        if (this.bitmapCompass != null) {
            this.bitmapCompass.recycle();
        }
        if (this.bitmapAirSock != null) {
            this.bitmapAirSock.recycle();
        }
        if (this.bitmapThermalLift != null) {
            this.bitmapThermalLift.recycle();
        }
    }
    
    public void fireDataChanged() {
        this.invalidate();
    }
    
    public VarioModel getModel() {
        return this.model;
    }
    
    protected void init() {
        System.gc();
        this.bitmapScale = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.vario);
        this.bitmapCompass = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.compass);
        this.bitmapAirSock = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.air_sock);
        this.bitmapThermalLift = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.thermal_lift);
        this.paint = new Paint();
        this.model = new VarioModel(this);
        this.colorBackground = -16777216;
        this.colorForeground = -1;
        this.colorFirstScale = -256;
        this.colorSecondScale = -65536;
        this.boarder = 20;
        this.readPersistentLabels();
        this.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && VarioView.this.isSettingMode()) {
                    final float n = VarioView.this.getWidth();
                    final float n2 = VarioView.this.getHeight();
                    final PointF pointF = new PointF(motionEvent.getX(), motionEvent.getY());
                    for (int i = 0; i < VarioView.this.labels.length; ++i) {
                        if (VarioView.this.labels[i].getBounds(n, n2).contains(pointF.x, pointF.y)) {
                            VarioView.this.createPopUP(i);
                            break;
                        }
                    }
                }
                return true;
            }
        });
    }
    
    void initLabelModels() {
        if (this.maping != null) {
            for (int n = 0; n < this.labels.length && n < this.maping.length; ++n) {
                if (this.maping[n] >= 0 && this.maping[n] < this.model.labelModels.length) {
                    this.labels[n].setLabelModel(this.model.labelModels[this.maping[n]]);
                }
            }
        }
    }
    
    public boolean isSettingMode() {
        return this.settingMode;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (canvas != null && this.paint != null) {
            this.paint.setColor(this.colorBackground);
            canvas.drawRect(0.0f, 0.0f, (float)this.getWidth(), (float)this.getHeight(), this.paint);
            this.paint.setColor(this.colorFirstScale);
            canvas.drawArc(this.getBmpBounds(), 180.0f, this.getFirstSweepAngle(), true, this.paint);
            this.paint.setColor(this.colorSecondScale);
            canvas.drawArc(this.getBmpBounds(), 180.0f, this.getSecondSweepAngle(), true, this.paint);
            if (this.bitmapScale != null) {
                canvas.drawBitmap(this.bitmapScale, null, this.getBmpBounds(), this.paint);
            }
            this.drawSkale(canvas);
            if (this.bitmapCompass != null) {
                canvas.drawBitmap(this.bitmapCompass, this.getRotationMatrix(), this.paint);
            }
            if (this.bitmapAirSock != null && !Float.isNaN(this.model.getWindDirection())) {
                canvas.drawBitmap(this.bitmapAirSock, null, this.getAirSockBounds(), this.paint);
            }
            if (this.bitmapThermalLift != null && !Float.isNaN(this.model.getThermalLiftDirection())) {
                canvas.drawBitmap(this.bitmapThermalLift, null, this.getThermalLiftBounds(), this.paint);
            }
            final float n = this.getWidth();
            final float n2 = this.getHeight();
            this.paint.setStrokeWidth(1.0f);
            for (Label label : this.labels) {
                if (this.settingMode) {
                    this.paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(label.getBounds(n, n2), this.paint);
                }
                this.paint.setStyle(Paint.Style.FILL);
                label.draw(canvas, this.paint, n, n2);
            }
        }
        else {
            this.invalidate();
        }
    }
    
    void readPersistentLabels() {
        if (this.maping != null) {
            final SharedPreferences sharedPreferences = VarioUtil.getSharedPreferences(this.getContext());
            for (int n = 0; n < this.maping.length && sharedPreferences != null; ++n) {
                this.maping[n] = sharedPreferences.getInt("maping_" + n, this.maping[n]);
            }
            this.initLabelModels();
        }
    }
    
    public void resetSettings() {
        this.maping = VarioIfc.DEFAULT_VIEW_SETTINGS.clone();
        final SharedPreferences.Editor edit = VarioUtil.getSharedPreferences(this.getContext()).edit();
        for (int i = 0; i < this.maping.length; ++i) {
            edit.putInt("maping_" + i, this.maping[i]);
        }
        edit.apply();
        this.readPersistentLabels();
    }
    
    public void saveSettings() {
        final SharedPreferences.Editor edit = VarioUtil.getSharedPreferences(this.getContext()).edit();
        for (int i = 0; i < this.maping.length; ++i) {
            edit.putInt("maping_" + i, this.maping[i]);
        }
        edit.apply();
        this.readPersistentLabels();
    }
    
    public void setSettingMode(final boolean settingMode) {
        this.settingMode = settingMode;
        this.invalidate();
    }
}
