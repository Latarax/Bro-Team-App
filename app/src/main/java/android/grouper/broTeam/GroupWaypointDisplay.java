package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupWaypointDisplay extends FragmentActivity implements OnMapReadyCallback {

    private String TAG = "GroupWaypointDisplay";
    private GoogleMap mMap;
    BottomNavigationView navigation;
    String mGroupId;

    private class Place
    {
        private String taskName;
        private String placeName;
        private String ID;
        private double lat;
        private double lng;

        private Place(String taskName, String placeName, String ID, double lat, double lng) {
            this.taskName = taskName;
            this.placeName = placeName;
            this.ID = ID;
            this.lat = lat;
            this.lng = lng;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String name) {
            this.placeName = name;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }
    }

    private List placeList = new ArrayList<Place>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_waypoint_display);
        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("iGroupId");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // Get all place data
        getPlaceData ();

        // THIS IS FOR THE BOTTOM NAV VIEW DO NOT TOUCH UNLESS KNOW WHAT DOING
        navigation = findViewById(R.id.bottomNavView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.groupTasksItem:
                        Intent a = new Intent(GroupWaypointDisplay.this, GroupTaskDisplay.class);
                        a.putExtra("iGroupId", mGroupId);
                        startActivity(a);
                        break;
                    case R.id.groupUsersItem:
                        Intent b = new Intent(GroupWaypointDisplay.this, GroupUsersDisplay.class);
                        b.putExtra("iGroupId", mGroupId);
                        startActivity(b);
                        break;
                    case R.id.groupNavItem:
                        // Current Activity
                        break;
                    case R.id.groupChatItem:
                        Intent d = new Intent(GroupWaypointDisplay.this, GroupChatDisplay.class);
                        d.putExtra("iGroupId", mGroupId);
                        startActivity(d);
                        break;
                }
                return false;
            }
        });
    }

    private void getPlaceData() {
        // get user instance and database reference
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // get pointer to user document in database
        CollectionReference taskCollection = database.collection("groupsList/"+mGroupId+"/tasks");
        Log.d("Task Collection", ""+taskCollection.getPath());

        taskCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        Log.d("Query Snapshot", "" + querySnapshot.getDocuments());

                        List<DocumentSnapshot> taskList = querySnapshot.getDocuments();
                        Log.d("List object", "" + taskList);

                        for (DocumentSnapshot documentSnapshot : taskList) {
                            Log.d("List Item", "" + documentSnapshot.getData());
                            Map<String, Object> data = documentSnapshot.getData();

                            // grab user reference in task
                            DocumentReference userRef = (DocumentReference) data.get("assignedUser");
                            String assignedUser = userRef.getId();

                            // if task belongs to user, add task data to list of places
                            if (assignedUser.contentEquals(user.getUid())) {
                                String taskName = data.get("taskName").toString();
                                String placeName = data.get("placeName").toString();
                                String placeID = data.get("placeID").toString();
                                double placeLat = ((double) data.get("placeLat"));
                                double placeLng = ((double) data.get("placeLng"));

                                Log.d(TAG, "Worked: " + placeName);

                                // create new place with data and add to list of user places list
                                Place place = new Place(taskName, placeName, placeID, placeLat, placeLng);
                                placeList.add(place);
                            }
                        }
                    }
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
