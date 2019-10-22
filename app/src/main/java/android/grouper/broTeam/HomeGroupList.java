package android.grouper.broTeam;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

        mRecyclerView = findViewById(R.id.groupRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new GroupCardAdapter(this, getMyList());
        mRecyclerView.setAdapter(myAdapter);
    }

    private ArrayList<CardModel> getMyList() {


        final ArrayList<CardModel> models = new ArrayList<>();
        final ArrayList<String> groups = new ArrayList<>();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("/userList")
                .whereEqualTo(user.getUid(), true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("myTag", "trying to get groupList");
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<String> list = (List<String>) document.get("groupList");
                                for (String t : list) {
                                    groups.add(t);
                                }
                            }
                        } else {
                            // failed
                            Log.d("failed", "failed to get groups");
                        }
                    }
                });
        
        if(groups.size() == 0)
        {
             CardModel m = new CardModel();
             m.setTitle("Bro Team");
             m.setDescription("Just a bunch of bro's");
             m.setImg(R.drawable.ic_launcher_background);
             models.add(m);

            CardModel p = new CardModel();
            p.setTitle("Party team");
            p.setDescription("Lets get liiiiit");
            p.setImg(R.drawable.ic_launcher_background);
            models.add(p);

            CardModel f = new CardModel();
            f.setTitle("Friends");
            f.setDescription("Lets help each other out");
            f.setImg(R.drawable.ic_launcher_background);
            models.add(f);

            CardModel r = new CardModel();
            r.setTitle("Roomates");
            r.setDescription("For keeping the apartment cleen");
            r.setImg(R.drawable.ic_launcher_background);
            models.add(r);
            return models;
        }

        for(final String gid: groups) {
            database.collection("/groupList")
                    .whereEqualTo(gid, true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
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

        return models;
    }
}
