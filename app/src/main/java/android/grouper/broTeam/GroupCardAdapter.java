package android.grouper.broTeam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupCardAdapter extends RecyclerView.Adapter<GroupCardHolder> {

    Context context;
    ArrayList<CardModel> cardModels;

    public GroupCardAdapter(Context context, ArrayList<CardModel> cardModels){
        this.context = context;
        this.cardModels = cardModels;
    }

    @NonNull
    @Override
    public GroupCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card_view, null);

        return new GroupCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupCardHolder holder, int position) {
        holder.mTitle.setText(cardModels.get(position).getTitle());
        holder.mDescription.setText(cardModels.get(position).getDescription());
        holder.mImgView.setImageResource(cardModels.get(position).getImg());

        holder.setCardClickListener(new cardClickListener() {
            @Override
            public void onCardClickListener(View v, int position) {
                String groupName = cardModels.get(position).getTitle();
                Intent intent = new Intent(context, GroupTaskDisplay.class);
                intent.putExtra("iTitle", groupName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardModels.size();
    }
}
