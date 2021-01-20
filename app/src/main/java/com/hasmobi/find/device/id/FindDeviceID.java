package com.hasmobi.find.device.id;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class FindDeviceID extends Activity {

    String deviceId;
    Button btLocation;
    TextView textView1, textView2, textView3, textView4, textView5;
    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_find_device_id);
        deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        TextView tv = findViewById(R.id.idholder);
        tv.setText(deviceId);


        btLocation = findViewById(R.id.bt_location);
        textView1 = findViewById(R.id.text_view1);
        textView2 = findViewById(R.id.text_view2);
        textView3 = findViewById(R.id.text_view3);
        textView4 = findViewById(R.id.text_view4);
        textView5 = findViewById(R.id.text_view5);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(FindDeviceID.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    getLocation1();
                }else{
                    ActivityCompat.requestPermissions(FindDeviceID.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

    }

    private void getLocation1(){
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null){
                    Geocoder geocoder = new Geocoder(FindDeviceID.this, Locale.getDefault());

                    try{
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                        textView1.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude :</b></font>" + addresses.get(0).getLatitude()));

                        textView2.setText(Html.fromHtml("<font color='#6200EE'><b>Longitude :</b></font>" + addresses.get(0).getLongitude()));

                        textView3.setText(Html.fromHtml("<font color='#6200EE'><b>Country Name :</b></font>" + addresses.get(0).getCountryName()));

                        textView4.setText(Html.fromHtml("<font color='#6200EE'><b>Locality :</b></font>" + addresses.get(0).getLocality()));

                        textView5.setText(Html.fromHtml("<font color='#6200EE'><b>Address :</b></font>" + addresses.get(0).getAddressLine(0)));

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void emailHandler(View v) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getResources().getString(R.string.emailSubject));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, deviceId);
        startActivity(emailIntent);
    }
}
