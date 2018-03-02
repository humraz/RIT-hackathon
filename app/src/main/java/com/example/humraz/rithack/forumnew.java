package com.example.humraz.rithack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;


public class forumnew extends ActionBarActivity {
    ChatView chatView;
    String fname;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forumm);

        //fname= getIntent().getStringExtra("fnamee");
        Firebase.setAndroidContext(this);
        url="https://hackathon-f1ee8.firebaseio.com/forum/";
        chatView = (ChatView) findViewById(R.id.chat_view);
      read2();


        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
                //update();
                if (flag==0)
                {
                    Toast.makeText(forumnew.this, "You Dont Have Permission To Post", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {


                    Firebase ref = new Firebase(url);
                    forumdata person = new forumdata();


                    person.setPost(chatMessage.getMessage());
                    person.setUser("humraz");
                    ref.push().setValue(person);
                }
                return true;
            }
        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
int flag=0;
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    public void read2()
    {

        final Firebase ref = new Firebase("https://hackathon-f1ee8.firebaseio.com/accidentlocations");
        //Value event listener for realtime data update

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersSnapshot) {

                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    ada a=userSnapshot.getValue(ada.class);
                    final double latitude = Double.parseDouble(a.getLat());
                    final double longitude = Double.parseDouble(a.getLongg());

                    double dist = distance(9.57770528, 76.6213677, latitude, longitude);
                    if (dist>0.7)
                    {
                    flag=0;
                    }
                    else
                    {
                    flag=1;
                    }

                }
                read();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }



/*
    public void update()
    {


        Firebase ref = new Firebase("https://pockeddmo-7e844.firebaseio.com/notif");
        notif person = new notif();
        person.setMsg("New Post In Forum");
        person.setNotif("true");
        ref.push().setValue(person);

    }*/
    public void read()
    {

        final Firebase ref = new Firebase(url);
        //Value event listener for realtime data update

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersSnapshot) {

                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    forumdata user = userSnapshot.getValue(forumdata.class);
String users= user.getUser().toString();
                   String post = user.getPost().toString() + " - " +users;
                    chatView.addMessage(new ChatMessage(post, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }



}
