package de.oganisyan.paraglidervario;

//import java.util.HashSet;
//import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import de.oganisyan.paraglidervario.device.OrentationDevice;
import de.oganisyan.paraglidervario.model.ServiceDataModel;
import de.oganisyan.paraglidervario.model.VarioModel;
import de.oganisyan.paraglidervario.util.VarioIfc;
import de.oganisyan.paraglidervario.util.VarioUtil;
import de.oganisyan.paraglidervario.widgets.HorizontalScrollView;
import de.oganisyan.paraglidervario.widgets.ImageButton;
import de.oganisyan.tracking.IController;
import de.oganisyan.tracking.IStatusListener;

import static android.view.KeyEvent.*;

//import de.oganisyan.paraglidervario.widgets.*;
//import de.oganisyan.geo.Airspace;
//import de.oganisyan.geo.db.*;

public class VarioActivity extends Activity implements VarioIfc
{
	private IController iController;
	private ServiceConnection iControllerConnection;
	private IServiceController iServiceController;
	private IServiceDataListener iServiceDataListener;
	private IStatusListener  iStatusListener;
	//private LocationService ls;
	private ServiceConnection mVarioConnection;
	//private MapModel mapModel;
	private Menu menu;
	private VarioModel model;
	private OrentationDevice orentationHelper;
	private ImageButton soundButton;
	private Switch switchService;
	private static int[] $SWITCH_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE;
	

	static int[] $SWITCH_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE() {
		int[] $switch_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE = VarioActivity.$SWITCH_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE;
		if ($switch_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE == null) {
			$switch_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE = new int[SERVICE_TYPE.values().length];
			while (true) {
				try {
					$switch_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE[SERVICE_TYPE.TRACKING.ordinal()] = 2;
					try {
						$switch_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE[SERVICE_TYPE.VARIO.ordinal()] = 1;
						VarioActivity.$SWITCH_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE = $switch_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE;
					}
					catch (NoSuchFieldError noSuchFieldError) {}
				}
				catch (NoSuchFieldError noSuchFieldError2) {
					continue;
				}
				break;
			}
		}
		return $switch_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE;
	}

	public VarioActivity() {
		super();
		this.switchService = null;
		this.model = null;
		//this.mapModel = null;
		this.soundButton = null;
		this.iController = null;
		this.iStatusListener = null;
		this.iControllerConnection = null;
		this.orentationHelper = null;
		this.mVarioConnection = null;
		this.iServiceController = null;
		this.iServiceDataListener = null;
		//this.ls = null;
	}

	static void access$3(final VarioActivity varioActivity, final IServiceController iServiceController) {
		varioActivity.iServiceController = iServiceController;
	}

	static void access$5(final VarioActivity varioActivity, final IServiceDataListener iServiceDataListener) {
		varioActivity.iServiceDataListener = iServiceDataListener;
	}

	static void access$8(final VarioActivity varioActivity, final IController iController) {
		varioActivity.iController = iController;
	}

	static void access$9(final VarioActivity varioActivity, final IStatusListener iStatusListener) {
		varioActivity.iStatusListener = iStatusListener;
	}

	private void getSettings() {
		this.soundButton.setChecked(VarioUtil.getSharedPreferences(this.getApplicationContext()).getBoolean("enableSound", this.soundButton.isChecked()));
	}

