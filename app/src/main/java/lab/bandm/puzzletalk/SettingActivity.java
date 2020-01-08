package lab.bandm.puzzletalk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SettingActivity extends AppCompatActivity {


    int color ;
    Button pickerBtn ;
    SharedPreferences preferences;
    ImageView ambil;
    int current_color;
    int change_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        color = getColor(R.color.CYAN);

        FINDID();
        preferences = getSharedPreferences("PrefName", MODE_PRIVATE);

        current_color = preferences.getInt("id_color",0);

        if(current_color==0){
            ambil.setBackgroundColor(getColor(R.color.bora));
        } else {
            ambil.setBackgroundColor(current_color);
        }



        pickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker();

            }
        });

    }
    public void FINDID() {

        pickerBtn = findViewById(R.id.colorpicker);
        ambil = findViewById(R.id.myColor);
    }

    public void picker() {

            AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    preferences = getSharedPreferences("PrefName", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("id_color");
                    editor.putInt("id_color",color);
                    ambil.setBackgroundColor(color);

                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {

                }

            });




            dialog.show();
        }



    }

