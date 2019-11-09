package android.grouper.broTeam;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;

public class AddMemberCardHolder extends RecyclerView.ViewHolder  {
    TextView mTitle;
    ImageButton deleteMember;

    public AddMemberCardHolder(@NotNull View itemView){
        super(itemView);

        this.mTitle = itemView.findViewById(R.id.addMemberUserName);
        this.deleteMember = itemView.findViewById(R.id.cancelAddMemberBtn);
    }
}
