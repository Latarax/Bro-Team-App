package android.grouper.broTeam;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MessageCardHolder extends RecyclerView.ViewHolder {
    TextView mText;
    TextView mUserName;

    public MessageCardHolder(View itemView) {
        super(itemView);
        mText = itemView.findViewById(R.id.messageText);
        mUserName = itemView.findViewById(R.id.messageUserName);
    }
}
