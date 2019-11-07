package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CreateNewTask extends AppCompatActivity {

    String mGroupid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_task);

        Intent intent = getIntent();
        mGroupid = intent.getStringExtra("iGroupId");

    }
}
