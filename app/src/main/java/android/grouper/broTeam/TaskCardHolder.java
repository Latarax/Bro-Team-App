package android.grouper.broTeam;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImgView;
    TextView mTitle, mDescription;
    cardClickListener mCardClickListener;

    TaskCardHolder(@NonNull View itemView) {
        super(itemView);

        this.mDescription = itemView.findViewById(R.id.descriptionIv);
        this.mTitle = itemView.findViewById(R.id.titleIv);
        this.mImgView = itemView.findViewById(R.id.imageIv);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.mCardClickListener.onCardClickListener(view, getLayoutPosition());
    }

    public void setCardClickListener(cardClickListener mCardClickListener) {
        this.mCardClickListener = mCardClickListener;
    }
}
