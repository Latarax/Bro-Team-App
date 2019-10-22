package android.grouper.broTeam;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        /*final ArrayList<DocumentReference> groups = new ArrayList<>();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("userid", ""+user.getUid());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference uId = database.collection("userList").document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists())
                            {
                                List<DocumentReference> list = (List<DocumentReference>) document.get("groupList");
                                List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                                for(DocumentReference documentReference : list)
                                {
                                    Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                                    tasks.add(documentSnapshotTask);
                                }

                                Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                    @Override
                                    public void onSuccess(List<Object> list) {
                                        for(Object object : list){
                                            Log.d("groups", ""+object.toString());
                                        }
                                    }
                                });

                            }
                        }
                    }
                });



        //groups.addAll(t);
                /*.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            Log.d("myTag", "trying to get groupList");
                            List<Type> types = (List<Type>)documentSnapshot.get("userList");
                            groups.addAll(types);
                        } else {
                          Log.d("mytag", "query document is empty");
                          return;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("mytag", "Error getting groups");
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
        }

        return models;*/
    }
}
