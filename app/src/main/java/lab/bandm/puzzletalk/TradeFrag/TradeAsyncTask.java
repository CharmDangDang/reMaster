package lab.bandm.puzzletalk.TradeFrag;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TradeAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    private String title;
    private String contents;
    private String YMDH;
    private String name;
    private String nation;
    private String userIp;
    private String currentTime;
    private final String IpAddress = "http://www.next-table.com/puzzletalk/PuzzleTALK_tradeInsert.php";

    public TradeAsyncTask(Context context, String title, String contents, String YMDH, String name, String nation, String userIp, String currentTime) {
        this.context = context;
        this.title = title;
        this.contents = contents;
        this.YMDH = YMDH;
        this.name = name;
        this.nation = nation;
        this.userIp = userIp;
        this.currentTime = currentTime;
    }

    @Override
    protected String doInBackground(String... strings) {

        String param = "title=" + title + "&content=" + contents + "&name=" + name + "&nation=" + nation + "&userIP=" + userIp + "&ymdh=" + YMDH + "&currentTime=" + currentTime + "";
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
                builder.append(line);
            }
            in.close();
            return builder.toString();
        } catch (Exception e) {
            return ("Error: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s.equals("1")) {
            Toast.makeText(context, "게시글 작성에 성공했습니다.", Toast.LENGTH_SHORT).show();

            //context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            Toast.makeText(context, "빠진 부분을 입력해주세요.", Toast.LENGTH_SHORT).show();

        }
    }
}
