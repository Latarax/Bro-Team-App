package android.grouper.broTeam;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InviteCardAdapter extends RecyclerView.Adapter<InviteCardHolder> {

    Context context;
    ArrayList<InviteCardModel> inviteCardModels;

    public InviteCardAdapter(Context context, ArrayList<InviteCardModel> inviteCardModels) {
        this.context = context;
        this.inviteCardModels = inviteCardModels;
    }

    @NonNull
    @Override
    public InviteCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_member_card, null);

        return new InviteCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InviteCardHolder holder, final int position) {
        holder.mTitle.setText(inviteCardModels.get(position).getGroupTitle());

        holder.deleteInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final FirebaseFirestore database = FirebaseFirestore.getInstance();

                DocumentReference userReference = database.document("usersList/"+user.getUid());
                userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        //DocumentReference groupReference = database.document("groupsList/"+inviteCardModels.get(position).getGroupId());

                        ArrayList<DocumentReference> groupInvites = (ArrayList<DocumentReference>) documentSnapshot.get("groupInvites");
                        groupInvites.remove(holder.getAdapterPosition());
                        database.document("usersList/"+user.getUid()).update("groupInvites", groupInvites);

                        inviteCardModels.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), inviteCardModels.size());
                        Toast.makeText(context, "Invite to "+holder.mTitle.getText().toString()+" deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final FirebaseFirestore database = FirebaseFirestore.getInstance();

                final DocumentReference groupReference = database.document("groupsList/"+inviteCardModels.get(position).getGroupId());
                final DocumentReference userReference = database.document("usersList/"+user.getUid());

                //update Groups member list
                groupReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Map<String, Object>> membersMap = (Map<String, Map<String, Object>>) documentSnapshot.get("Members");

                        Log.d("Members Map", ""+membersMap);
                        Log.d("Members Map", ""+membersMap.size());

                        Map<String, Object> newMember = new HashMap<>();
                        newMember.put("Member", userReference);
                        newMember.put("isAdmin", false);

                        membersMap.put(""+membersMap.size(), newMember);
                        database.document("groupsList/"+inviteCardModels.get(position).getGroupId()).update("Members", membersMap);

                        // Update Users groups list
                        userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                ArrayList<DocumentReference> groups = (ArrayList<DocumentReference>) documentSnapshot.get("groupList");
                                groups.add(groupReference);
                                database.collection("usersList").document(user.getUid()).update(
                                        "groupList", groups
                                );

                                ArrayList<DocumentReference> groupInvites = (ArrayList<DocumentReference>) documentSnapshot.get("groupInvites");
                                groupInvites.remove(holder.getAdapterPosition());
                                database.document("usersList/"+user.getUid()).update("groupInvites", groupInvites);

                                inviteCardModels.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), inviteCardModels.size());
                                Toast.makeText(context, "You have joined "+holder.mTitle.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return inviteCardModels.size();
    }
}
