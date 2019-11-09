package android.grouper.broTeam;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InviteCardHolder extends RecyclerView.ViewHolder {

    TextView mTitle;
    Button joinGroup;
    ImageButton deleteInvite;

    public InviteCardHolder(@NonNull View itemView) {
        super(itemView);

        this.mTitle = itemView.findViewById(R.id.groupInviteTitle);
        this.joinGroup = itemView.findViewById(R.id.joinGroupInvite);
        this.deleteInvite = itemView.findViewById(R.id.deleteInvite);
    }

}
