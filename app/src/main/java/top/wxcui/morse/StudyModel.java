package top.wxcui.morse;
//自定义引用类

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


import java.util.Timer;
import java.util.TimerTask;

import top.wxcui.morse.studymodel.Class1;

import top.wxcui.morse.studymodel.Class1ViewModel;
import top.wxcui.morse.studymodel.Class2;
import top.wxcui.morse.studymodel.Class2ViewModel;
import top.wxcui.morse.studymodel.Class3;
import top.wxcui.morse.studymodel.Class3ViewModel;
import top.wxcui.morse.studymodel.SoundsPlayer;
import top.wxcui.morse.studymodel.TabLayoutMediator;
import top.wxcui.morse.transfrom.MorseCoder;
import top.wxcui.morse.transfrom.sound1;
import top.wxcui.morse.transfrom.sound2;
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

public class StudyModel extends AppCompatActivity {
    private TextView textView_adding;
    private ImageView control_led;
    //训练模式1下对应课程的内容
    private String[] Class1_Lessons;
    private String[] Class2_Lessons;
    private EditText the_result_edittext;


    //获取设置值
    private SharedPreferences sharedPreferences;//设置对象
    private SharedPreferences.OnSharedPreferenceChangeListener listener;//设置数据变化监听器
    private int WPM;//WPM所设值
    private int MODE;//训练模式选择
    private int delay;
    private SoundsPlayer soundsPlayer;
    CountDownTimer countDownTimer;
    private  SoundPool soundPool;

    private int flag_countDownTimer=0;//一次倒计时完毕的标志

    MorseCoder morseCoder=new MorseCoder();
    private char[] chars;

