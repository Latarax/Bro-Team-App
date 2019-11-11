package android.grouper.broTeam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MessageCardAdapter extends RecyclerView.Adapter<MessageCardHolder> {
    Context c;
    String userID;
    ArrayList<MessageModel> models;
    private final int MESSAGE_TYPE_IN = 0;
    private final int MESSAGE_TYPE_OUT = 1;


    public MessageCardAdapter(Context c, ArrayList<MessageModel> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MessageCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_message_card_view, null);
        return new MessageCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageCardHolder holder, int position) {
        holder.mUserName.setText(models.get(position).getMessageUserName());
        holder.mText.setText(models.get(position).getMessageText());
    }

    public int getItemCount() {
        return models.size();
    }
}