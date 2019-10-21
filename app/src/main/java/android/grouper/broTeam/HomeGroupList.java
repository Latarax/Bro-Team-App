package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        mRecyclerView = findViewById(R.id.groupRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new GroupCardAdapter(this, getMyList(email));
        mRecyclerView.setAdapter(myAdapter);
    }

    private ArrayList<CardModel> getMyList(String email) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("userList");

        Query query = myRef.child(email).getParent();

        myRef.child("usersList").getKey();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


        ArrayList<CardModel> models = new ArrayList<>();

        CardModel m = new CardModel();
        m.setTitle("NewsFeed");
        m.setDescription("This is a newsfeed description");
        m.setImg(R.drawable.ic_launcher_background);
        models.add(m);

        return models;
    }
}
