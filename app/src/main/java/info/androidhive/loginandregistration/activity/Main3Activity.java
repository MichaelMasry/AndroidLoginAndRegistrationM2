package info.androidhive.loginandregistration.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.helper.Landmark;
import info.androidhive.loginandregistration.helper.LandmarkAdapter;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Main3Activity extends AppCompatActivity {

    private final String url3 = "http://192.168.43.35/userDB/loadDB.php";
    private ListView listView;
    private ArrayList<Landmark> landmarkList;
    private LandMarkAdapter landMarkAdapter;
    private static LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);



        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        listView = findViewById(R.id.listView);
        landmarkList = new ArrayList<>();
        landMarkAdapter = new LandMarkAdapter(this, landmarkList);
        Intent i = getIntent();
        Bundle b = getIntent().getExtras();
        double lat = b.getDouble("Lat");
        double lon = b.getDouble("Lon");
        currentLocation = new LatLng(lat,lon);
        loadData();
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Go to map view
                Intent ii =  new Intent(getApplicationContext(),MapsActivity.class);
                Bundle b = new Bundle();
                Landmark tempMark = (Landmark) landmarkList.get(position);
                b.putDouble("Lat", tempMark.getLatitude());
                b.putDouble("Lon", tempMark.getLongitude());
                ii.putExtras(b);
                startActivity(ii);
            }
        });
    }


    public void loadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject lmObj = jsonArray.getJSONObject(i);
                        double latitude = Double.parseDouble(lmObj.getString("Latitude"));
                        double longitude = Double.parseDouble(lmObj.getString("Longitude"));
                        double distance = getDistance(currentLocation,new LatLng(
                                latitude,longitude
                        ));
                        String name = lmObj.getString("name");
                        double rate = Double.parseDouble(lmObj.getString("rating"));
                        String image = lmObj.getString("image_path");
                        String phone = lmObj.getString("Phone_Number");
                        Landmark lm = new Landmark(name, distance, rate,image, phone);
                        lm.setLatitude(latitude);
                        lm.setLongitude(longitude);
                        landmarkList.add(lm);
                    }
                    sortByDistance(landmarkList);
                    listView.setAdapter(landMarkAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Main3Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    class LandMarkAdapter extends ArrayAdapter<Landmark> {
        //   private Context context;
        //    private List<Landmark> landmarkList;
        public LandMarkAdapter(Context context, ArrayList<Landmark> landmarkList) {
            super(context, 0, landmarkList);
            //   this.context=context;
            //   this.landmarkList=landmarkList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Landmark user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.textViewTitle);
            TextView tvHome = (TextView) convertView.findViewById(R.id.textViewShortDesc);
            TextView ratingTextView = (TextView) convertView.findViewById(R.id.textViewRating);
            ImageView logo = (ImageView) convertView.findViewById(R.id.imageView);
            // Populate the data into the template view using the data object
            tvName.setText(user.getName());
            tvHome.setText(user.getDistance() + " KM");
            ratingTextView.setText(user.getRate() + "");
            // Adding specific logo
         /*   Resources res = getResources();
            String mDrawableName = user.getImage();
            int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
            Drawable drawable = res.getDrawable(resID );
            logo.setImageDrawable(drawable);*/
            String url = "http://192.168.43.35/" + user.getImage();
            Picasso.get().load(url).
                        resize(80,80).centerInside().into(logo);
            // Return the completed view to render on screen
            return convertView;
        }
    }

    public double getDistance(LatLng l1 , LatLng l2){
        Location loc1 = new Location("");
        loc1.setLatitude(l1.latitude);
        loc1.setLongitude(l1.longitude);
        Location loc2 = new Location("");
        loc2.setLatitude(l2.latitude);
        loc2.setLongitude(l2.longitude);
        double t = loc1.distanceTo(loc2);
        double t2=t/1000;
        return Double.parseDouble(new DecimalFormat("##.##").format(t2));
    }

    // Adding a Button to Action Bar
    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sortByRating) {
            listView.setAdapter(null);
            Collections.sort(landmarkList, new Comparator<Landmark>() {
                @Override
                public int compare(Landmark o1, Landmark o2) {
                    return Double.compare(o2.getRate(),o1.getRate());
                }
            });
            for(int i = 0;i<landmarkList.size();i++){
                System.out.println("-----------------" + landmarkList.get(i).getRate());
            }
            landMarkAdapter.notifyDataSetChanged();
            listView.setAdapter(landMarkAdapter);
            listView.setVisibility(View.VISIBLE);
        }
        if (id == R.id.sortByDistance){
            listView.setAdapter(null);
            sortByDistance(landmarkList);
            landMarkAdapter.notifyDataSetChanged();
            listView.setAdapter(landMarkAdapter);
            listView.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void sortByDistance(ArrayList<Landmark> landmarkList){
        Collections.sort(landmarkList, new Comparator<Landmark>() {
            @Override
            public int compare(Landmark o1, Landmark o2) {
                return Double.compare(o2.getDistance(),o1.getDistance());
            }
        });
    }
}