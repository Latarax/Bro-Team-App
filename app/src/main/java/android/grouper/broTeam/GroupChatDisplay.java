package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupChatDisplay extends AppCompatActivity {

    BottomNavigationView navigation;
    String mGroupId;
    String chatID;
    String mUsername;
    String mUserId;
    ArrayList<MessageModel> models = new ArrayList<>();
    RecyclerView mRecyclerView;
    MessageCardAdapter myAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    EditText chatTextEntry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_display);
        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("iGroupId");
        mUserId = intent.getStringExtra("iUserId");
        mRecyclerView = findViewById(R.id.ChatRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MessageCardAdapter(this, models);
        mRecyclerView.setAdapter(myAdapter);
        displayMessages();
        final Button sendMessage = findViewById(R.id.chatEntryButton);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        getSupportActionBar().setTitle("Group Chat");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_group_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // THIS IS FOR THE BOTTOM NAV VIEW DO NOT TOUCH UNLESS KNOW WHAT DOING
        navigation = findViewById(R.id.bottomNavView);
        navigation.getMenu().getItem(3).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.groupTasksItem:
                        Intent a = new Intent(GroupChatDisplay.this, GroupTaskDisplay.class);
                        a.putExtra("iGroupId", mGroupId);
                        item.setChecked(true);
                        startActivity(a);
                        break;
                    case R.id.groupUsersItem:
                        Intent b = new Intent(GroupChatDisplay.this, GroupUsersDisplay.class);
                        b.putExtra("iGroupId", mGroupId);
                        item.setChecked(true);
                        startActivity(b);
                        break;
                    case R.id.groupNavItem:
                        Intent c = new Intent(GroupChatDisplay.this, GroupWaypointDisplay.class);
                        c.putExtra("iGroupId", mGroupId);
                        item.setChecked(true);
                        startActivity(c);
                        break;
                    case R.id.groupChatItem:
                        // CURRENT DISPLAY
                        break;
                }
                return false;
            }
        });
    }



    private void makeCard(String userName, String messageText) {
        MessageModel m = new MessageModel();
        m.setMessageUserName(userName);
        m.setMessageText(messageText);
        models.add(m);
        myAdapter.notifyDataSetChanged();
    }

    private void sendMessage() {
        Log.d("uname outside", mUsername);
        chatTextEntry = findViewById(R.id.chatEntryLine);
        String messageText = chatTextEntry.getText().toString();
        DocumentReference chatData = db.collection("chats").document(chatID);
        Map<String, Object> data = new HashMap<>();
        data.put("content", messageText);
        data.put("username", mUsername);
        chatData.update("messages", FieldValue.arrayUnion(data));
    }

    private void displayMessages() {
        String currUser = currentUser.getUid();
        DocumentReference userData = db.collection("usersList").document(currUser);
        userData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot info = task.getResult();
                    mUsername = info.get("Username").toString();
                    Log.d("Legit Username", mUsername);
                    DocumentReference groupData = db.collection("groupsList").document(mGroupId);
                    groupData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot docInfo = task.getResult();
                            Log.d("GroupID", mGroupId);
                            chatID = docInfo.get("chatID").toString();
                            Log.d("chatID:", chatID);
                            DocumentReference chatData = db.collection("chats").document(chatID);
                            /*chatData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()  {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot docInfo = task.getResult();
                                        ArrayList<Map<String, Object>> messages = (ArrayList<Map<String, Object>>) docInfo.get("messages");
                                        Log.d("chat stuff", messages.toString());
                                        for (int i = 0; i < messages.size(); i++) {
                                            Map<String, Object> messageInfo = messages.get(i);
                                            Log.d("individual", messageInfo.toString());
                                            String messageText = messageInfo.get("content").toString();
                                            Log.d("content", messageText);
                                            String userName = messageInfo.get("uid").toString();
                                            makeCard(userName, messageText);
                                        }

                             */
                            /*
                            final String TAG = "bunz";
                            db.collection("chats")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot snapshots,
                                                            @Nullable FirebaseFirestoreException e) {
                                            if (e != null) {
                                                Log.w(TAG, "listen:error", e);
                                                return;
                                            }

                                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                                switch (dc.getType()) {
                                                    case ADDED:
                                                        int listSize;
                                                        Map<String, Object> messagesList = dc.getDocument().getData();
                                                        Log.d(TAG, "New city: " + messagesList);
                                                        Log.d("werk", messagesList.get("messages").toString());
                                                        ArrayList<Object> messagesList2 = (ArrayList<Object>) messagesList.get("messages");
                                                        listSize = messagesList2.size();
                                                        for (int i = 0; i < messagesList2.size(); i++) {
                                                            Log.d("werkkk", messagesList2.get(i).toString());
                                                            Map<String, Object> message = (Map<String, Object>) messagesList2.get(i);
                                                            Log.d("yuppp", message.get("content").toString());
                                                            String content = message.get("content").toString();
                                                            String username = message.get("username").toString();
                                                            makeCard(username, content);
                                                        }
                                                        break;
                                                    case MODIFIED:
                                                        Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                                        messagesList = dc.getDocument().getData();
                                                        Log.d(TAG, "New city: " + messagesList);
                                                        Log.d("werk", messagesList.get("messages").toString());
                                                        messagesList2 = (ArrayList<Object>) messagesList.get("messages");
                                                        models.clear();
                                                        for (int i = 0; i < messagesList2.size(); i++) {
                                                            Log.d("werkkk", messagesList2.get(i).toString());
                                                            Map<String, Object> message = (Map<String, Object>) messagesList2.get(i);
                                                            Log.d("yuppp", message.get("content").toString());
                                                            String content = message.get("content").toString();
                                                            String username = message.get("username").toString();
                                                            makeCard(username, content);
                                                        }
                                                        break;
                                                    case REMOVED:
                                                        Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                                        break;
                                                }
                                            }

                                        }


                                    });

                             */
                                        final DocumentReference docRef = db.collection("chats").document(chatID);
                                        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                                @Nullable FirebaseFirestoreException e) {
                                                if (e != null) {
                                                    Log.w("Meh", "Listen failed.", e);
                                                    return;
                                                }

                                                if (snapshot != null && snapshot.exists()) {
                                                    Log.d("Snapshot", ""+snapshot);
                                                    models.clear();
                                                    ArrayList<Map<String, Object>> messages = (ArrayList<Map<String, Object>>) snapshot.get("messages");
                                                    for (int i = 0; i < messages.size(); i++) {
                                                        Map<String, Object> message = messages.get(i);
                                                        String messageText = message.get("content").toString();
                                                        Log.d("content", messageText);
                                                        String userName = message.get("username").toString();
                                                        makeCard(userName, messageText);
                                                    }
                                                } else {
                                                    Log.d("Eh", "Current data: null");
                                                }
                                            }
                                        });

                        }
                    }
                    });
                }
            }
        });
    }

}
