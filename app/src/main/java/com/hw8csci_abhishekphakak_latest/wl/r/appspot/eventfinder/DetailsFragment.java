package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;


public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailFragment";



    public DetailsFragment() {
        // Required empty public constructor

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        if (bundle != null) {
            // Retrieve the values from the bundle
            String eventName = bundle.getString("event_name");
            String eventId = bundle.getString("event_id");
            String venueName = bundle.getString("venue_name");

            ProgressBar progressBar = view.findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.VISIBLE);

            CardView cardView = view.findViewById(R.id.artist_cardview);
            cardView.setVisibility(View.GONE);

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url_event = "https://hw8csci-abhishekphakak-latest.wl.r.appspot.com/api/event?event_id="+eventId;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_event, null,
                    response2 -> {
                        try {
                            progressBar.setVisibility(View.GONE);

                            Log.d(TAG, "deatilfragment "+response2);
                            String name = "";
                            for (int i = 0; i < response2.getJSONObject("_embedded").getJSONArray("attractions").length(); i++) {
                                name = name + response2.getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(i).getString("name");
                                name = name + "   ";
                            }
                            String venue = response2.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");

                            String date = response2.getJSONObject("dates").getJSONObject("start").getString("localDate");
                            LocalDate localDate = LocalDate.parse(date);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                            String date2 = localDate.format(formatter);

                            String time = response2.getJSONObject("dates").getJSONObject("start").getString("localTime");
                            LocalTime localTime = LocalTime.parse(time);
                            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("h:mm a");
                            String time2 = localTime.format(formatter2);

                            String genre = "";
                            int flag = 0;
                            if(response2.getJSONArray("classifications").getJSONObject(0).has("segment")){
                                if(!response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name").equals("Undefined")) {
                                    if(flag != 0){
                                        genre = genre + " | ";
                                    }
                                    String segment =  response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                                    genre = genre + segment;
                                    flag = flag + 1;
                                }


                            }
                            if(response2.getJSONArray("classifications").getJSONObject(0).has("genre")){
                                if(!response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name").equals("Undefined")) {
                                    if(flag != 0){
                                        genre = genre + " | ";
                                    }
                                    String genres =  response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
                                    genre = genre + genres;
                                    flag = flag + 1;
                                }

                            }
                            if(response2.getJSONArray("classifications").getJSONObject(0).has("subGenre")){
                                if(!response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").getString("name").equals("Undefined")) {
                                    if(flag != 0){
                                        genre = genre + " | ";
                                    }
                                    String subGenre =  response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").getString("name");
                                    genre = genre + subGenre;
                                    flag = flag + 1;
                                }

                            }
                            if(response2.getJSONArray("classifications").getJSONObject(0).has("type")){
                                if(!response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("type").getString("name").equals("Undefined")) {
                                    if(flag != 0){
                                        genre = genre + " | ";
                                    }
                                    String type =  response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("type").getString("name");
                                    genre = genre + type;
                                    flag = flag + 1;
                                }

                            }
                            if(response2.getJSONArray("classifications").getJSONObject(0).has("subType")){
                                if(!response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("subType").getString("name").equals("Undefined")) {
                                    if(flag != 0){
                                        genre = genre + " | ";
                                    }
                                    String subType =  response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("subType").getString("name");
                                    genre = genre + subType;
                                    flag = flag + 1;
                                }
                            }

                            if(response2.has("priceRanges")){
                                String price = "";
                                int flag2 = 0;

                                if(response2.getJSONArray("priceRanges").getJSONObject(0).has("min")){
                                    price = price + response2.getJSONArray("priceRanges").getJSONObject(0).getString("min");
                                    flag2 = flag2 + 1;
                                }




                                if(response2.getJSONArray("priceRanges").getJSONObject(0).has("max")){
                                    if(flag2==1){
                                        price = price + " - ";
                                    }
                                    price = price + response2.getJSONArray("priceRanges").getJSONObject(0).getString("max");
                                }



                                if(response2.getJSONArray("priceRanges").getJSONObject(0).has("currency")){
                                    if(flag2==1){
                                        price = price + response2.getJSONArray("priceRanges").getJSONObject(0).getString("currency");
                                    }
                                }
                                TextView artist_price = view.findViewById(R.id.artist_price);
                                artist_price.setSingleLine(true);
                                artist_price.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                                artist_price.setMarqueeRepeatLimit(-1);
                                artist_price.post(() -> {artist_price.setSelected(true);});
                                artist_price.setText(price);
                            }
                            else{
                                String price = "Not available";
                                TextView artist_price = view.findViewById(R.id.artist_price);
                                artist_price.setText(price);
                            }


                            String status = "";
                            if(response2.getJSONObject("dates").has("status")){
                                if(response2.getJSONObject("dates").getJSONObject("status").has("code")){
                                    status = response2.getJSONObject("dates").getJSONObject("status").getString("code");
                                }

                            }

                            String url = response2.getString("url");

                            String image_url = response2.getJSONObject("seatmap").getString("staticUrl");

                            TextView artist_name = view.findViewById(R.id.artist_name);
                            artist_name.setSingleLine(true);
                            artist_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            artist_name.setMarqueeRepeatLimit(-1);
                            artist_name.post(() -> {artist_name.setSelected(true);});
                            artist_name.setText(name);

                            TextView artist_venue = view.findViewById(R.id.artist_venue);
                            artist_venue.setSingleLine(true);
                            artist_venue.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            artist_venue.setMarqueeRepeatLimit(-1);
                            artist_venue.post(() -> {artist_venue.setSelected(true);});
                            artist_venue.setText(venue);

                            TextView artist_date = view.findViewById(R.id.artist_date);
                            artist_date.setSingleLine(true);
                            artist_date.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            artist_date.setMarqueeRepeatLimit(-1);
                            artist_date.post(() -> {artist_date.setSelected(true);});
                            artist_date.setText(date2);

                            TextView artist_time = view.findViewById(R.id.artist_time);
                            artist_time.setSingleLine(true);
                            artist_time.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            artist_time.setMarqueeRepeatLimit(-1);
                            artist_time.post(() -> {artist_time.setSelected(true);});
                            artist_time.setText(time2);

                            TextView artist_genre = view.findViewById(R.id.artist_genres);
                            artist_genre.setSingleLine(true);
                            artist_genre.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            artist_genre.setMarqueeRepeatLimit(-1);
                            artist_genre.post(() -> {artist_genre.setSelected(true);});
                            artist_genre.setText(genre);

                            TextView artist_status = view.findViewById(R.id.artist_status);
                            Log.d(TAG, "onCreateView: onsale outside if"+status);
                            if(status.equals("onsale")){
                                artist_status.setText("On Sale");
                                artist_status.setBackgroundResource(R.drawable.button_onsale);
                                Log.d(TAG, "onCreateView: orange button is this");
                            }
                            if(status.equals("rescheduled")){
                                artist_status.setText("Rescheduled");
                                artist_status.setBackgroundResource(R.drawable.button_rescheduled_postponed);
                            }
                            if(status.equals("offsale")){
                                artist_status.setText("Off Sale");
                                artist_status.setBackgroundResource(R.drawable.button_offsale);
                            }
                            if(status.equals("canceled")){
                                artist_status.setText("Canceled");
                                artist_status.setBackgroundResource(R.drawable.button_canceled);
                            }
                            if(status.equals("postponed")){
                                artist_status.setText("Postponed");
                                artist_status.setBackgroundResource(R.drawable.button_rescheduled_postponed);
                            }

                            TextView artist_buy = view.findViewById(R.id.artist_buy);
                            artist_buy.setSingleLine(true);
                            artist_buy.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            artist_buy.setMarqueeRepeatLimit(-1);
                            artist_buy.post(() -> {artist_buy.setSelected(true);});
                            artist_buy.setText(url);
                            artist_buy.setPaintFlags(artist_buy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                            artist_buy.setOnClickListener(view1 -> {
                                String url1 = artist_buy.getText().toString();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url1));
                                startActivity(i);
                            });

                            ImageView artist_image = view.findViewById(R.id.artist_image);
                            Picasso.get().load(image_url).into(artist_image);

                            cardView.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON response", e);
                        }
                    }, error -> Log.e(TAG, "Error in JSON request", error));
            queue.add(jsonObjectRequest);

        }
        return view;
    }


}