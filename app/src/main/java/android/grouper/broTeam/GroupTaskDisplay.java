package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GroupTaskDisplay extends AppCompatActivity {

    RecyclerView mRecyclerView;
    TaskCardAdapter myAdapter;
    String mTitle, mDescription;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_task_display);

        Intent intent = getIntent();

        mTitle = intent.getStringExtra("iTitle");

        mDescription = intent.getStringExtra("iDescription");

        mRecyclerView = findViewById(R.id.groupRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new TaskCardAdapter(this, getMyList());
        mRecyclerView.setAdapter(myAdapter);

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

    private ArrayList<CardModel> getMyList() {

        ArrayList<CardModel> models = new ArrayList<>();

        CardModel m = new CardModel();
        m.setTitle(mTitle);
        m.setDescription(mDescription);
        m.setImg(R.drawable.ic_group_member_background);
        models.add(m);

        return models;
    }
}
