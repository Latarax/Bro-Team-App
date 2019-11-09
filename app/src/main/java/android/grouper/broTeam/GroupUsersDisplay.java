package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class GroupUsersDisplay extends AppCompatActivity {

    BottomNavigationView navigation;
    String groupId;
    ArrayList<MemberCardModel> models = new ArrayList<>();
    RecyclerView mRecyclerView;
    MemberCardAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_users_display);
        Intent intent = getIntent();
        groupId = intent.getStringExtra("iGroupId");
        displayMembers();
        mRecyclerView = findViewById(R.id.UsersRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MemberCardAdapter(this, models);
        mRecyclerView.setAdapter(myAdapter);

        // THIS IS FOR THE BOTTOM NAV VIEW DO NOT TOUCH UNLESS KNOW WHAT DOING
        navigation = findViewById(R.id.bottomNavView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.groupTasksItem:
                        Intent a = new Intent(GroupUsersDisplay.this, GroupTaskDisplay.class);
                        a.putExtra("iGroupId", groupId);
                        startActivity(a);
                        break;
                    case R.id.groupUsersItem:
                        // CURRENT ACTIVITY
                        break;
                    case R.id.groupNavItem:
                        Intent c = new Intent(GroupUsersDisplay.this, GroupWaypointDisplay.class);
                        c.putExtra("iGroupId", groupId);
                        startActivity(c);
                        break;
                    case R.id.groupChatItem:
                        Intent d = new Intent(GroupUsersDisplay.this, GroupChatDisplay.class);
                        d.putExtra("iGroupId", groupId);
                        startActivity(d);
                        break;
                }
                return false;
            }
        });
    }

    private void makeCard(String username) {
        MemberCardModel m = new MemberCardModel();
        m.setTitle(username);
        models.add(m);
        myAdapter.notifyDataSetChanged();
    }

    private void displayMembers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference groupData = db.collection("groupsList").document(groupId);
        final ArrayList<String> members = new ArrayList<>();
        groupData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot docInfo = task.getResult();
                    Log.d("GroupID", groupId);
                    //ArrayList<Map<String, Object>> membersMap = (ArrayList<Map<String, Object>>) docInfo.get("Members");
                    Map<String, Map<String, Object>> membersMap = (Map<String, Map<String, Object>>) docInfo.get("Members");
                    Log.d("members", membersMap.get("0").toString());

                    for (int i = 0; i < membersMap.size(); i++) {
                        Map<String, Object> member = membersMap.get("" + i);
                        final DocumentReference groupMember = (DocumentReference) member.get("Member");
                        groupMember.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String member = documentSnapshot.getString("Username");
                                members.add(member);
                                makeCard(member);
                            }
                        });

                    }

                }
            }
        });
    }
}
