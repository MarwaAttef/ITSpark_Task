package com.example.marwa.firebaseads.splash;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marwa.firebaseads.R;
import com.example.marwa.firebaseads.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    ImageView img;
    TextView tv;
    SharedPreferences.Editor preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        img = (ImageView) findViewById(R.id.imgLogo);
        tv = (TextView) findViewById(R.id.logo);
        preferences = getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
        preferences.putInt("data", 0);
        preferences.commit();


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        }


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer.
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    //--------------------------------- onRequestPermissionsResult ---------------------------------------------//
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0].equals(Manifest.permission.READ_CONTACTS)) {

                    // permission was granted, do your work....
                    preferences.putInt("data", 1);
                    preferences.commit();
                } else {
                    // permission denied
                    preferences.putInt("data", 0);
                    preferences.commit();
                    //          Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_LONG).show();
                    // Disable the functionality that depends on this permission.
                }
                return;
        }

    }

    //--------------------------------- check And Request Permissions------------------------------------------/
    public static final int MY_PERMISSIONS_REQUEST = 2;

    private boolean checkAndRequestPermissions() {
        int permissionContact = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        int permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        } else {
            // got permission use it
            preferences.putInt("data", 1);
            preferences.commit();
        }

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST);
            return false;
        }
        return true;
    }


}

