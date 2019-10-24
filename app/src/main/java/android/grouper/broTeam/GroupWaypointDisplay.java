package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GroupWaypointDisplay extends AppCompatActivity {

    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_waypoint_display);



        // THIS IS FOR THE BOTTOM NAV VIEW DO NOT TOUCH UNLESS KNOW WHAT DOING
        navigation = findViewById(R.id.bottomNavView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.groupTasksItem:
                        Intent a = new Intent(GroupWaypointDisplay.this, GroupTaskDisplay.class);
                        startActivity(a);
                        break;
                    case R.id.groupUsersItem:
                        Intent b = new Intent(GroupWaypointDisplay.this, GroupUsersDisplay.class);
                        startActivity(b);
                        break;
                    case R.id.groupNavItem:
                        // CURRENT ACTIVITY
                        break;
                    case R.id.groupChatItem:
                        Intent d = new Intent(GroupWaypointDisplay.this, GroupChatDisplay.class);
                        startActivity(d);
                        break;
                }
                return false;
            }
        });
    }
}
