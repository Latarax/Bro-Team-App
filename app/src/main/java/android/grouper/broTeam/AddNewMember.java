package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AddNewMember extends AppCompatActivity {

    String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_member);
        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("iGroupId");
    }
}
