package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GroupTaskDisplay extends AppCompatActivity {

    TextView mTitleTv, mDescTv;
    ImageView mImageV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_task_display);

        ActionBar actionBar = getSupportActionBar();

        mTitleTv = findViewById(R.id.titleIv);

        Intent intent = getIntent();

        String mTitle = intent.getStringExtra("iTitle");

        actionBar.setTitle(mTitle);

        mTitleTv.setText(mTitle);
    }
}
