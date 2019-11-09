package android.grouper.broTeam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddMemberCardAdapter extends RecyclerView.Adapter<AddMemberCardHolder> {
    Context context;
    ArrayList<AddMemberCardModel> memberCardModels;

    public AddMemberCardAdapter(Context context, ArrayList<AddMemberCardModel> memberCardModels){
        this.context = context;
        this.memberCardModels = memberCardModels;
    }

    @NonNull
    @Override
    public AddMemberCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_member_card, null);

        return new AddMemberCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddMemberCardHolder holder, int position) {

        holder.mTitle.setText(memberCardModels.get(position).getMemberName());

        holder.deleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberCardModels.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), memberCardModels.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberCardModels.size();
    }
}
