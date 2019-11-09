package android.grouper.broTeam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MemberCardAdapter extends RecyclerView.Adapter<MemberCardHolder> {

    Context c;
    ArrayList<MemberCardModel> models;

    public MemberCardAdapter(Context c, ArrayList<MemberCardModel> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MemberCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_card_view, null);

        return new MemberCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberCardHolder holder, int position) {
        holder.mTitle.setText(models.get(position).getTitle());

        holder.deleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove user from members list and groups list
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore database = FirebaseFirestore.getInstance();
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
