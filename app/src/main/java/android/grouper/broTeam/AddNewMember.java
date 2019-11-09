package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddNewMember extends AppCompatActivity {

    String mGroupId;
    EditText emailInput;
    Button addUser, cancel, confirm;
    RecyclerView recyclerView;
    AddMemberCardAdapter memberCardAdapter;
    ArrayList<AddMemberCardModel> memberCardModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_member);
        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("iGroupId");

        emailInput = findViewById(R.id.addUserEmailInput);
        addUser = findViewById(R.id.addMemberToListBtn);
        cancel = findViewById(R.id.cancelAddMembersBtn);
        confirm = findViewById(R.id.confirmMembersBtn);

        recyclerView = findViewById(R.id.addedUserRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberCardAdapter = new AddMemberCardAdapter(this, memberCardModels);
        recyclerView.setAdapter(memberCardAdapter);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findUser();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToUsersDisplay = new Intent(AddNewMember.this, GroupUsersDisplay.class );
                goToUsersDisplay.putExtra("iGroupId", mGroupId);
                startActivity(goToUsersDisplay);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInvites();
            }
        });

        getSupportActionBar().setTitle("Add New Members");
    }

    private void sendInvites() {
        if (memberCardModels.size() > 0){

            final FirebaseFirestore database = FirebaseFirestore.getInstance();
            final DocumentReference groupReference = database.document("groupsList/"+mGroupId);

            for (AddMemberCardModel m : memberCardModels){
                final DocumentReference memberReference = database.document("usersList/"+m.getMemberId());
                memberReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<DocumentReference> groupInvites = (ArrayList<DocumentReference>) documentSnapshot.get("groupInvites");
                        groupInvites.add(groupReference);
                        database.document(memberReference.getPath()).update("groupInvites", groupInvites);
                        Toast.makeText(getApplicationContext(), "Members Invited", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            Intent goToUsersDisplay = new Intent(AddNewMember.this, GroupUsersDisplay.class );
            goToUsersDisplay.putExtra("iGroupId", mGroupId);
            startActivity(goToUsersDisplay);

        } else {
            // no new members
            Toast.makeText(getApplicationContext(), "No Members to Invite", Toast.LENGTH_SHORT).show();
        }
    }

    private void findUser() {

        String userEmail = emailInput.getText().toString().trim();
        if (userEmail.isEmpty()){
            emailInput.setError("Must Enter User Email");
            emailInput.requestFocus();
            return;
        }

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = database.collection("usersList");
        usersCollection.whereEqualTo("Email", userEmail).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                            if (documents.size() > 0 ) {
                                Log.d("Query Documents", "" + documents);
                                for (DocumentSnapshot d : documents) {
                                    Log.d("Document Snapshot", "" + d);
                                    String username = d.getString("Username");
                                    String userId = d.getId();
                                    makeCard(username, userId);
                                }
                            } else {

                                // user not found
                                emailInput.setError("User Not Found");
                                emailInput.requestFocus();
                            }
                        }
                    }
                });
    }

    private void makeCard(String username, String userId) {
        AddMemberCardModel m = new AddMemberCardModel();
        m.setMemberName(username);
        m.setMemberId(userId);

        for(AddMemberCardModel invite : memberCardModels){
            if(invite.getMemberId().contentEquals(userId)){
                // user already added
                emailInput.setError("User already added below");
                emailInput.requestFocus();
                return;
            }
        }

        memberCardModels.add(m);
        memberCardAdapter.notifyDataSetChanged();
    }
}
