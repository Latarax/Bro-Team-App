package android.grouper.broTeam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskCardAdapter extends RecyclerView.Adapter<TaskCardHolder> {
    Context context;
    ArrayList<TaskCardModel> cardModels;

    public TaskCardAdapter(Context context, ArrayList<TaskCardModel> taskCardModels){
        this.context = context;
        this.cardModels = taskCardModels;
    }

    @NonNull
    @Override
    public TaskCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_vew, null);

        return new TaskCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskCardHolder holder, int position) {
        holder.mTitle.setText(cardModels.get(position).getTitle());
        holder.mDescription.setText(cardModels.get(position).getDescription());
        holder.mImgView.setImageResource(cardModels.get(position).getImg());

        holder.setCardClickListener(new cardClickListener() {
            @Override
            public void onCardClickListener(View v, int position) {
                String groupName = cardModels.get(position).getTitle();
                String description = cardModels.get(position).getDescription();
                String taskId = cardModels.get(position).getTaskId();
                String groupId = cardModels.get(position).getGroupId();
                Intent goToTaskEdit = new Intent(context, EditGroupTask.class);
                goToTaskEdit.putExtra("iTitle", groupName);
                goToTaskEdit.putExtra("iDescription", description);
                goToTaskEdit.putExtra("iTaskId", taskId);
                goToTaskEdit.putExtra("iGroupId", groupId);
                context.startActivity(goToTaskEdit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardModels.size();
    }
}
