package com.example.weatherapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FiveDayWeather extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String BaseUrl = "http://api.openweathermap.org/";
    public static String AppId = "";
    Location mLocation;
    TextView weatherData;
    String lat;
    String lon;
    GoogleApiClient mGoogleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    TextView textView;
    RecyclerView recyclerView;


    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000*5;  /* 15 secs */
    private long FASTEST_INTERVAL = 60000*3; /* 5 secs */

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private Toolbar toolbar;

    private final static int ALL_PERMISSIONS_RESULT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_day_weather);
        toolbar=findViewById(R.id.toolbar_forecast);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("5 Days ForeCast");

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Retrofit
        recyclerView = (RecyclerView) findViewById(R.id.recyclerForecast);



    }

    private void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherForecast> call = service.getForecastWeatherData(lat, lon, AppId,"metric");
        call.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(@NonNull Call<WeatherForecast> call, @NonNull Response<WeatherForecast> response) {
                if (response.code() == 200) {
                    WeatherForecast weatherForecast = response.body();
                    assert weatherForecast != null;
                    List<WeatherForecast.List> arrayList=new ArrayList(weatherForecast.list);

                    RecyclerAdapter adapter = new RecyclerAdapter(arrayList,getBaseContext());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherForecast> call, @NonNull Throwable t) {
               // textView.setText(t.getMessage());
            }
        });
    }


    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            //latLng.setText("Please install Google Play services.");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLocation != null) {
            lat = String.valueOf(mLocation.getLatitude());
            lon = String.valueOf(mLocation.getLongitude());
            getCurrentData();

            //latLng.setText("Latitude : "+mLocation.getLatitude()+" , Longitude : "+mLocation.getLongitude());
        }

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            // latLng.setText("Latitude : "+location.getLatitude()+" , Longitude : "+location.getLongitude());
            lat = String.valueOf(mLocation.getLatitude());
            lon = String.valueOf(mLocation.getLongitude());
            getCurrentData();
        }

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else
                finish();

            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }

    private boolean hasPermission(Object permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(FiveDayWeather.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    public void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
}


    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    List<WeatherForecast.List> arrayList;
    private Context context;

        public RecyclerAdapter(List<WeatherForecast.List> arrayList,Context context) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.weatherforcastelayout, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
            Double MinTemp=(arrayList.get(position).main.tempMin);
            Double MaxTemp=(arrayList.get(position).main.tempMax);
            holder.minTemperature.setText((MinTemp.toString())+"° C");
            holder.maxTemperature.setText((MaxTemp.toString())+"° C");
            holder.Humidity.setText(arrayList.get(position).main.humidity.toString().concat(" %"));
            holder.Description.setText(arrayList.get(position).weather.get(0).description);
            holder.WindSpeed.setText((arrayList.get(position).wind.speed.toString()).concat(" kmph"));
            holder.WindDirection.setText(getDirection(arrayList.get(position).wind.deg));
            String url="http://openweathermap.org/img/w/"+arrayList.get(position).weather.get(0).icon+".png";
            long unixSeconds = arrayList.get(position).dt.longValue();
            Date date = new java.util.Date(unixSeconds*1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm a");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+5:30"));
            String formattedDate = sdf.format(date);

            Glide.with(holder.itemView.getContext()).load(url).placeholder(R.drawable.common_google_signin_btn_icon_dark).into(holder.icon);
            holder.Details.setText(formattedDate);
        }


            public String getDirection(Double directionInDegrees){
                String cardinalDirection = null;
                if( (directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
                        (directionInDegrees >= 0) && (directionInDegrees <= 11.25)    ){
                    cardinalDirection = "N";
                } else if( (directionInDegrees >= 11.25 ) && (directionInDegrees <= 33.75)){
                    cardinalDirection = "NNE";
                } else if( (directionInDegrees >= 33.75 ) &&(directionInDegrees <= 56.25)){
                    cardinalDirection = "NE";
                } else if( (directionInDegrees >= 56.25 ) && (directionInDegrees <= 78.75)){
                    cardinalDirection = "ENE";
                } else if( (directionInDegrees >= 78.75 ) && (directionInDegrees <= 101.25) ){
                    cardinalDirection = "E";
                } else if( (directionInDegrees >= 101.25) && (directionInDegrees <= 123.75) ){
                    cardinalDirection = "ESE";
                } else if( (directionInDegrees >= 123.75) && (directionInDegrees <= 146.25) ){
                    cardinalDirection = "SE";
                } else if( (directionInDegrees >= 146.25) && (directionInDegrees <= 168.75) ){
                    cardinalDirection = "SSE";
                } else if( (directionInDegrees >= 168.75) && (directionInDegrees <= 191.25) ){
                    cardinalDirection = "S";
                } else if( (directionInDegrees >= 191.25) && (directionInDegrees <= 213.75) ){
                    cardinalDirection = "SSW";
                } else if( (directionInDegrees >= 213.75) && (directionInDegrees <= 236.25) ){
                    cardinalDirection = "SW";
                } else if( (directionInDegrees >= 236.25) && (directionInDegrees <= 258.75) ){
                    cardinalDirection = "WSW";
                } else if( (directionInDegrees >= 258.75) && (directionInDegrees <= 281.25) ){
                    cardinalDirection = "W";
                } else if( (directionInDegrees >= 281.25) && (directionInDegrees <= 303.75) ){
                    cardinalDirection = "WNW";
                } else if( (directionInDegrees >= 303.75) && (directionInDegrees <= 326.25) ){
                    cardinalDirection = "NW";
                } else if( (directionInDegrees >= 326.25) && (directionInDegrees <= 348.75) ){
                    cardinalDirection = "NNW";
                } else {
                    cardinalDirection = "?";
                }

                return cardinalDirection;
            }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView minTemperature,maxTemperature,Humidity,Description,WindSpeed,WindDirection,Details;

            public ViewHolder(View itemView) {
                super(itemView);
                icon=itemView.findViewById(R.id.weatherIcon);
                minTemperature=itemView.findViewById(R.id.miniTemp);
                maxTemperature=itemView.findViewById(R.id.maxTemp);
                Humidity=itemView.findViewById(R.id.humidity);
                Description=itemView.findViewById(R.id.description);
                WindSpeed=itemView.findViewById(R.id.windSpeed);
                WindDirection=itemView.findViewById(R.id.windDegree);
                Details=itemView.findViewById(R.id.details);


            }
        }
    }
