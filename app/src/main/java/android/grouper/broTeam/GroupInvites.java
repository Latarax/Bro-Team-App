package android.grouper.broTeam;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GroupInvites extends AppCompatActivity {

    RecyclerView mRecyclerView;
    InviteCardAdapter mInviteAdapter;
    ArrayList<InviteCardModel> inviteCardModels = new ArrayList<>();
    ProgressBar progressBar;
    TextView noInvites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_invites);

        mRecyclerView = findViewById(R.id.groupInviteRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        noInvites = findViewById(R.id.no_invites);

        getMyInvites();

        mInviteAdapter = new InviteCardAdapter(this, inviteCardModels);
        mRecyclerView.setAdapter(mInviteAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Invitations to Groups");

    }

    private void getMyInvites() {

        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference userReference = database.collection("usersList").document(user.getUid());

        userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                ArrayList<DocumentReference> groupInvites = (ArrayList<DocumentReference>) documentSnapshot.get("groupInvites");

                if (groupInvites.size() > 0) {

                    for (DocumentReference invite : groupInvites) {
                        invite.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot document) {
                                makeCard(document.getString("groupName"), document.getId());
                            }
                        });
                    }

                    if (inviteCardModels.isEmpty()){
                        noInvites.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);

                } else {
                    // no invites
                    noInvites.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void makeCard(String groupName, String groupId) {
        InviteCardModel i = new InviteCardModel();
        i.setGroupTitle(groupName);
        i.setGroupId(groupId);
        inviteCardModels.add(i);
        mInviteAdapter.notifyDataSetChanged();
    }
}
