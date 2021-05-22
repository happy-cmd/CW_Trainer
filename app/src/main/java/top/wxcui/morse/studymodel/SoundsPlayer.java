package top.wxcui.morse.studymodel;


import android.os.AsyncTask;
import android.os.CountDownTimer;

import top.wxcui.morse.transfrom.MorseCoder;
import top.wxcui.morse.transfrom.sound1;
import top.wxcui.morse.transfrom.sound2;

public class SoundsPlayer  {
    MorseCoder morseCoder=new MorseCoder();
    String morse;//
    char[] morse_squence;






    public SoundsPlayer(char[] mode_lesson) {

        //String s = String.valueOf('c');
     morse=String.valueOf(mode_lesson);
     morse_squence=morseCoder.encode(morse).toCharArray();


    }

    public char[] transform() {
        return morse_squence;
    }




}




