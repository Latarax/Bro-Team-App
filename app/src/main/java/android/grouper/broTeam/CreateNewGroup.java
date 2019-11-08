package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateNewGroup extends AppCompatActivity {

    EditText gTitle, gDescription;
    Button cancelBtn, createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);
        // Intent intent = getIntent();

        gTitle = findViewById(R.id.groupTitleInput);
        gDescription = findViewById(R.id.groupDescriptionInput);
        cancelBtn = findViewById(R.id.cancelCreateGroupButton);
        createBtn = findViewById(R.id.createGroupButton);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToHome = new Intent(CreateNewGroup.this, HomeGroupList.class);
                goToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToHome);
                finish();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup(gTitle, gDescription);

            }
        });
    }

    private void createGroup(EditText gTitle, EditText gDescription) {

        Log.d("Create Group", "attempting to create group");

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        // get pointer to user document in database
        final DocumentReference userId = database.collection("usersList").document(user.getUid());
        CollectionReference groupsList = database.collection("groupsList");

        // CollectionReference tasks = new CollectionReference();
        Map<String, Object> Members = new HashMap<>();
        Map<String, Object> newMember = new HashMap<>();

        newMember.put("Member", userId);
        newMember.put("isAdmin", true);
        Members.put("0", newMember);

        Map<String, Object> data = new HashMap<>();
        data.put("groupName", gTitle.getText().toString());
        data.put("description", gDescription.getText().toString());
        data.put("owner", user.getUid());
        data.put("Members", Members);


        groupsList.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                Log.d("Document created", documentReference.getId());
                CollectionReference taskCollection = database.collection("groupsList").document(documentReference.getId()).collection("tasks");

                Map<String, Object> tasks = new HashMap<>();
                tasks.put("assignedUser", userId);
                tasks.put("taskName", "Congrats on your new group!");
                tasks.put("description", "Add more people to your group!");
                tasks.put("isDone", false);
                tasks.put("placeName", "");
                tasks.put("placeID", "");
                tasks.put("placeLat", 0);
                tasks.put("placeLng", 0);
                taskCollection.add(tasks);

                userId.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<DocumentReference> groups = (ArrayList<DocumentReference>) documentSnapshot.get("groupList");
                        groups.add(documentReference);
                        database.collection("usersList").document(user.getUid()).update(
                                "groupList", groups
                        );

                        Intent goToHome = new Intent(CreateNewGroup.this, HomeGroupList.class);
                        goToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goToHome);
                        finish();
                    }
                });
            }
        });
    }


}
