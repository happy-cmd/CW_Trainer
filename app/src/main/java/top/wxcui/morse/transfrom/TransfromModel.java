package top.wxcui.morse.transfrom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import top.wxcui.morse.R;

abstract class OnMultiClickListener implements View.OnClickListener{
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME =200;
    private static long lastClickTime;

    public abstract void onMultiClick(View v);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onMultiClick(v);
        }
    }
}
public class TransfromModel extends AppCompatActivity {
    private MorseCoder morseCoder=new MorseCoder();
    private EditText text_english;
    private TextView text_morse;
    private sound1 dash_sound=new sound1();
    private sound2 dot_sound=new sound2();
    private Boolean Swithc_DoubleKey;//双键左右互换

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfrom_model);
        Button button1_english=findViewById(R.id.button1_transfrom);
        Button button2_morse=findViewById(R.id.button2_transfrom);
        Button button3_clear=findViewById(R.id.button3_transfrom);
        Button button4_dot=findViewById(R.id.button4_dot);
        Button button5_dash=findViewById(R.id.button5_dash);
        Button button6_space=findViewById(R.id.button6_space);
        text_english=findViewById(R.id.english_transfrom);
        text_morse=findViewById(R.id.morsecode_transfrom);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        Swithc_DoubleKey=sharedPreferences.getBoolean("preference_Swithc_DoubleKey",false);
        if(Swithc_DoubleKey) {
            button4_dot=findViewById(R.id.button5_dash);
            button5_dash=findViewById(R.id.button4_dot);
            button5_dash.setText(R.string.button5_dash_content);
            button4_dot.setText(R.string.button4_dot_content);
        }
      


        button1_english.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                String m=text_english.getText().toString();
                text_morse.setText(morseCoder.encode(m));
            }
        });
        button2_morse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m=text_morse.getText().toString();
                text_english.setText(morseCoder.decode(m));
            }
        });
        button3_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_english.setText(null);
                text_morse.setText(null);
            }
        });


        button4_dot.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                dot_sound.start();
                String m=text_morse.getText().toString();
                m=m+'.';
                text_morse.setText(m);


            }
        });

        button5_dash.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                dash_sound.start();
                String m=text_morse.getText().toString();
                m=m+'-';
                text_morse.setText(m);


            }
        });

        button6_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m=text_morse.getText().toString();
                m=m+'/';
                text_morse.setText(m);
            }
        });

    }

}