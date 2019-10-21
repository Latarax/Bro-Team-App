package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/*
 #####################################################
 # First activity after log-in, displays user groups #
 #####################################################
 */

public class HomeGroupList extends AppCompatActivity {

    RecyclerView mRecyclerView;
    GroupCardAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_group_list);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        mRecyclerView = findViewById(R.id.groupRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new GroupCardAdapter(this, getMyList(email));
        mRecyclerView.setAdapter(myAdapter);
    }

    private ArrayList<CardModel> getMyList(String email) {


        final ArrayList<CardModel> models = new ArrayList<>();
        final ArrayList<String> groups = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference myRef = database.collection("userList/" + user.getUid());

        myRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot document : task.getResult()) {
                    List<String> list = (List<String>) document.get("groupList");
                    for (String s : list){
                        groups.add(s);
                    }
                }
            }
        });

        if(groups.size() == 0)
        {
            return models;
        }

        for(final String gid: groups) {
            CollectionReference ref = database.collection("groupsList");
            ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.getId().contentEquals(gid)) {
                            CardModel m = new CardModel();
                            m.setTitle(document.get("groupName").toString());
                            m.setDescription(document.get("description").toString());
                            m.setImg(R.drawable.ic_launcher_foreground);
                            models.add(m);
                        }
                    }
                }
            });
        }

        for(int i = 0; i < 4; i++) {
            CardModel m = new CardModel();
            m.setTitle("NewsFeed");
            m.setDescription("This is a newsfeed description");
            m.setImg(R.drawable.ic_launcher_background);
            models.add(m);
        }
        return models;
    }
}
