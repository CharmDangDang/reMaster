package lab.bandm.puzzletalk.TradeFrag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import lab.bandm.puzzletalk.R;

public class TradeFrag extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fab_main, fab_add, fab_kr, fab_jp, fab_my;
    private TradeRecyclerAdapter adapter;
    private ArrayList<TradeData> list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final static int MyContent = 711;
    private final static int WholeContent = 910711;
    private final static int JPContent = 713;
    private final static int KRContent = 712;
    FabClick fabClick = new FabClick();


    ArrayList<ReplyRD> replyCnt = new ArrayList<>();
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;
    private Context context;

    public TradeFrag() {
    }

    public TradeFrag(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade, container, false);
        FINDID(view);
        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_close);
        list = new ArrayList<>();
        showResult(WholeContent);

        recyclerView.setHasFixedSize(true);
        adapter = new TradeRecyclerAdapter(getActivity(), list, replyCnt);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                replyCnt.clear();
                adapter.notifyDataSetChanged();
                showResult(WholeContent);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setColorSchemeColors(R.color.p_red, R.color.p_blue, R.color.p_green, R.color.p_yami, R.color.p_hikari, R.color.p_heal);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void TextSearch(String text) {
        adapter.getFilter().filter(text);
    }

    public void showResult(int Code) {

        SharedPreferences preferences = context.getSharedPreferences("PrefName", Context.MODE_PRIVATE);
        String myID = preferences.getString("로그인아이디", "");


        TradeCallTask tradeCallTask = new TradeCallTask();
        tradeCallTask.execute();

        String TAG_JSON = "webnautes";
        String TAG_title = "trade_title";
        String TAG_content = "trade_content";
        String TAG_username = "trade_username";
        String TAG_nation = "trade_nation";
        String TAG_date = "trade_date";
        String TAG_Time = "trade_currentTime";
        String TAG_isOk = "trade_isOk";
        //trade_title": "스지 2개로 악킬 구함",
        //            "trade_content": "냉무",
        //            "trade_username": "cym0711",
        //            "trade_nation": "한국",
        //            "trade_date": "2019-12-24 06:15:49",
        //            "trade_isOk": "OK"


        try {
            JSONObject jsonObject = new JSONObject(tradeCallTask.get());
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String title = item.getString(TAG_title);
                String content = item.getString(TAG_content);
                String username = item.getString(TAG_username);
                String nation = item.getString(TAG_nation);
                String date = item.getString(TAG_date);
                String currentTime = item.getString(TAG_Time);
                String isOk = item.getString(TAG_isOk);


                TradeData tradeData = new TradeData();

                tradeData.setTrade_title(title);
                tradeData.setTrade_content(content);
                tradeData.setTrade_id(username);
                tradeData.setNation(nation);
                tradeData.setTrade_YMDH(date);
                tradeData.setCurrentTime(currentTime.trim());
                tradeData.setIsOK(isOk);
                switch (Code) {
                    case WholeContent:
                        list.add(tradeData);
                        break;
                    case MyContent:
                        if (tradeData.getTrade_id().equals(myID)) {
                            list.add(tradeData);
                        }
                        break;
                    case JPContent:
                        if (tradeData.getNation().equals("일본")) {
                            list.add(tradeData);
                        }
                        break;
                    case KRContent:
                        if (tradeData.getNation().equals("한국")) {
                            list.add(tradeData);
                        }
                        break;
                }
            }
        } catch (JSONException ignored) {

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();


        }


    }

    class FabClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.trade_fab:
                    toggleFab();
                    break;
                case R.id.trade_create_button:
                    toggleFab();
                    Intent intent = new Intent(context, TradeCreateActivity.class);
                    startActivity(intent);
                    break;
                case R.id.trade_korea:
                    toggleFab();
                    list.clear();
                    replyCnt.clear();
                    adapter.notifyDataSetChanged();
                    showResult(KRContent);

                    break;
                case R.id.trade_japan:
                    toggleFab();
                    list.clear();
                    replyCnt.clear();
                    adapter.notifyDataSetChanged();
                    showResult(JPContent);

                    break;
                case R.id.trade_my:
                    toggleFab();
                    list.clear();
                    replyCnt.clear();
                    adapter.notifyDataSetChanged();
                    showResult(MyContent);
                    break;

            }

        }
    }

    private void FINDID(View view) {
        recyclerView = view.findViewById(R.id.trade_recycler);
        fab_main = view.findViewById(R.id.trade_fab);
        fab_main.setOnClickListener(fabClick);
        fab_add = view.findViewById(R.id.trade_create_button);
        fab_add.setOnClickListener(fabClick);
        fab_kr = view.findViewById(R.id.trade_korea);
        fab_kr.setOnClickListener(fabClick);
        fab_jp = view.findViewById(R.id.trade_japan);
        fab_jp.setOnClickListener(fabClick);
        fab_my = view.findViewById(R.id.trade_my);
        fab_my.setOnClickListener(fabClick);

        swipeRefreshLayout = view.findViewById(R.id.Trade_refresh);

    }

    private void toggleFab() {

        if (isFabOpen) {
            fab_main.setImageResource(R.drawable.ic_add_white);
            fab_add.startAnimation(fab_close);
            fab_jp.startAnimation(fab_close);
            fab_kr.startAnimation(fab_close);
            fab_my.startAnimation(fab_close);
            fab_add.setClickable(false);
            fab_jp.setClickable(false);
            fab_kr.setClickable(false);
            fab_my.setClickable(false);
            isFabOpen = false;
        } else {
            fab_main.setImageResource(R.drawable.ic_close_white);
            fab_add.startAnimation(fab_open);
            fab_jp.startAnimation(fab_open);
            fab_kr.startAnimation(fab_open);
            fab_my.startAnimation(fab_open);
            fab_add.setClickable(true);
            fab_jp.setClickable(true);
            fab_kr.setClickable(true);
            fab_my.setClickable(true);
            isFabOpen = true;

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        replyCnt.clear();
        adapter.notifyDataSetChanged();
        showResult(WholeContent);
    }
}
