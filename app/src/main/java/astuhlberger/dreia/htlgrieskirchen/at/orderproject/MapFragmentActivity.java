package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by fabia_000 on 23.06.2016.
 */
public class MapFragmentActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    GoogleMap map;
    UiSettings settings;
    LocationManager locationManager;
    double longi;
    double lati;
    boolean checkPermission;
    public static final int REQUEST_PERMISSIONS = 1;
    List<String> permissionsNeeded = new ArrayList<>();
    String restaurantname;
    String radius;
    String url;
    ArrayList<Place> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        list = new ArrayList<Place>();

        Intent i = getIntent();
        Bundle params = i.getExtras();
        if (params != null) {
            restaurantname = params.getString("name");
        }
        radius = "15000";


        //if(checkAndRequestPermissions()){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        longi = 0;
        lati = 0;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //}

    }

    private void searchPlacesByName() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        //working URL:
        //https://maps.googleapis.com/maps/api/place/radarsearch/json?location=48,13&radius=5000&types=restaurant|food&key=AIzaSyDLKQfxfhCqM0JOt4Doh4tm5WUAmxTmz0o
        //getting details from placeID
        //https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJXy2MCxcqdEcRuFPxPPt8FNg&key=AIzaSyDLKQfxfhCqM0JOt4Doh4tm5WUAmxTmz0o


        url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + loc.getLatitude() + "," + loc.getLongitude() + "&radius=" + radius + "&types=restaurant" +
                "&key=AIzaSyC1P623gntAIHzT5fTiivCaPLGmfWo14CE";

        getPlaces gP = new getPlaces();
        gP.execute();


    }

    private class getPlaces extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection huc;
            String data;

            try {
                huc = (HttpsURLConnection) new URL(url).openConnection();
                InputStream in = new BufferedInputStream(huc.getInputStream());

                data = readStream(in);

                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; jsonArray.length() > i; i++) {
                    JSONObject one = jsonArray.getJSONObject(i);
                    JSONObject two = one.getJSONObject("geometry");
                    JSONObject three = two.getJSONObject("location");

                    Place p = new Place();
                    p.setName(one.getString("name"));
                    p.setLat(three.getDouble("lat"));
                    p.setLng(three.getDouble("lng"));
                    p.setAddress(one.getString("vicinity"));

                    list.add(p);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            MarkerOptions mO = new MarkerOptions();
            LatLng ll;
            for (Place p1 : list) {
                Log.d("Place", p1.toString());
                mO.title(p1.getName());
                mO.snippet(p1.getAddress());
                ll = new LatLng(p1.getLat(), p1.getLng());
                mO.position(ll);
                map.addMarker(mO);
                Log.d("Filled", "Marker to Map");
            }
        }
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer("");

        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            Log.e("IOE", "IOException");
        }

        return data.toString();
    }

    private boolean checkAndRequestPermissions() {

        checkPermission = false;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkPermission = true;


            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);


        } else {
            Toast.makeText(this, "Location Permission is missing", Toast.LENGTH_LONG).show();
        }


        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), REQUEST_PERMISSIONS);
            return false;
        }


        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        configMap();
        searchPlacesByName();
    }

    private void configMap() {

        settings = map.getUiSettings();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(true);

        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LatLng currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(currentLocation, 10);
        map.moveCamera(camera);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        longi = location.getLongitude();
        lati = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);

                    if (perms.get(permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permission", "location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("Permission", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission.SEND_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(this, permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("SMS and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        }
    }*/


}
