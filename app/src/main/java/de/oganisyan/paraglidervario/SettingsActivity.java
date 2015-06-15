package de.oganisyan.paraglidervario;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

import de.oganisyan.paraglidervario.device.PressureDevice;
import de.oganisyan.paraglidervario.util.KalmanFilter;
import de.oganisyan.paraglidervario.util.VarioIfc;
import de.oganisyan.paraglidervario.util.VarioUtil;
import de.oganisyan.paraglidervario.widgets.TextBox;

//import de.oganisyan.geo.db.*;
//import de.oganisyan.geo.*;
//import java.io.*;
//import de.oganisyan.geo.util.*;

public class SettingsActivity extends Activity implements VarioIfc
{
    EditText acceleratorCalibEdit;
    EditText airfieldHeightEdit;
    EditText airfieldQfeEdit;
    EditText airfieldQnhEdit;
    EditText baroIntervalEdit;
    ArrayList<CheckBox> cbList;
    EditText dampingEdit;
    KalmanFilter filter;
    private IServiceController iServiceController;
    EditText kalmanDampingEdit;
    float lastQFE;
    EditText liftBarrierEdit;
    EditText liftMaxDistEdit;
    private ServiceConnection mVarioConnection;
    View.OnFocusChangeListener onFocusChangeListener;
    PressureDevice pDevice;
    CheckBox setBarometerQfeCheck;
    TextBox[] tbAudio;
    TextWatcher textWatcher;
    CheckBox useAcceleratorSensor;
    CheckBox useKalmanFilter;
    CheckBox useStorageFilter;
    EditText freqTomRadioEdit;
    
    public SettingsActivity() {
        super();
        this.tbAudio = new TextBox[6];
        this.lastQFE = p0;
        this.pDevice = null;
        this.filter = new KalmanFilter(0.01f, 100.0f);
        this.onFocusChangeListener = new View.OnFocusChangeListener() {
            public void onFocusChange(final View view, final boolean b) {
                if (!b) {
                    final float mAirfieldHeight = VarioUtil.getFloat(airfieldHeightEdit.getText().toString(), 0.0f);
                    final float mAirfieldQfe = VarioUtil.getFloat(airfieldQfeEdit.getText().toString(), p0);
                    final float mAirfieldQnh = VarioUtil.getFloat(airfieldQnhEdit.getText().toString(), p0);
                    switch (view.getId()) {
                        case R.id.airfieldHeightEdit: 
                        case R.id.airfieldQfeEdit: {
                                airfieldQnhEdit.setText(VarioUtil.getString(VarioUtil.calcQNH(mAirfieldQfe, mAirfieldHeight), 2));
                            break;
                        }
                        case R.id.airfieldQnhEdit: {
                                airfieldHeightEdit.setText(VarioUtil.getString(VarioUtil.calcHeight(mAirfieldQfe, mAirfieldQnh), 0));
                            break;
                        }
                    }
                }
            }
        };
        this.textWatcher = new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                if (!setBarometerQfeCheck.isChecked()) {
                    airfieldQnhEdit.setText(VarioUtil.getString(VarioUtil.calcQNH(VarioUtil.getFloat(airfieldQfeEdit.getText().toString(), p0), VarioUtil.getFloat(airfieldHeightEdit.getText().toString(), 0.0f)), 2));
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        };
        this.cbList = new ArrayList<CheckBox>();
    }
    
    static /* synthetic */ void access$1(final SettingsActivity settingsActivity, final IServiceController iServiceController) {
        settingsActivity.iServiceController = iServiceController;
    }
    
    /*private void initAirspaceList() {
        DBHelper.open(this);
        final Set<String> stringSet = VarioUtil.getSharedPreferences(this.getApplicationContext()).getStringSet("Airspaces", new HashSet<String>());
        final LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.airspaceListContent);
        linearLayout.removeAllViews();
        this.cbList.clear();
        for (final String text : Airspace.getSourceNames()) {
            final LinearLayout linearLayout2 = new LinearLayout(this);
            linearLayout2.setOrientation(0);
            linearLayout.addView((View)linearLayout2, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, -2));
            final CheckBox checkBox = new CheckBox((Context)this);
            checkBox.setText((CharSequence)text);
            this.cbList.add(checkBox);
            linearLayout2.addView((View)checkBox, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(0, -2, 1.0f));
            final Button button = new Button((Context)this);
            button.setBackgroundColor(-16777216);
            button.setBackgroundResource(R.drawable.ic_recycle);
            linearLayout2.addView((View)button, (ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-2, -2, 0.0f));
            checkBox.setChecked(stringSet.contains(text));
            button.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View view) {
                    Airspace.delete(text);
                    SettingsActivity.this.initAirspaceList();
                }
            });
        }
    }*/
    
