package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity2_event_details_tab extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter2 viewPagerAdapter;

    private static String event_id_1 = "";
    private static final String TAG = "MainActivityFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_event_details_tab);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);


//        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the home button to a custom drawable with a green arrow
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

            // Enable the home button to act as a back button
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String eventName = getIntent().getStringExtra("event_name");
        String eventId = getIntent().getStringExtra("event_id");
        event_id_1 = eventId;
        String venueName = getIntent().getStringExtra("venue_name");
        setTitle(eventName);


        RequestQueue queue = Volley.newRequestQueue(MainActivity2_event_details_tab.this);
        String url_event = "https://hw8csci-abhishekphakak-latest.wl.r.appspot.com/api/event?event_id="+eventId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_event, null,
                response2 -> {
                    try {
                        String url_share = response2.getString("url");
                        String event_id = response2.getString("id");

                        String venue = response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");

                        String date = response2.getJSONObject("dates").getJSONObject("start").getString("localDate");
                        LocalDate localDate = null;
                        String date3 = "";
                        String time3 = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            localDate = LocalDate.parse(date);
                        }
                        DateTimeFormatter formatter = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                        }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            date3 = localDate.format(formatter);
                        }

                        String time = response2.getJSONObject("dates").getJSONObject("start").getString("localTime");
                        LocalTime localTime = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            localTime = LocalTime.parse(time);
                        }
                        DateTimeFormatter formatter2 = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formatter2 = DateTimeFormatter.ofPattern("h:mm a");
                        }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            time3 = localTime.format(formatter2);
                        }

                        String segment = response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");

                        String url = response2.getJSONArray("images").getJSONObject(0).getString("url");

                        JSONObject eventObject = new JSONObject();
                        try {
                            eventObject.put("event_name", eventName);
                            eventObject.put("event_venue", venue);
                            eventObject.put("segment", segment);
                            eventObject.put("event_date", date3);
                            eventObject.put("event_time", time3);
                            eventObject.put("image", url);
                            eventObject.put("event_ids",event_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        ImageView facebookview = toolbar.findViewById(R.id.facebook);
                        facebookview.setOnClickListener(view1 -> {
                            String url1 = "https://www.facebook.com/sharer/sharer.php?u="+url_share;
                            String replacedUrl = url1.replaceAll("\\|", "");
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(replacedUrl));
                            startActivity(i);
                        });
                        ImageView twitterview = toolbar.findViewById(R.id.twitter);
                        twitterview.setOnClickListener(view1 -> {
                            String url2 = "https://twitter.com/intent/tweet?text=Check%20"+eventName+"%20on%20Ticketmaster:%20"+url_share;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url2));
                            startActivity(i);
                        });

                        ImageView like_btn = toolbar.findViewById(R.id.likeee);


                        like_btn.setOnClickListener(view1 -> {
                            Drawable currentDrawable = like_btn.getDrawable();
                            if (currentDrawable != null && currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.heart_filled).getConstantState())) {
                                like_btn.setImageResource(R.drawable.heart_outline);
                                SharedPreferences sharedPref = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.remove(event_id);
                                editor.apply();

                                Snackbar snackbar = Snackbar.make(view1, eventName+" removed from favorites", Snackbar.LENGTH_LONG);
                                View snackbarView = snackbar.getView();
                                TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                                snackbarText.setTextColor(Color.BLACK);
                                snackbarView.setBackgroundResource(R.drawable.button2);
                                snackbar.show();
                            } else {
                                like_btn.setImageResource(R.drawable.heart_filled);
                                Log.d(TAG, "onCreate: clicked on the like button so shared preferenceshould be working");
                                SharedPreferences sharedPref = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(event_id, eventObject.toString());
                                editor.apply();

                                Snackbar snackbar = Snackbar.make(view1, eventName+" added to favorites", Snackbar.LENGTH_LONG);
                                View snackbarView = snackbar.getView();
                                TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                                snackbarText.setTextColor(Color.BLACK);
                                snackbarView.setBackgroundResource(R.drawable.button2);
                                snackbar.show();
                            }
                        });

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                    }
                }, error -> Log.e(TAG, "Error in JSON request", error));
        queue.add(jsonObjectRequest);



        TextView titleTextView = toolbar.findViewById(R.id.title);
        titleTextView.setText(eventName);
        titleTextView.setSingleLine(true);
        titleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleTextView.setMarqueeRepeatLimit(-1);
        titleTextView.post(() -> {titleTextView.setSelected(true);});


        Bundle bundle = new Bundle();
        bundle.putString("event_name", eventName);
        bundle.putString("event_id", eventId);
        bundle.putString("venue_name", venueName);

        tabLayout = findViewById(R.id.Tab_Layout);
        viewPager2 = findViewById(R.id.Viewpager);

        FragmentManager fm = getSupportFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter2(fm, getLifecycle(), bundle);
        viewPager2.setAdapter(viewPagerAdapter);

        TabLayout.Tab tabDetails = tabLayout.newTab();
        tabDetails.setText("DETAILS");
        tabDetails.setIcon(R.drawable.info_icon);
        tabDetails.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green2), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabDetails);

        TabLayout.Tab tabArtist = tabLayout.newTab();
        tabArtist.setText("ARTIST (S)");
        tabArtist.setIcon(R.drawable.artist_icon);
        tabArtist.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabArtist);

        TabLayout.Tab tabVenue = tabLayout.newTab();
        tabVenue.setText("VENUE");
        tabVenue.setIcon(R.drawable.venue_icon);
        tabVenue.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabVenue);

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.green2));
        tabLayout.setTabTextColors(Color.WHITE, ContextCompat.getColor(this, R.color.green2));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green2), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = findViewById(R.id.tool);
        ImageView like_btn = toolbar.findViewById(R.id.likeee);
        SharedPreferences sharedPref2 = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        if (sharedPref2.contains(event_id_1)) {
            like_btn.setImageResource(R.drawable.heart_filled);
        } else {
            like_btn.setImageResource(R.drawable.heart_outline);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        // Handle the back button
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}