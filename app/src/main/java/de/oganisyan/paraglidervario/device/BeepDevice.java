package de.oganisyan.paraglidervario.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioTrack;

import de.oganisyan.paraglidervario.util.VarioIfc;
import de.oganisyan.paraglidervario.util.VarioUtil;

public class BeepDevice implements VarioIfc
{
    private static BeepDevice instance;
    private static Object instanceLock;
    
    static {
        synchronized (BeepDevice.instanceLock = new Object()) {
            BeepDevice.instance = new BeepDevice();
        }
    }
    
    public static BeepDevice instanceOf() {
        synchronized (BeepDevice.instanceLock) {
            return BeepDevice.instance;
        }
    }
    
    public static BeepDevice start() {
        synchronized (BeepDevice.instanceLock) {
            return BeepDevice.instance = new AudioDeviceImpl();
        }
    }
    
/*    public boolean isRunning() {
        synchronized (BeepDevice.instanceLock) {
            return BeepDevice.instance instanceof AudioDeviceImpl;
        }
    }*/
    
    public void reInitAudioStreams(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
    }
    
    public void reInitAudioStreams(final Context context) {
        final SharedPreferences sharedPreferences = VarioUtil.getSharedPreferences(context);
        final float float1 = sharedPreferences.getFloat("AudioSetting0", BeepDevice.DEFAULT_AUDIO_SETTINGS[0]);
        final float float2 = sharedPreferences.getFloat("AudioSetting1", BeepDevice.DEFAULT_AUDIO_SETTINGS[1]);
        final float float3 = sharedPreferences.getFloat("AudioSetting2", BeepDevice.DEFAULT_AUDIO_SETTINGS[2]);
        final float float4 = sharedPreferences.getFloat("AudioSetting3", BeepDevice.DEFAULT_AUDIO_SETTINGS[3]);
        final float float5 = sharedPreferences.getFloat("AudioSetting4", BeepDevice.DEFAULT_AUDIO_SETTINGS[4]);
        final float float6 = sharedPreferences.getFloat("AudioSetting5", BeepDevice.DEFAULT_AUDIO_SETTINGS[5]);
        this.setEnebleBeep(sharedPreferences.getBoolean("enableSound", true));
        this.reInitAudioStreams(float1, float2, float3, float4, float5, float6);
    }
    
    public void setEmulatioMode(final boolean b) {
    }
    
    public void setEmulatioValue(final double n) {
    }
    
    public void setEnebleBeep(final boolean b) {
    }
    
    public void setValue(final double n) {
    }
    
    public void stop() {
        synchronized (BeepDevice.instanceLock) {
            BeepDevice.instance = new BeepDevice();
        }
    }
    
    static class AudioDeviceImpl extends BeepDevice implements Runnable
    {
/*        static final int KILL = 2;
        static final int RUN = 0;
        static final int SLEEP = 1;*/
        private short[] audioStreamDw;
        private short[] audioStreamUp;
        int duration;
        float dwMaxBoundary;
        float dwMinBoundary;
        boolean emulationMode;
        float frequencyDw;
        float frequencyUp;
        Thread myThread;
        int rate;
        int status;
        AudioTrack track;
        float upMaxBoundary;
        float upMinBoundary;
        double value;
        
        protected AudioDeviceImpl() {
            super();
            this.frequencyUp = 1000.0f;
            this.frequencyDw = 400.0f;
            this.upMinBoundary = 0.5f;
            this.upMaxBoundary = 4.0f;
            this.dwMinBoundary = 1.0f;
            this.dwMaxBoundary = 4.0f;
            this.duration = 441;
            this.rate = 44100;
            this.value = 0.0;
            this.status = 1;
            this.emulationMode = false;
            this.myThread = new Thread(this);
            this.reInitAudioStreams(AudioDeviceImpl.DEFAULT_AUDIO_SETTINGS[0], AudioDeviceImpl.DEFAULT_AUDIO_SETTINGS[1], AudioDeviceImpl.DEFAULT_AUDIO_SETTINGS[2], AudioDeviceImpl.DEFAULT_AUDIO_SETTINGS[3], AudioDeviceImpl.DEFAULT_AUDIO_SETTINGS[4], AudioDeviceImpl.DEFAULT_AUDIO_SETTINGS[5]);
            this.myThread.start();
        }
        
