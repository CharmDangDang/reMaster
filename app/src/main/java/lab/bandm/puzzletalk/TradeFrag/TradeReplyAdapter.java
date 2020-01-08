package lab.bandm.puzzletalk.TradeFrag;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lab.bandm.puzzletalk.GetYMDH;
import lab.bandm.puzzletalk.R;

public class TradeReplyAdapter extends RecyclerView.Adapter<TradeReplyAdapter.CustomViewHolder> {

    ArrayList<ReplyData> dataArrayList;

    private Activity context;

    public TradeReplyAdapter(Activity context, ArrayList<ReplyData> list) {
        this.context = context;
        this.dataArrayList =list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView idview;
        TextView YMDHview;
        TextView contentview;


        public CustomViewHolder(View view) {
            super(view);
            this.idview = view.findViewById(R.id.r_id);
            this.YMDHview = view.findViewById(R.id.r_YMDH);
            this.contentview = view.findViewById(R.id.r_content);


        }

    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.contentview.setText(dataArrayList.get(position).getR_content());
        holder.idview.setText(dataArrayList.get(position).getR_id());
        holder.YMDHview.setText(GetYMDH.formatTimeString(Long.parseLong(dataArrayList.get(position).getR_YMDH())));


    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public void addReply(ReplyData replyData){

        dataArrayList.add(replyData);
    }


}
