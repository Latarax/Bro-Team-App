package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/*
 #####################################################
 # First activity after log-in, displays user groups #
 #####################################################
 */

public class HomeGroupList extends AppCompatActivity {

    RecyclerView mRecyclerView;
    GroupCardAdapter myAdapter;
    ProgressBar progressBar;
    ArrayList<CardModel> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_group_list);

        mRecyclerView = findViewById(R.id.myTaskRecyclerView); // get container for cards
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)); //set layout
        progressBar = findViewById(R.id.progressBar);

        ImageButton createGroupBtn = findViewById(R.id.createGroupBtn);
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCreateGroup = new Intent(HomeGroupList.this, CreateNewGroup.class);
                startActivity(goToCreateGroup);
            }
        });

        getMyList(); // get list of groups from database and make cards in them
        Log.d("test", "Finished getting groups");

        myAdapter = new GroupCardAdapter(this, models); //set adapter objects and onclick listeners
        mRecyclerView.setAdapter(myAdapter); // put all cards on display
    }

    //This will grab all the groups the current user is enrolled in and make cards
    //to be displayed
    public void getMyList() {

        // get user instance and database reference
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // get pointer to user document in database
        DocumentReference userId = database.collection("usersList").document(user.getUid());

        progressBar.setVisibility(View.VISIBLE);
        // get list of groups via task
        userId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d("task group call", ""+task.getResult());
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult(); // get snapshot of user details
                    Log.d("DocumentSnapshot data: ", "" + document.getData());

                    // cast group list into array of references
                    ArrayList<DocumentReference> groups = (ArrayList<DocumentReference>) document.get("groupList");
                    if(groups.size() > 0) {
                        // go through list and get group details
                        for (int i = 0; i < groups.size(); i++) {

                            final String gid = groups.get(i).getId();
                            Log.d("group index", "" + gid);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference groupId = db.collection("groupsList").document(gid);

                            // go into group and get group details
                            groupId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> t) {
                                    if (t.isSuccessful()) {

                                        Log.d("task group call", "" + t.getResult());
                                        DocumentSnapshot documentSnapshot = t.getResult(); // get snapshot of group details
                                        Log.d("DocumentSnapshot group", "" + documentSnapshot.getData());

                                        // extract title and description
                                        String gTitle = (String) documentSnapshot.get("groupName");
                                        String description = (String) documentSnapshot.get("description");

                                        Log.d("Card list", "" + models);
                                        makeCard(gTitle, description, gid); //make extracted details into cards
                                    }
                                }
                            });
                        }

                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        String gTitle = "You're not in any groups :(";
                        String description = "Create or join groups for more";
                        makeCard(gTitle, description, null);
                    }
                } else { // TO-DO: If no groups, display default cards
                    Log.d("failed to get", "get failed with ", task.getException());
                }
            }
        });
    }

    // Will make a card with the passed group details
    public void makeCard(String title, String description, String gid){
        CardModel m = new CardModel();
        m.setTitle(title);
        m.setDescription(description);
        m.setImg(R.mipmap.ic_group_icon_round);
        m.setGroupId(gid);
        models.add(m);
        myAdapter.notifyDataSetChanged();
        Log.d("Card list", ""+myAdapter.cardModels);
    }
}
