package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GroupTaskDisplay extends AppCompatActivity {

    RecyclerView mRecyclerView, uRecyclerView, aRecyclerView;
    TaskCardAdapter myAdapter, uAdapter, aAdapter;
    String mGroupId;
    ProgressBar progressBar;

    ArrayList<CardModel> mModels = new ArrayList<>();
    ArrayList<CardModel> uModels = new ArrayList<>();
    ArrayList<CardModel> aModels = new ArrayList<>();
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_task_display);

        Intent intent = getIntent();

        mGroupId = intent.getStringExtra("iGroupId");

        mRecyclerView = findViewById(R.id.myTaskRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        uRecyclerView = findViewById(R.id.unassignedRecyclerView);
        uRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        aRecyclerView = findViewById(R.id.assignedRecyclerView);
        aRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);

        getMyList();

        myAdapter = new TaskCardAdapter(this, mModels);
        uAdapter = new TaskCardAdapter(this, uModels);
        aAdapter = new TaskCardAdapter(this, aModels);

        mRecyclerView.setAdapter(myAdapter);
        uRecyclerView.setAdapter(uAdapter);
        aRecyclerView.setAdapter(aAdapter);

        // THIS IS FOR THE BOTTOM NAV VIEW DO NOT TOUCH UNLESS KNOW WHAT DOING
        navigation = findViewById(R.id.bottomNavView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.groupTasksItem:
                        // CURRENT ACTIVITY
                        break;
                    case R.id.groupUsersItem:
                        Intent b = new Intent(GroupTaskDisplay.this, GroupUsersDisplay.class);
                        startActivity(b);
                        break;
                    case R.id.groupNavItem:
                        Intent c = new Intent(GroupTaskDisplay.this, GroupWaypointDisplay.class);
                        startActivity(c);
                        break;
                    case R.id.groupChatItem:
                        Intent d = new Intent(GroupTaskDisplay.this, GroupChatDisplay.class);
                        startActivity(d);
                        break;
                }
                return false;
            }
        });

    }

    private void getMyList() {

        // get user instance and database reference
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // get pointer to user document in database
        DocumentReference groupId = database.collection("usersList").document(mGroupId);

        progressBar.setVisibility(View.VISIBLE);
        // get list of groups via task
        groupId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d("task group call", ""+task.getResult());
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult(); // get snapshot of user details
                    Log.d("DocumentSnapshot data: ", "" + document.getData());

                    // cast group list into array of references
                    ArrayList<CollectionReference> groups = (ArrayList<CollectionReference>) document.get("tasks");

                    // go through list and get group details
                    for(int i = 0; i < groups.size(); i++){

                        final String tid = groups.get(i).getId();
                        Log.d("group index", ""+tid);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference groupId = db.collection("groupsList").document(tid);

                        // go into group and get group details
                        groupId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> t) {
                                if(t.isSuccessful()){

                                    Log.d("task group call", ""+t.getResult());
                                    DocumentSnapshot documentSnapshot = t.getResult(); // get snapshot of group details
                                    Log.d("DocumentSnapshot group", ""+documentSnapshot.getData());

                                    // extract title and description
                                    String tTitle = (String) documentSnapshot.get("taskName");
                                    String description = (String) documentSnapshot.get("description");
                                    String assignedUser = (String) documentSnapshot.get("assignedUser");

                                    if(assignedUser.contentEquals("dummyUser")){
                                        makeCardUnassigned(tTitle, description, tid);
                                    } else if(assignedUser.contentEquals(user.getUid())) {
                                        makeCardMy(tTitle, description, tid); //make extracted details into cards
                                    } else {
                                        makeCardAssigned(tTitle, description, tid);
                                    }
                                }
                            }
                        });
                    }

                    progressBar.setVisibility(View.GONE);

                } else { // TO-DO: If no groups, display default cards
                    Log.d("failed to get", "get failed with ", task.getException());
                    String gTitle = "You're not in any groups :(";
                    String description = "Either create or join groups for more";

                    CardModel m = new CardModel();
                    m.setTitle(gTitle);
                    m.setDescription(description);
                    m.setImg(R.drawable.ic_group_icon_background);
                    mModels.add(m);
                }
            }
        });
    }

    // Will make a card with the passed group details
    public void makeCardMy(String title, String description, String gid){
        CardModel m = new CardModel();
        m.setTitle(title);
        m.setDescription(description);
        m.setImg(R.drawable.ic_group_icon_background);
        m.setGroupId(gid);
        mModels.add(m);
        myAdapter.notifyDataSetChanged();
        Log.d("Card list", ""+myAdapter.cardModels);
    }

    // Will make a card with the passed group details
    public void makeCardUnassigned(String title, String description, String gid){
        CardModel m = new CardModel();
        m.setTitle(title);
        m.setDescription(description);
        m.setImg(R.drawable.ic_group_icon_background);
        m.setGroupId(gid);
        uModels.add(m);
        uAdapter.notifyDataSetChanged();
        Log.d("Card list", ""+myAdapter.cardModels);
    }

    // Will make a card with the passed group details
    public void makeCardAssigned(String title, String description, String tid){
        CardModel m = new CardModel();
        m.setTitle(title);
        m.setDescription(description);
        m.setImg(R.drawable.ic_group_icon_background);
        m.setGroupId(tid);
        aModels.add(m);
        aAdapter.notifyDataSetChanged();
        Log.d("Card list", ""+myAdapter.cardModels);
    }
}
