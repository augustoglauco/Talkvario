package de.oganisyan.paraglidervario;

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

//import de.oganisyan.geo.Airspace;

public class VarioActivity extends Activity implements VarioIfc
{
	private IController iController;
	private ServiceConnection iControllerConnection;
	private IServiceController iServiceController;
	private IServiceDataListener iServiceDataListener;
	private IStatusListener iStatusListener;
	//private LocationService ls;
	private ServiceConnection mVarioConnection;
	//private MapModel mapModel;
	private Menu menu;
	private VarioModel model;
	private OrentationDevice orentationHelper;
	private ImageButton soundButton;
	private Switch switchService;
	private static String TAG = "VarioActivity";
	private static boolean flag = true;
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
					catch (NoSuchFieldError noSuchFieldError) {
						Log.i(TAG, "NoSuchFieldError");
					}
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
			boolean b ;
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
		if (this.model != null) {
			this.model.setSettingMode(settingMode);
			this.menu.setGroupVisible(R.id.menu_view_grp_standard, !settingMode);
			this.menu.setGroupVisible(R.id.menu_view_grp_config, settingMode);
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
		this.mVarioConnection = new ServiceConnection() {
			public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
				VarioActivity.access$3(VarioActivity.this, IServiceController.Stub.asInterface(binder));
				VarioActivity.this.setEnebleBeep(VarioActivity.this.soundButton.isChecked());
				VarioActivity.access$5(VarioActivity.this, new IServiceDataListener.Stub() {
					public void onDataChanged(final ServiceDataModel data) throws RemoteException {
						if (VarioActivity.this.model != null) {
							VarioActivity.this.model.setData(data);
						}
					}
				});
				try {
					VarioActivity.this.iServiceController.addListener(VarioActivity.this.iServiceDataListener);
				}
				catch (RemoteException ex) {
					Toast.makeText(VarioActivity.this.getBaseContext(), "Error :" + ex.getMessage() + "\n" + ex.getClass().getName(), Toast.LENGTH_SHORT).show();
				}
			}

			public void onServiceDisconnected(final ComponentName componentName) {
				while (true) {
					try {
						VarioActivity.this.iServiceController.removeListener(VarioActivity.this.iServiceDataListener);
						VarioActivity.access$3(VarioActivity.this, null);
						VarioActivity.access$5(VarioActivity.this, null);
					}
					catch (RemoteException ex) {
						Toast.makeText(VarioActivity.this.getBaseContext(), "Error :" + ex.getMessage() + "\n" + ex.getClass().getName(), Toast.LENGTH_SHORT).show();
						continue;
					}
					break;
				}
			}
		};
		if (!b || this.startService(new Intent((Context)this, (Class<VarioService>)VarioService.class)) != null) {
			this.bindService(new Intent((Context)this, (Class<VarioService>) VarioService.class), this.mVarioConnection, Context.BIND_ABOVE_CLIENT); //8
		}
		this.iControllerConnection = (ServiceConnection)new ServiceConnection() {
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
					final VarioModel access$4 = VarioActivity.this.model;
					VarioModel.TrackingStatus trackingStatus;
					if (VarioActivity.this.iController.getStatus()) {
						trackingStatus = VarioModel.TrackingStatus.TR_ON;
					}
					else {
						trackingStatus = VarioModel.TrackingStatus.TR_OFF;
					}
					access$4.setTrackingStatus(trackingStatus);
					VarioActivity.this.iController.addListener(VarioActivity.this.iStatusListener);
				}
				catch (RemoteException ex) {
					VarioActivity.this.model.setTrackingStatus(VarioModel.TrackingStatus.TR_DOWN);
					Toast.makeText(VarioActivity.this.getBaseContext(), (CharSequence)("Error :" + ex.getMessage() + "\n" + ex.getClass().getName()), Toast.LENGTH_SHORT).show();
				}
			}

			public void onServiceDisconnected(final ComponentName componentName) {
				while (true) {
					try {
						VarioActivity.this.iController.removeListener(VarioActivity.this.iStatusListener);
						VarioActivity.access$9(VarioActivity.this, null);
						VarioActivity.access$8(VarioActivity.this, null);
					}
					catch (RemoteException ex) {
						Toast.makeText(VarioActivity.this.getBaseContext(), (CharSequence)("Error :" + ex.getMessage() + "\n" + ex.getClass().getName()), Toast.LENGTH_SHORT).show();
						continue;
					}
					break;
				}
			}
		};
		final boolean serviceRunning = this.isServiceRunning(SERVICE_TYPE.VARIO);
		if (!serviceRunning || this.startService(new Intent(IController.class.getName())) != null) {
			this.bindService(new Intent(IController.class.getName()), this.iControllerConnection, Context.BIND_ABOVE_CLIENT); //8
		}
		else {
			this.model.setTrackingStatus(VarioModel.TrackingStatus.TR_DOWN);
			this.iControllerConnection = null;
			VarioUtil.showTrackingDialog(this);
		}
		this.switchService.setChecked(serviceRunning);
	}

	void doUnBindService() {
		if (this.mVarioConnection != null) {
			this.unbindService(this.mVarioConnection);
			this.mVarioConnection = null;
		}
		if (this.iControllerConnection != null) {
			this.unbindService(this.iControllerConnection);
			this.iControllerConnection = null;
		}
	}



	public void onCreate(final Bundle bundle) {
		super.onCreate(bundle);
		this.setContentView(R.layout.activity_view);
		//this.ls = new LocationService((Context)this);
		(this.switchService = (Switch)this.findViewById(R.id.switchService)).setChecked(true);
		final HorizontalScrollView horizontalScrollView = (HorizontalScrollView)this.findViewById(R.id.horizontalScrollView1);
		this.model = horizontalScrollView.getVarioView().getModel();
		this.soundButton = (ImageButton)this.findViewById(R.id.button);
		this.model.setSatView((ImageButton)this.findViewById(R.id.buttonSat));
		this.switchService.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
				if (b) {
					VarioActivity.this.doBindService(true);
				}
				else {
					VarioActivity.this.doUnBindService();
					VarioActivity.this.stopService(new Intent((Context)VarioActivity.this, (Class<VarioService>)VarioService.class));
					VarioActivity.this.stopService(new Intent(IController.class.getName()));
				}
			}
		});
		this.soundButton.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
			public void onClick(final View view) {
				VarioActivity.this.setEnebleBeep(VarioActivity.this.soundButton.isChecked());
			}
		});
		this.orentationHelper = new OrentationDevice(this.getApplicationContext()) {
			public void onOrentationChanged(final float n) {
				VarioActivity.this.onOrentationChanged(n);           }
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

	public boolean onOptionsItemSelected(final MenuItem menuItem) {
		boolean onOptionsItemSelected = true;
		switch (menuItem.getItemId()) {
			default: {
				onOptionsItemSelected = super.onOptionsItemSelected(menuItem);
				break;
			}
			case R.id.menu_view_audio: {
				this.startActivity(new Intent((Context)this, (Class<AudioActivity>)AudioActivity.class));
				break;
			}
			case R.id.menu_view_settings: {
				this.startActivity(new Intent((Context)this, (Class<SettingsActivity>)SettingsActivity.class));
				break;
			}
			case R.id.menu_view_exit: {
				this.finish();
				break;
			}
			case R.id.menu_view_tracking: {
				VarioUtil.showTrackingApp(this);
				break;
			}
			case R.id.menu_view_config: {
				this.setConfigerMode(onOptionsItemSelected);
				break;
			}
			case R.id.menu_view_defaults: {
				this.model.resetSettings();
				this.setConfigerMode(false);
				break;
			}
			case R.id.menu_view_save: {
				this.model.saveSettings();
				this.setConfigerMode(false);
				break;
			}
			case R.id.menu_view_cancel: {
				this.model.cancelSettings();
				this.setConfigerMode(false);
				break;
			}
		}
		return onOptionsItemSelected;
	}

	public void onBackPressed() {
		super.openOptionsMenu();
	}

	public void onOrentationChanged(final float n) {
		if (this.model != null) {
			this.model.setOrentation(n);
		}
		///if (this.mapModel != null) {
		//	this.mapModel.setOrentation(n);
		//}
	}

	protected void onPostCreate(final Bundle bundle) {
		super.onPostCreate(bundle);
		this.getSettings();
	}

	protected void onSaveInstanceState(final Bundle bundle) {
		super.onSaveInstanceState(bundle);
		this.saveSettings();
	}

	protected void onStart() {
		super.onStart();
		// Store our shared preference
		SharedPreferences sp = getSharedPreferences("VARIOACTIVITY", MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putBoolean("active", true);
		ed.commit();
	}

	protected void onStop(){
		super.onStop();
		// Store our shared preference
		SharedPreferences sp = getSharedPreferences("VARIOACTIVITY", MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putBoolean("active", false);
		ed.commit();
	}

	protected void onDestroy() {
		if (this.orentationHelper != null) {
			this.orentationHelper.destroy();
		}
		this.saveSettings();
		while (true) {
			try {
				this.doUnBindService();
				super.onDestroy();
				if (this.model != null) {
					this.model.destroy();
				}
				//if (this.mapModel != null) {
				//	this.mapModel.destroy();
				//}
			}
			catch (Throwable t) {
				Log.e("MainActivity", "Failed to unbind from the service", t);
				continue;
			}
			break;
		}
	}

	enum SERVICE_TYPE
	{
		TRACKING("TRACKING", 1),
		VARIO("VARIO", 0);

		private String stringValue;
		SERVICE_TYPE(String toString, int value) {
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

			if ((this.iServiceController != null) && (event.getAction()== KeyEvent.ACTION_DOWN))
			{
				if (codTecla.equals("66")) {
					return true;
				}
				else if (codTecla.equals("24")){
					if (flag){
						flag = false;
						this.iServiceController.fala(Speech.FALA_TOM); // Tom
					}
					return true;
				}
			}
			else if ((this.iServiceController != null) && (event.getAction()== KeyEvent.ACTION_UP)) {
				if (codTecla.equals("66")) {
					this.iServiceController.fala(Speech.FALA_DADOSVOO); // SayIt
					return true;
				}
				else if (codTecla.equals("24"))
				{
					this.iServiceController.fala(Speech.FALA_PARATOM); // desliga tom
					flag=true;
					return true;
				}
			}
		}
		catch (RemoteException ex) {
			Log.d(getClass().getName(), "in dispatchKeyEvent():" + ex.getMessage());
		}

		return super.dispatchKeyEvent(event);
	}
}