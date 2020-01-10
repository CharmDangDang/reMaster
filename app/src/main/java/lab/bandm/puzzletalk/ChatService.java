package lab.bandm.puzzletalk;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatService extends Service {

     String DB_TAGNAME = "Message";
    //윈도우 매니저 관련 변수들
    WindowManager windowManager;
    WindowManager.LayoutParams params;
    //채팅창 뷰 & 최소화 뷰
     View chatView;
     View miniIcon;
    //화면 이동시 위치값 초기화
     float xpos = 0;
     float ypos = 0;
    //화면 이동시 초기 위치값
     float xpos_OG, ypos_OG;
     boolean touchClickChecker;
    //사용하는 디바이스 해상도 (아직은 사용 안 함)
     int display_x;
     int display_y;

    // 0 = 채팅뷰상태 1=미니멈상태
     int view_state = 0;
    // 0 = 카운트 하기전 1 = 카운트 하고 난 후
     int count_state = 0;
    //오버레이뷰 로케이션
     int view_location_X = 0;
     int view_location_Y = 0;
    //사용자 수 받을 변수
     Integer user_count;

    //파이어베이스 (채팅내용, 이용자수)
     DatabaseReference myRef, count_prefs;
    //기타 등등
     RecyclerView recyclerView;
     ArrayList<ChatData> chatDataArrayList = new ArrayList<>();
     RecyclerView.Adapter chatAdapter;
     RecyclerView.LayoutManager layoutManager;
     Button btn_exit, btn_send, btn_minimize;
     EditText chat_edit;
     TextView id;
     String myId;
     String roomNumber;

     int id_color;
     SharedPreferences prefs;
     int LAYOUT_FLAG = 0;
     MyChildListener myChildListener;
     MyCountPrfValueListener myCountPrfValueListener;



    @SuppressLint({"RtlHardcoded", "InflateParams"})
    @Override
    public void onCreate() {
        super.onCreate();
        //안드로이드 버전 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        getLoginID();
        setChatLayout();
        findID();
        setRecyclerView();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //서비스 종료시 실행되는 메서드
    @Override
    public void onDestroy() {
        if (windowManager != null) {
            windowManager.removeView(chatView);
        }
        //사용자 수 줄이고 서버에 저장하고 사용자가 아무도 없으면 모든 데이터 삭제
        user_count--;
        count_prefs.setValue(user_count);
        if (user_count == 0 && Integer.parseInt(roomNumber)>2) {
            count_prefs.removeValue();
            myRef.removeValue();
        }
        /*Intent intent = new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        super.onDestroy();
    }

    //로그인한 아이디 변수값 세팅
    void getLoginID() {
        prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        myId = prefs.getString("로그인아이디", "");
        id_color = prefs.getInt("색깔", Color.RED);
        roomNumber = prefs.getString("방번호", "");
       /* id.setText(myId);
        id.setTextColor(id_color);*/
    }

    //리사이클러뷰 설정과 리사이클러뷰에 띄울 파이어베이스 데이터 경로
    void setRecyclerView() {
        id.setText(myId);
        id.setTextColor(id_color);
        recyclerView.setHasFixedSize(true);                                 //리사이클러뷰의 최적화?를 설정
        layoutManager = new LinearLayoutManager(chatView.getContext());              //레이아웃매니저 만들기
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(chatDataArrayList, myId);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.getBackground().setAlpha(200);


        Log.d("룸넘버가 들어오는지 확인", roomNumber);
        //참가자 수 확인용으로 쓸것
        count_prefs = FirebaseDatabase.getInstance().getReference().child("COUNT").child(roomNumber).child("user_Count");
        count_prefs.addValueEventListener(myCountPrfValueListener);
        //데이터 인스턴스 부르고,참조하는곳찾고,그 밑에 Messge child생성(상위폴더라고 보면 됨)
        myRef = FirebaseDatabase.getInstance().getReference().child(DB_TAGNAME).child(roomNumber);
        myRef.addChildEventListener(myChildListener);
    }


    // R.id ~~~~ 설정
    @SuppressLint("ClickableViewAccessibility")
    void findID() {
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        id = chatView.findViewById(R.id.chat_id);
        recyclerView = chatView.findViewById(R.id.recylcerView);
        btn_send = chatView.findViewById(R.id.Enterchat);
        btn_send.setOnClickListener(myOnClickListener);
        btn_exit = chatView.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(myOnClickListener);
        chat_edit = chatView.findViewById(R.id.chatEdit);

        //방바꾸기

        btn_minimize = chatView.findViewById(R.id.btn_mini);
        btn_minimize.setOnClickListener(myOnClickListener);

        myChildListener = new MyChildListener();
        myCountPrfValueListener = new MyCountPrfValueListener();

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        display_x = size.x;
        display_y = size.y;

    }


    // 채팅창 뷰 설정
    @SuppressLint("InflateParams")
    void setChatLayout() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        chatView = (layoutInflater).inflate(R.layout.service_chat_view, null);

        chatView.setOnTouchListener(new MyOnTouchListener());
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams(600
                , 800,
                LAYOUT_FLAG,
                // very important, this sends touch events to underlying views
                //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                ,
                PixelFormat.TRANSLUCENT);

        windowManager.addView(chatView, params);
    }

    // 최소화 키 눌렀을때 실행
    @SuppressLint("InflateParams")
    void setMiniLayout() {
        view_state = 1;
        windowManager.removeView(chatView);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        miniIcon = (layoutInflater).inflate(R.layout.activity_mini, null);
        miniIcon.setBackgroundResource(R.drawable.puztalkminiicon);
        miniIcon.setOnTouchListener(new MyOnTouchListener());
        miniIcon.setOnClickListener(new MyOnClickListener());
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                180, 180,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        miniIcon.setFocusable(false);
        miniIcon.setFocusableInTouchMode(false);

        params.x = view_location_X;
        params.y = view_location_Y;
        windowManager.addView(miniIcon, params);
    }


    // 오버레이뷰 버튼 클릭 리스너 모음집
    class MyOnClickListener implements View.OnClickListener {

        @SuppressLint("RestrictedApi")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Enterchat:
                    String msg = chat_edit.getText().toString();                 //메세지 불러오기
                    if (msg.length() != 0) {
                        String enemyNick = myId;   //prefs에서 아이디값 불러오기
                        ChatData chat = new ChatData(enemyNick, msg,id_color);                         //chatdata 선언
                        myRef.push().setValue(chat);//데이터에 chatdata 넣기
                        chat_edit.setText("");
                        //chat_edit.clearFocus();
                        recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                        //updateOverlayView(0);
                    }//리사이클러뷰 항상 하단으로 포지션주기
                    break;
                case R.id.btn_exit:
                    stopSelf();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case R.id.btn_mini:
                    setMiniLayout();
                    break;

                default:
                    view_state = 0;
                    windowManager.removeView(miniIcon);

                    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                            600,
                            800,
                            LAYOUT_FLAG,
                            // very important, this sends touch events to underlying views
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            ,
                            PixelFormat.TRANSLUCENT);

                    params.x = view_location_X;
                    params.y = view_location_Y;
                    windowManager.addView(chatView, params);
                    break;
            }
        }
    }

    // 오버레이뷰를 이동시킬때 사용하는 리스너
    class MyOnTouchListener implements View.OnTouchListener {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int pointerCount = event.getPointerCount();


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:                //사용자 터치 다운이면
                    if (pointerCount == 1) {
                        xpos = event.getRawX();
                        ypos = event.getRawY();

                        //초기 터치 위치저장
                        xpos_OG = event.getRawX();
                        ypos_OG = event.getRawY();
                        return false;
                    }      //뷰의 시작 점
                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 1) {
                        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) v.getLayoutParams();
                        float dx = xpos - event.getRawX();
                        float dy = ypos - event.getRawY();
                        xpos = event.getRawX();
                        ypos = event.getRawY();

                        if (display_x > lp.x)
                            lp.x = (int) (lp.x - dx);
                        if (display_y > lp.y)
                            lp.y = (int) (lp.y - dy);

                        view_location_X = lp.x;
                        view_location_Y = lp.y;

                        windowManager.updateViewLayout(v, lp);
                        //초기 터치 위치랑 움직인 거리 비교해서 참거짓 판별
                        return touchClickChecker = Math.abs(xpos - xpos_OG) >= 5 || Math.abs(ypos - ypos_OG) >= 5;
                    }
            }
                    return touchClickChecker;
            }
        }

    class MyChildListener implements ChildEventListener {

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            ChatData chat = dataSnapshot.getValue(ChatData.class);
            /*  Log.d("채팅 데이터 보기", String.valueOf(dataSnapshot.getValue(ChatData.class)));*/

            if (chat != null) {
                ((ChatAdapter) chatAdapter).addChat(chat);
                    Log.d("채팅 데이터 보기", "실행댐");
                recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
            if (view_state == 1) {
                miniIcon.setBackgroundResource(R.drawable.chatalertjjin);
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
    }

    class MyCountPrfValueListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if ((dataSnapshot.getValue()) == null && (count_state == 0)) {
                count_prefs.setValue(1);
                count_state = 1;
            } else {
                user_count = dataSnapshot.getValue(Integer.class);
                if ((user_count != null) && (count_state == 0)) {
                    user_count++;
                    count_prefs.setValue(user_count++);
                    count_state = 1;
                }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

}