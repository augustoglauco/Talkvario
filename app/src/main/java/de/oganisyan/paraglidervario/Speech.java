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
    private static final String TAG = "SpeechService";
    private final IBinder mBinder = new LocalBinder();
    public static boolean fimFala = true;
    //private int audioManagerMode = AudioManager.STREAM_NOTIFICATION;
    private boolean paraBluetooth = false;
    private AudioManager audioManager;
    private AudioTrack audioTrack;
    private final int sampleRate = 44100;
    public static boolean tomContinuo = true;
    public static final int FALA_TOM = 1;
    public static final int FALA_DADOSVOO = 0;
    public static final int FALA_PARATOM = -1;

    //public void setParaBluetooth(boolean paraBluetooth) {
     //   this.paraBluetooth = paraBluetooth;
    //}

   // public void setAudioManagerMode(int audioManagerMode) {
   //     this.audioManagerMode = audioManagerMode;
   // }

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
        str = "   Serviço Ativo";
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        if (audioTrack != null) {
            audioTrack.release();
        }
        if (audioManager != null) {
            audioManager = null;
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
                        //controleAudioManager(0, mSpeaker, mAudioManagerStream);
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
            } else sayIt(str, TextToSpeech.QUEUE_FLUSH, false);
        } else
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

    public void sayIt(String str, int mAudioManagerStream, boolean mSpeaker  ) {

        HashMap<String, String> myHashAlarm = new HashMap<String, String>();
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(mAudioManagerStream));
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "FINISHED PLAYING");
        int modo = TextToSpeech.QUEUE_ADD;

        controleAudioManager(0, mSpeaker, mAudioManagerStream);
        this.setFimFala(false);
        mTts.speak(str, modo, myHashAlarm);
    }

	/*public void saySilence(long tempo, int mAudioManagerStream) {
		HashMap<String, String> myHashAlarm = new HashMap<String, String>();
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(mAudioManagerStream));
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "FINISHED PLAYING");
    	this.setFimFala(false);
		mTts.playSilence(tempo, TextToSpeech.QUEUE_ADD, myHashAlarm) ;
	}*/

    private byte[] genTone(float mFreqOfTone, int mNumSamples) {

        double sample[] = new double[mNumSamples];
        byte generatedSnd[] = new byte[2 * mNumSamples];

        for (int i = 0; i < mNumSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / mFreqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (double dVal : sample) {
            short val = (short) (dVal * 32767);
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
        return generatedSnd;
    }

    private void playTone(byte mGeneratedSnd[], int mAudioManagerStream ) {

       // if (controleAudioManager(0)) { //verifica se pegou focus
            try {
                if (audioTrack == null) {
                    audioTrack = new AudioTrack(mAudioManagerStream,
                            sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_16BIT, mGeneratedSnd.length,
                            AudioTrack.MODE_STREAM);
                }
                this.setFimFala(false);
                audioTrack.play();                             // Play the track
                while(tomContinuo){
                    audioTrack.write(mGeneratedSnd, 0, mGeneratedSnd.length);     // Load the track
                }
                setFimFala(true);
                //controleAudioManager(1);

            } catch (Exception e) {
                Log.v(TAG, "Erro playing tone:" + e.toString());
            }
        //}
    }

    public void gerarTom(int mAudioManagerStream, boolean mSpeaker) {

        int duration = 1; // miliseconds
        int numSamples = duration * sampleRate;
        float freqOfTone = 19000; // hz

        final SharedPreferences sharedPreferences = VarioUtil.getSharedPreferences(getApplicationContext());
        freqOfTone = Float.parseFloat(sharedPreferences.getString("freqTomRadioEdit", Float.toString(freqOfTone)));
        if (controleAudioManager(0, mSpeaker, mAudioManagerStream)){
            playTone(genTone(freqOfTone, numSamples), mAudioManagerStream);
            controleAudioManager(1, mSpeaker, mAudioManagerStream);
            sayIt("  Róger",AudioManager.STREAM_NOTIFICATION, false);
    }
    }

    private boolean controleAudioManager(int controle, boolean mSpeaker, int mAudiomanagerStream ) {
        boolean resultado = true;
        switch (controle) {
            case 0: // entrada para falar algo
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true); // diminuir ou para media
                if (audioManager.isBluetoothA2dpOn()) { // verificar se esta setado o direcionamento entre speaker ou BTH
                    if (mSpeaker) {
                        audioManager.setBluetoothScoOn(true);
                        try {
                            //audioManager.setStreamVolume(mAudiomanagerStream, audioManager.getStreamMaxVolume(mAudiomanagerStream),0);
                            //Class audioSystemClass = Class.forName("android.media.AudioSystem");
                            //Method setForceUse = audioSystemClass.getMethod("setForceUse", int.class, int.class);
                                // First 1 == FOR_MEDIA, second 1 == FORCE_SPEAKER. To go back to the default
                                // behavior, use FORCE_NONE (0).
                              /* FORCE_NONE = 0;
                                 FORCE_SPEAKER = 1;
                                 FORCE_HEADPHONES = 2;
                                 FORCE_BT_SCO = 3;
                                 FORCE_BT_A2DP = 4;
                                 FORCE_WIRED_ACCESSORY = 5;
                                 FORCE_BT_CAR_DOCK = 6;
                                 FORCE_BT_DESK_DOCK = 7;
                                 FORCE_ANALOG_DOCK = 8;
                                 FORCE_DIGITAL_DOCK = 9;
                                 FORCE_NO_BT_A2DP = 10;
                                 FORCE_SYSTEM_ENFORCED = 11;
                                 FORCE_HDMI_SYSTEM_AUDIO_ENFORCED = 12;
                                 NUM_FORCE_CONFIG = 13; */
                            //setForceUse.invoke(null, 1, 1);
                        } catch (Exception ex) {
                            resultado = false;
                        }
                    }
                }
                break;

            case 1: // saida após falar algo
                if (audioManager.isBluetoothA2dpOn()) { // verificar se esta setado o direcionamento entre speaker ou BTH
                    try {
                        //Class audioSystemClass = Class.forName("android.media.AudioSystem");
                        //Method setForceUse = audioSystemClass.getMethod("setForceUse", int.class, int.class);
                        //setForceUse.invoke(null, 1, 0);
                        audioManager.setBluetoothScoOn(false);
                    } catch (Exception ex) {
                        resultado = false;
                    }
                }
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);  // aumentar ou prosseguir media
                break;
            default:

        }
        return resultado;
    }
}