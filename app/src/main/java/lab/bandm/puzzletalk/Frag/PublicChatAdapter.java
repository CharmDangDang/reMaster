package lab.bandm.puzzletalk.Frag;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lab.bandm.puzzletalk.ChatData;
import lab.bandm.puzzletalk.MainActivity;
import lab.bandm.puzzletalk.OnlongClick;
import lab.bandm.puzzletalk.R;

public class PublicChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatData> mDataset;
    private String myNickname;
    private int inch = (int) (getScreenInches() + 0.5);
    private OnlongClick longClick = new OnlongClick();


    PublicChatAdapter(ArrayList<ChatData> myDataset, String myNickname) {

        addChats(myDataset);
        this.myNickname = myNickname;
    }

    public class EnemyChatViewHolder extends RecyclerView.ViewHolder {
        TextView text_Nickname;
        TextView text_msg;

        EnemyChatViewHolder(@NonNull View itemView) {
            super(itemView);
            text_Nickname = itemView.findViewById(R.id.enemy_id);
            text_msg = itemView.findViewById(R.id.enemy_chat);

        }
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder {
        TextView text_Mymsg;

        MyChatViewHolder(@NonNull View itemView) {
            super(itemView);
            text_Mymsg = itemView.findViewById(R.id.my_chat);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mychat_right, parent, false);
                return new MyChatViewHolder(view);

            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.enemychat_left, parent, false);
                return new EnemyChatViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatData chatData = mDataset.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                PublicChatAdapter.MyChatViewHolder myChatViewHolder = (MyChatViewHolder) holder;
                myChatViewHolder.text_Mymsg.setText(chatData.getMsg());
                myChatViewHolder.text_Mymsg.setTextSize(3 * inch);
                myChatViewHolder.text_Mymsg.setOnLongClickListener(longClick);
                break;
            case 1:
                PublicChatAdapter.EnemyChatViewHolder enemyChatViewHolder = (EnemyChatViewHolder) holder;
                enemyChatViewHolder.text_Nickname.setText(chatData.getEnemyname());
                enemyChatViewHolder.text_Nickname.setTextColor(chatData.getNameColor());
                enemyChatViewHolder.text_msg.setTextSize(3 * inch);
                enemyChatViewHolder.text_msg.setText(chatData.getMsg());
                enemyChatViewHolder.text_msg.setOnLongClickListener(longClick);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatData chatData = mDataset.get(position);

        if (chatData.getEnemyname().equals(myNickname)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    void addChat(ChatData chat) {
        mDataset.add(chat);
    }

    private void addChats(ArrayList<ChatData> chat) {
        this.mDataset = chat;
        if (mDataset != null) {
            notifyItemInserted(mDataset.size() - 1);
        }
    }

    private double getScreenInches() {

        DisplayMetrics dm = new DisplayMetrics();
        MainActivity.context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double wi = (double) width / (double) dm.xdpi;
        double hi = (double) height / (double) dm.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);

        return Math.sqrt(x + y);
    }
}
