package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupTaskDisplay extends AppCompatActivity {

    RecyclerView mRecyclerView;
    TaskCardAdapter myAdapter;
    String mTitle, mDescription;
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
