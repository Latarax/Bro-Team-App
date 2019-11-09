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
import java.util.Map;

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
    public void onBindViewHolder(@NonNull final MemberCardHolder holder, final int position) {
        holder.mTitle.setText(models.get(position).getTitle());

        holder.deleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove user from members list and groups list
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final FirebaseFirestore database = FirebaseFirestore.getInstance();

                DocumentReference groupReference = database.document("groupsList/"+models.get(position).getGroupId());
                final DocumentReference userReference = database.document("usersList/"+models.get(position).getMemberId());

                groupReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("Document Snapshot", ""+documentSnapshot.get("Members"));
                        Map<String, Map<String, Object>> membersMap = (Map<String, Map<String, Object>>) documentSnapshot.get("Members");

                        Log.d("Members Map", ""+membersMap);
                        for (int i = 0; i < membersMap.size(); i++) {
                            Map<String, Object> member = membersMap.get("" + i);
                            if (membersMap.get(""+i).containsValue(userReference)){
                                membersMap.remove(""+i);
                                database.document("groupsList/"+models.get(position).getGroupId())
                                        .update("Members", membersMap);
                                break;
                            }
                        }

                        userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot document) {

                                Log.d("Document Snapshot", ""+document);

                                ArrayList<DocumentReference> groupList = (ArrayList<DocumentReference>) document.get("groupList");
                                groupList.remove(holder.getAdapterPosition());
                                database.document("usersList/"+models.get(position).getMemberId()).update("groupList", groupList);

                                models.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), models.size());
                                Toast.makeText(c, "Invite to "+holder.mTitle.getText().toString()+" deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
