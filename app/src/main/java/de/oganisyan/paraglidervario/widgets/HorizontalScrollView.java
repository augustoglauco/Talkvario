package de.oganisyan.paraglidervario.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
//import de.oganisyan.paraglidervario.widgets.MapView;
//import de.oganisyan.paraglidervario.widgets.VarioView;

public class HorizontalScrollView extends android.widget.HorizontalScrollView {

   //private MapView mapView = null;
   private VarioView varioView = null;


   public HorizontalScrollView(Context var1) {
      super(var1);
      this.init();
   }

   public HorizontalScrollView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   public HorizontalScrollView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   private boolean checkScrollMode(View var1) {
      boolean var2 = false;
      if(var1 instanceof ViewGroup) {
         for(int var4 = 0; var4 < ((ViewGroup)var1).getChildCount(); ++var4) {
             var2 = !(!var2 && !this.checkScrollMode(((ViewGroup) var1).getChildAt(var4)));
         }
      } else {
         boolean var3 = var1 instanceof HorizontalScrollView.ScrollMode;
         var2 = false;
         if(var3) {
             return !(((ScrollMode) var1).deligateEventToScroll());

         }
      }

      return var2;
   }

/*   public MapView getMapView() {
      return this.mapView;
   }*/

   public VarioView getVarioView() {
      return this.varioView;
   }

   void init() {
      DisplayMetrics var1 = new DisplayMetrics();
      ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(var1);
      int var2 = Math.max(var1.widthPixels, 720);
      LinearLayout var3 = new LinearLayout(this.getContext());
      var3.setOrientation(LinearLayout.HORIZONTAL);
      this.addView(var3, new LayoutParams(-2, -2));
      this.varioView = new VarioView(this.getContext());
      var3.addView(this.varioView, new LayoutParams(var2, -2));
/*      this.mapView = new MapView(this.getContext());
      var3.addView(this.mapView, new LayoutParams(var2, -2));*/
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2;
       var2 = !this.checkScrollMode(this);

      return var2 && super.onInterceptTouchEvent(var1);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var2 = true;

      boolean var4;
      try {
         if(this.checkScrollMode(this)) {
            return true;
         }

         var4 = super.onTouchEvent(var1);
      } catch (IllegalArgumentException var5) {
         return true;
      }

      if(!var4) {
         var2 = false;
      }

      return var2;
   }

   public interface ScrollMode {

      boolean deligateEventToScroll();
   }
}
