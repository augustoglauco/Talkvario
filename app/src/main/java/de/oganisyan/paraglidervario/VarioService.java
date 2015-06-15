package de.oganisyan.paraglidervario;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import de.oganisyan.paraglidervario.Speech.LocalBinder;
import de.oganisyan.paraglidervario.device.AccelerationDevice;
import de.oganisyan.paraglidervario.device.AltitudeDevice;
import de.oganisyan.paraglidervario.device.BeepDevice;
import de.oganisyan.paraglidervario.device.LocationService;
import de.oganisyan.paraglidervario.model.ServiceDataModel;
import de.oganisyan.paraglidervario.util.VarioIfc;
import de.oganisyan.paraglidervario.util.VarioUtil;

public class VarioService extends Service implements VarioIfc
{
    private AccelerationDevice accHelper;
    private LocationService ls;
    ArrayList<Messenger> mClients;
    int mValue;
    private NotificationManager notificationManager;
    private AltitudeDevice prHelper;
    private ArrayList<IServiceDataListener> serviceDataListeners;
    private Speech mSpeech;
    private boolean mBound;    
    AtomicBoolean isRunning;
    private Handler handler=new Handler();

	public VarioService() {
        super();
        this.mClients = new ArrayList<Messenger>();
        this.mValue = 0;
        this.ls = null;
        this.serviceDataListeners = new ArrayList<IServiceDataListener>();
        this.mBound = false;
        this.mSpeech = null;
        this.isRunning=new AtomicBoolean(false);
        //this.audio = new AudioUtil();
    }
    
    private void broadcastMessageToUI(final ServiceDataModel serviceDataModel) {
        if (serviceDataModel != null) {
            BeepDevice.instanceOf().setValue(serviceDataModel.getSpeed());
            for (IServiceDataListener serviceDataListener : this.serviceDataListeners) {
                this.onDataChanged(serviceDataListener, serviceDataModel);
            }
        }
    }
    
    private void getSettingsFromPreferences() {
        final SharedPreferences sharedPreferences = VarioUtil.getSharedPreferences(this.getApplicationContext());
        final int int1 = VarioUtil.getInt(sharedPreferences.getString("airfieldHeightEdit", "0"), 0);
        final float float1 = VarioUtil.getFloat(sharedPreferences.getString("airfieldQfeEdit", "xxx"), 1013.25f);
        final int int2 = VarioUtil.getInt(sharedPreferences.getString("baroIntervalEdit", "xxx"), 250);
        final boolean boolean1 = sharedPreferences.getBoolean("useKalmanFilter", true);
        final int int3 = VarioUtil.getInt(sharedPreferences.getString("kalmanDampingEdit", "xxx"), 5);
        final boolean boolean2 = sharedPreferences.getBoolean("useStorageFilter", true);
        final int int4 = VarioUtil.getInt(sharedPreferences.getString("dampingEdit", "xxx"), 4);
        final int int5 = VarioUtil.getInt(sharedPreferences.getString("acceleratorCalibEdit", "xxx"), 5000);
        final boolean boolean3 = sharedPreferences.getBoolean("useAcceleratorSensor", false);
        this.prHelper.setHeight(int1, float1);
        this.prHelper.setIntervall(int2);
        this.prHelper.setStorageFilter(boolean2, int4);
        this.prHelper.setKalmanFilter(boolean1, int3);
        this.accHelper.setCalibration(int5);
        this.accHelper.setUsedAccelerationSensor(boolean3);
    }
    
    private void onDataChanged(final IServiceDataListener serviceDataListener, final ServiceDataModel serviceDataModel) {
        if (serviceDataListener == null || serviceDataModel == null) {
            return;
        }
        try {
            serviceDataListener.onDataChanged(serviceDataModel);
        }
        catch (RemoteException ex) {
            Log.d(this.getClass().getName(), ex.getMessage());
        }
    }
    
    public IBinder onBind(final Intent intent) {
        return new IServiceController.Stub() {
            public void addListener(final IServiceDataListener serviceDataListener) throws RemoteException {
                if (serviceDataListener != null) {
                    VarioService.this.serviceDataListeners.add(serviceDataListener);
                }
                VarioService.this.onDataChanged(serviceDataListener, new ServiceDataModel(VarioService.this.ls.getLastKnownGPSLocation(), VarioService.this.prHelper.getData()));
            }
            
            public void disableBeepEmulation() throws RemoteException {
                BeepDevice.instanceOf().setEmulatioMode(false);
                BeepDevice.instanceOf().reInitAudioStreams(VarioService.this.getApplicationContext());
            }
            
            public void enableBeepEmulation(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) throws RemoteException {
                BeepDevice.instanceOf().setEmulatioMode(true);
                BeepDevice.instanceOf().reInitAudioStreams(n, n2, n3, n4, n5, n6);
            }
            
            public void getSettingsFromPreferences() throws RemoteException {
                VarioService.this.getSettingsFromPreferences();
            }
            
            public void initBeepDevice() throws RemoteException {
                BeepDevice.instanceOf().reInitAudioStreams(VarioService.this.getApplicationContext());
            }
            
            public void removeListener(final IServiceDataListener serviceDataListener) throws RemoteException {
                if (serviceDataListener != null) {
                    VarioService.this.serviceDataListeners.remove(serviceDataListener);
                }
            }
            
            public void setBeepEmulatioValue(final float n) throws RemoteException {
                BeepDevice.instanceOf().setEmulatioValue(n);
            }

            public void setEnebleBeep(final boolean enebleBeep) throws RemoteException {
                BeepDevice.instanceOf().setEnebleBeep(enebleBeep);
            }

            public void fala(int n) throws RemoteException {
                VarioService.this.fala(n);
            }

        };
    }

