package lab.bandm.puzzletalk;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.madrapps.pikolo.ColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity {


    int color;
    Button exit, submit;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    ImageView colorview;
    ColorPicker colorPicker;

    int current_color;
    int change_color;
    RadioButton kr, jp;


    @SuppressLint({"CommitPrefEdits", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Objects.requireNonNull(getSupportActionBar()).hide();
        FINDID();
        preferences = getSharedPreferences("PrefName", MODE_PRIVATE);
        editor = preferences.edit();
        String server = preferences.getString("서버", "한국");
        current_color = preferences.getInt("색깔", 0);

        if (server.equals("한국")) {

            kr.setChecked(true);
        } else {
            jp.setChecked(true);
        }

        if (current_color == 0) {
            colorview.setBackgroundColor(getColor(R.color.bora));
            colorPicker.setColor(getColor(R.color.bora));
        } else {
            colorview.setBackgroundColor(current_color);
            colorPicker.setColor(current_color);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kr.isChecked()) {
                    editor.remove("서버");
                    editor.putString("서버", "한국");
                } else if (jp.isChecked()) {
                    editor.remove("서버");
                    editor.putString("서버", "일본");
                }
                editor.remove("색깔");
                editor.putInt("색깔", change_color);
                editor.apply();
                Toast.makeText(SettingActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                change_color = color;
                colorview.setBackgroundColor(color);
            }
        });

    }

    public void FINDID() {
        kr = findViewById(R.id.korea_server);
        jp = findViewById(R.id.japan_server);
        submit = findViewById(R.id.setting_submit);
        exit = findViewById(R.id.setting_exit);
        colorPicker = findViewById(R.id.colorpicker);
        colorview = findViewById(R.id.colorview);

    }

}

