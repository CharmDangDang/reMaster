package lab.bandm.puzzletalk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

import lab.bandm.puzzletalk.clickUtil.ProtectedOverlappingClick;

public class LoginActivity extends AppCompatActivity {


    Button btnenter, btnsign , btnAS;
    EditText edit_id, edit_pwd;
    String id, pwd;
    SharedPreferences prefs;
    CheckBox id_Save, pwd_Save;
    private Pattern pattern = Pattern.compile("^[a-zA-Z가-힣0-9]{4,15}$");

    //중복 클릭으로 인해 로그인이나 회원가입이 여러번 실행되는 상황 방지
    SingleClickListener singleClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FindID_and_setClickListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void FindID_and_setClickListener() {
        btnenter = findViewById(R.id.btnenter);
        btnsign = findViewById(R.id.btnsign);
        edit_id = findViewById(R.id.edit_id);
        edit_pwd = findViewById(R.id.edit_pwd);
        id_Save = findViewById(R.id.id_Save);
        pwd_Save = findViewById(R.id.pw_Save);
        btnAS = findViewById(R.id.btnAS);
        btnAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        singleClickListener = new SingleClickListener();

        prefs = getSharedPreferences("PrefName", Activity.MODE_PRIVATE);
        String saved_Id = prefs.getString("id_save", "");
        String saved_Pwd = prefs.getString("pwd_save", "");
        boolean chk1 = prefs.getBoolean("chk_id", false);
        boolean chk2 = prefs.getBoolean("chk_pwd", false);
        if (chk1) {
            edit_id.setText(saved_Id);
            id_Save.setChecked(true);
        }
        if (chk2) {
            edit_pwd.setText(saved_Pwd);
            pwd_Save.setChecked(true);
        }

        btnsign.setOnClickListener(singleClickListener);

        btnenter.setOnClickListener(singleClickListener);

        edit_pwd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btnenter.callOnClick();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);    //hide keyboard
                    }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(id_Save.isChecked()){
            editor.putString("id_save", edit_id.getText().toString());
            editor.putBoolean("chk_id", id_Save.isChecked());
        } else {
            editor.remove("id_save");
            editor.remove("chk_id");
        }
        if(pwd_Save.isChecked()){
            editor.putString("pwd_save", edit_pwd.getText().toString());
            editor.putBoolean("chk_pwd", pwd_Save.isChecked());
        } else {
            editor.remove("pwd_save");
            editor.remove("chk_pwd");
        }
        editor.putString("로그인아이디", id);
        editor.apply();
    }

    class SingleClickListener extends ProtectedOverlappingClick {

        @Override
        public void onSingleClick(View v) {
            String INSERT_DB = "http://www.next-table.com/puzzletalk/PuzzleTALK_UserInsert.php";
            switch (v.getId()){
                case R.id.btnenter:
                    id = edit_id.getText().toString().trim();
                    pwd = edit_pwd.getText().toString().trim();

                    if(pattern.matcher(id).matches()&&pattern.matcher(pwd).matches()) {
                        String LOGIN_DB = "http://www.next-table.com/puzzletalk/PuzzleTALK_UserLogin.php";
                        UserAsyncTask userAsyncTask = new UserAsyncTask(id, pwd, LOGIN_DB, getApplicationContext());
                        userAsyncTask.execute();

                    } else {
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();

                    }
                    break;
                case R.id.btnsign:
                    id = edit_id.getText().toString().trim();
                    pwd = edit_pwd.getText().toString().trim();
                    if(pattern.matcher(id).matches()&&pattern.matcher(pwd).matches()) {
                        UserAsyncTask userAsyncTask = new UserAsyncTask(id, pwd, INSERT_DB, getApplicationContext());
                        userAsyncTask.execute();


                    } else {
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
