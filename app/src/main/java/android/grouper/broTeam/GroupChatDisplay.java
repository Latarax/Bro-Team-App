package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GroupChatDisplay extends AppCompatActivity {

    BottomNavigationView navigation;
    String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_display);
        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("iGroupId");

        getSupportActionBar().setTitle("Group Chat");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_group_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // THIS IS FOR THE BOTTOM NAV VIEW DO NOT TOUCH UNLESS KNOW WHAT DOING
        navigation = findViewById(R.id.bottomNavView);
        navigation.getMenu().getItem(3).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.groupTasksItem:
                        Intent a = new Intent(GroupChatDisplay.this, GroupTaskDisplay.class);
                        a.putExtra("iGroupId", mGroupId);
                        item.setChecked(true);
                        startActivity(a);
                        break;
                    case R.id.groupUsersItem:
                        Intent b = new Intent(GroupChatDisplay.this, GroupUsersDisplay.class);
                        b.putExtra("iGroupId", mGroupId);
                        item.setChecked(true);
                        startActivity(b);
                        break;
                    case R.id.groupNavItem:
                        Intent c = new Intent(GroupChatDisplay.this, GroupWaypointDisplay.class);
                        c.putExtra("iGroupId", mGroupId);
                        item.setChecked(true);
                        startActivity(c);
                        break;
                    case R.id.groupChatItem:
                        // CURRENT DISPLAY
                        break;
                }
                return false;
            }
        });
    }
}
