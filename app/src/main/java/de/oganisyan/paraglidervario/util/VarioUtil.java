package de.oganisyan.paraglidervario.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.math.BigDecimal;

import de.oganisyan.paraglidervario.R;

public abstract class VarioUtil implements VarioIfc
{
    public static float calcHeight(final float n, final float n2) {
        return (pow(n2 / p0, mConst1) - pow(n / p0, mConst1)) / mConst2;
    }
    
    public static float calcQNH(final float qfe, final float aitfeldHeight) {
        return p0 * pow(pow(qfe / p0, mConst1) + mConst2 * aitfeldHeight, 5.25588f);
    }
    
    public static float getFloat(final String s, final float n) {
        float floatValue = n;
        try {
            floatValue = Float.valueOf(s);
            return floatValue;
        }
        catch (Exception ex) {
            return floatValue;
        }
    }
    
    public static int getInt(final String s, final int n) {
        int intValue = n;
        try {
            intValue = Integer.valueOf(s);
            return intValue;
        }
        catch (Exception ex) {
            return intValue;
        }
    }
    
    public static float getRoundFloat(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    
    public static SharedPreferences getSharedPreferences(final Context context) {
        return context.getSharedPreferences(VarioUtil.class.getName(), 4);
    }
    
    public static String getString(final float n, final int n2) {
        final float n3 = (float)Math.pow(10.0, n2);
        final float n4 = Math.round(n * n3);
        String s;
        if (n2 > 0) {
            s = Float.toString(n4 / n3);
        }
        else {
            s = Integer.toString((int)(n4 / n3));
        }
        return s;
    }
    
    public static float pow(final float n, final float n2) {
        return (float)Math.pow(n, n2);
    }
    
    public static void showTrackingApp(final Activity activity) {
        try {
            final Intent intent = new Intent("android.intent.action.MAIN");
            intent.addFlags(268435456);
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName("de.oganisyan.tracking", "de.oganisyan.tracking.SettingsActivity"));
            activity.startActivity(intent);
        }
        catch (Exception ex) {
            final Intent intent2 = new Intent("android.intent.action.VIEW");
            intent2.setData(Uri.parse("market://details?id=de.oganisyan.tracking"));
            activity.startActivity(intent2);
        }
    }
    
    public static void showTrackingDialog(final Activity activity) {
        if (!getSharedPreferences((Context)activity).getBoolean("tracking_msg_chekBox", false)) {
            final Dialog dialog = new Dialog((Context)activity);
            dialog.setContentView(R.layout.tracking_msg);
            dialog.setTitle((CharSequence)activity.getString(R.string.tracking_msg_titel));
            ((Button)dialog.findViewById(R.id.dialogButtonOK)).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View view) {
                    dialog.dismiss();
                }
            });
            ((Button)dialog.findViewById(R.id.dialogButtonStore)).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View view) {
                    dialog.dismiss();
                    VarioUtil.showTrackingApp(activity);
                }
            });
            ((CheckBox)dialog.findViewById(R.id.tracking_msg_chekBox)).setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                    final SharedPreferences.Editor edit = VarioUtil.getSharedPreferences((Context)activity).edit();
                    edit.putBoolean("tracking_msg_chekBox", b);
                    edit.apply();
                }
            });
            dialog.show();
        }
    }
}
