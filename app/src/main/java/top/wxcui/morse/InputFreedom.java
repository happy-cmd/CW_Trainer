package top.wxcui.morse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import top.wxcui.morse.transfrom.MorseCoder;




public class InputFreedom extends AppCompatActivity {
    private MorseCoder morseCoder = new MorseCoder();
    private EditText The_Result_Edittext;
    private TextView Display_View;

    private SharedPreferences sharedPreferences;//设置对象
    private SharedPreferences.OnSharedPreferenceChangeListener listener;//设置数据变化监听器
    private int WPM;//WPM所设值
    private Boolean Swithc_DoubleKey;//双键左右互换


    //可控进度条
    private ProgressBar  limit_progressbar_word  = null;
    private ProgressBar  limit_progressbar_space  = null;
    private int m = 0;//进度条的数值
    private int m_space=0;


    //计时器状态   注意，这里将来将引入 具体的摩尔斯字符间隙
    private Boolean isStrat_countDownTimer = false;
    private CountDownTimer countDownTimer1_word;
    private CountDownTimer countDownTimer2_space;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_freedom);

        The_Result_Edittext = findViewById(R.id.The_Result_Edittext);
        The_Result_Edittext.setKeyListener(null);
        Display_View = findViewById(R.id.Display_View);
        Button FRC_clean = findViewById(R.id.FRC_clean);
        Button Input_dash = findViewById(R.id.Input_dash);
        Button Input_dot = findViewById(R.id.Input_dot);
        Button Input_single_key = findViewById(R.id.Input_single_key);
        Display_View = findViewById(R.id.Display_View);
        limit_progressbar_word = findViewById(R.id.progressBar_limit_word);
        limit_progressbar_space=findViewById(R.id.progressBar_limit_space);
        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        WPM = Integer.valueOf(sharedPreferences.getString("the_WPM_speed", "5"));
        Swithc_DoubleKey=sharedPreferences.getBoolean("preference_Swithc_DoubleKey",false);


        /*
防止过快点击的方法来源：https://blog.csdn.net/zhufuing/article/details/53021835
@MIN_CLICK_DELAY_TIME 将为设置所控制
 */

        abstract class OnMultiClickListener implements View.OnClickListener {
            private final int MIN_CLICK_DELAY_TIME = 1080/WPM;
            private long lastClickTime;


            public abstract void onMultiClick(View v);

            @Override
            public void onClick(View v) {
                long curClickTime = System.currentTimeMillis();
                if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                    // 超过点击间隔后再将lastClickTime重置为当前点击时间
                    lastClickTime = curClickTime;
                    onMultiClick(v);
                }
            }
        }

        int time_between_word=1200/WPM*3; //一个单词内各个字母的空 ms
        int time_between_space=1200/WPM*7;//单词间的空格 ms
        Input_single_key.setEnabled(false);
        if(Swithc_DoubleKey) {
            Input_dash=findViewById(R.id.Input_dot);
            Input_dot=findViewById(R.id.Input_dash);
            Input_dash.setText(R.string.but1_dash_content);
            Input_dot.setText(R.string.but1_dot_content);
        }





        countDownTimer1_word = new CountDownTimer(time_between_word, time_between_word / 10) {
            public void onTick(long millisUntilFinished) {
                m += 10;
                limit_progressbar_word.setProgress(m);
                isStrat_countDownTimer = true;

            }
            public void onFinish() {
                limit_progressbar_word.setProgress(0);
                m = 0;
                isStrat_countDownTimer = false;
                The_Result_Edittext.append(morseCoder.decode(Display_View.getText().toString()));//自动提交结果区
                Display_View.setText(null);//刷新-归零摩尔斯电码输入区
            }

        };
        countDownTimer2_space=new CountDownTimer(time_between_space, time_between_space / 10) {
            public void onTick(long millisUntilFinished) {
                m_space += 10;
                limit_progressbar_space.setProgress(m_space);
                isStrat_countDownTimer = true;

            }
            public void onFinish() {
                limit_progressbar_space.setProgress(0);
                m_space = 0;
                isStrat_countDownTimer = false;
                The_Result_Edittext.append("   ");//自动提交结果区 以三格空表示断
               // Display_View.setText(null);//刷新-归零摩尔斯电码输入区
            }

        };
    SoundPool soundPool;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes=new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
            soundPool=new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();

        }
        else
            soundPool=new SoundPool(2,0,0);
        int sound_e;
        int sound_t;


        if (WPM < 6) {
            sound_e=soundPool.load(this,R.raw.wpm5e,1);
            sound_t=soundPool.load(this,R.raw.wpm5t,1);
        } else if (WPM < 10) {
            sound_e=soundPool.load(this,R.raw.wpm7e,1);
            sound_t=soundPool.load(this,R.raw.wpm7t,1);
        } else if (WPM < 12) {
            sound_e=soundPool.load(this,R.raw.wpm10e,1);
            sound_t=soundPool.load(this,R.raw.wpm10t,1);
        } else if (WPM < 14) {
            sound_e=soundPool.load(this,R.raw.wpm12e,1);
            sound_t=soundPool.load(this,R.raw.wpm12t,1);
        } else if (WPM < 16) {
            sound_e=soundPool.load(this,R.raw.wpm14e,1);
            sound_t=soundPool.load(this,R.raw.wpm14t,1);
        } else if (WPM < 18) {
            sound_e=soundPool.load(this,R.raw.wpm16e,1);
            sound_t=soundPool.load(this,R.raw.wpm16t,1);
        } else if (WPM < 20) {
            sound_e=soundPool.load(this,R.raw.wpm18e,1);
            sound_t=soundPool.load(this,R.raw.wpm18t,1);
        } else {
            sound_e=soundPool.load(this,R.raw.wpm20e,1);
            sound_t=soundPool.load(this,R.raw.wpm20t,1);
        }



        Input_dot.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {

                if (!isStrat_countDownTimer) {
                    countDownTimer1_word.start();
                    countDownTimer2_space.start();
                }
                else {
                    countDownTimer1_word.cancel();
                    countDownTimer2_space.cancel();
                    isStrat_countDownTimer = false;
                    limit_progressbar_word.setProgress(0);
                    limit_progressbar_space.setProgress(0);
                    m = 0;
                    m_space=0;
                    countDownTimer1_word.start();
                    countDownTimer2_space.start();

                }
                Display_View.append(".");
                soundPool.play(sound_e,1,1,1,0,1);


            }
        });

        Input_dash.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {


                if (!isStrat_countDownTimer) {
                    countDownTimer1_word.start();
                    countDownTimer2_space.start();
                }
                else {
                    countDownTimer1_word.cancel();
                    countDownTimer2_space.cancel();
                    isStrat_countDownTimer = false;
                    limit_progressbar_word.setProgress(0);
                    limit_progressbar_space.setProgress(0);
                    m = 0;
                    m_space=0;
                    countDownTimer1_word.start();
                    countDownTimer2_space.start();

                }
                Display_View.append("-");
//                AsyncPlayer ap=new AsyncPlayer("Player");
//                Uri uri=Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.wpm10e);
//                ap.play(getBaseContext(), uri, false, USAGE_MEDIA);
                //dash_sound.start();
                soundPool.play(sound_t,1,1,1,0,1);

            }
        });
        FRC_clean.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                The_Result_Edittext.setText(null);
                Display_View.setText(null);
            }
        });



    }



}