	private boolean isServiceRunning(final SERVICE_TYPE service_TYPE) {
		for (final ActivityManager.RunningServiceInfo activityManager$RunningServiceInfo : ((ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
			boolean b;
			switch ($SWITCH_TABLE$de$oganisyan$paraglidervario$VarioActivity$SERVICE_TYPE()[service_TYPE.ordinal()]) {
			default: {
				continue;
			}
			case 1: {
				if (activityManager$RunningServiceInfo.service.getClassName().equals(VarioService.class.getName())) {
					b = activityManager$RunningServiceInfo.started;
					break;
				}
			}
			case 2: {
				if (activityManager$RunningServiceInfo.service.getClassName().equals("de.oganisyan.tracking.Controller")) {
					b = activityManager$RunningServiceInfo.started;
					break;
				}
				continue;
			}
			}
			return b;
		}
		return false;
	}

	private void saveSettings() {
		final SharedPreferences.Editor edit = VarioUtil.getSharedPreferences(this.getApplicationContext()).edit();
		edit.putBoolean("enableSound", this.soundButton.isChecked());
		edit.apply();

	}

	private void setConfigerMode(final boolean settingMode) {
		if (model != null) {
			model.setSettingMode(settingMode);
			menu.setGroupVisible(R.id.menu_view_grp_standard, !settingMode);
			menu.setGroupVisible(R.id.menu_view_grp_config, settingMode);
		}
	}

	private void setEnebleBeep(final boolean enebleBeep) {
		try {
			if (iServiceController != null) iServiceController.setEnebleBeep(enebleBeep);
		}
		catch (RemoteException ex) {
			Log.d(this.getClass().getName(), "in setEnebleBeep():" + ex.getMessage());
		}
	}

 	void doBindService(final boolean b) {
		mVarioConnection = new ServiceConnection() {
			public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
				VarioActivity.access$3(VarioActivity.this,IServiceController.Stub.asInterface(binder));
				setEnebleBeep(soundButton.isChecked());

				VarioActivity.access$5(VarioActivity.this, new IServiceDataListener.Stub() {
					public void onDataChanged(final ServiceDataModel data) throws RemoteException {
						if (VarioActivity.this.model != null) {
							VarioActivity.this.model.setData(data);
						}
					}
				});
				try {
					iServiceController.addListener(VarioActivity.this.iServiceDataListener);
				}
				catch (RemoteException ex) {
					Toast.makeText(VarioActivity.this.getBaseContext(), "Error :" + ex.getMessage()
							+ "\n" + ex.getClass().getName(), Toast.LENGTH_SHORT).show();
				}
			}

			public void onServiceDisconnected(final ComponentName componentName) {
				while (true) {
					try {
						iServiceController.removeListener(VarioActivity.this.iServiceDataListener);
						VarioActivity.access$3(VarioActivity.this, null);
						VarioActivity.access$5(VarioActivity.this, null);
						//iServiceController = null;
						//iServiceDataListener = null;
					}
					catch (RemoteException ex) {
						Toast.makeText(getBaseContext(), "Error: "+ ex.getMessage()	+ "\n" +
								ex.getClass().getName(), Toast.LENGTH_SHORT).show();
						continue;
					}
					break;
				}
			}
		};
		if (!b || startService(new Intent(this, VarioService.class)) != null) {
			bindService(new Intent(this, VarioService.class), mVarioConnection,
					Context.BIND_ADJUST_WITH_ACTIVITY);
		}

		iControllerConnection = new ServiceConnection() {
			public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
				VarioActivity.access$8(VarioActivity.this, IController.Stub.asInterface(binder));
				VarioActivity.access$9(VarioActivity.this, new IStatusListener.Stub() {
					public void onStatusChanged(final boolean b) throws RemoteException {
						final VarioModel access$4 = VarioActivity.this.model;
						VarioModel.TrackingStatus trackingStatus;
						if (b) {
							trackingStatus = VarioModel.TrackingStatus.TR_ON;
						}
						else {
							trackingStatus = VarioModel.TrackingStatus.TR_OFF;
						}
						access$4.setTrackingStatus(trackingStatus);
					}
				});
				try {
					final VarioModel varioModel = model;
					VarioModel.TrackingStatus trackingStatus;
					if (iController.getStatus()) {
						trackingStatus = VarioModel.TrackingStatus.TR_ON;
					}
					else {
						trackingStatus = VarioModel.TrackingStatus.TR_OFF;
					}
					varioModel.setTrackingStatus(trackingStatus);
					iController.addListener(iStatusListener);
				}
				catch (RemoteException ex) {
					model.setTrackingStatus(VarioModel.TrackingStatus.TR_DOWN);
					Toast.makeText(getBaseContext(), "Error :" + ex.getMessage() + "\n" +
							ex.getClass().getName(), Toast.LENGTH_SHORT).show();
				}
			}

			public void onServiceDisconnected(final ComponentName componentName) {
				while (true) {
					try {
						iController.removeListener(VarioActivity.this.iStatusListener);
						iController = null;
						iStatusListener = null;
						VarioActivity.access$9(VarioActivity.this, null);
						VarioActivity.access$8(VarioActivity.this, null);
					}
					catch (RemoteException ex) {
						Toast.makeText(getBaseContext(), "Error :" + ex.getMessage() + "\n" +
								ex.getClass().getName(), Toast.LENGTH_SHORT).show();
						continue;
					}
					break;
				}
			}
		};
		final boolean serviceRunning = isServiceRunning(SERVICE_TYPE.VARIO);
		if (!serviceRunning || startService(new Intent(IController.class.getName())) != null) {
			bindService(new Intent(IController.class.getName()), iControllerConnection, Context.BIND_ADJUST_WITH_ACTIVITY);
		}
		else {
			model.setTrackingStatus(VarioModel.TrackingStatus.TR_DOWN);
			iControllerConnection = null;
			VarioUtil.showTrackingDialog(this);
		}
		this.switchService.setChecked(serviceRunning);
	}

	void doUnBindService() {
		if (mVarioConnection != null) {
			unbindService(this.mVarioConnection);
			mVarioConnection = null;
		}
		if (iControllerConnection != null) {
			unbindService(this.iControllerConnection);
			iControllerConnection = null;
		}
	}

	public void onBackPressed() {		
		super.openOptionsMenu();
	}

