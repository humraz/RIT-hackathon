package com.example.humraz.rithack;
/**
 * Created by humra on 3/1/2018.
 */
import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyfishjy.library.RippleBackground;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static int notificationFlag = 0;
    SensorManager sensorManager;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    String mPermission2 = Manifest.permission.ACCESS_COARSE_LOCATION;
    private NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        read();
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);


        if(notificationFlag ==1)
        { final PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
            AudioManager am;
            am= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

Toast.makeText(this, "Operation Cancelled",Toast.LENGTH_LONG).show();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MainActivity.this)
                            .setContentTitle("You Cancelled!")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("False Alarm! Operation Cancelled."))
                            .setContentText("False Alarm! Operation Cancelled.")
                            .setSound(Uri.parse("android.resource://"
                                    + getApplicationContext().getPackageName() + "/" + R.raw.tin))
                            //.setSound(R.raq)
                            .setSmallIcon(R.drawable.question);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            notificationFlag=0;
        }
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission2},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

        // Define a listener that responds to location updates
        // tv = (TextView) findViewById(R.id.a);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        scheduleAlarm();


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

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    public void read()
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

                    }
                    else
                    {
                        Intent in = new Intent(MainActivity.this, MainActivity.class);
                        final PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0,in , 0);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this)   ;
                        mBuilder =
                                new NotificationCompat.Builder(MainActivity.this)
                                        .setContentTitle("Accident Prone Area")
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText("You Are entering an Accident Prone Area at a high speed, go slow."))
                                        .setContentText("Accident Prone Area, slow down please")
                                        .setSound(Uri.parse("android.resource://"
                                                + getApplicationContext().getPackageName() + "/" + R.raw.tin))
                                        //.setSound(R.raq)
                                        .setSmallIcon(R.drawable.question);

                        mBuilder.setContentIntent(contentIntent);
                        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                        // Toast.makeText(MainActivity.this,"Accident Has Been Detected Near You! Be Carefull",Toast.LENGTH_SHORT).show();
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Accident Prone Area")
                                .setContentText("You are Entering An accident prone area!")
                                .setConfirmText("Show On Map?")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("http://maps.google.com/maps?daddr="+latitude+","+longitude));


                                        startActivity(intent);


                                    }
                                })
                                .show();                       break;

                    }

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }



    RippleBackground rippleBackground;
    GPSTracker gps;
    public void soshelpp(View view)
    {
        rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
       /* Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();*/
            // sendNotification(Double.toString((latitude)));
            SharedPreferences pref3=getSharedPreferences("number",MODE_PRIVATE);
            String nu=pref3.getString("no1","0");
            SmsManager sms = SmsManager.getDefault();
            String phoneNumber=nu;
            String lat= Double.toString(latitude);
            String lng= Double.toString(longitude);

            String message="Help Me, I Have Met With An Accident http://maps.google.com/?q="+lat+","+lng;

            ///phone sending message code

            sms.sendTextMessage(phoneNumber, null, message, null, null);
            sensorManager.unregisterListener(this);
            Toast.makeText(this,"Help is being sent to your Location!",Toast.LENGTH_LONG).show();
/*


                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                        + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();*/
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }
    public void calamb(View view)

    {gps = new GPSTracker(MainActivity.this);
        Firebase.setAndroidContext(this);
    Firebase ref = new Firebase("https://hackathon-f1ee8.firebaseio.com/ambulance");
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
    //Getting values to store

    //Creating Person object
    ada sale = new ada();

    //Adding values
    sale.setLat(Double.toString(latitude));
    sale.setLongg(Double.toString(longitude));
        sale.setFlag("1");
        sale.setCount("0");


    //Storing values to firebase
    ref.push().setValue(sale);
        Toast.makeText(this,"An Ambulance has been sent to your Location",Toast.LENGTH_SHORT).show();


    }
    public void police(View view)
    {String a1="100";
        startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + a1)));
    }
    public void ambulance(View view)
    {String a1="108";
        startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + a1)));
    }
    public void fire(View view)
    {String a1="101";
        startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + a1)));
    }
    public void traffic(View view)
    {String a1="1099";
        startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + a1)));
    }
    public void child(View view)
    {String a1="1098";
        startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + a1)));
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void con(View view
    )
    {
        Intent in = new Intent(this, GetContacts.class);
        startActivity(in);
    }
    public void con3(View view
    )
    {
        Intent in = new Intent(this, forumnew.class);
        startActivity(in);
    }
    public void con4(View view
    )
    {
        Intent in = new Intent(this, MapsActivity.class);
        startActivity(in);
    }
    public void con2(View view
    )
    {
        Intent in = new Intent(this, newsact.class);
        startActivity(in);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / 9.8f;
        float gY = y / 9.8f;
        float gZ = z / 9.8f;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
        //   tv.setText(Float.toString(gForce));

    }
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                1, pIntent);

    }
}
