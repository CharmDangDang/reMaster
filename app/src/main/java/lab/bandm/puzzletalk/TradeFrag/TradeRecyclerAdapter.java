package lab.bandm.puzzletalk.TradeFrag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lab.bandm.puzzletalk.GetYMDH;
import lab.bandm.puzzletalk.R;


public class TradeRecyclerAdapter extends RecyclerView.Adapter<TradeRecyclerAdapter.CustomViewHolder> implements Filterable {
    ArrayList<TradeData> filteredList;
    ArrayList<TradeData> unfilteredList; //임시
    ArrayList<ReplyRD> replyCnt;

    private Activity context;

    public TradeRecyclerAdapter(Activity context, ArrayList<TradeData> list,ArrayList<ReplyRD> reply) {
        this.context = context;
        this.filteredList = list;
        this.unfilteredList = list;
        this.replyCnt = reply;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView name;
        TextView time;
        TextView isOK;
        ImageView nationView;

        public CustomViewHolder(View view) {
            super(view);
            this.title = view.findViewById(R.id.trade_title);
            this.time = view.findViewById(R.id.trade_YMDH);
            this.name = view.findViewById(R.id.trade_id);
            this.isOK = view.findViewById(R.id.trade_isOk);
            this.nationView = view.findViewById(R.id.trade_nation);
            this.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mPosition = getAdapterPosition();
                    Intent intent = new Intent(context.getApplicationContext(), TradeContentActivity.class);
                    intent.putExtra("position", mPosition);
                    intent.putExtra("trade", filteredList);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trade_item, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.title.setText(filteredList.get(position).getTrade_title());

        viewholder.name.setText(filteredList.get(position).getTrade_id());
        if (filteredList.get(position).getCurrentTime() != null) {
            viewholder.time.setText(GetYMDH.formatTimeString(Long.parseLong(filteredList.get(position).getCurrentTime())).trim());
        }
        viewholder.isOK.setText(filteredList.get(position).getIsOK());
        if (filteredList.get(position).getNation().equals("한국")) {
            viewholder.nationView.setBackgroundColor(Color.rgb(255, 0, 0));
        } else {
            viewholder.nationView.setBackgroundColor(Color.rgb(0, 0, 255));
        }
    }

    @Override
    public int getItemCount() {
        return (null != filteredList ? filteredList.size() : 0);
    }
    public void addReplyCnt(ReplyRD replyRD) { replyCnt.add(replyRD);}

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                //에디트 텍스트에 써있는값을 불러옴
                if (charString.isEmpty()) {
                    filteredList = unfilteredList; //에디트텍스트에 아무것도 안써져있다면 모든 자료를 보여주기
                } else {
                    ArrayList<TradeData> filteringList = new ArrayList<>();  //필터링중일떄 ex)'전ㅎ' 일때 보여줄 리스트
                    for (TradeData tradeData : unfilteredList) {

                        if (tradeData.getTrade_title().toLowerCase().contains(charString.toLowerCase()))
                            filteringList.add(tradeData);
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<TradeData>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}



