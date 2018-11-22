package info.androidhive.loginandregistration.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;

public class DetailActivity extends AppCompatActivity {


    static String enteredRate;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Michael4","correct");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Log.i("Michael5","correct");
        Intent i = getIntent();
        Bundle b = i.getExtras();
        final String Name = b.getString("Name");
        String Logo = b.getString("Logo");
        final String Phone = b.getString("Phone");
        String Rate = b.getDouble("Rating")+"";
        userName = b.getString("userName");
        String Distance = b.getDouble("Distance")+"";
        final double Lat = b.getDouble("Lat");
        final double Lon = b.getDouble("Lon");
        Log.i("Michael6","correct");
        //Name of the LandMark
        //Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/DroidSansFallback.ttf");
        TextView name = findViewById(R.id.NameTextView);
        //name.setTypeface(tf);
        name.setText(Name);

        //Details
        TextView details = findViewById(R.id.detailstextView);
        details.setText("Rating : "+ Rate+" ");


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Lat,Lon, 1);
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            String location = "";
            if(!address.equals(null)){
                location=address;
            }else {
                if(!city.equals(null)){
                    location=city;
                }else {
                    if(!state.equals(null)){
                        location=state;
                    }else{
                        if(!country.equals(null)){
                            location=country;
                        }else{
                            location="";
                        }
                    }
                }
            }
            if(location.equals("")) {
                details.setText("Rating : "+ Rate);
            }else {
                details.setText("Rating : "+ Rate + " Location : "+ location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Logo
        ImageView logo = findViewById(R.id.imageView2);
        String url = AppConfig.IP_Address + Logo;
        Picasso.get().load(url).resize(80,80).centerInside().into(logo);

        //Phone Number
        TextView phoneText = findViewById(R.id.phoneNumberText);
        phoneText.setText("Phone Number : "+ Phone);

        //Navigation
        ImageButton navigateButton = findViewById(R.id.navigate);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show al map w y draw al Way open MapsActivity
                //Intent iii = new Intent(getApplicationContext(),MapsActivity.class);
                //Bundle b2 = new Bundle();
                //b2.putDouble("Lat",Lat);
                //b2.putDouble("Lon",Lon);
                //iii.putExtras(b2);
                //startActivity(iii);
                String L1 = Lat+"";
                String L2 = Lon+"";
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+L1+","+L2);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        RatingBar mBar = (RatingBar) findViewById(R.id.ratingBar);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar rtBar, float rating,boolean fromUser) {
                double enteredRating = (double) rating;
                enteredRate = enteredRating+"";
                Log.i("Rate is : ",enteredRate);
            }
        });

        //Dial Call
        findViewById(R.id.callButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone(Phone);
            }
        });
        findViewById(R.id.btnLinkToReviewScreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // yft7 activity al reviews
                Intent iii = new Intent(getApplicationContext(),reviewsActivity.class);
                iii.putExtra("Name",Name);
                iii.putExtra("username",userName);
                startActivity(iii);
            }
        });

    }

private void dialContactPhone(final String phoneNumber) {
    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
}

}

