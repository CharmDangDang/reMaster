package lab.bandm.puzzletalk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class UserAsyncTask extends AsyncTask<String, String, String> {

    private String data = "";
    private String id;
    private String pword;
    private String IpAddress;
    String myToken;
    String saved_id;
    String tempToken;
    @SuppressLint("StaticFieldLeak")
    private Context context;
 //   FirebaseInstanceId firebaseInstanceId;
 FirebaseInstanceId firebaseInstanceId;
   // private DatabaseReference databaseReference;
   // private FirebaseDatabase database_token;
 DatabaseReference reference;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    UserAsyncTask(String id, String pword, String ipAddress, Context context) {
        this.id = id;
        this.pword = pword;
        IpAddress = ipAddress;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        String param = "u_id=" + id + "&u_pword=" + pword + "";
        Log.e("POST", param);
        try {
            /* 서버연결 */
            URL url = new URL(IpAddress);
            Log.e("POST", IpAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();

            /* 안드로이드 -> 서버 파라메터값 전달 */
            OutputStream outs = conn.getOutputStream();
            outs.write(param.getBytes(StandardCharsets.UTF_8));
            outs.flush();
            outs.close();

            /* 서버 -> 안드로이드 파라메터값 전달 */
            InputStream is;
            BufferedReader in;


            is = conn.getInputStream();
            in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                builder.append(line).append("\n");
            }
            data = builder.toString().trim();
            in.close();
            return builder.toString();
        } catch (Exception e) {
            return ("Error: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        preferences = context.getSharedPreferences("PrefName",Context.MODE_PRIVATE);
        editor = preferences.edit();
        switch (IpAddress) {
            case "http://www.next-table.com/puzzletalk/PuzzleTALK_UserLogin.php":
                if (data.equals(id + pword)) {
                    Toast.makeText(context, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    ChkToken();
                    editor.putString("로그인아이디",id);
                    editor.apply();
                    context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    Toast.makeText(context, "다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case "http://www.next-table.com/puzzletalk/PuzzleTALK_UserInsert.php":
                if (data.equals("1")) {
                    Toast.makeText(context, "가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    editor.putString("서버","한국");
                    editor.putInt("색깔", Color.RED);
                    editor.apply();
                    ChkToken();
                } else {
                    Toast.makeText(context, "다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void ChkToken() {

        reference = FirebaseDatabase.getInstance().getReference("token").child(id);
        firebaseInstanceId = FirebaseInstanceId.getInstance();
        firebaseInstanceId.getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("FIREBASE", "getInstanceId failed", task.getException());
                    return;
                }
                TokenRD tokenRD=new TokenRD();
                tokenRD.setmToken(Objects.requireNonNull(task.getResult()).getToken());
                editor.putString("myToken",task.getResult().getToken());
                editor.apply();
                reference.setValue(tokenRD);

            }
        });



    }

}
