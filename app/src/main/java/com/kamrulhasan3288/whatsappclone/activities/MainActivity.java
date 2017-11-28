package com.kamrulhasan3288.whatsappclone.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kamrulhasan3288.whatsappclone.R;
import com.kamrulhasan3288.whatsappclone.adepter.ChatListAdepter;
import com.kamrulhasan3288.whatsappclone.infrastructure.Utils;
import com.kamrulhasan3288.whatsappclone.model.InstantMessage;

public class MainActivity extends AppCompatActivity {

    /**
     *--------xml instance-----------
     **/
    private EditText chatMessage;
    private ListView mListView;

    /**
     *-------class instance------------
     **/
    private String displayName;
    private DatabaseReference databaseReference;
    private ChatListAdepter adepter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *-----initialize xml instance------------
         **/
        chatMessage = findViewById(R.id.messageInput);
        mListView = findViewById(R.id.chat_list_view);

        /**
         *-----set tool bar---------
         **/
        setUpDisplayName();
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(displayName);
        }

        /**
         *------set database reference----------
         **/
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.DATABASE_REFERENCE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adepter = new ChatListAdepter(MainActivity.this,databaseReference,displayName);
        mListView.setAdapter(adepter);
        adepter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adepter.cleanUp();
    }

    /**
     *----send the chat message----------
     **/
    public void messageSend(View view){
        String message = chatMessage.getText().toString();
        if (!message.equals("")){
            InstantMessage chat = new InstantMessage(message,displayName);
            databaseReference.child("messages").push().setValue(chat);
            chatMessage.setText("");
        }
    }

    /**
     *------get display name--------------
     **/
    public void setUpDisplayName(){
        SharedPreferences preferences = getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
        displayName = preferences.getString(Utils.USER_NAME,"Anonymous");
    }

}