    Button START;
    Button END;
    Button CLEAR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_model);
        TabLayout tabLayout = findViewById(R.id.chooosebar_tablayout);
        ViewPager2 viewPager2 = findViewById(R.id.choosebar_viewpager2);
        the_result_edittext = findViewById(R.id.The_Result_Exercises);
        textView_adding = findViewById(R.id.studymodel_adding);
        control_led = findViewById(R.id.studymodel_LED);

        //按钮使能-初始化
        START = findViewById(R.id.studymodel_button1);
        END = findViewById(R.id.studymodel_button2);
        CLEAR = findViewById(R.id.studymodel_button3);
        START.setEnabled(false);
        END.setEnabled(false);


        //设置值初始化
        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        WPM = Integer.valueOf(sharedPreferences.getString("the_WPM_speed", "5"));
        delay = 1200 / WPM;
        MODE = Integer.parseInt(sharedPreferences.getString("preference_TrainRules", "1"));

        listener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        if (key.equals("the_WPM_speed")) {
                            WPM = Integer.valueOf(sharedPreferences.getString("the_WPM_speed", "5"));
                            //当设置中修改WPM时，同样修改此class中的WPM值
                         //   the_result_edittext.setText(WPM + "!");
                            delay = 1200 / WPM;
                        }
                        ;
                        if (key.equals("preference_TrainRules")) {
                            MODE = Integer.parseInt(sharedPreferences.getString("preference_TrainRules", "1"));
                            //当设置中修改WPM时，同样修改此class中的WPM值
                            Toast.makeText(getApplicationContext(),"注意，如果您在课程学习训练中，请重新点击课程内容以应用新的排序！",Toast.LENGTH_LONG).show();
                        }
                        ;

                    }
                };
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
             sound_t=soundPool.load(this,R.raw.wpm5e,1);
        } else if (WPM < 10) {
            sound_e=soundPool.load(this,R.raw.wpm7e,1);
            sound_t=soundPool.load(this,R.raw.wpm7e,1);
        } else if (WPM < 12) {
            sound_e=soundPool.load(this,R.raw.wpm10e,1);
            sound_t=soundPool.load(this,R.raw.wpm10e,1);
        } else if (WPM < 14) {
            sound_e=soundPool.load(this,R.raw.wpm12e,1);
            sound_t=soundPool.load(this,R.raw.wpm12t,1);
        } else if (WPM < 16) {
            sound_e=soundPool.load(this,R.raw.wpm14e,1);
            sound_t=soundPool.load(this,R.raw.wpm14e,1);
        } else if (WPM < 18) {
            sound_e=soundPool.load(this,R.raw.wpm16e,1);
            sound_t=soundPool.load(this,R.raw.wpm16e,1);
        } else if (WPM < 20) {
            sound_e=soundPool.load(this,R.raw.wpm18e,1);
            sound_t=soundPool.load(this,R.raw.wpm18e,1);
        } else {
            sound_e=soundPool.load(this,R.raw.wpm20e,1);
            sound_t=soundPool.load(this,R.raw.wpm20e,1);
        }

        Class1_Lessons = getResources().getStringArray(R.array.class1_lesson);
        Class2_Lessons=getResources().getStringArray(R.array.class2_lesson);

        countDownTimer = new CountDownTimer(1333*delay, delay) {

            int i = 0;
            int number_word = 0;
            Boolean flag_space = false;
            Boolean flag_word = false;
            int time_three = 0;
            int time_seven = 0;
            String message="";


            @Override
            public void onTick(long millisUntilFinished) {

                for (; i < chars.length; i++) {
                    if (flag_space) {
                        time_seven = time_seven - 1;
                        if (time_seven == 0)
                            flag_space = false;
                        i=i-1;
                    } else if (flag_word) {
                        time_three = time_three - 1;
                        if (time_three == 0)
                            flag_word = false;
                        i=i-1;

                    } else if (chars[i] == '.') {
                        message=message+".";
                        soundPool.play(sound_e,1,1,1,0,1);

                    } else if (chars[i] == '-') {
                        message=message+"-";
                        soundPool.play(sound_t,1,1,1,0,1);

                    } else {

                        number_word += 1;
                        if (number_word % 5 != 0) {
                            flag_word = true;
                            time_three = 2;

                            the_result_edittext.append(morseCoder.decode(message));
                            message="";

                        } else {
                            flag_space = true;
                            time_seven = 6;
                            number_word=0;
                            the_result_edittext.append(morseCoder.decode(message));
                            message="";
                            the_result_edittext.append("  ");

                        }
                    }
                    i=i+1;
                    break;
                }
            }
            @Override
            public void onFinish() {
                i = 0;
                number_word = 0;
                flag_space = false;
                flag_word = false;
                time_three = 0;
                time_seven = 0;
            }
        };
        the_result_edittext.setFocusable(false);//禁止输入
        Class1ViewModel class1ViewModel = new ViewModelProvider(this).get(Class1ViewModel.class);
        Class2ViewModel class2ViewModel = new ViewModelProvider(this).get(Class2ViewModel.class);
        Class3ViewModel class3ViewModel = new ViewModelProvider(this).get(Class3ViewModel.class);

        class1ViewModel.getSelectedItem().observe(this, item -> {
            loading(item);
            if (countDownTimer!= null)
                countDownTimer.cancel();

        });

        class2ViewModel.getSelectedItem().observe(this, item -> {
            loading(item);
            if (countDownTimer!= null)
                countDownTimer.cancel();
        });

        class3ViewModel.getSelectedItem().observe(this, item -> {
            loading2(item);//自定义内容的加载
            if (countDownTimer!= null)
                countDownTimer.cancel();
        });


        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new Class1(class1ViewModel);
                    case 1:
                        return new Class2(class2ViewModel);
                    case 2:
                        return new Class3(class3ViewModel);
                    default:
                        return null;
                }
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });
//是否选择滑动
        viewPager2.setUserInputEnabled(false);
        new TabLayoutMediator(tabLayout, viewPager2, true, false, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                int m = position + 1;
                tab.setText("训练模式" + m);
            }
        }).attach();



        START.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {

                chars = soundsPlayer.transform();
              //  the_result_edittext.setText(chars, 0, chars.length);
                countDownTimer.start();
                START.setEnabled(false);
                END.setEnabled(true);

            }
        });

        END.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (countDownTimer!= null)
                countDownTimer.cancel();
                countDownTimer = new CountDownTimer(1333*delay, delay) {

                    int i = 0;
                    int number_word = 0;
                    Boolean flag_space = false;
                    Boolean flag_word = false;
                    int time_three = 0;
                    int time_seven = 0;
                    String message="";


                    @Override
                    public void onTick(long millisUntilFinished) {

                        for (; i < chars.length; i++) {
                            if (flag_space) {
                                time_seven = time_seven - 1;
                                if (time_seven == 0)
                                    flag_space = false;
                                i=i-1;
                            } else if (flag_word) {
                                time_three = time_three - 1;
                                if (time_three == 0)
                                    flag_word = false;
                                i=i-1;

                            } else if (chars[i] == '.') {
                                message=message+".";
                                soundPool.play(sound_e,1,1,1,0,1);

                            } else if (chars[i] == '-') {
                                message=message+"-";
                                soundPool.play(sound_t,1,1,1,0,1);

                            } else {

                                number_word += 1;
                                if (number_word % 5 != 0) {
                                    flag_word = true;
                                    time_three = 2;

                                    the_result_edittext.append(morseCoder.decode(message));
                                    message="";

                                } else {
                                    flag_space = true;
                                    time_seven = 6;
                                    number_word=0;
                                    the_result_edittext.append(morseCoder.decode(message));
                                    message="";
                                    the_result_edittext.append("  ");

                                }
                            }
                            i=i+1;
                            break;
                        }
                    }
                    @Override
                    public void onFinish() {
                        i = 0;
                        number_word = 0;
                        flag_space = false;
                        flag_word = false;
                        time_three = 0;
                        time_seven = 0;
                    }
                };
                START.setEnabled(true);
                END.setEnabled(false);

            }
        });
        CLEAR.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                the_result_edittext.setText(null);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer!=null)
            countDownTimer.cancel();
    }

    //注销设置的变化监测器
    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.studymodel_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Inlet_SelfLeson:
                Start_SelfLesson();
                return true;
            case R.id.main_menu_setting:
                start_Setting();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void start_Setting() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }

    private void Start_SelfLesson() {
        Intent intent = new Intent(this, SelfLesson.class);
        startActivity(intent);

    }


    public void loading(String choice) {
        textView_adding.setText(R.string.key_adding);
        control_led.setBackgroundColor(Color.parseColor("#FF0000"));
        //按钮使能
        START.setEnabled(false);
        END.setEnabled(false);


        //加载使用函数
        char[] m = choice.toCharArray();
        int classname = m[0] - '0';
        int first = m[7] - '0';
        int second = m[8] - '0';
        int lessonname = first * 10 + second;
        char[] lesson = loading_sequence(classname, lessonname);

        char[] mode_lesson = loading_mode_sequence(lesson);
        //测试，看看是否产生正确
        //the_result_edittext.setText(mode_lesson,0,mode_lesson.length);

        //根据 随机序列，经morse电码转码，播放音频，显示内容
        //SoundsPlayer初始化
        soundsPlayer = new SoundsPlayer(mode_lesson);
        //加载使用函数
        textView_adding.setText(choice);
        control_led.setBackgroundColor(Color.parseColor("#7FFF00"));

        //按钮使能--打开START
        START.setEnabled(true);
    }


    /*
    根据具体训练规则，生成待播放的摩尔斯电码序列
    @Mode:训练规则里的设置
     */
    public char[] loading_mode_sequence(char[] lesson) {
        int Size = 0;
        char[] lesson_random;
        int m = 0;
        switch (MODE) {
            case 1://随机排列 输入 lesson数组，其长度为所有可能的情况
                //Math.random()是令系统随机选取大于等于 0.0 且小于 1.0 的伪随机 double 值
                //Java 中Double型强制转换成int型时是向下取整
                Size = 10 * lesson.length;
                lesson_random = new char[Size];
                m = 0;
                for (int j = 0; j < Size; j++) {

                    final double d = Math.random();
                    final int i = (int) (d * lesson.length);
                    lesson_random[j] = lesson[i];

                }
                return lesson_random;
            case 2://有序排列
                String lesson2 = String.valueOf(lesson);
                String repeat = lesson2;
                String repeat2 = "";
                for (int i = 0; i < 10; i++)
                    repeat2 = repeat2 + repeat;
                return repeat2.toCharArray();

            default:
                return null;
        }

    }

    /*
    加载自定义的内容--重复仅为5次
     */
    public void loading2(String choice) {
        textView_adding.setText(R.string.key_adding);
        control_led.setBackgroundColor(Color.parseColor("#FF0000"));
        //按钮使能
        START.setEnabled(false);
        END.setEnabled(false);
        char[] mode_lesson;
        //加载使用函数
        if (MODE == 2) {

            int m = 0;

            String lesson2 = String.valueOf(choice);
            String repeat = lesson2;
            String repeat2 = "";
            for (int i = 0; i < 5; i++)
                repeat2 = repeat2 + repeat;
            mode_lesson = repeat2.toCharArray();
        } else {
            char[] lesson = choice.toCharArray();
            int Size = 0;
            int m = 0;
            Size = 5 * lesson.length;
            mode_lesson = new char[Size];
            m = 0;
            for (int j = 0; j < Size; j++) {

                final double d = Math.random();
                final int i = (int) (d * lesson.length);
                mode_lesson[j] = lesson[i];
            }

        }
        //测试，看看是否产生正确
        //the_result_edittext.setText(mode_lesson,0,mode_lesson.length);

        //根据 随机序列，经morse电码转码，播放音频，显示内容
        //SoundsPlayer初始化
        soundsPlayer = new SoundsPlayer(mode_lesson);
        //加载使用函数
        textView_adding.setText(choice);
        control_led.setBackgroundColor(Color.parseColor("#7FFF00"));

        //按钮使能--打开START
        START.setEnabled(true);


    }

    /*
将所选课程内容转为 字符数组
 */
    public char[] loading_sequence(int Classname, int lesson) {
        if (Classname == 1) {
            char[] message = Class1_Lessons[lesson - 1].toCharArray();
            return message;
        }else if(Classname==2){
            char[] message=Class2_Lessons[lesson-1].toCharArray();
            return  message;
        }
        else
            return null;
    }


    //输入为摩尔斯电码 字符数组


}



