package de.oganisyan.paraglidervario;

import android.app.*;
import de.oganisyan.paraglidervario.widgets.*;
import de.oganisyan.paraglidervario.util.*;
import android.widget.*;
import android.view.*;
import android.util.*;
import android.os.*;
import android.content.*;

public class AudioActivity extends Activity implements VarioIfc
{
    SeekBar emulationSeekBar;
    TextBox headerEmulationText;
    private IServiceController iServiceController;
    private ServiceConnection mVarioConnection;
    float[] maxs;
    float[] mins;
    SeekBar[] sb;
    TextBox[] tb;
    
    public AudioActivity() {
        super();
        this.tb = new TextBox[6];
        this.sb = new SeekBar[6];
        this.mins = new float[] { 400.0f, 0.0f, 0.0f, 400.0f, 0.0f, 0.0f };
        this.maxs = new float[] { 2400.0f, 8.0f, 6.0f, 2400.0f, 8.0f, 6.0f };
    }
    
    static /* synthetic */ void access$1(final AudioActivity audioActivity, final IServiceController iServiceController) {
        audioActivity.iServiceController = iServiceController;
    }
    
    static float getSkaleValue(final float n, final float n2, final int n3) {
        return n + (n2 - n) / 100.0f * n3;
    }
    
    private void restoreView(float n, final TextBox textBox, final SeekBar seekBar, final float n2, final float n3, final int n4) {
        if (n < n2) {
            n = n2;
        }
        else if (n > n3) {
            n = n3;
        }
        textBox.setValue(VarioUtil.getString(n, n4));
        seekBar.setProgress((int)(100.0f * ((n - n2) / (n3 - n2))));
    }
    
    private void saveSettings() {
        final SharedPreferences.Editor edit = VarioUtil.getSharedPreferences(this.getApplicationContext()).edit();
        int i = 0;
        while (i < this.tb.length) {
            while (true) {
                try {
                    edit.putFloat("AudioSetting" + i, Float.valueOf(this.tb[i].getValue().toString()));
                    ++i;
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
        edit.apply();
    }
    
    public void onBackPressed() {
        super.openOptionsMenu();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_audio);
        this.tb[0] = (TextBox)this.findViewById(R.id.textBox01);
        this.tb[1] = (TextBox)this.findViewById(R.id.textBox02);
        this.tb[2] = (TextBox)this.findViewById(R.id.textBox03);
        this.tb[3] = (TextBox)this.findViewById(R.id.textBox04);
        this.tb[4] = (TextBox)this.findViewById(R.id.textBox05);
        this.tb[5] = (TextBox)this.findViewById(R.id.textBox06);
        this.sb[0] = (SeekBar)this.findViewById(R.id.seekBar01);
        this.sb[1] = (SeekBar)this.findViewById(R.id.seekBar02);
        this.sb[2] = (SeekBar)this.findViewById(R.id.seekBar03);
        this.sb[3] = (SeekBar)this.findViewById(R.id.seekBar04);
        this.sb[4] = (SeekBar)this.findViewById(R.id.seekBar05);
        this.sb[5] = (SeekBar)this.findViewById(R.id.seekBar06);
        this.emulationSeekBar = (SeekBar)this.findViewById(R.id.emulationSeekBar);
        this.headerEmulationText = (TextBox)this.findViewById(R.id.headerEmulationText);
        for (int i = 0; i < this.tb.length; ++i) {
            this.sb[i].setOnSeekBarChangeListener(new OnSeekBarChangeListener(this.tb[i], this.mins[i], this.maxs[i], AudioActivity.RADIXS[i]));
        }
        this.emulationSeekBar.setOnSeekBarChangeListener(new OnEmulationChangeListener());
        this.emulationSeekBar.setProgress(50);
        this.emulationSeekBar.setEnabled(false);
        this.headerEmulationText.setEnabled(false);
        this.headerEmulationText.setText(R.string.headerEmulationText2);
        this.mVarioConnection = new ServiceConnection() {
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                boolean enabled = true;
                AudioActivity.access$1(AudioActivity.this, IServiceController.Stub.asInterface(binder));
                AudioActivity.this.emulationSeekBar.setEnabled(AudioActivity.this.iServiceController != null);
                final TextBox headerEmulationText = AudioActivity.this.headerEmulationText;
                if (AudioActivity.this.iServiceController == null) {
                    enabled = false;
                }
                headerEmulationText.setEnabled(enabled);
                final TextBox headerEmulationText2 = AudioActivity.this.headerEmulationText;
                int text;
                if (AudioActivity.this.iServiceController != null) {
                    text = R.string.headerEmulationText1;
                }
                else {
                    text = R.string.headerEmulationText2;
                }
                headerEmulationText2.setText(text);
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
                AudioActivity.access$1(AudioActivity.this, null);
            }
        };
        if (!this.bindService(new Intent(this, VarioService.class), this.mVarioConnection, Context.BIND_WAIVE_PRIORITY)) {
            this.mVarioConnection = null;
        }
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(R.menu.activity_audio_menu, menu);
        return true;
    }
    
    protected void onDestroy() {
        if (this.mVarioConnection != null) {
            this.unbindService(this.mVarioConnection);
            this.mVarioConnection = null;
        }
        super.onDestroy();
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
    	boolean onOptionsItemSelected = false;
        switch (menuItem.getItemId()) {
            case R.id.menu_save: {
                this.saveSettings();    
            }
            case R.id.menu_cancel: {
                this.finish();
                try {
                	if(AudioActivity.this.iServiceController!=null)
                		AudioActivity.this.iServiceController.initBeepDevice();
				} catch (RemoteException e) {
					Log.d(this.getClass().getName(), "onOptionsItemSelected(): " + e.getMessage());
				}
                onOptionsItemSelected = true;
            }
        }
        return onOptionsItemSelected;
    }
    
    protected void onPostCreate(final Bundle bundle) {
    	super.onPostCreate(bundle);
        SharedPreferences sharedPreferences;
        try {
        		sharedPreferences = VarioUtil.getSharedPreferences(this.getApplicationContext());       
        		float n;
        		for (int i = 0; i < this.tb.length; ++i) {                         
        			n = sharedPreferences.getFloat("AudioSetting" + i, AudioActivity.DEFAULT_AUDIO_SETTINGS[i]);            	
        			this.restoreView(n, this.tb[i], this.sb[i], this.mins[i], this.maxs[i], AudioActivity.RADIXS[i]);
        		}
        }
        catch (Exception ex) {
        	Log.d(this.getClass().getName(), "onPostCreate(): " + ex.getMessage());
        }        	
    }
    
    protected void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        for (int i = 0; i < this.tb.length; ++i) {
            bundle.putFloat("AudioSetting" + i, getSkaleValue(this.mins[i], this.maxs[i], this.sb[i].getProgress()));
        }
    }
    
