package com.kamrulhasan3288.whatsappclone.adepter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.kamrulhasan3288.whatsappclone.R;
import com.kamrulhasan3288.whatsappclone.model.InstantMessage;

import java.util.ArrayList;

/**
 * Created by kamrulhasan on 11/28/17.
 */

public class ChatListAdepter extends BaseAdapter{

    private Activity activity;
    private DatabaseReference databaseReference;
    private ArrayList<DataSnapshot> dataSnapShotList;
    private String displayName;


    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            dataSnapShotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdepter(Activity activity, DatabaseReference databaseReference,String displayName) {
        this.activity = activity;
        this.databaseReference = databaseReference.child("messages");
        this.databaseReference.addChildEventListener(childEventListener);
        this.displayName = displayName;
        dataSnapShotList = new ArrayList<>();
    }

    public class ViewHolder{
        private TextView authorName;
        private TextView message;
        private LinearLayout.LayoutParams mParams;
    }

    @Override
    public int getCount() {
        return dataSnapShotList.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot = dataSnapShotList.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row,parent,false);

            ViewHolder holder = new ViewHolder();
            holder.authorName = convertView.findViewById(R.id.author_name);
            holder.message = convertView.findViewById(R.id.chat_message);
            holder.mParams = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);
        }

        InstantMessage chatMessage = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        boolean isMe = chatMessage.getAuthor().equals(displayName);
        setChatRowApperance(isMe,holder);


        holder.message.setText(chatMessage.getMessage());
        holder.authorName.setText(chatMessage.getAuthor());
        return convertView;
    }

    private void setChatRowApperance(boolean isItMe,ViewHolder holder){
        if (isItMe){
            holder.mParams.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.message.setBackgroundResource(R.drawable.bubble2);
        }else {
            holder.mParams.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.message.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.mParams);
        holder.message.setLayoutParams(holder.mParams);
    }

    public void cleanUp(){
        databaseReference.removeEventListener(childEventListener);
    }
}
