package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.MapView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class VenueFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mMapView;

    private String venue_lat;
    private String venue_lng;

    private String openHoursDetail = "";

    private String generalRule = "";

    private String childRule = "";

    private static final String TAG = "VenueFragment";


    public VenueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();

        View view = inflater.inflate(R.layout.fragment_venue, container, false);

        if (bundle != null) {
            // Retrieve the values from the bundle
            String eventName = bundle.getString("event_name");
            String eventId = bundle.getString("event_id");
            String venueName = bundle.getString("venue_name");


            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);

            ProgressBar progressBar = view.findViewById(R.id.progressBar5);
            ScrollView scrollView = view.findViewById(R.id.scrollview2);
            scrollView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url_event = "https://hw8csci-abhishekphakak-latest.wl.r.appspot.com/api/venue?venue_name="+venueName;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_event, null,
                    response2 -> {
                        try {
                            Log.d(TAG, "venuefragment - addressline 1 "+response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1"));

                            String city_state = "";

                            String phoneNumberDetail = "";

                            if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("address")){
                                if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").has("line1")){
                                    Log.d(TAG, "onCreateView: hellllllloooo");
                                    String address= response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1");
                                    TextView stadium_address = view.findViewById(R.id.stadium_address);
                                    stadium_address.setSingleLine(true);
                                    stadium_address.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                                    stadium_address.setMarqueeRepeatLimit(-1);
                                    stadium_address.post(() -> {stadium_address.setSelected(true);});
                                    stadium_address.setText(address);

                                    RequestQueue queue2 = Volley.newRequestQueue(getActivity());
                                    String url_get_venue_loc = "https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&key=AIzaSyA5x3gVYXsFMVq3HlxA1_vM4jBxgNYrUa4";
                                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url_get_venue_loc, null,
                                            response3 -> {
                                                try {
                                                    venue_lat = response3.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                                                    venue_lng = response3.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
                                                    LatLng venueLocation = new LatLng(Double.parseDouble(venue_lat), Double.parseDouble(venue_lng));
                                                    mMap.addMarker(new MarkerOptions().position(venueLocation).title(venueName));
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLocation, 15));
                                                } catch (JSONException e) {
                                                    Log.e(TAG, "Error parsing JSON response", e);
                                                }
                                            }, error -> Log.e(TAG, "Error in JSON request", error));
                                    queue2.add(jsonObjectRequest2);
                                }
                            }

                            if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("city")){
                                if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").has("name")){
                                    String city= response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name");
                                    city_state  = city_state + city;
                                }
                            }

                            if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("state")){
                                if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").has("name")){
                                    String state= response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("name");
                                    city_state = city_state + ", ";
                                    city_state  = city_state + state;
                                }
                            }

                            if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("boxOfficeInfo")){
                                if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").has("phoneNumberDetail")){
                                    String contact= response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");
                                    phoneNumberDetail = phoneNumberDetail + contact;
                                }
                            }

                            if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("boxOfficeInfo")){
                                if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").has("openHoursDetail")){
                                    String hour_detail= response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("openHoursDetail");
                                    openHoursDetail = openHoursDetail + hour_detail;
                                }
                            }

                            if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("generalInfo")){
                                if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").has("generalRule")){
                                    String general_rule = response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("generalRule");
                                    generalRule = generalRule + general_rule;
                                    Log.d(TAG, "onCreateView: genrule"+generalRule);
                                }
                            }

                            if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("generalInfo")){
                                if(response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").has("childRule")){
                                    String child_rule = response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("childRule");
                                    childRule = childRule + child_rule;
                                }
                            }



                            TextView venue_name = view.findViewById(R.id.stadium_name);
                            venue_name.setSingleLine(true);
                            venue_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            venue_name.setMarqueeRepeatLimit(-1);
                            venue_name.post(() -> {venue_name.setSelected(true);});
                            venue_name.setText(venueName);

                            TextView stadium_city_state = view.findViewById(R.id.stadium_city);
                            stadium_city_state.setSingleLine(true);
                            stadium_city_state.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            stadium_city_state.setMarqueeRepeatLimit(-1);
                            stadium_city_state.post(() -> {stadium_city_state.setSelected(true);});
                            stadium_city_state.setText(city_state);

                            TextView stadium_contact = view.findViewById(R.id.stadium_contact_info);
                            stadium_contact.setSingleLine(true);
                            stadium_contact.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            stadium_contact.setMarqueeRepeatLimit(-1);
                            stadium_contact.post(() -> {stadium_contact.setSelected(true);});
                            stadium_contact.setText(phoneNumberDetail);

                            TextView stadium_openhours_label = view.findViewById(R.id.openHours_label);
                            TextView stadium_openhours = view.findViewById(R.id.openHours);
                            int flag_yellow_display = 0;
                            if (openHoursDetail != null && !openHoursDetail.isEmpty()) {
                                stadium_openhours_label.setVisibility(View.VISIBLE);
                                stadium_openhours.setVisibility(View.VISIBLE);
                                stadium_openhours.setMaxLines(3);
                                stadium_openhours.setEllipsize(TextUtils.TruncateAt.END);
                                stadium_openhours.setText(openHoursDetail);

                                final boolean[] isExpanded = {false}; // declare as final array of size 1

                                stadium_openhours.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (isExpanded[0]) { // access the array element
                                            stadium_openhours.setMaxLines(3);
                                            stadium_openhours.setEllipsize(TextUtils.TruncateAt.END);
                                            isExpanded[0] = false; // modify the array element
                                        } else {
                                            stadium_openhours.setMaxLines(Integer.MAX_VALUE);
                                            stadium_openhours.setEllipsize(null);
                                            isExpanded[0] = true;
                                        }
                                    }
                                });
                                flag_yellow_display++;
                                CardView cardView = view.findViewById(R.id.hello_yellow_tab);
                                cardView.setVisibility(View.VISIBLE);
                            } else {
                                stadium_openhours_label.setVisibility(View.GONE);
                                stadium_openhours.setVisibility(View.GONE);
                                flag_yellow_display--;
                            }

                            TextView stadium_gen_rule_label = view.findViewById(R.id.generalRule_label);
                            TextView stadium_gen_rule = view.findViewById(R.id.generalRule);
                            int flag_yellow_display_2 = 0;
                            if (!generalRule.isEmpty()) {
                                stadium_gen_rule_label.setVisibility(View.VISIBLE);
                                stadium_gen_rule.setVisibility(View.VISIBLE);
                                stadium_gen_rule.setMaxLines(3);
                                stadium_gen_rule.setEllipsize(TextUtils.TruncateAt.END);
                                stadium_gen_rule.setText(generalRule);

                                final boolean[] isExpanded = {false}; // declare as final array of size 1

                                stadium_gen_rule.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (isExpanded[0]) { // access the array element
                                            stadium_gen_rule.setMaxLines(3);
                                            stadium_gen_rule.setEllipsize(TextUtils.TruncateAt.END);
                                            isExpanded[0] = false; // modify the array element
                                        } else {
                                            stadium_gen_rule.setMaxLines(Integer.MAX_VALUE);
                                            stadium_gen_rule.setEllipsize(null);
                                            isExpanded[0] = true;
                                        }
                                    }
                                });
                                flag_yellow_display_2++;
                                CardView cardView = view.findViewById(R.id.hello_yellow_tab);
                                cardView.setVisibility(View.VISIBLE);
                            } else {
                                stadium_gen_rule_label.setVisibility(View.GONE);
                                stadium_gen_rule.setVisibility(View.GONE);
                                flag_yellow_display_2--;
                            }


                            TextView stadium_child_rule = view.findViewById(R.id.childRule);
                            TextView stadium_child_rule_label = view.findViewById(R.id.childRule_label);
                            int flag_yellow_display_3 = 0;

                            if (childRule != null && !childRule.isEmpty()) {
                                stadium_child_rule_label.setVisibility(View.VISIBLE);
                                stadium_child_rule.setVisibility(View.VISIBLE);
                                stadium_child_rule.setMaxLines(3);
                                stadium_child_rule.setEllipsize(TextUtils.TruncateAt.END);
                                stadium_child_rule.setText(childRule);

                                final boolean[] isExpanded = {false}; // declare as final array of size 1

                                stadium_child_rule.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (isExpanded[0]) { // access the array element
                                            stadium_child_rule.setMaxLines(3);
                                            stadium_child_rule.setEllipsize(TextUtils.TruncateAt.END);
                                            stadium_child_rule.setText(childRule.substring(0, Math.min(childRule.length(), 100)) + "...");
                                            isExpanded[0] = false; // modify the array element
                                        } else {
                                            stadium_child_rule.setMaxLines(Integer.MAX_VALUE);
                                            stadium_child_rule.setEllipsize(null);
                                            stadium_child_rule.setText(childRule);
                                            isExpanded[0] = true;
                                        }
                                    }
                                });

                                flag_yellow_display_3++;
                                CardView cardView = view.findViewById(R.id.hello_yellow_tab);
                                cardView.setVisibility(View.VISIBLE);
                            } else {
                                stadium_child_rule_label.setVisibility(View.GONE);
                                stadium_child_rule.setVisibility(View.GONE);
                                flag_yellow_display_3--;
                            }


                            Log.d(TAG, "onCreateView: flaggggg"+flag_yellow_display);

                            progressBar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);



                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON response", e);
                        }
                    }, error -> Log.e(TAG, "Error in JSON request", error));
            queue.add(jsonObjectRequest);

        }
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (venue_lat != null && !venue_lat.isEmpty() && venue_lng != null && !venue_lng.isEmpty()) {
            LatLng venueLocation = new LatLng(Double.parseDouble(venue_lat), Double.parseDouble(venue_lng));
            mMap.addMarker(new MarkerOptions().position(venueLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLocation, 15));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }

}