    class OnEmulationChangeListener implements SeekBar.OnSeekBarChangeListener
    {
        public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
            final float beepEmulatioValue = Math.max(AudioActivity.getSkaleValue(AudioActivity.this.mins[1],
            AudioActivity.this.maxs[1],	AudioActivity.this.sb[1].getProgress()) + AudioActivity.getSkaleValue(AudioActivity.this.mins[2],
       		AudioActivity.this.maxs[2], AudioActivity.this.sb[2].getProgress()), AudioActivity.getSkaleValue(AudioActivity.this.mins[4],
       		AudioActivity.this.maxs[4], AudioActivity.this.sb[4].getProgress()) + AudioActivity.getSkaleValue(AudioActivity.this.mins[5],
       		AudioActivity.this.maxs[5], AudioActivity.this.sb[5].getProgress())) * ((seekBar.getProgress() - 50.0f) / 50.0f);
            
            AudioActivity.this.headerEmulationText.setValue(String.valueOf(VarioUtil.getString(beepEmulatioValue, 1)) + " m/s");
            try {
                if (AudioActivity.this.iServiceController != null) {
                    AudioActivity.this.iServiceController.setBeepEmulatioValue(beepEmulatioValue);
                }
            }
            catch (RemoteException ex) {
                Log.d(this.getClass().getName(), "onStartTrackingTouch(): " + ex.getMessage());
            }
        }
        
        public void onStartTrackingTouch(final SeekBar seekBar) {
            try {
                if (AudioActivity.this.iServiceController != null) {
	                    AudioActivity.this.iServiceController.enableBeepEmulation(AudioActivity.getSkaleValue(AudioActivity.this.mins[0], 
	                    AudioActivity.this.maxs[0], AudioActivity.this.sb[0].getProgress()), AudioActivity.getSkaleValue(AudioActivity.this.mins[1], 
	                    AudioActivity.this.maxs[1], AudioActivity.this.sb[1].getProgress()), AudioActivity.getSkaleValue(AudioActivity.this.mins[2], 
	                    AudioActivity.this.maxs[2], AudioActivity.this.sb[2].getProgress()), AudioActivity.getSkaleValue(AudioActivity.this.mins[3], 
	                    AudioActivity.this.maxs[3], AudioActivity.this.sb[3].getProgress()), AudioActivity.getSkaleValue(AudioActivity.this.mins[4], 
	                    AudioActivity.this.maxs[4], AudioActivity.this.sb[4].getProgress()), AudioActivity.getSkaleValue(AudioActivity.this.mins[5], 
	                    AudioActivity.this.maxs[5], AudioActivity.this.sb[5].getProgress()));
                }
            }
            catch (RemoteException ex) {
                Log.d(this.getClass().getName(), "onStartTrackingTouch(): " + ex.getMessage());
            }
        }
        
        public void onStopTrackingTouch(final SeekBar seekBar) {
            AudioActivity.this.headerEmulationText.setValue("");
            try {
                if (AudioActivity.this.iServiceController != null) {
                    AudioActivity.this.iServiceController.disableBeepEmulation();
                }
            }
            catch (RemoteException ex) {
                Log.d(this.getClass().getName(), "onStartTrackingTouch(): " + ex.getMessage());
            }
        }
    }
    
    class OnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener
    {
        float max;
        float min;
        TextBox myTextBox;
        int radx;
        
        OnSeekBarChangeListener(final TextBox myTextBox, final float min, final float max, final int radx) {
            super();
            this.myTextBox = myTextBox;
            this.min = min;
            this.max = max;
            this.radx = radx;
        }
        
        public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
            this.myTextBox.setValue(VarioUtil.getString(this.min + (this.max - this.min) / 100.0f * seekBar.getProgress(), this.radx));
        }
        
        public void onStartTrackingTouch(final SeekBar seekBar) {
        }
        
        public void onStopTrackingTouch(final SeekBar seekBar) {
        }
    }
}
