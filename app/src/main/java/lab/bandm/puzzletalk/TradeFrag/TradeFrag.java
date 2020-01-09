package lab.bandm.puzzletalk.TradeFrag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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
    private EditText trade_search_edit;
    private RecyclerView recyclerView;
    private FloatingActionButton create_button;
    private TradeRecyclerAdapter adapter;
    private ArrayList<TradeData> list;
    private TextView refreshBtn;
    private SwipeRefreshLayout swipeRefreshLayout;


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
        recyclerView = view.findViewById(R.id.trade_recycler);
        refreshBtn = view.findViewById(R.id.ildanTest);
        trade_search_edit = view.findViewById(R.id.trade_search_edit);
        swipeRefreshLayout = view.findViewById(R.id.Trade_refresh);
        list = new ArrayList<>();
        showResult();
        recyclerView.setHasFixedSize(true);
        adapter = new TradeRecyclerAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);

        create_button = view.findViewById(R.id.trade_create_button);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                adapter.notifyDataSetChanged();
                showResult();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        swipeRefreshLayout.setColorSchemeColors(R.color.p_red,R.color.p_blue,R.color.p_green,R.color.p_yami,R.color.p_hikari,R.color.p_heal);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));




        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TradeCreateActivity.class);
                startActivity(intent);

            }
        });


        return view;
    }

    public void showResult() {


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
                list.add(tradeData);


            }


        } catch (JSONException ignored) {

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();


        }


    }
}
