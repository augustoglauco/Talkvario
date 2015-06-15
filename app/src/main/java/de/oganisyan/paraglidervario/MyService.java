package de.oganisyan.paraglidervario;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.*;
import android.speech.*;
import android.util.Log;

public class MyService extends Service{
protected static AudioManager mAudioManager;
protected SpeechRecognizer mSpeechRecognizer;
protected Intent mSpeechRecognizerIntent;
protected final Messenger mServerMessenger = new Messenger(new IncomingHandler(this));

public static final String NOTIFICATION = "de.oganisyan.paraglidervario";
public static final String COMANDO = "comando";

protected boolean mIsListening;
protected volatile boolean mIsCountDownOn;
private static boolean mIsStreamSolo;

static final int MSG_RECOGNIZER_START_LISTENING = 1;
static final int MSG_RECOGNIZER_CANCEL = 2;


private static final String TAG = null;

@Override
public void onCreate()
{
    super.onCreate();

    //MainActivity.methodText.setText("onCreate");

    mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
    mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                     RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                                     this.getPackageName());
}

protected static class IncomingHandler extends Handler
{
    private WeakReference<MyService> mtarget;

    IncomingHandler(MyService target)
    {
        mtarget = new WeakReference<MyService>(target);
    }

    @Override
    public void handleMessage(Message msg)
    {
       // MainActivity.methodText.setText("handleMessage");

        final MyService target = mtarget.get();

        switch (msg.what)
        {
            case MSG_RECOGNIZER_START_LISTENING:

/*                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    // turn off beep sound
                    if (!mIsStreamSolo)
                    {
                        //mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, true);
                        //mIsStreamSolo = true;
                    }
                }*/
                 if (!target.mIsListening)
                 {
                     target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                     target.mIsListening = true;
                 }
                 break;

             case MSG_RECOGNIZER_CANCEL:
                if (mIsStreamSolo)
               {
                    mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, false);
                    mIsStreamSolo = false;
               }
                  target.mSpeechRecognizer.cancel();
                  target.mIsListening = false;
                  //Log.d(TAG, "message canceled recognizer"); //$NON-NLS-1$
                  break;
         }
   } 
} 

// Count down timer for Jelly Bean work around
protected CountDownTimer mNoSpeechCountDown = new CountDownTimer(5000, 5000)
{

    @Override
    public void onTick(long millisUntilFinished)
    {
       // MainActivity.methodText.setText("onTick");
        // TODO Auto-generated method stub

    }

    @Override
    public void onFinish()
    {

        //MainActivity.methodText.setText("onFinish");

        mIsCountDownOn = false;
        Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
        try
        {
            mServerMessenger.send(message);
            message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
            mServerMessenger.send(message);
        }
        catch (RemoteException e)
        {

        }
    }
};

@Override
public void onDestroy()
{
    super.onDestroy();

    //MainActivity.methodText.setText("onDestroy");

    if (mIsCountDownOn)
    {
        mNoSpeechCountDown.cancel();
    }
    if (mSpeechRecognizer != null)
    {
        mSpeechRecognizer.destroy();        
    }
}

protected class SpeechRecognitionListener implements RecognitionListener
{
    private static final String TAG = "SpeechApp";

    @Override
    public void onBeginningOfSpeech()
    {
        //MainActivity.methodText.setText("onBeginningOfSpeech");
        // speech input will be processed, so there is no need for count down anymore
        if (mIsCountDownOn)
        {
            mIsCountDownOn = false;
            mNoSpeechCountDown.cancel();
        }               
        //Log.d(TAG, "onBeginingOfSpeech"); //$NON-NLS-1$
    }

    @Override
    public void onBufferReceived(byte[] buffer)
    {
       // MainActivity.methodText.setText("onBufferReceived");
    }

    @Override
    public void onEndOfSpeech()
    {
        //Log.d(TAG, "onEndOfSpeech"); //$NON-NLS-1$
       // MainActivity.methodText.setText("onEndOfSpeech");
     }

    @Override
    public void onError(int error)
    {
       // MainActivity.methodText.setText("onError");

        if (mIsCountDownOn)
        {
            mIsCountDownOn = false;
            mNoSpeechCountDown.cancel();
        }
         mIsListening = false;
         Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
         try
         {
                mServerMessenger.send(message);
         }
         catch (RemoteException e)
         {

         }
        //Log.d(TAG, "error = " + error); //$NON-NLS-1$
    }

   @Override
    public void onEvent(int eventType, Bundle params)
    {
        //MainActivity.methodText.setText("onEvent");
    }

    @Override
    public void onPartialResults(Bundle partialResults)
    {
        //MainActivity.methodText.setText("onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle params)
    {
       // MainActivity.methodText.setText("onReadyForSpeech");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            mIsCountDownOn = true;
            mNoSpeechCountDown.start();

        }
        Log.d(TAG, "onReadyForSpeech"); //$NON-NLS-1$
    }

    @Override
    public void onResults(Bundle results)
    {
        //MainActivity.methodText.setText("onResults");

        System.out.println(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
        //MainActivity.resultsText.setText(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).toString());
        ArrayList<String> strlist = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i = 0; i < strlist.size();i++ ) {
                Log.d("Speech", "result=" + strlist.get(i));
        }
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(COMANDO, strlist.get(1));
        sendBroadcast(intent);
        mIsListening = false;
        //VarioService.sendMessage(MyService.MSG_RECOGNIZER_START_LISTENING);        
    }

    @Override
    public void onRmsChanged(float rmsdB)
    {
        //MainActivity.methodText.setText("onRmsChanged");
    }

}

    @Override
    public IBinder onBind(Intent intent) {

   // MainActivity.methodText.setText("onBind");

    // TODO Auto-generated method stub
    Log.d(TAG, "onBind");  //$NON-NLS-1$
    return mServerMessenger.getBinder();
    }
}