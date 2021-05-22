package top.wxcui.morse.transfrom;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class sound2 {
    final double duration_dot = 1; // duration of sound
    final int sampleRate =22050; // Hz (maximum frequency is 7902.13Hz (B8)22050)
    final int numSamples_dot =(int) duration_dot * sampleRate/5;//22050个点
    final double samples1[] = new double[numSamples_dot];
    final short buffer1[] = new short[numSamples_dot];
    final int frequency=800;
    AudioTrack audioTrack_dot = new AudioTrack(AudioManager.STREAM_MUSIC,
            sampleRate, AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT, buffer1.length,
            AudioTrack.MODE_STATIC);
    public sound2(){
        for (int i = 0; i < numSamples_dot; ++i) {
            samples1[i] = Math.sin(2 * Math.PI * i / (sampleRate / frequency)); // Sine wave
            buffer1[i] = (short) (samples1[i] * Short.MAX_VALUE);  // Higher amplitude increases volume
        }

        audioTrack_dot.write(buffer1, 0, buffer1.length);

    }
    public void start(){

        if(audioTrack_dot.getPlayState()==3)
            //Log.i("Audio", "playState_before_stop: " + audioTrack_dash.getPlayState());
            audioTrack_dot.stop();
        audioTrack_dot.play();

    };


}