        private float getSoundLevel(final float n) {
            //Math.abs(n);
            float n2;
            if (n >= 0.0f) {
                n2 = this.upMinBoundary;
            }
            else {
                n2 = this.dwMinBoundary;
            }
            float n3;
            if (n >= 0.0f) {
                n3 = this.upMaxBoundary;
            }
            else {
                n3 = this.dwMaxBoundary;
            }
            float n4;
            if (Math.abs(n) < n2) {
                n4 = 0.0f;
            }
            else if (Math.abs(n) > n3) {
                n4 = 1.0f;
            }
            else {
                n4 = (Math.abs(n) - n2) / (n3 - n2);
            }
            return n4;
        }
        
        private void runPlay() throws InterruptedException {
            float v1_1;
            float v2 = 800f;
            
            short[] v1;
            this.track = new AudioTrack(3, this.rate, 4, 2, AudioTrack.getMinBufferSize(this.rate, 4, 2), 1);
            this.track.play();
            while(this.status == 0) {
                double v11 = this.value;
                float v10 = this.getSoundLevel(((float)v11));
                if(v10 > 0f) {
                    float v8;
                    for(v8 = 0f; (((double)v8)) < (((double)this.duration)) * (1.3 - (((double)v10))
                            ); v8 += v2 / v1_1) {
                        if(v11 > 0) {
                            v1 = this.audioStreamUp;
                        }
                        else {
                            v1 = this.audioStreamDw;
                        }

                        this.writeSamples(v1);

                        if(v11 > 0) {
                            v1_1 = this.frequencyUp;
                        }
                        else {
                            v1_1 = this.frequencyDw;
                        }
                    }

                    this.track.flush();
                }

                Thread.sleep(((long)(((int)((((double)this.duration)) * (1.3 - (((double)v10))))))));
            }
        }
        
        private void runSleep() throws InterruptedException {
            while (true) {
                try {
                    while (this.status == 1) {
                        Thread.sleep(this.duration);
                    }
                }
                catch (InterruptedException ex) {
                    if (this.status != 1) {
                        throw ex;
                    }
                    continue;
                }
                break;
            }
        }
        
        private void writeSamples(final short[] array) {
            this.track.write(array, 0, array.length);
        }
        
        public void reInitAudioStreams(final float frequencyUp, final float upMinBoundary, final float n, final float frequencyDw, final float dwMinBoundary, final float n2) {
            super.reInitAudioStreams(frequencyUp, upMinBoundary, n, frequencyDw, dwMinBoundary, n2);
            this.frequencyUp = frequencyUp;
            this.upMinBoundary = upMinBoundary;
            this.upMaxBoundary = n + upMinBoundary;
            this.frequencyDw = frequencyDw;
            this.dwMinBoundary = dwMinBoundary;
            this.dwMaxBoundary = n2 + dwMinBoundary;
            final float n3 = 6.2831855f * frequencyUp / this.rate;
            final float n4 = 6.2831855f * frequencyDw / this.rate;
            final short[] audioStreamUp = new short[(int)(6.283185307179586 / n3)];
            final short[] audioStreamDw = new short[(int)(6.283185307179586 / n4)];
            for (int i = 0; i < audioStreamUp.length; ++i) {
                audioStreamUp[i] = (short)(32767.0 * Math.sin(n3 * i));
            }
            for (int j = 0; j < audioStreamDw.length; ++j) {
                audioStreamDw[j] = (short)(32767.0 * Math.sin(n4 * j));
            }
            this.audioStreamDw = audioStreamDw;
            this.audioStreamUp = audioStreamUp;
        }
        
        public void run() {
            while (true) {
                try {
                    while (true) {
                        switch (this.status) {
                            case 0: {
                                this.runPlay();
                                continue;
                            }
                            case 1: {
                                this.runSleep();
                                continue;
                            }
                            case 2: {
                                break ;
                            }
                        }
                    }
                }
                catch (InterruptedException ex) {
                }
            }
        }
        
        public void setEmulatioMode(final boolean emulationMode) {
            this.value = 0.0;
            this.emulationMode = emulationMode;
        }
        
        public void setEmulatioValue(final double value) {
            if (this.emulationMode) {
                this.value = value;
            }
        }
        
        public void setEnebleBeep(final boolean enebleBeep) {
            super.setEnebleBeep(enebleBeep);
            int status;
            if (enebleBeep) {
                status = 0;
            }
            else {
                status = 1;
            }
            this.status = status;
            this.myThread.interrupt();
        }
        
        public void setValue(final double value) {
            if (!this.emulationMode) {
                this.value = value;
            }
        }
        
        public void stop() {
            super.stop();
            this.status = 2;
            this.myThread.interrupt();
        }
    }
}