  /*  private void saveAirspaceList() {
        final HashSet<String> set = new HashSet<String>();
        for (final CheckBox checkBox : this.cbList) {
            if (checkBox.isChecked()) {
                set.add(checkBox.getText().toString());
            }
        }
        final SharedPreferences.Editor edit = VarioUtil.getSharedPreferences(this.getApplicationContext()).edit();
        edit.putStringSet("Airspaces", (Set<String>)set);
        edit.commit();
        super.onDestroy();
    }*/
    
    private void saveSettings() {
        final SharedPreferences.Editor edit = VarioUtil.getSharedPreferences(getApplicationContext()).edit();
        //this.saveAirspaceList();
        edit.putString("airfieldQfeEdit", airfieldQfeEdit.getText().toString());
        edit.putString("airfieldHeightEdit", airfieldHeightEdit.getText().toString());
        edit.putString("liftBarrierEdit", liftBarrierEdit.getText().toString());
        edit.putString("liftMaxDistEdit", liftMaxDistEdit.getText().toString());
        edit.putString("baroIntervalEdit", baroIntervalEdit.getText().toString());
        edit.putString("kalmanDampingEdit", kalmanDampingEdit.getText().toString());
        edit.putString("dampingEdit", dampingEdit.getText().toString());
        edit.putBoolean("useKalmanFilter", useKalmanFilter.isChecked());
        edit.putBoolean("useStorageFilter", useStorageFilter.isChecked());
        edit.putBoolean("useAcceleratorSensor", useAcceleratorSensor.isChecked());
        edit.putString("acceleratorCalibEdit", acceleratorCalibEdit.getText().toString());
        edit.putString("freqTomRadioEdit", freqTomRadioEdit.getText().toString());
        edit.apply();
    }
    
