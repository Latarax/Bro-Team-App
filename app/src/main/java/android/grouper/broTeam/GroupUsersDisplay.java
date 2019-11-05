package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GroupUsersDisplay extends AppCompatActivity {

    BottomNavigationView navigation;
    String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_users_display);
        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("iGroupId");


        // THIS IS FOR THE BOTTOM NAV VIEW DO NOT TOUCH UNLESS KNOW WHAT DOING
        navigation = findViewById(R.id.bottomNavView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.groupTasksItem:
                        Intent a = new Intent(GroupUsersDisplay.this, GroupTaskDisplay.class);
                        a.putExtra("iGroupId", mGroupId);
                        startActivity(a);
                        break;
                    case R.id.groupUsersItem:
                        // CURRENT ACTIVITY
                        break;
                    case R.id.groupNavItem:
                        Intent c = new Intent(GroupUsersDisplay.this, GroupWaypointDisplay.class);
                        c.putExtra("iGroupId", mGroupId);
                        startActivity(c);
                        break;
                    case R.id.groupChatItem:
                        Intent d = new Intent(GroupUsersDisplay.this, GroupChatDisplay.class);
                        d.putExtra("iGroupId", mGroupId);
                        startActivity(d);
                        break;
                }
                return false;
            }
        });
    }
}
