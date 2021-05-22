package top.wxcui.morse.transfrom;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class sound1 {
    final double duration_dash =3; // duration of sound
    final int sampleRate = 22050; // Hz (maximum frequency is 7902.13Hz (B8))
    final int numSamples_dash =(int) duration_dash * sampleRate/5;//22050个点
    final double samples1[] = new double[numSamples_dash];
    final short buffer1[] = new short[numSamples_dash];
    final int frequency=800;
    final AudioTrack audioTrack_dash = new AudioTrack(AudioManager.STREAM_MUSIC,
            sampleRate, AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT, buffer1.length,
            AudioTrack.MODE_STATIC);
    public sound1(){
        for (int i = 0; i < numSamples_dash; ++i) {
            samples1[i] = Math.sin(2 * Math.PI * i/ (sampleRate / frequency)); // Sine wave
            buffer1[i] = (short) (samples1[i] * Short.MAX_VALUE);  // Higher amplitude increases volume

        }
        audioTrack_dash.write(buffer1, 0, buffer1.length);

    }
    public void start(){
       if(audioTrack_dash.getPlayState()==3)
           //Log.i("Audio", "playState_before_stop: " + audioTrack_dash.getPlayState());
           audioTrack_dash.stop();
        //Log.i("Audio", "playState_after_stop: " + audioTrack_dash.getPlayState());

        audioTrack_dash.play();
        //Log.i("Audio", "playState: " + audioTrack_dash.getPlayState());

    };
    ;
}