    public void onBackPressed() {
        super.openOptionsMenu();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_settings);
        //this.initAirspaceList();
        this.findViewById(R.id.buttonLoadAirspace).setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
            //TODO: Removido AirSpace
            }
        });
        this.airfieldQfeEdit = (EditText)this.findViewById(R.id.airfieldQfeEdit);
        this.airfieldHeightEdit = (EditText)this.findViewById(R.id.airfieldHeightEdit);
        this.liftBarrierEdit = (EditText)this.findViewById(R.id.liftBarrierEdit);
        this.liftMaxDistEdit = (EditText)this.findViewById(R.id.liftMaxDistEdit);
        this.airfieldQnhEdit = (EditText)this.findViewById(R.id.airfieldQnhEdit);
        this.setBarometerQfeCheck = (CheckBox)this.findViewById(R.id.setBarometerQfeCheck);
        this.baroIntervalEdit = (EditText)this.findViewById(R.id.baroIntervalEdit);
        this.kalmanDampingEdit = (EditText)this.findViewById(R.id.kalmanDumpingEdit);
        this.dampingEdit = (EditText)this.findViewById(R.id.dampingEdit);
        this.acceleratorCalibEdit = (EditText)this.findViewById(R.id.acceleratorCalibEdit);
        this.useKalmanFilter = (CheckBox)this.findViewById(R.id.useKalmanFilter);
        this.useStorageFilter = (CheckBox)this.findViewById(R.id.useStorageFilter);
        this.useAcceleratorSensor = (CheckBox)this.findViewById(R.id.useAcceleratorSensor);
        this.tbAudio[0] = (TextBox)this.findViewById(R.id.upFreqBox);
        this.tbAudio[1] = (TextBox)this.findViewById(R.id.upMinBarrierBox);
        this.tbAudio[2] = (TextBox)this.findViewById(R.id.upRangeBarrierBox);
        this.tbAudio[3] = (TextBox)this.findViewById(R.id.dwFreqBox);
        this.tbAudio[4] = (TextBox)this.findViewById(R.id.dwMinBarrierBox);
        this.tbAudio[5] = (TextBox)this.findViewById(R.id.dwRangeBarrierBox);
        this.freqTomRadioEdit = (EditText)this.findViewById(R.id.freqTomRadioEditBox);
        this.airfieldQnhEdit.setOnFocusChangeListener(this.onFocusChangeListener);
        this.airfieldQfeEdit.setOnFocusChangeListener(this.onFocusChangeListener);
        this.airfieldHeightEdit.setOnFocusChangeListener(this.onFocusChangeListener);
        this.mVarioConnection = new ServiceConnection() {
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                SettingsActivity.access$1(SettingsActivity.this, IServiceController.Stub.asInterface(binder));
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
                SettingsActivity.access$1(SettingsActivity.this, null);
            }
        };
        if (!this.bindService(new Intent(this, VarioService.class), this.mVarioConnection, Context.BIND_NOT_FOREGROUND)) {
            this.mVarioConnection = null;
        }
        (this.pDevice = new PressureDevice(this.getApplicationContext()) {
            public void onSensorChanged(final SensorEvent sensorEvent) {
                lastQFE = filter.update(sensorEvent.values[0]);
            }
        }).start();
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(R.menu.activity_settings_menu, menu);
        return true;
    }
    
    protected void onDestroy() {
        if (this.mVarioConnection != null) {
            this.unbindService(this.mVarioConnection);
            this.mVarioConnection = null;
        }
        this.pDevice.stop();
        super.onDestroy();
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
    	// TODO verificar funcionamento   
    	switch (menuItem.getItemId()) {

    	case R.id.menu_save:
    		this.saveSettings();

    	case R.id.menu_cancel:
    		if(iServiceController == null) {
    			this.finish(); 
    			return true;
    		}
    		try {
    			iServiceController.getSettingsFromPreferences();
    		}
    		catch(Exception v0_1) {
                return false;
    		}
    		this.finish();
    		return true;

    	case R.id.menu_defaults:                   
    		this.useDefaults();
    	}  
    	
    	return true;
    }
   
    protected void onPostCreate(final Bundle bundle) {
        super.onPostCreate(bundle);
        if (bundle != null) {
            airfieldQfeEdit.setText(bundle.getString("airfieldQfeEdit"));
            airfieldHeightEdit.setText(bundle.getString("airfieldHeightEdit"));
            liftBarrierEdit.setText(bundle.getString("liftBarrierEdit"));
            liftMaxDistEdit.setText(bundle.getString("liftMaxDistEdit"));
            baroIntervalEdit.setText(bundle.getString("baroIntervalEdit"));
            kalmanDampingEdit.setText(bundle.getString("kalmanDampingEdit"));
            dampingEdit.setText(bundle.getString("dampingEdit"));
            acceleratorCalibEdit.setText(bundle.getString("acceleratorCalibEdit"));
            useKalmanFilter.setChecked(bundle.getBoolean("useKalmanFilter"));
            useStorageFilter.setChecked(bundle.getBoolean("useStorageFilter"));
            useAcceleratorSensor.setChecked(bundle.getBoolean("useAcceleratorSensor"));
            freqTomRadioEdit.setText(bundle.getString("freqTomRadioEdit"));
        }
        else {
            final SharedPreferences sharedPreferences = VarioUtil.getSharedPreferences(this.getApplicationContext());
            airfieldQfeEdit.setText(sharedPreferences.getString("airfieldQfeEdit", Float.toString(p0)));
            airfieldHeightEdit.setText(sharedPreferences.getString("airfieldHeightEdit", Integer.toString(defaultHeight)));
            liftBarrierEdit.setText(sharedPreferences.getString("liftBarrierEdit", Float.toString(defaultLiftBarrier)));
            liftMaxDistEdit.setText(sharedPreferences.getString("liftMaxDistEdit", Integer.toString(defaultLiftMaxDist)));
            baroIntervalEdit.setText(sharedPreferences.getString("baroIntervalEdit", Integer.toString(defaultBaroInterval)));
            kalmanDampingEdit.setText(sharedPreferences.getString("kalmanDampingEdit", Integer.toString(defaultKalmanDumping)));
            dampingEdit.setText(sharedPreferences.getString("dampingEdit", Integer.toString(defaultBaroDamping)));
            acceleratorCalibEdit.setText(sharedPreferences.getString("acceleratorCalibEdit", Integer.toString(defaultCalibInterval)));
            useKalmanFilter.setChecked(sharedPreferences.getBoolean("useKalmanFilter", defaultUseKalmanFilter));
            useStorageFilter.setChecked(sharedPreferences.getBoolean("useStorageFilter", defaultUseStorageFilter));
            useAcceleratorSensor.setChecked(sharedPreferences.getBoolean("useAcceleratorSensor", defaultUseAcceleraton));
            freqTomRadioEdit.setText(sharedPreferences.getString("freqTomRadioEdit", Float.toString(defaultTomRadio)));

            for (int i = 0; i < tbAudio.length; ++i) {
                tbAudio[i].setValue(VarioUtil.getString(sharedPreferences.getFloat("AudioSetting" + i, DEFAULT_AUDIO_SETTINGS[i]), RADIXS[i]));
            }
        }
        this.setBarometerQfeCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                final boolean checked = ((CheckBox)view).isChecked();
                airfieldQfeEdit.setEnabled(!checked);
                if (checked) {
                    airfieldQfeEdit.setText(VarioUtil.getString(lastQFE, 2));
                }
            }
        });
        airfieldQnhEdit.setText(VarioUtil.getString(VarioUtil.calcQNH(VarioUtil.getFloat(this.airfieldQfeEdit.getText().toString(), p0), VarioUtil.getFloat(this.airfieldHeightEdit.getText().toString(), 0.0f)), 2));
    }
    
    protected void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("airfieldQfeEdit", this.airfieldQfeEdit.getText().toString());
        bundle.putString("airfieldHeightEdit", this.airfieldHeightEdit.getText().toString());
        bundle.putString("liftBarrierEdit", this.liftBarrierEdit.getText().toString());
        bundle.putString("liftMaxDistEdit", this.liftMaxDistEdit.getText().toString());
        bundle.putString("baroIntervalEdit", this.baroIntervalEdit.getText().toString());
        bundle.putString("kalmanDampingEdit", this.kalmanDampingEdit.getText().toString());
        bundle.putString("dampingEdit", this.dampingEdit.getText().toString());
        bundle.putBoolean("useKalmanFilter", this.useKalmanFilter.isChecked());
        bundle.putBoolean("useStorageFilter", this.useStorageFilter.isChecked());
        bundle.putBoolean("useAcceleratorSensor", this.useAcceleratorSensor.isChecked());
        bundle.putString("acceleratorCalibEdit", this.acceleratorCalibEdit.getText().toString());
        bundle.putString("freqTomRadioEdit", this.freqTomRadioEdit.getText().toString());
    }
    
    protected void useDefaults() {
        this.setBarometerQfeCheck.setChecked(defaultUseBaroQfe);
        this.airfieldQfeEdit.setEnabled(true);
        this.airfieldQfeEdit.setText(Float.toString(p0));
        this.airfieldQnhEdit.setText(Float.toString(p0));
        this.airfieldHeightEdit.setText(Integer.toString(defaultHeight));
        this.liftBarrierEdit.setText(Float.toString(defaultLiftBarrier));
        this.liftMaxDistEdit.setText(Integer.toString(defaultLiftMaxDist));
        this.baroIntervalEdit.setText(Integer.toString(defaultBaroInterval));
        this.kalmanDampingEdit.setText(Integer.toString(defaultKalmanDumping));
        this.dampingEdit.setText(Integer.toString(defaultBaroDamping));
        this.acceleratorCalibEdit.setText(Integer.toString(defaultCalibInterval));
        this.useKalmanFilter.setChecked(defaultUseKalmanFilter);
        this.useStorageFilter.setChecked(defaultUseStorageFilter);
        this.useAcceleratorSensor.setChecked(defaultUseAcceleraton);
        this.freqTomRadioEdit.setText(Float.toString(defaultTomRadio));
    }
}
