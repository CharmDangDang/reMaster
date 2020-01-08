package lab.bandm.puzzletalk;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatData> mDataset;
    private String myNickname;


    public ChatAdapter(ArrayList<ChatData> myDataset, String myNickname) {

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
                MyChatViewHolder myChatViewHolder = (MyChatViewHolder) holder;
                myChatViewHolder.text_Mymsg.setText(chatData.getMsg());

                break;
            case 1:
                EnemyChatViewHolder enemyChatViewHolder = (EnemyChatViewHolder) holder;
                enemyChatViewHolder.text_Nickname.setText(chatData.getEnemyname());
                enemyChatViewHolder.text_Nickname.setTextColor(Color.RED);

                enemyChatViewHolder.text_msg.setText(chatData.getMsg());
                break;
            default:
                break;
        }
    }
    //14 * MainActivity.context.getResources().getDisplayMetrics().density

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
    public void addChat(ChatData chat) {
        mDataset.add(chat);
    }
    private void addChats(ArrayList<ChatData> chat) {
        this.mDataset = chat;
        if (mDataset != null) {
            notifyItemInserted(mDataset.size() - 1);
        }
    }

}