	public void onCreate(final Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_view);
		//this.ls = new LocationService(this);
		(switchService = (Switch)this.findViewById(R.id.switchService)).setChecked(true);
		final HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		model = horizontalScrollView.getVarioView().getModel();
		soundButton = (ImageButton)this.findViewById(R.id.button);
		model.setSatView((ImageButton)this.findViewById(R.id.buttonSat));
		switchService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
				if (b) {
					doBindService(true);
				}
				else {
					doUnBindService();
					stopService(new Intent(VarioActivity.this, VarioService.class));
					stopService(new Intent(IController.class.getName()));
				}
			}
		});

		soundButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {
				setEnebleBeep(soundButton.isChecked());
			}
		});

		orentationHelper = new OrentationDevice(getApplicationContext()) {
			public void onOrentationChanged(final float n) {
				onOrentationChanged(n);           }
		};
		//this.mapModel = horizontalScrollView.getMapView().getModel();
		//this.model.setMapModel(this.mapModel);
		this.doBindService(false);	
	
	}

	public boolean onCreateOptionsMenu(final Menu menu) {
		this.menu = menu;
		this.getMenuInflater().inflate(R.menu.activity_view_menu, menu);
		this.setConfigerMode(false);
		return true;
	}

	protected void onDestroy() {
		if (orentationHelper != null) {
			orentationHelper.destroy();
		}
		this.saveSettings();
		while (true) {
			try {
				doUnBindService();
				super.onDestroy();
				if (model != null) {
					model.destroy();
				}
				/*if (this.mapModel != null) {
					this.mapModel.destroy();
				}   */
			}
			catch (Throwable t) {
				Log.e("MainActivity", "Failed to unbind from the service", t);
				continue;
			}
			break;
		}
	}

	public boolean onOptionsItemSelected(final MenuItem menuItem) {
		boolean onOptionsItemSelected = true;
		switch (menuItem.getItemId()) {
		default: {
			onOptionsItemSelected = super.onOptionsItemSelected(menuItem);
			break;
		}
		case R.id.menu_view_audio: {
			startActivity(new Intent(this, AudioActivity.class));
			break;
		}
		case R.id.menu_view_settings: {
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		}
		case R.id.menu_view_exit: {
			finish();
			break;
		}
		case R.id.menu_view_tracking: {
			VarioUtil.showTrackingApp(this);
			break;
		}
		case R.id.menu_view_config: {
			setConfigerMode(onOptionsItemSelected);
			break;
		}
		case R.id.menu_view_defaults: {
			model.resetSettings();
			setConfigerMode(false);
			break;
		}
		case R.id.menu_view_save: {
			model.saveSettings();
			setConfigerMode(false);
			break;
		}
		case R.id.menu_view_cancel: {
			model.cancelSettings();
			setConfigerMode(false);
			break;
		}
		}
		return onOptionsItemSelected;
	}

	public void onOrentationChanged(final float n) {
		if (model != null) {
			model.setOrentation(n);
		}
/*		if (this.mapModel != null) {
			this.mapModel.setOrentation(n);
		}*/
	}

	protected void onPostCreate(final Bundle bundle) {
		super.onPostCreate(bundle);
		getSettings();
	}

	@Override
	protected void onSaveInstanceState(final Bundle bundle) {
		super.onSaveInstanceState(bundle);
		saveSettings();
	}

	protected void onStart() {
		/*if (this.mapModel != null) {
			DBHelper.open(this);
			//TODO rever se linha abaixo funciona
			this.mapModel.setAirspaces(Airspace.loadFromDB(VarioUtil.getSharedPreferences(this.getApplicationContext()).getStringSet("Airspaces", new HashSet<String>())));
			this.mapModel.setLocation(this.ls.getLastKnownGPSLocation());                        
		}	*/
		super.onStart();
		
		 // Store our shared preference
        SharedPreferences sp = getSharedPreferences("VARIOACTIVITY", MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putBoolean("active", true);
        ed.apply();
	}
	
	protected void onStop(){
	      super.onStop();	         
	        // Store our shared preference
	        SharedPreferences sp = getSharedPreferences("VARIOACTIVITY", MODE_PRIVATE);
	        Editor ed = sp.edit();
	        ed.putBoolean("active", false);
	        ed.apply();
	}
	
	enum SERVICE_TYPE
	{
		TRACKING("TRACKING"),
		VARIO("VARIO");

		private String stringValue;
		private SERVICE_TYPE(String toString) {
			stringValue = toString;
		}
		@Override
		public String toString() {
			return stringValue;
		} 

	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		try {
			String codTecla = String.valueOf(event.getKeyCode());


			if ((this.iServiceController != null) && (event.getAction()== ACTION_DOWN))
			{
				if (codTecla.equals("66")) {
					return true;
				}
				else if (codTecla.equals("24"))
				{
					iServiceController.fala(1);
					return true;
					//KeyEvent.DispatcherState state = this.findViewById(android.R.id.content).getKeyDispatcherState();
					//if (state != null) {
					//	state.startTracking(event, this);
					//}
				}
			}else if ((iServiceController != null) && (event.getAction()== ACTION_UP))
			{

				if (codTecla.equals("66")) {
					iServiceController.fala(0);
					return true;
				}
				else if (codTecla.equals("24"))
				{
					//this.findViewById(android.R.id.content).getKeyDispatcherState().handleUpEvent(event);
					//if (event.isTracking() && !event.isCanceled())
					//{
						iServiceController.fala(2);
						return true;
					//}
				}
			}
		}
		catch (RemoteException ex) {
			Log.d(getClass().getName(), "in dispatchKeyEvent():" + ex.getMessage());
		}

		return super.dispatchKeyEvent(event);
	}

}
