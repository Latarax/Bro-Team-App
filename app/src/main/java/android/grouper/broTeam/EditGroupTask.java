package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditGroupTask extends AppCompatActivity {

    EditText taskTitle, taskDescription, taskLocation, assignedTo;
    String groupId, taskId, iTitle, iDescription;
    Button deleteTask, saveTask, completeTask;

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
        assignedTo = findViewById(R.id.taskClaimUser);
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

        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        DocumentReference task = database.collection("groupsList").document(groupId)
                                            .collection("tasks").document(taskId);
        task.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                        assignedTo.setText(documentSnapshot.get("Username").toString());
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


        String assignedUsername = assignedTo.getText().toString().trim();
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
        });

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
