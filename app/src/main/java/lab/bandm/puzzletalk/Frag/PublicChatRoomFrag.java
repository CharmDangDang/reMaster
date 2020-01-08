package lab.bandm.puzzletalk.Frag;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import lab.bandm.puzzletalk.ChatData;
import lab.bandm.puzzletalk.ChatService;
import lab.bandm.puzzletalk.R;
import yuku.ambilwarna.AmbilWarnaDialog;

import static android.content.Context.CLIPBOARD_SERVICE;

public class PublicChatRoomFrag extends Fragment {
    private Button btn_createRoom;
    private Button btn_roomNum;
    private Button btn_reset;
    private Button publicEnter;
    private EditText roomNumber;
    private EditText publicChat;
    private View view;
    private AmbilWarnaDialog ambilWarnaDialog;
    private Pattern pattern = Pattern.compile("^[0-9]{1,8}$");
    private ClipboardManager clip;
    private String RoomNumbered;
    private String myId;
    private SharedPreferences prefs;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PublicChatAdapter publicChatAdapter;
    private ArrayList<ChatData> arrayList = new ArrayList<>();
    private publicCustomOnClick onClick;
    private DatabaseReference publicRef;
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;
    private String DB_TAGNAME = "Message";
    private MyKeyListener myKeyListener = new MyKeyListener();
    private PublicChildListener childListener = new PublicChildListener();

    public PublicChatRoomFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            view = inflater.inflate(R.layout.fragment_public_chat_room, container, false);
        }
        onClick = new publicCustomOnClick();
        FINDID();



        publicRef = FirebaseDatabase.getInstance().getReference().child(DB_TAGNAME).child("1");
        publicRef.addChildEventListener(childListener);
        recyclerView.setHasFixedSize(true);                                 //리사이클러뷰의 최적화?를 설정
        layoutManager = new LinearLayoutManager(this.getContext());              //레이아웃매니저 만들기
        recyclerView.setLayoutManager(layoutManager);
        prefs = Objects.requireNonNull(this.getActivity()).getSharedPreferences("PrefName", Context.MODE_PRIVATE);
        myId = prefs.getString("로그인아이디", "");

        publicChatAdapter = new PublicChatAdapter(arrayList, myId);
        recyclerView.setAdapter(publicChatAdapter);
        clip = (ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(CLIPBOARD_SERVICE);





        return view;
    }

    public void FINDID() {
        btn_createRoom = view.findViewById(R.id.btn_create_room);
        btn_createRoom.setOnClickListener(onClick);
        btn_roomNum = view.findViewById(R.id.ctrlV);
        btn_roomNum.setOnClickListener(onClick);
        btn_reset = view.findViewById(R.id.roomNumReset);
        btn_reset.setOnClickListener(onClick);
        roomNumber = view.findViewById(R.id.roomNum);
        recyclerView = view.findViewById(R.id.publicRecyclerView);
        publicChat = view.findViewById(R.id.publicChatEdit);
        publicChat.setOnKeyListener(myKeyListener);
        publicEnter = view.findViewById(R.id.publicEnterchat);
        publicEnter.setOnClickListener(onClick);

    }

    private void checkPermission() {
        if (!Settings.canDrawOverlays(this.getContext())) {              // 체크
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + Objects.requireNonNull(this.getActivity()).getPackageName()));
            this.getActivity().startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        } else {
            RoomNumbered = roomNumber.getText().toString().trim();
            SharedPreferences prefs = Objects.requireNonNull(this.getActivity()).getSharedPreferences("PrefName", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("방번호", roomNumber.getText().toString());
            //editor.putInt("아이디색상", set_my_id.getCurrentTextColor());
            editor.apply();
            Intent intent = new Intent(this.getContext(), ChatService.class);
            Objects.requireNonNull(this.getContext()).startService(intent);
            BackToTheFuture();


        }
    }

    class publicCustomOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_create_room:
                    if (pattern.matcher(roomNumber.getText().toString()).matches()) {
                        checkPermission();
                    } else {
                        Toast.makeText(getContext(), "0~9999까지의 숫자만 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.ctrlV:
                    ClipData.Item item = Objects.requireNonNull(clip.getPrimaryClip()).getItemAt(0);
                    roomNumber.setText(item.getText().toString());
                    break;
                case R.id.roomNumReset:
                    roomNumber.setText("");
                    break;
                case R.id.publicEnterchat:
                    String msg = publicChat.getText().toString();                 //메세지 불러오기
                    if (msg.length() != 0) {
                        String enemyNick = myId;   //prefs에서 아이디값 불러오기
                        ChatData chat = new ChatData(enemyNick, msg);                         //chatdata 선언
                        publicRef.push().setValue(chat);//데이터에 chatdata 넣기
                        publicChat.setText("");
                        //chat_edit.clearFocus();
                        recyclerView.scrollToPosition(publicChatAdapter.getItemCount() - 1);
                        // updateOverlayView(0);
                    }//리사이클러뷰 항상 하단으로 포지션주기
                    break;


            }
        }
    }

    class PublicChildListener implements ChildEventListener {


        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            ChatData chat = dataSnapshot.getValue(ChatData.class);
            /*  Log.d("채팅 데이터 보기", String.valueOf(dataSnapshot.getValue(ChatData.class)));*/

            if (chat != null) {
                publicChatAdapter.addChat(chat);
                Log.d("채팅 데이터 보기", "실행댐");
                recyclerView.scrollToPosition(publicChatAdapter.getItemCount() - 1);
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

    public void BackToTheFuture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    class MyKeyListener implements View.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                publicEnter.callOnClick();
                return false;
            }
            return true;
        }
    }
}







