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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditGroupTask extends AppCompatActivity {

    private String TAG = "EditGroupTask";
    EditText taskTitle, taskDescription, taskLocation;
    Spinner assignedTo;
    String groupId, taskId, iTitle, iDescription;
    Button deleteTask, saveTask, completeTask;
    List<String> groupMembers = new ArrayList<>();

    // place stuff
    private String placeName;
    private LatLng placeLatLng;
    private double placeLat;
    private double placeLng;
    private String placeID;
    int AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_task);
        Intent intent = getIntent();

        iTitle = intent.getStringExtra("iTitle");
        iDescription = intent.getStringExtra("iDescription");
        taskId = intent.getStringExtra("iTaskId");
        groupId = intent.getStringExtra("iGroupId");

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

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

        taskLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

                // Start the autocomplete intent.
                Intent autoPlaceIntent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields).build(EditGroupTask.this);
                startActivityForResult(autoPlaceIntent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                placeName = place.getName();
                placeLatLng = place.getLatLng();
                placeLat = placeLatLng.latitude;
                placeLng = placeLatLng.longitude;
                placeID = place.getId();
                taskLocation.setText(placeName);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.d(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void getTaskDetails() {

        assignedTo = findViewById(R.id.assignUserDropBox);
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference taskReference = database.collection("groupsList").document(groupId)
                .collection("tasks").document(taskId);

        taskReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(!documentSnapshot.get("placeName").toString().isEmpty()) {
                    taskLocation.setText(documentSnapshot.get("placeName").toString());
                } else {
                    taskLocation.setText("No Location Set");
                }

                DocumentReference assignedUser = (DocumentReference) documentSnapshot.get("assignedUser");
                assignedUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        groupMembers.add(documentSnapshot.getString("Username"));

                        DocumentReference group = database.collection("groupsList").document(groupId);
                        group.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Log.d("Document snapshot", ""+documentSnapshot);
                                Map<String, Map<String, Object>> membersMap = (Map<String, Map<String, Object>>) documentSnapshot.get("Members");
                                Log.d("Member Map", ""+membersMap);
                                for (int i = 0; i < membersMap.size(); i++) {

                                    Map<String, Object> member = membersMap.get("" + i);
                                    Log.d("Member map", ""+member);
                                    final DocumentReference groupMember = (DocumentReference) member.get("Member");
                                    groupMember.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            if(!groupMembers.contains(documentSnapshot.getString("Username"))) {
                                                groupMembers.add(documentSnapshot.getString("Username"));
                                            }
                                        }
                                    });
                                }

                                if(!groupMembers.contains("Unassigned")){
                                    groupMembers.add("Unassigned");
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(EditGroupTask.this,
                                        android.R.layout.simple_spinner_item, groupMembers);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                assignedTo.setAdapter(dataAdapter);
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

        if (taskName.isEmpty()) {
            taskTitle.setError("Must enter a Task Title");
            taskTitle.requestFocus();
            return;
        } else if (taskDescribe.isEmpty()){
            taskDescription.setError("Must enter a Task Description");
            taskDescription.requestFocus();
            return;
        }
        if(placeName == null){
            placeName = "";
            placeID = "";
            placeLat = 0;
            placeLng = 0;
        }

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final DocumentReference task = database.collection("groupsList").document(groupId)
                .collection("tasks").document(taskId);


        String assignedUsername = assignedTo.getSelectedItem().toString();
        database.collection("usersList").whereEqualTo("Username", assignedUsername)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                DocumentSnapshot user = documentSnapshotList.get(0);

                task.update("assignedUser", database.collection("usersList").document(user.getId()));
                task.update("description", taskDescribe);
                task.update("placeName", placeName);
                task.update("placeID", placeID);
                task.update("placeLat", placeLat);
                task.update("placeLng", placeLng);
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
