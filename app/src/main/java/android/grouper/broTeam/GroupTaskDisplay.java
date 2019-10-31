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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        CollectionReference taskCollection = database.collection("groupsList/"+mGroupId+"/tasks");
        Log.d("Task Collection", ""+taskCollection.getPath());

        progressBar.setVisibility(View.VISIBLE);
        taskCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    Log.d("Query Snapshot", ""+querySnapshot.getDocuments());

                    List<DocumentSnapshot> taskList = querySnapshot.getDocuments();
                    Log.d("List object", ""+taskList);

                    for (DocumentSnapshot documentSnapshot : taskList) {
                        Log.d("List Item", ""+documentSnapshot.getData());
                        Map<String, Object> data= documentSnapshot.getData();
                        String tTitle = data.get("taskName").toString();
                        String description = data.get("description").toString();
                        DocumentReference userRef = (DocumentReference) data.get("assignedUser");
                        String assignedUser = userRef.getId();
                        String tid = documentSnapshot.getId();

                        Log.d("Document Values", ""+tTitle+" "+description+" "+assignedUser+" "+tid+" "+user.getUid());

                        if(assignedUser.contentEquals("dummyUser")){
                            makeCardUnassigned(tTitle, description, tid);
                        } else if(assignedUser.contentEquals(user.getUid())) {
                            makeCardMy(tTitle, description, tid);
                        } else {
                            makeCardAssigned(tTitle, description, tid);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    // Will make a card with the passed group details
    public void makeCardMy(String title, String description, String tid){
        CardModel m = new CardModel();
        m.setTitle(title);
        m.setDescription(description);
        m.setImg(R.mipmap.ic_group_member_round);
        m.setGroupId(tid);
        mModels.add(m);
        myAdapter.notifyDataSetChanged();
        Log.d("Card list", ""+myAdapter.cardModels);
    }

    // Will make a card with the passed group details
    public void makeCardUnassigned(String title, String description, String tid){
        CardModel m = new CardModel();
        m.setTitle(title);
        m.setDescription(description);
        m.setImg(R.mipmap.ic_group_member_round);
        m.setGroupId(tid);
        uModels.add(m);
        uAdapter.notifyDataSetChanged();
        Log.d("Card list", ""+uAdapter.cardModels);
    }

    // Will make a card with the passed group details
    public void makeCardAssigned(String title, String description, String tid){
        CardModel m = new CardModel();
        m.setTitle(title);
        m.setDescription(description);
        m.setImg(R.mipmap.ic_group_member_round);
        m.setGroupId(tid);
        aModels.add(m);
        aAdapter.notifyDataSetChanged();
        Log.d("Card list", ""+aAdapter.cardModels);
    }
}
