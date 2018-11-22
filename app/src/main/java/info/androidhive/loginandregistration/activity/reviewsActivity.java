package info.androidhive.loginandregistration.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.helper.Review;

public class reviewsActivity extends AppCompatActivity {

    ArrayList<Review> reviewsArrayList;
    ListView reviewsList;
    ReviewAdapter reviewAdapter;
    public static final String Key_Landmark_name = "Landmark_name";
    public static final String Key_user_name = "user_name";
    public static final String Key_the_user_review = "the_user_review";
    private static final String TAG = DetailActivity.class.getSimpleName();
    String url = AppConfig.URL_UPLOAD_REVIEW;

    EditText reviewText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        reviewText = findViewById(R.id.editText);

        reviewsArrayList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this,reviewsArrayList);

        Intent i = getIntent();
        final String Name = i.getStringExtra("Name");
        final String owner = i.getStringExtra("username");

        final TextView header = findViewById(R.id.Header);
        header.setText("People talk about " + Name + " :");

        reviewsList = findViewById(R.id.listView);
        // Load all reviews written to the specific landmark
        loadReviews(Name);

        ImageButton addReviewButton= findViewById(R.id.addReview);
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = reviewText.getText().toString();
                if(reviewText.equals(null) ||
                        reviewText.getText() == null || tmp.equals("")) {
                    Toast.makeText(getApplicationContext(),"Please, Write Review First!",Toast.LENGTH_LONG).show();
                }else{
                    Log.i("Landmark",Name);
                    Log.i("Owner",owner);
                    Log.i("ReviewText",reviewText.getText().toString());
                    uploadReview(Name, owner, reviewText.getText().toString());
                    onBackPressed();
                    //reviewsList.setAdapter(null);
                    //for(int i=0;i<reviewsArrayList.size();i++){
                    //    reviewsArrayList.remove(i);
                    //}
                    //Log.i("Size",reviewsArrayList.size()+"");
                    //loadReviews(Name);
                    //reviewAdapter.notifyDataSetChanged();
                    //reviewsList.setAdapter(reviewAdapter);
                }
            }
        });
    }

    private void uploadReview(final String Landmark_name,final String user_name,final String the_user_review){

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "UploadReview Response: " + response);
                Toast.makeText(reviewsActivity.this,"Submitted Successfully", Toast.LENGTH_SHORT).show();
                reviewText.setText("");
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Key_Landmark_name, Landmark_name);
                params.put(Key_user_name, user_name);
                params.put(Key_the_user_review, the_user_review);
                return params;
            }


        };

        Volley.newRequestQueue(this).add(strReq);

    }

    private void loadReviews(final String name) {
        String url = AppConfig.URL_LOAD_REVIEWS;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject lmObj = jsonArray.getJSONObject(i);
                        String owner = lmObj.getString("user_name");
                        String review = lmObj.getString("the_user_review");
                        Review r = new Review(review, owner);
                        reviewsArrayList.add(r);
                    }
                    reviewsList.setAdapter(reviewAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(reviewsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put(Key_Landmark_name, name);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    class ReviewAdapter extends ArrayAdapter<Review> {

        public ReviewAdapter(Context context, ArrayList<Review> reviewList) {
            super(context, 0, reviewsArrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Review review = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_layout, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.textViewTitle);
            TextView tvHome = (TextView) convertView.findViewById(R.id.textViewShortDesc);
            TextView ratingTextView = (TextView) convertView.findViewById(R.id.textViewRating);
            ImageView logo = (ImageView) convertView.findViewById(R.id.imageView);
            // Populate the data into the template view using the data object
            tvName.setText(review.getReviewText());
            tvHome.setText(review.getOwner());
            // Return the completed view to render on screen
            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }
}