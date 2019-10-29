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
    ArrayList<CardModel> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_group_list);

        mRecyclerView = findViewById(R.id.groupRecyclerView); // get container for cards
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)); //set layout

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

                    // go through list and get group details
                    for(int i = 0; i < groups.size(); i++){

                        Log.d("group index", ""+groups.get(i).getId());
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference groupId = db.collection("groupsList").document(groups.get(i).getId());

                        // go into group and get group details
                        groupId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> t) {
                                if(t.isSuccessful()){

                                    Log.d("task group call", ""+t.getResult());
                                    DocumentSnapshot documentSnapshot = t.getResult(); // get snapshot of group details
                                    Log.d("DocumentSnapshot group", ""+documentSnapshot.getData());

                                    // extract title and description
                                    String gTitle = (String) documentSnapshot.get("groupName");
                                    String description = (String) documentSnapshot.get("description");

                                    Log.d("Card list", ""+models);
                                    makeCard(gTitle, description); //make extracted details into cards
                                }
                            }
                        });
                    }
                } else { // TO-DO: If no groups, display default cards
                    Log.d("failed to get", "get failed with ", task.getException());
                    String gTitle = "You're not in any groups :(";
                    String description = "Either create or join groups for more";

                    CardModel m = new CardModel();
                    m.setTitle(gTitle);
                    m.setDescription(description);
                    m.setImg(R.drawable.ic_group_icon_background);
                    models.add(m);
                }
            }
        });


        /*CardModel m = new CardModel();
        m.setTitle("Bro Team");
        m.setDescription("Just a bunch of bro's");
        m.setImg(R.drawable.ic_group_icon_background);
        models.add(m);

        CardModel p = new CardModel();
        p.setTitle("Party team");
        p.setDescription("Lets get liiiiit");
        p.setImg(R.drawable.ic_group_icon_background);
        models.add(p);

        CardModel f = new CardModel();
        f.setTitle("Squad");
        f.setDescription("Lets help each other out");
        f.setImg(R.drawable.ic_group_icon_background);
        models.add(f);

        CardModel r = new CardModel();
        r.setTitle("Roommates");
        r.setDescription("For keeping the apartment clean");
        r.setImg(R.drawable.ic_group_icon_background);
        models.add(r);*/

        //return models;
    }

    // Will make a card with the passed group details
    public void makeCard(String title, String description){
        CardModel m = new CardModel();
        m.setTitle(title);
        m.setDescription(description);
        m.setImg(R.drawable.ic_group_icon_background);
        models.add(m);
        Log.d("Card list", ""+models);
    }
}
