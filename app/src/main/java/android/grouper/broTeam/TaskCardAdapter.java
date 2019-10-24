package android.grouper.broTeam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskCardAdapter extends RecyclerView.Adapter<GroupCardHolder> {
    Context context;
    ArrayList<CardModel> cardModels;

    public TaskCardAdapter(Context context, ArrayList<CardModel> cardModels){
        this.context = context;
        this.cardModels = cardModels;
    }

    @NonNull
    @Override
    public GroupCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_vew, null);

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
                /*String groupName = cardModels.get(position).getTitle();
                String description = cardModels.get(position).getDescription();
                Intent intent = new Intent(context, GroupTaskDisplay.class);
                intent.putExtra("iTitle", groupName);
                intent.putExtra("iDescription", description);
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardModels.size();
    }
}
