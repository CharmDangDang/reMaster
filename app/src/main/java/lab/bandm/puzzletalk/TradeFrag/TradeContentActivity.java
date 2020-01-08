package lab.bandm.puzzletalk.TradeFrag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lab.bandm.puzzletalk.GetYMDH;
import lab.bandm.puzzletalk.TokenRD;
import lab.bandm.puzzletalk.clickUtil.ProtectedOverlappingClick;

import static lab.bandm.puzzletalk.R.id;
import static lab.bandm.puzzletalk.R.layout;
import static lab.bandm.puzzletalk.R.string;

public class TradeContentActivity extends AppCompatActivity {
    static RequestQueue requestQueue;
    TextView Ind_title,Ind_content,Ind_ID,Ind_Nation,Ind_is_ok,Ind_YMDH;
    int position;
    ArrayList<TradeData> tradeData = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TradeReplyAdapter adapter;
    ArrayList<ReplyData> replyDataSet = new ArrayList<>();
    EditText reply_edit;
    Button reply_submit;
    SharedPreferences preferences;
    DatabaseReference reference;
    DatabaseReference getTokenReference;
    SingleClickListener singleClickListener;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_trade_content);
        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);
        tradeData = (ArrayList<TradeData>) intent.getSerializableExtra("trade");

        FINDID();
        assert tradeData != null;
        Ind_title.setText(getString(string.trade_title)+tradeData.get(position).getTrade_title());
        Ind_content.setText(getString(string.trade_content)+tradeData.get(position).getTrade_content());
        Ind_ID.setText("작성자 : "+tradeData.get(position).getTrade_id());
        Ind_Nation.setText("서버 : "+tradeData.get(position).getNation());
        Ind_is_ok.setText("거래 :"+tradeData.get(position).getIsOK());
        Ind_YMDH.setText("작성일 : "+tradeData.get(position).getTrade_YMDH());
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }


        adapter = new TradeReplyAdapter(this,replyDataSet);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        reference = FirebaseDatabase.getInstance().getReference().child("reply").child(tradeData.get(position).getTrade_title());
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ReplyData reply = dataSnapshot.getValue(ReplyData.class);
                Log.d("채팅 데이터 보기", String.valueOf(dataSnapshot.getValue(ReplyData.class)));

                if (reply != null) {
                    adapter.addReply(reply);
                    Log.d("채팅 데이터 보기", "실행댐");
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        singleClickListener = new SingleClickListener();
        reply_submit.setOnClickListener(singleClickListener);











// 에디트텍스트에 댓글 확인 누르면






    }

    public void FINDID() {
        Ind_title = findViewById(id.Ind_title);
        Ind_content = findViewById(id.Ind_content);
        Ind_ID = findViewById(id.Ind_ID);
        Ind_Nation = findViewById(id.Ind_Nation);
        Ind_is_ok = findViewById(id.Ind_is_ok);
        Ind_YMDH = findViewById(id.Ind_YMDH);
        recyclerView = findViewById(id.reply_recyclerview);
        reply_edit = findViewById(id.reply_edit);
        reply_submit = findViewById(id.reply_btn);


    }

    class SingleClickListener extends ProtectedOverlappingClick {

        @Override
        public void onSingleClick(View v) {
            preferences = getSharedPreferences("PrefName",MODE_PRIVATE);
            String myId = preferences.getString("로그인아이디","");
            String r_content = reply_edit.getText().toString();
            String r_YMDH = String.valueOf(System.currentTimeMillis());
            ReplyData replyData = new ReplyData();
            replyData.setR_id(myId);
            replyData.setR_content(r_content);
            replyData.setR_YMDH(r_YMDH);
            reference.push().setValue(replyData);
            getTokenReference = FirebaseDatabase.getInstance().getReference("write").child(tradeData.get(position).getTrade_title());
            getTokenReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    TokenRD tokenRD = dataSnapshot.getValue(TokenRD.class);
                    send(tokenRD);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
    public void send(TokenRD tokenRD) {

        JSONObject requestData = new JSONObject();
        String header = "퍼톡에서 알림이 왔어요!";
        String body = "작성하신 게시글에 댓글이 달렸습니다! 빨리 확인해주세요!";

        try {
            requestData.put("priority", "high");

            JSONObject dataObj = new JSONObject();

            dataObj.put("head",header);
            dataObj.put("contents", body);

            requestData.put("data", dataObj);
            JSONArray idArray = new JSONArray();

                idArray.put(0, tokenRD.getmToken());

//            idArray.put(1,regId);
            requestData.put("registration_ids", idArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendData(requestData, new SendResponseListener() {
            @Override
            public void onRequestCompleted() {
            }
            @Override
            public void onRequestStarted() {
            }
            @Override
            public void onRequestWithError(VolleyError error) {

            }
        });
    }

    public interface SendResponseListener {
        public void onRequestStarted();

        public void onRequestCompleted();

        public void onRequestWithError(VolleyError error);
    }
    public void sendData(JSONObject requestData, final SendResponseListener listener) {
        JsonObjectRequest request = new JsonObjectRequest(

                Request.Method.POST, "https://fcm.googleapis.com/fcm/send", requestData,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onRequestCompleted();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestWithError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key=AAAAjARIqKs:APA91bF-hoV-bEfA-0XfNNXkeAIIHESKzA8y_RlSpy7PvmOUgzwoJGKgMYf7c0VFEkRgFh7Fu_wigDMcJjZdbGKqu3pkijczOModxLPZqf3hAfwojID0FWU3SsW8LUQ_LO7GH3N8fWuD");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setShouldCache(false);
        listener.onRequestStarted();
        requestQueue.add(request);
    }
}
