package lab.bandm.puzzletalk.TradeFrag;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lab.bandm.puzzletalk.GetYMDH;
import lab.bandm.puzzletalk.R;


public class TradeRecyclerAdapter extends  RecyclerView.Adapter<TradeRecyclerAdapter.CustomViewHolder> {

    ArrayList<TradeData> dataArrayList;   //M
    //ArrayList<TradeData> filteredList;

    public ArrayList<TradeData> SubjectListTemp; //임시

    public TradeRecyclerAdapter.SubjectDataFilter subjectDataFilter; // filterd

    private Activity context;

    public TradeRecyclerAdapter(Activity context, ArrayList<TradeData> list) {
        this.context = context;
        this.dataArrayList =list;
        this.SubjectListTemp = list;
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

        }

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trade_item, viewGroup, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {

        viewholder.title.setText(dataArrayList.get(position).getTrade_title());
        viewholder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),TradeContentActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("trade",dataArrayList);
                context.startActivity(intent);
            }
        });
        viewholder.name.setText(dataArrayList.get(position).getTrade_id());
        if (dataArrayList.get(position).getCurrentTime() != null) {
            viewholder.time.setText(GetYMDH.formatTimeString(Long.parseLong(dataArrayList.get(position).getCurrentTime())).trim());
        }
        viewholder.isOK.setText(dataArrayList.get(position).getIsOK());
        if (dataArrayList.get(position).getNation().equals("한국")) {
            viewholder.nationView.setBackgroundColor(Color.rgb(255, 0, 0));
        } else {
            viewholder.nationView.setBackgroundColor(Color.rgb(0, 0, 255));
        }
    }

    @Override
    public int getItemCount() {
        return (null != dataArrayList ? dataArrayList.size() : 0);
    }






        private class SubjectDataFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                charSequence = charSequence.toString().toLowerCase();

                FilterResults filterResults = new FilterResults();

                if (charSequence != null && charSequence.toString().length() > 0) {
                    ArrayList<TradeData> arrayList1 = new ArrayList<TradeData>();

                    for (int i = 0, l = dataArrayList.size(); i < l; i++) {
                        TradeData tradeData = dataArrayList.get(i);

                        if (tradeData.toString().toLowerCase().contains(charSequence))

                            arrayList1.add(tradeData);
                    }
                    filterResults.count = arrayList1.size();

                    filterResults.values = arrayList1;
                } else {
                    synchronized (this) {
                        filterResults.values = dataArrayList;

                        filterResults.count = dataArrayList.size();
                    }
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                SubjectListTemp = (ArrayList<TradeData>) results.values;

                notifyDataSetChanged();

            }



            public Filter getFilter() {

                if (subjectDataFilter == null) {

                    subjectDataFilter = new TradeRecyclerAdapter.SubjectDataFilter();
                }
                return subjectDataFilter;
            }

        }

}



