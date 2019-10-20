package android.grouper.broTeam;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupCardHolder extends RecyclerView.ViewHolder {

    ImageView mImgView;
    TextView mTitle, mDescription;

    cardClickListener mCardClickListener;

    public GroupCardHolder(@NonNull View itemView) {
        super(itemView);

        this.mDescription = itemView.findViewById(R.id.descriptionIv);
        this.mTitle = itemView.findViewById(R.id.titleIv);
        this.mImgView = itemView.findViewById(R.id.imageIv);
    }


    public void setCardClickListener(cardClickListener mCardClickListener) {
        this.mCardClickListener = mCardClickListener;
    }
}
