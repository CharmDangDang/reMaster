package lab.bandm.puzzletalk.TradeFrag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lab.bandm.puzzletalk.R;
import lab.bandm.puzzletalk.TokenRD;
import lab.bandm.puzzletalk.clickUtil.ProtectedOverlappingClick;

import static lab.bandm.puzzletalk.R.id;
import static lab.bandm.puzzletalk.R.layout;

public class TradeContentActivity extends AppCompatActivity {
    static RequestQueue requestQueue;
    TextView Ind_title, Ind_content, Ind_ID, Ind_Nation, Ind_is_ok, Ind_YMDH;
    int position;
    ArrayList<TradeData> tradeData = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TradeReplyAdapter adapter;
    ArrayList<ReplyData> replyDataSet = new ArrayList<>();
    EditText reply_edit;
    ImageButton reply_submit, content_done, content_back, content_delete;
    SharedPreferences preferences;
    DatabaseReference reference;
    DatabaseReference getTokenReference;
    SingleClickListener singleClickListener;
    OnClickOnContent onClickOnContent = new OnClickOnContent();
    TokenRD tokenRD;
    String myToken;
    String myId;
    private final int CODE_DELETE = 1102201;
    private final int CODE_DONE = 1102202;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_trade_content);
        preferences = getSharedPreferences("PrefName", MODE_PRIVATE);
        myId = preferences.getString("로그인아이디", "");
        myToken = preferences.getString("myToken", "");
        Objects.requireNonNull(getSupportActionBar()).hide();


        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        tradeData = (ArrayList<TradeData>) intent.getSerializableExtra("trade");


        FINDID();
        Ind_title.setText(tradeData.get(position).getTrade_title());
        Ind_content.setText(tradeData.get(position).getTrade_content());
        Ind_ID.setText("작성자 : " + tradeData.get(position).getTrade_id());
        Ind_Nation.setText("서버 : " + tradeData.get(position).getNation());
        Ind_is_ok.setText("거래 : " + tradeData.get(position).getIsOK());
        Ind_YMDH.setText(tradeData.get(position).getTrade_YMDH());
        if (!tradeData.get(position).getTrade_id().equals(myId)){
            content_done.setVisibility(View.INVISIBLE);
            content_delete.setVisibility(View.INVISIBLE);
        }
        if (tradeData.get(position).getIsOK().equals("X")) {
            content_done.setVisibility(View.INVISIBLE);
            reply_edit.setVisibility(View.INVISIBLE);
            reply_submit.setVisibility(View.INVISIBLE);
        }
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }


        adapter = new TradeReplyAdapter(this, replyDataSet);
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

        getTokenReference = FirebaseDatabase.getInstance().getReference("write").child(tradeData.get(position).getTrade_title());
        getTokenReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tokenRD = dataSnapshot.getValue(TokenRD.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
        content_back = findViewById(id.trade_content_back);
        content_back.setOnClickListener(onClickOnContent);
        content_done = findViewById(id.trade_content_done);
        content_done.setOnClickListener(onClickOnContent);
        content_delete = findViewById(id.trade_content_delete);
        content_delete.setOnClickListener(onClickOnContent);


    }

    class OnClickOnContent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case id.trade_content_back:
                    finish();
                    break;
                case id.trade_content_delete:
                    MakeDialog(CODE_DELETE);
                    break;

                case id.trade_content_done:
                    MakeDialog(CODE_DONE);
                    break;
            }
        }
    }

    public void MakeDialog(final int code) {
        CharSequence dialog_title = null;
        CharSequence dialog_content = null;
        final AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(TradeContentActivity.this);
        if (code == CODE_DELETE) {
            dialog_title = "삭제";
            dialog_content = "삭제하시겠습니까?";
        } else if (code == CODE_DONE) {
            dialog_title = "거래완료";
            dialog_content = "거래완료하시겠습니까?";
        }

        builder.setTitle(dialog_title)        // 제목 설정
                .setMessage(dialog_content)        // 메세지 설정
                .setCancelable(true) // 뒤로 버튼 클릭시 취소 가능 설정
                .setIcon(R.drawable.puztalkminiicon)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TradeAsyncTask taskForDelete = new TradeAsyncTask(TradeContentActivity.this, Ind_title.getText().toString(), code);
                        taskForDelete.execute();
                        if (code == CODE_DELETE) {
                            reference = FirebaseDatabase.getInstance().getReference().child("write").child(Ind_title.getText().toString());
                            reference.setValue(null);
                            reference = FirebaseDatabase.getInstance().getReference().child("reply").child(Ind_title.getText().toString());
                            reference.setValue(null);
                            finish();
                        } else if(code == CODE_DONE) {
                            content_done.setVisibility(View.INVISIBLE);
                            reply_edit.setVisibility(View.INVISIBLE);
                            reply_submit.setVisibility(View.INVISIBLE);
                        }


                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });



        dialog = builder.create();    // 알림창 객체 생성
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();    // 알림창 띄우기

    }


    class SingleClickListener extends ProtectedOverlappingClick {

        @Override
        public void onSingleClick(View v) {
            if (!reply_edit.getText().toString().trim().equals("")) {
                String r_content = reply_edit.getText().toString();
                String r_YMDH = String.valueOf(System.currentTimeMillis());
                ReplyData replyData = new ReplyData();
                replyData.setR_id(myId);
                replyData.setR_content(r_content);
                replyData.setR_YMDH(r_YMDH);
                reference.push().setValue(replyData);
                if (!tokenRD.getmToken().equals(myToken)) {
                    send(tokenRD);
                }
                reply_edit.setText("");
                keyboardHidden();
            } else {
                Toast.makeText(TradeContentActivity.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void send(TokenRD tokenRD) {

        JSONObject requestData = new JSONObject();
        String header = "퍼톡에서 알림이 왔어요!";
        String body = "게시글 [" + Ind_title.getText().toString() + "] 에 댓글이 달렸습니다!";

        try {
            requestData.put("priority", "high");

            JSONObject dataObj = new JSONObject();

            dataObj.put("head", header);
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
        void onRequestStarted();

        void onRequestCompleted();

        void onRequestWithError(VolleyError error);
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
                return new HashMap<>();
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

    private void keyboardHidden() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
