package lab.bandm.puzzletalk.TradeFrag;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TradeAsyncTask extends AsyncTask<String, String, String> {
    private final static int CODE_CREATE = 1102200;
    private final static int CODE_DELETE = 1102201;
    private final static int CODE_DONE = 1102202;
    private Context context;
    private String title;
    private String contents;
    private String YMDH;
    private String name;
    private String nation;
    private String userIp;
    private String currentTime;
    int CODE;

    public TradeAsyncTask(Context context,String title,int CODE) {
        this.context = context;
        this.title = title;
        this.CODE = CODE;
    }

    public TradeAsyncTask(Context context, String title, String contents, String YMDH, String name, String nation, String userIp, String currentTime, int CODE) {
        this.context = context;
        this.title = title;
        this.contents = contents;
        this.YMDH = YMDH;
        this.name = name;
        this.nation = nation;
        this.userIp = userIp;
        this.currentTime = currentTime;
        this.CODE = CODE;
    }

    @Override
    protected String doInBackground(String... strings) {
        String param = null;
        String ipAddress = null;
        if(CODE==CODE_CREATE) {
            ipAddress = "http://www.next-table.com/puzzletalk/PuzzleTALK_tradeInsert.php";
            param = "title=" + title + "&content=" + contents + "&name=" + name + "&nation=" + nation + "&userIP=" + userIp + "&ymdh=" + YMDH + "&currentTime=" + currentTime + "";
        } else if (CODE==CODE_DELETE) {
            ipAddress = "http://www.next-table.com/puzzletalk/PuzzleTALK_tradedelete.php";
            param = "title=" + title;
        } else if (CODE==CODE_DONE) {
            ipAddress = "http://www.next-table.com/puzzletalk/PuzzleTALK_tradedone.php";
            param = "title=" + title;
        }
        try {
            /* 서버연결 */
            URL url = new URL(ipAddress);
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
            if(CODE==CODE_CREATE) {
                Toast.makeText(context, "게시글 작성에 성공했습니다.", Toast.LENGTH_SHORT).show();
            } else if (CODE==CODE_DELETE) {
                Toast.makeText(context, "게시글 삭제에 성공했습니다.", Toast.LENGTH_SHORT).show();
            } else if (CODE==CODE_DONE) {
                Toast.makeText(context, "거래가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }

        } else{
            Toast.makeText(context, "실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
