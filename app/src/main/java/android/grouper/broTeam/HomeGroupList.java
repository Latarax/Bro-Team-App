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


        ArrayList<CardModel> models = new ArrayList<>();
        ArrayList<DocumentReference> groups = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference userId = database.collection("usersList").document(user.getUid());
         userId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if (task.isSuccessful()) {
                     DocumentSnapshot document = task.getResult();
                     if (document.exists()) {
                         Log.d("DocumentSnapshot data: ", "" + document.getData());
                         if(!document.getData().containsKey("groupList")){
                             Log.d("no array", "No groupList");
                         } else {
                             Log.d("DocumentSnapshot data: ", ""+document.getData());
                         }
                         //Object groupArray = document.getData("groupList");
                         ///Log.d("array object", ""+groupArray);
                         /*groupArray.get().addOnSuccessListener(new OnCompleteListener<DocumentSnapshot>(){
                             @Override
                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                 if(task.isSuccessful())
                                 {
                                     DocumentSnapshot a = task.getResult();
                                     if(a.exists()) {
                                         Log.d("array", ""+a.getData());
                                     } else {
                                         Log.d("no array", "No array");
                                     }
                                 }
                             }
                         })*/
                     } else {
                         Log.d("no field", "No such document");
                     }
                 } else {
                     Log.d("failed to get", "get failed with ", task.getException());
                 }
             }
         });

        /*database.collection("userList").document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d("document snapshot", "" + document.get("email"));

                        } else {
                            Log.d("document snapshot", "no such document");
                        }
                    }
                });*/

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


        /*Log.d("groups", "trying to get group info");
        for(Type gid: groups) {
            database.collection("/groupList")
                    .whereEqualTo(gid.toString(), true)
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
        }*/

        //return models;
    }
}
