package top.wxcui.morse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import top.wxcui.morse.Help.Help;
import top.wxcui.morse.transfrom.TransfromModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if(myToolbar!=null)
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_help:
                start_Help();
                return true;
            case  R.id.main_menu_setting:
                start_Setting();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    public void start_input_freedom(View view) {
        Intent intent=new Intent(this,InputFreedom.class);
        startActivity(intent);

    }

    public void start_Setting() {
        Intent intent=new Intent(this,SettingsActivity.class);
        startActivity(intent);

    }
    private void start_Help() {
        Intent intent=new Intent(this, Help.class);
        startActivity(intent);
    }


    public void start_StudyModel(View view) {
        Intent intent=new Intent(this,StudyModel.class);
        startActivity(intent);
    }



    public void start_TransfromModel(View view) {
        Intent intent=new Intent(this,TransfromModel.class);
        startActivity(intent);
    }

    public void start_end(View view) {
        Intent intent=new Intent(this,end.class);
        startActivity(intent);
    }
}