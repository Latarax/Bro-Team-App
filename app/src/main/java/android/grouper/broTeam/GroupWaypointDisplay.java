package android.grouper.broTeam;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

public class GroupWaypointDisplay extends AppCompatActivity implements OnMapReadyCallback {

    private String TAG = "GroupWaypointDisplay";
    private GoogleMap mMap;
    BottomNavigationView navigation;
    String mGroupId;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationProviderClient;
    Boolean mLocationPermissionGranted = true;
    int DEFAULT_ZOOM = 10;


    private class Place {
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

    private ArrayList<Place> placeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_waypoint_display);
        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("iGroupId");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // THIS IS FOR THE BOTTOM NAV VIEW DO NOT TOUCH UNLESS KNOW WHAT DOING
        navigation = findViewById(R.id.bottomNavView);
        navigation.getMenu().getItem(1).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.groupTasksItem:
                        Intent a = new Intent(GroupWaypointDisplay.this, GroupTaskDisplay.class);
                        a.putExtra("iGroupId", mGroupId);
                        item.setChecked(true);
                        startActivity(a);
                        break;
                    case R.id.groupUsersItem:
                        Intent b = new Intent(GroupWaypointDisplay.this, GroupUsersDisplay.class);
                        b.putExtra("iGroupId", mGroupId);
                        item.setChecked(true);
                        startActivity(b);
                        break;
                    case R.id.groupNavItem:
                        // Current Activity
                        break;
                    case R.id.groupChatItem:
                        Intent d = new Intent(GroupWaypointDisplay.this, GroupChatDisplay.class);
                        d.putExtra("iGroupId", mGroupId);
                        item.setChecked(true);
                        startActivity(d);
                        break;
                }
                return false;
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_group_home);
        getSupportActionBar().setTitle("Task Locations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getPlaceData() {
        // get user instance and database reference
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // get pointer to user document in database
        CollectionReference taskCollection = database.collection("groupsList/" + mGroupId + "/tasks");
        Log.d("Task Collection", "" + taskCollection.getPath());

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
                            //Map<String, Object> data = documentSnapshot.getData();

                            // grab user reference in task
                            DocumentReference userRef = (DocumentReference) documentSnapshot.get("assignedUser");
                            String assignedUser = userRef.getId();

                            // if task belongs to user, add task data to list of places
                            if (assignedUser.contentEquals(user.getUid())) {
                                String taskName = documentSnapshot.getString("taskName");
                                String placeName = documentSnapshot.getString("placeName");
                                String placeID = documentSnapshot.getString("placeID");
                                double placeLat = documentSnapshot.getDouble("placeLat");
                                double placeLng = documentSnapshot.getDouble("placeLng");
                                boolean isDone = documentSnapshot.getBoolean("isDone");


                                if (!placeID.isEmpty() && !isDone) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(placeLat, placeLng)).title("Task: " + taskName).snippet("Location: " + placeName));
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = true;

        updateLocationUI();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
            mLocationPermissionGranted = true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        // Get all Tasks assigned to user
        getPlaceData();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastLocation.getLatitude(),
                                            mLastLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
