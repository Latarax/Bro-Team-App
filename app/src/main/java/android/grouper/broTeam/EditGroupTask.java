package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditGroupTask extends AppCompatActivity {

    EditText taskTitle, taskDescription, taskLocation;
    Spinner assignedTo;
    String groupId, taskId, iTitle, iDescription;
    Button deleteTask, saveTask, completeTask;
    List<String> groupMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_task);
        Intent intent = getIntent();

        iTitle = intent.getStringExtra("iTitle");
        iDescription = intent.getStringExtra("iDescription");
        taskId = intent.getStringExtra("iTaskId");
        groupId = intent.getStringExtra("iGroupId");

        Log.d("GroupId", ""+groupId);
        Log.d("TaskId", ""+taskId);

        taskTitle = findViewById(R.id.taskTitle);
        taskDescription = findViewById(R.id.taskDescription);
        taskLocation = findViewById(R.id.taskAddress);
        deleteTask = findViewById(R.id.taskDeleteButton);
        saveTask = findViewById(R.id.taskSaveButton);
        completeTask = findViewById(R.id.taskCompleteButton);

        getTaskDetails();

        taskTitle.setText(iTitle);
        taskDescription.setText(iDescription);

        deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteTask();
            }
        });

        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveTask();
            }
        });

        completeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompleteTask();
            }
        });

    }

    private void getTaskDetails() {

        assignedTo = findViewById(R.id.assignUserDropBox);
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference group = database.collection("groupsList").document(groupId);

        group.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Map<String, Map<String, Object>> membersMap = (Map<String, Map<String, Object>>) documentSnapshot.get("Members");
                Log.d("Members Map", ""+membersMap);
                for(int i = 0; i < membersMap.size(); i++){

                    Log.d("object", ""+membersMap.get(""+i));
                    Map<String, Object> member = membersMap.get(""+i);
                    Log.d("Members Map", ""+member.get("Member"));
                    DocumentReference groupMember = (DocumentReference) member.get("Member");

                    Log.d("Document Reference", ""+groupMember);
                    groupMember.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d("Adding username", ""+documentSnapshot.get("Username").toString());
                            groupMembers.add(documentSnapshot.get("Username").toString());
                            Log.d("Group list", ""+groupMembers);
                        }
                    });
                }

                DocumentReference taskReference = database.collection("groupsList").document(groupId)
                        .collection("tasks").document(taskId);
                taskReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(!documentSnapshot.get("location").toString().isEmpty()) {
                            taskLocation.setText(documentSnapshot.get("location").toString());
                        } else {
                            taskLocation.setText("No Location Set");
                        }
                        DocumentReference assignedUser = (DocumentReference) documentSnapshot.get("assignedUser");
                        assignedUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                int assignedUser;
                                for(assignedUser = 0; assignedUser < groupMembers.size(); assignedUser++){
                                    if(groupMembers.get(assignedUser).contentEquals(documentSnapshot.get("assignedUser").toString()))
                                        break;
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(EditGroupTask.this,
                                        android.R.layout.simple_spinner_item, groupMembers);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                assignedTo.setAdapter(dataAdapter);
                                assignedTo.setSelection(assignedUser);

                                //assignedTo.setText(documentSnapshot.get("Username").toString());
                            }
                        });
                    }
                });
            }
        });
    }


    private void DeleteTask() {
        Map<String, Object> data = new HashMap<>();
        data.put("assignedUser", FieldValue.delete());
        data.put("description", FieldValue.delete());
        data.put("isDone", FieldValue.delete());
        data.put("location", FieldValue.delete());
        data.put("taskName", FieldValue.delete());

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference task = database.collection("groupsList").document(groupId)
                                            .collection("tasks").document(taskId);
        task.update(data);
        task.delete();
        Toast.makeText(getApplicationContext(), "Task Deleted Successfully", Toast.LENGTH_SHORT).show();
        Intent goToGroupTaskDisplay = new Intent(EditGroupTask.this, GroupTaskDisplay.class );
        goToGroupTaskDisplay.putExtra("iGroupId", groupId);
        startActivity(goToGroupTaskDisplay);
        finish();
    }


    private void SaveTask() {

        final String taskName = taskTitle.getText().toString().trim();
        final String taskDescribe = taskDescription.getText().toString().trim();
        final String location = taskLocation.getText().toString().trim();

        if (taskName.isEmpty()) {
            taskTitle.setError("Must enter a Task Title");
            taskTitle.requestFocus();
            return;
        } else if (taskDescribe.isEmpty()){
            taskDescription.setError("Must enter a Task Description");
            taskDescription.requestFocus();
            return;
        }

        if (location.isEmpty()){

        }

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final DocumentReference task = database.collection("groupsList").document(groupId)
                .collection("tasks").document(taskId);


        /*String assignedUsername = assignedTo.;
        database.collection("usersList").whereEqualTo("Username", assignedUsername)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                DocumentSnapshot user = documentSnapshotList.get(0);

                task.update("assignedUser", database.collection("usersList").document(user.getId()));
                task.update("description", taskDescribe);
                task.update("location", location);
                task.update("taskName", taskName);

                Toast.makeText(getApplicationContext(), "Task Updated", Toast.LENGTH_SHORT).show();
                Intent goToGroupTaskDisplay = new Intent(EditGroupTask.this, GroupTaskDisplay.class );
                goToGroupTaskDisplay.putExtra("iGroupId", groupId);
                startActivity(goToGroupTaskDisplay);
                finish();
            }
        });*/

    }

    private void CompleteTask() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference task = database.collection("groupsList").document(groupId)
                                            .collection("tasks").document(taskId);

        task.update("isDone", true);
        Intent goToGroupTaskDisplay = new Intent(EditGroupTask.this, GroupTaskDisplay.class );
        goToGroupTaskDisplay.putExtra("iGroupId", groupId);
        startActivity(goToGroupTaskDisplay);
        finish();

    }
}
