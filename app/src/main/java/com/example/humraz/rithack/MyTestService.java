package com.example.humraz.rithack;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by humra on 3/1/2018.
 */
public class MyTestService extends IntentService implements SensorEventListener {
    //public int notificationFlag = 0;

    public MyTestService() {
        super("MyTestService");

    }

    private static final int REQUEST_CODE_PERMISSION = 2;
    String a;
    SensorManager sensorManager;

    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    // GPSTracker class
    GPSTracker gps;
    int count;
    LocationManager locManager;
    LocationListener li;

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
      //  send2("Carefull");
        System.out.println("servicestarted");
        Toast.makeText(this, "Started" ,Toast.LENGTH_LONG).show();
        count = 0;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
//sendNotification("hi bitch");

        class speed implements LocationListener{
            @Override
            public void onLocationChanged(Location loc) {
                Float thespeed=loc.getSpeed();
                //Toast.makeText(MainActivity.this,String.valueOf(thespeed), Toast.LENGTH_LONG).show();
                System.out.println("checker" + thespeed);
                if(thespeed>0.5)
                {
                  //  sendNotification("You Are Going Too Fast! Slow Down!");
                }
            }
            @Override
            public void onProviderDisabled(String arg0) {}
            @Override
            public void onProviderEnabled(String arg0) {}
            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}}
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        li = new speed();

       // locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, li);


    }




    CountDownTimer t;

   final int[] c={0};
public void canc()
{
t.cancel();}
    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / 9.8f;
        float gY = y / 9.8f;
        float gZ = z / 9.8f;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
        System.out.println("hiiiiiiigeforce" +gForce);
        if (gForce>3)
        {

            final Context c = this;
            gps = new GPSTracker(MyTestService.this);

            if (gps.canGetLocation()) {

                MainActivity.notificationFlag =1;
                sensorManager.unregisterListener(MyTestService.this);

         t=  new CountDownTimer(20000, 1000) {




                    public void onTick(long millisUntilFinished) {

                        sendNotification("Should We Send Help??",millisUntilFinished);
                        if (MainActivity.notificationFlag==0) {
                            canc();
                            Toast.makeText(getApplicationContext(), "You Canceled The Operation", Toast.LENGTH_SHORT).show();

                        }
                    }

                    public void onFinish() {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        if(MainActivity.notificationFlag==1) {
                            Toast.makeText(getApplicationContext(), "Help is Being Sent To Your Location\nLat: "
                                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                            if(count==0) {
                                sendnot2();
                                SharedPreferences pref3=getSharedPreferences("number",MODE_PRIVATE);
                                String nu=pref3.getString("no1","0");
                                String nu2 =pref3.getString("no2","0");
                                String nu3 =pref3.getString("no3","0");
                                senddat(latitude,longitude);
                                SmsManager sms = SmsManager.getDefault();
                                String phoneNumber = nu;
                                String lat = Double.toString(latitude);
                                String lng = Double.toString(longitude);
                                count++;
                              //  Spanned Text = Html.fromHtml( "<b>text3:</b>  Text with a " + "<a href=\"http://maps.google.com/?q=" + lat + "," + lng+">link</a> " );
                                String message = "Help I Have Been In an accident, my location is http://maps.google.com/?q=" + lat + "," + lng;
                                sms.sendTextMessage(phoneNumber, null, message, null, null);
                                if (!nu2.equals("0"))
                                    sms.sendTextMessage(nu2, null, message, null, null);
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "You Canceled ", Toast.LENGTH_LONG).show();
                        }
                    }

                }.start();







            } else {

                gps.showSettingsAlert();
            }
            System.out.println("force" + gForce);
        }

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 1;


    private void sendNotification(final String msg,long millisUntilFinished) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
/*        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
              //  mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);*/
                if(MainActivity.notificationFlag==0) {
                return;
                }
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MyTestService.this)
                                .setContentTitle("Are You OK? (" + millisUntilFinished / 1000 +" Seconds left)")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(msg))
                                .setContentText(msg)
                                .setSound(Uri.parse("android.resource://"
                                        + getApplicationContext().getPackageName() + "/" + R.raw.tin))
                                //.setSound(R.raq)
                                .setSmallIcon(R.drawable.question);
                AudioManager am;
                am= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                am.setStreamVolume(AudioManager.STREAM_ALARM, 10,AudioManager.FLAG_PLAY_SOUND);

                mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }


    int f=0;

public void senddat(final double latitude, final double longitude)
{

  //  System.out.println("hiiiiii"+result);
    Firebase.setAndroidContext(this);
   /* Firebase ref = new Firebase("https://hackathon-f1ee8.firebaseio.com/accidentlocations");

    //Getting values to store

    //Creating Person object
    ada sale = new ada();

    //Adding values
    sale.setLat(Double.toString(latitude));
    sale.setLongg(Double.toString(longitude));


    //Storing values to firebase
    ref.push().setValue(sale);*/






    final Firebase ref = new Firebase("https://hackathon-f1ee8.firebaseio.com/accidentlocations");
    //Value event listener for realtime data update


    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot usersSnapshot) {
            if (usersSnapshot.exists()) {
                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {

                    ada ord = userSnapshot.getValue(ada.class);
                    String am= ord.getLat();
                    String ad = ord.getLongg();
                    if (am.equals(Double.toString(latitude))||(ad.equals(Double.toString(longitude))));
                    int a =Integer.parseInt(ord.getCount());
                    f=1;
                    userSnapshot.getRef().child("count").setValue(Integer.toString(a++));
                    }
                }
            }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
        }
    });

if (f==0)
{
    Firebase ref2 = new Firebase("https://hackathon-f1ee8.firebaseio.com/accidentlocations");

    //Getting values to store

    //Creating Person object
    ada sale = new ada();

    //Adding values
    sale.setLat(Double.toString(latitude));
    sale.setLongg(Double.toString(longitude));
    sale.setCount("0");


    //Storing values to firebase
    ref2.push().setValue(sale);
}




}

public void  sendnot2()
{

    final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(MyTestService.this)
                    .setContentTitle("Help is being sent")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Sit tight, Authorities have also been alerted"))
                    .setContentText("Sit tight, Authorities have also been alerted")
                    .setSound(Uri.parse("android.resource://"
                            + getApplicationContext().getPackageName() + "/" + R.raw.tin))
                    //.setSound(R.raq)
                    .setSmallIcon(R.drawable.question);
    mBuilder.setContentIntent(contentIntent);
    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


}


















/*

          public void onFinish() {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(), "Help is Being Sent To Your Location\nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MyTestService.this)
                                .setContentTitle("Help is being sent")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText("Sit tight, Authorities have also been alerted"))
                                .setContentText("Sit tight, Authorities have also been alerted")
                                .setSound(Uri.parse("android.resource://"
                                        + getApplicationContext().getPackageName() + "/" + R.raw.tin))
                                //.setSound(R.raq)
                                .setSmallIcon(R.drawable.question);
                        // sendlocat(latitude, longitude);
                        SharedPreferences pref3=getSharedPreferences("number",MODE_PRIVATE);
                        String nu=pref3.getString("no1","0");
                        SmsManager sms = SmsManager.getDefault();
                        String phoneNumber = nu;
                        String lat = Double.toString(latitude);
                        String lng = Double.toString(longitude);
                        count++;
                        String message = "http://maps.google.com/?q=" + lat + "," + lng;
                        sms.sendTextMessage(phoneNumber, null, message, null, null);
             //   mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

     //       }
     //   }.start();





    }
*/



}
