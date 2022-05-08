package com.example.forecastapp;

import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.Equalizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.android.volley.VolleyLog.TAG;

public class MainActivity extends AppCompatActivity {
    static ArrayList<modelclass> weatherarray = new ArrayList<>();
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    ProgressBar pb;
    RelativeLayout ry;
    double lan, lon;
    private RecyclerView lv;
    //private Adapter ad;
    //int Requestcode = 100;
    Adapterclass adapterclass;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        lv = findViewById(R.id.rc);
        pb=findViewById(R.id.pb);
        ry=findViewById(R.id.home);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i("hi", "onCreate: permission");
            getlastlocation();
        } else {
            asklocationpermission();

        }

        lv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapterclass = new Adapterclass(this, weatherarray);
        lv.setAdapter(adapterclass);

    }
    private void asklocationpermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "asklocation permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                gps();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void gps() {

            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Enable Gps").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }).setNegativeButton("No ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            final  AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("hello", "onRequestPermissionsResult: ");
                getlastlocation();
            } else {
                asklocationpermission();
                Log.i("hello", "onRequestPermissionsResult22: ");
            }
        }
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getlastlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.i("datalo", "getlastlocation: ");
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
    locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if(location!=null){

                Log.i("datalo", "onSuccess: "+location.getLongitude());

                Log.i("datalo", "onSuccess: "+location.getLatitude());
              //  Toast.makeText(MainActivity.this, "data"+location.getLongitude(), Toast.LENGTH_SHORT).show();

              //  Toast.makeText(MainActivity.this, "data"+location.getLatitude(), Toast.LENGTH_SHORT).show();
                getdata(location.getLongitude(),location.getLatitude());
            }
        }
    });
    locationTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

        }
    });


    }

    private void getdata(double lon,double lati) {
        Log.i("nnnn", "getdata: data");
        String urll = "https://api.openweathermap.org/data/2.5/onecall?lat="+lati+"&lon="+lon+"&exclude=hourly,minutely,current&appid=15c9ce189460866c7b8135ef41c6f3e5";
       // String urll="https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=hourly,minutely,current&appid=15c9ce189460866c7b8135ef41c6f3e5";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urll, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pb.setVisibility(View.GONE);
                ry.setVisibility(View.VISIBLE);
                try {
                    weatherarray.clear();
                    String timezone = response.getString("timezone");
                    JSONArray da = response.getJSONArray("daily");
                    //  int size=da.length();

                    for (int i = 0; i < da.length()-1; i++) {
                        Long date =  da.getJSONObject(i).getLong("dt");
                        Double temp =  da.getJSONObject(i).getJSONObject("temp").getDouble("day");
                        String wheather = da.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                        String icon = da.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                        Log.i("daily", "onResponse: " + date);
                        Log.i("daily", "onResponse: " + temp);
                        Log.i("daily", "onResponse: " + wheather);
                        Log.i("daily", "onResponse: " + icon);
                        weatherarray.add(new modelclass(icon, timezone, date, wheather, temp));

                    }
                    Log.i("daily", "onCreate: " + weatherarray);
                    adapterclass.notifyDataSetChanged();

                    Log.i("daily", "onCreate: " + weatherarray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG);
            }
        });
        queue.add(request);
        Log.i("daily", "onCreate: " + weatherarray);
    }


}


