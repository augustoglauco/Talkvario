package de.oganisyan.paraglidervario;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Binder;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

import de.oganisyan.paraglidervario.util.VarioUtil;

public class Speech extends Service implements OnInitListener {

	private String str;
	private TextToSpeech mTts;
	private static final String TAG="SpeechService";
	private final IBinder mBinder = new LocalBinder();
	public static boolean fimFala = true;
	private int audioManagerMode = AudioManager.STREAM_NOTIFICATION;
    private boolean paraBluetooth = false;
    private AudioManager audioManager;
    private AudioTrack audioTrack;
    private final int duration = 200; // miliseconds
    private final int sampleRate = 44100;
    private final int numSamples = duration/1000 * sampleRate;
    private final double sample[] = new double[numSamples];
    private float freqOfTone = 19000; // hz
    private final byte generatedSnd[] = new byte[2 * numSamples];
    public static final int dadosVoo = 0;
    public static final int tom = 1;

    public void setParaBluetooth(boolean paraBluetooth) {
        this.paraBluetooth = paraBluetooth;
    }

	public void setAudioManagerMode(int audioManagerMode) {
		this.audioManagerMode = audioManagerMode;
	}

	public boolean isFimFala() {
		return fimFala;
	}

	public void setFimFala(boolean mFimFala) {
		fimFala = mFimFala;
	}

	@Override
	public void onCreate() {
		mTts = new TextToSpeech(this, this);
		mTts.setSpeechRate(1);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		Log.v(TAG, "oncreate_service");
		str ="   Serviço Ativo";
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}
        if (audioTrack != null){
            audioTrack.release();
        }
        if (audioManager != null){
            audioManager =null;
        }

		super.onDestroy();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
				@Override 
				public void onDone(String utteranceId) {
					/* 
					 * When myTTS is done speaking the current "audio file", 
					 * call playAudio on the next audio file. 
					 */ 
					if (utteranceId.equals("FINISHED PLAYING")) {
						mTts.stop();
						setFimFala(true);
                        if ((audioManager.isBluetoothA2dpOn())||(audioManager.isBluetoothScoAvailableOffCall())) {
                            audioManager.setBluetoothScoOn(false);
                            audioManager.stopBluetoothSco();
                            audioManager.setSpeakerphoneOn(true);
                        }
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
					} 
				} 
 
				@Override 
				public void onError(String utteranceId) {
				} 
 
				@Override 
				public void onStart(String utteranceId) {
				} 
			}); 
			
			int result = mTts.setLanguage(Locale.getDefault());
			if (result == TextToSpeech.LANG_MISSING_DATA ||
					result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.v(TAG, "Language is not available.");
			} else sayIt(str, TextToSpeech.QUEUE_FLUSH);
		}
		else
            Log.v(TAG, "Could not initialize TextToSpeech.");
	}

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        Speech getService() {
            // Return this instance of LocalService so clients can call public methods
            return Speech.this;
        }
    }

	public void sayIt(String str, int modo) {
		HashMap<String, String> myHashAlarm = new HashMap<String, String>();
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(audioManagerMode));
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "FINISHED PLAYING");

        controleAudioManager(0);
		this.setFimFala(false);
		mTts.speak(str, modo, myHashAlarm);
	}

	/*public void saySilence(long tempo) {
		HashMap<String, String> myHashAlarm = new HashMap<String, String>();
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(audioManagerMode));
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "FINISHED PLAYING");
    	this.setFimFala(false);
		mTts.playSilence(tempo, TextToSpeech.QUEUE_ADD, myHashAlarm) ;
	}*/

    private void genTone(){
        // fill out the array

        final SharedPreferences sharedPreferences = VarioUtil.getSharedPreferences(getApplicationContext());
        freqOfTone = Float.parseFloat(sharedPreferences.getString("freqTomRadioEdit", Float.toString(freqOfTone)));

        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (double dVal : sample) {
            short val = (short) (dVal * 32767);
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    private void playTone(){

        controleAudioManager(0);

        try {
            if (audioTrack == null) {
                audioTrack = new AudioTrack(audioManagerMode,
                    sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                    AudioTrack.MODE_STREAM);
            }
            audioTrack.setNotificationMarkerPosition(numSamples);
            audioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
                @Override
                public void onPeriodicNotification(AudioTrack track) {
                    // nothing to do
                }

                @Override
                public void onMarkerReached(AudioTrack track) {
                    controleAudioManager(1);
                    setFimFala(true);
                }
            });

            this.setFimFala(false);
            audioTrack.write(generatedSnd, 0, generatedSnd.length);     // Load the track
            audioTrack.play();                             // Play the track
        }
        catch (Exception e){
            Log.v(TAG, "Erro playing tone:" + e.toString());
        }
    }

    public void gerarTom() {
        genTone();
        playTone();
    }

    private void controleAudioManager(int controle){

        switch (controle) {
            case 0: // entrada para falar algo
                    // diminuir ou para media
                    // verificar se esta setado o direcionamento entre speaker ou BTH
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                if (audioManager.isBluetoothA2dpOn()){
                    if (paraBluetooth) {
                        audioManager.setBluetoothScoOn(true);
                        audioManager.startBluetoothSco();
                    }
                    else {
                        audioManager.setBluetoothScoOn(false);
                        audioManager.stopBluetoothSco();
                    }
                }
                break;

            case 1: // saida após falar algo
                    // aumentar ou prosseguir media
                    // verificar se esta setado o direcionamento entre speaker ou BTH

                if (audioManager.isBluetoothA2dpOn()) {
                    audioManager.setBluetoothScoOn(false);
                    audioManager.stopBluetoothSco();
                }

                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                break;
            default:

        }

    }

}