    /** Defines  for service binding, passed to bindService() */
    public ServiceConnection mSpeechConnection = new ServiceConnection() {

        @Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			LocalBinder binder = (LocalBinder) service;
			mSpeech = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
    };
    
    public void onCreate() {
        super.onCreate();
        this.ls = new LocationService(this);
        this.prHelper = new AltitudeDevice(this.getApplicationContext()) {
            public void onSensorChanged(final SensorEvent sensorEvent) {
                final double n = this.getData().getSpeed();
            	final boolean naN = Float.isNaN(this.getData().getQfe());
                super.onSensorChanged(sensorEvent);
                if (n != this.getData().getSpeed()) {
                    VarioService.this.accHelper.setVSpeed(this.getData().getSpeed());
                    if (naN) {
                        final double n2 = Math.round(100.0f * this.getData().getQfe());
                        final SharedPreferences.Editor edit = VarioUtil.getSharedPreferences(VarioService.this.getApplicationContext()).edit();
                        edit.putString("airfieldQfeEdit", Double.toString(n2 / 100.0));
                        edit.putBoolean("setBarometerQfeCheck", false);
                        edit.apply();
                    }
                }
                VarioService.this.broadcastMessageToUI(new ServiceDataModel(VarioService.this.ls.getLastKnownGPSLocation(), this.getData()));
            }
        };
        Log.i("MyService", "Service Started.");
        this.accHelper = new AccelerationDevice(this.getApplicationContext());

        //(this.nm = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE)).notify(R.string.service_started, new Notification.Builder(this)...getNotification());

        Intent intent = new Intent(this, VarioActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Variometer_Service")
                .setContentText(getText(R.string.service_started))
                .setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(true)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
                //.addAction(R.drawable.icon, "Call", pIntent)
                ///.addAction(R.drawable.icon, "More", pIntent)
                //.addAction(R.drawable.icon, "And more", pIntent).build();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify("Variometer",0, n);

        this.getSettingsFromPreferences();
        this.prHelper.start();
        BeepDevice.start().reInitAudioStreams(this.getApplicationContext());
        
        bindService(new Intent(getApplicationContext(), Speech.class), mSpeechConnection, BIND_AUTO_CREATE);
    }
    
    public void onDestroy() {
        super.onDestroy();
        this.notificationManager.cancel("Variometer", 0);
        this.prHelper.stop();
        this.accHelper.unregisterListeners();
        this.mClients.clear();
        BeepDevice.instanceOf().stop();
        Log.i("VarioService", "Service Stopped.");
        this.mSpeech.stopSelf();
        this.mSpeech = null;
    }
    
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        Log.i("MyService", "Received start id " + n2 + ": " + intent);               
        return 1;
    }
          
    public boolean onUnbind(final Intent intent) {

        if (this.mSpeechConnection != null) {
            this.unbindService(this.mSpeechConnection);
            this.mSpeechConnection = null;
        }
        return super.onUnbind(intent);
    }

    public void fala(final int modo)
    {
        if((this.mSpeech != null) && (this.mSpeech.isFimFala()))
        {
            final Thread background = new Thread(new Runnable() {
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            mFala(modo);
                        }
                    });
                }
            });
            background.start();
        }
    }

    private void mFala(int modo) {
        //verticalSpeed = vericalSpeed;
        //fl = calcHeight;  a altitude padrao acima dos 3000 ft (QNE)
        //a1 = calcHeight2; altitude em relacao ao QNH (nivel medio mar)
        //a2 = calcHeight2; - aitfeldHeight; altitude acima da decolagem ou aerodromo
        //qnh = calcQNH; - Pressao ao nivel do mar em hPa
        //qfe = qfe; (ajuste a zero) - Pressao do barometro no momento
        //airfieldHeight = aitfeldHeight;

        SharedPreferences sp = getSharedPreferences("VARIOACTIVITY", MODE_PRIVATE);
        boolean bvarioActivity = sp.getBoolean("active", false);
        final StringBuilder texto = new StringBuilder();

        if (mBound && bvarioActivity ) {
            if (modo == 0) {
                int n = (int) prHelper.getData().getA1();
                texto.append("Altitude:").append(n).append("metros.");

                int z = (int) prHelper.getData().getA2();
                if (z > 0)
                    texto.append(", , Acima da rampa ").append(z).append(" metros.");
                else
                    texto.append(", , Abaixo da rampa ").append(z).append(" metros.");

                float v = VarioUtil.getRoundFloat(prHelper.getData().getSpeed(), 1);
                if (v > 0)
                    texto.append(", , Subindo a ").append(v).append(" metros por segundo.");
                else
                    texto.append(", , Descendo a ").append(v).append(" metros por segundo.");

                try {
                    mSpeech.setParaBluetooth(false);
                    mSpeech.setAudioManagerMode(AudioManager.STREAM_NOTIFICATION);
                    mSpeech.sayIt(texto.toString(), TextToSpeech.QUEUE_ADD);
                } catch (Throwable t) {
                    // just end the background thread
                }
            } else  if (modo == 1) {
                try {
                    //String tom = "Radio on";
                    mSpeech.setAudioManagerMode(AudioManager.STREAM_RING);
                    mSpeech.setParaBluetooth(true);
                    //mSpeech.sayIt(tom, TextToSpeech.QUEUE_ADD);
                    mSpeech.gerarTom();
                } catch (Throwable t) {
                    // just end the background thread
                }
            } else  if (modo == 2){
                mSpeech.setAudioContinuo(false);
            }
        }
    }
}
