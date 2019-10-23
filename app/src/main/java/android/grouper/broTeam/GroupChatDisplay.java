package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GroupChatDisplay extends AppCompatActivity {

    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_display);


        // THIS IS FOR THE BOTTOM NAV VIEW DO NOT TOUCH UNLESS KNOW WHAT DOING
        navigation = findViewById(R.id.bottomNavView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.groupTasksItem:
                        Intent a = new Intent(GroupChatDisplay.this, GroupTaskDisplay.class);
                        startActivity(a);
                        break;
                    case R.id.groupUsersItem:
                        Intent b = new Intent(GroupChatDisplay.this, GroupUsersDisplay.class);
                        startActivity(b);
                        break;
                    case R.id.groupNavItem:
                        Intent c = new Intent(GroupChatDisplay.this, GroupWaypointDisplay.class);
                        startActivity(c);
                        break;
                    case R.id.groupChatItem:
                        Intent d = new Intent(GroupChatDisplay.this, GroupChatDisplay.class);
                        startActivity(d);
                        break;
                }
                return false;
            }
        });
    }
}
