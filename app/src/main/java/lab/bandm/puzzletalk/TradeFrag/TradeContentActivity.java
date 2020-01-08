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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import lab.bandm.puzzletalk.GetYMDH;

import static lab.bandm.puzzletalk.R.id;
import static lab.bandm.puzzletalk.R.layout;
import static lab.bandm.puzzletalk.R.string;

public class TradeContentActivity extends AppCompatActivity {

    TextView Ind_title,Ind_content,Ind_ID,Ind_Nation,Ind_is_ok,Ind_YMDH;
    int position;
    ArrayList<TradeData> tradeData = null;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TradeReplyAdapter adapter;
    ArrayList<ReplyData> replyDataSet = null;
    EditText reply_edit;
    Button reply_submit;
    SharedPreferences preferences;
    DatabaseReference reference;


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

        adapter = new TradeReplyAdapter(this,replyDataSet);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);

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







        reply_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("PrefName",MODE_PRIVATE);
                String myId = preferences.getString("로그인아이디","");
                String r_content = reply_edit.getText().toString();
                String r_YMDH = GetYMDH.callYMDH();
                String mToken = preferences.getString("내토큰","");
                ReplyData replyData = new ReplyData(myId,r_YMDH,r_content,mToken);
                reference.setValue(replyData);

// 에디트텍스트에 댓글 확인 누르면



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


    }
}
