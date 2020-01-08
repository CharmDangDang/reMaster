package lab.bandm.puzzletalk.TradeFrag;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import lab.bandm.puzzletalk.GetYMDH;
import lab.bandm.puzzletalk.R;
import lab.bandm.puzzletalk.TokenRD;

public class TradeCreateActivity extends AppCompatActivity {
    private EditText tradeTitleEdit;
    private EditText tradeContentEdit;
    private Button tradeCreateBtn;
    private Button tradeCancelBtn;
    private final static int CALL_ID = 1991;
    private final static int CALL_NATION = 711;
    DatabaseReference reference;
    DatabaseReference tokenReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_create);
        FINDID_trade();
        MyListener listener = new MyListener();
        tradeCreateBtn.setOnClickListener(listener);
        tradeCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String CallINFO(int callNum) {
        SharedPreferences preferences = getSharedPreferences("PrefName", Context.MODE_PRIVATE);
        String id = preferences.getString("로그인아이디", "");
        String nation = preferences.getString("국가", "");
        if (callNum == CALL_NATION) {
            if (nation.equals("")) {
                return "한국";
            } else {
                return nation;
            }
        } else if (callNum == CALL_ID) {
            return id;
        }
        return null;
    }

    public void FINDID_trade() {
        tradeTitleEdit = findViewById(R.id.trade_create_title);
        tradeContentEdit = findViewById(R.id.trade_create_content);
        tradeCreateBtn = findViewById(R.id.trade_create_OK);
        tradeCancelBtn = findViewById(R.id.trade_create_Cancel);
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final String title = tradeTitleEdit.getText().toString();
            String content = tradeContentEdit.getText().toString();
            String YMDH = GetYMDH.callYMDH();
            String Name = CallINFO(CALL_ID);
            String nation = CallINFO(CALL_NATION);
            String currentTime = String.valueOf(System.currentTimeMillis());
            Log.d("시간", "onClick: "+currentTime);
            String IP = getLocalIpAddress();
            TradeAsyncTask task = new TradeAsyncTask(TradeCreateActivity.this, title, content, YMDH, Name, nation, IP, currentTime);
            task.execute();
            tokenReference = FirebaseDatabase.getInstance().getReference("token").child(CallINFO(CALL_ID));
            tokenReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    TokenRD token = dataSnapshot.getValue(TokenRD.class);
                    TokenInsert(token,title);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });







            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();


    }
    public void TokenInsert(TokenRD tokenRD,String title) {
        reference = FirebaseDatabase.getInstance().getReference("write").child(title);
        reference.setValue(tokenRD);

    }
}

