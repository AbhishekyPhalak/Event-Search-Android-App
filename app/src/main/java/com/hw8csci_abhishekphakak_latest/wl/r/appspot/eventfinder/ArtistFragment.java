package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ArtistFragment extends Fragment {


    private static final String TAG = "ArtistFragment";
    RecyclerView recyclerView;

    public ArtistFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();

        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        if (bundle != null) {
            // Retrieve the values from the bundle
            String eventName = bundle.getString("event_name");
            String eventId = bundle.getString("event_id");
            String venueName = bundle.getString("venue_name");

            ProgressBar progressBar = view.findViewById(R.id.progressBar3);
            progressBar.setVisibility(View.VISIBLE);


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url_event = "https://hw8csci-abhishekphakak-latest.wl.r.appspot.com/api/event?event_id="+eventId;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_event, null,
                    response2 -> {
                        try {
                            int flag_music = 0;
                            String type = response2.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                            Log.d(TAG, "string type "+type);
                            if(type.equals("Music")) {
                                flag_music = 1;
                                Log.d(TAG, "onCreateView: its working");
                                ArrayList<JSONObject> artist_DataList = new ArrayList<>();
                                try {
                                    if(response2.getJSONObject("_embedded").has("attractions")){
                                        for (int i = 0; i < response2.getJSONObject("_embedded").getJSONArray("attractions").length(); i++) {
                                            JSONObject jsonObject = response2.getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(i);
                                            artist_DataList.add(jsonObject);
                                        }
                                        Log.d(TAG, "Artist fragment artist Data list:"+artist_DataList);
                                        recyclerView = view.findViewById(R.id.recyclerView_artist);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        SpotifyAdapter spotifyAdapter = new SpotifyAdapter(getActivity(), artist_DataList);
                                        recyclerView.setAdapter(spotifyAdapter);
                                        progressBar.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "Error parsing JSON response", e);
                                }
                            }
                            if(flag_music == 0){
                                LinearLayout linear = view.findViewById(R.id.no_artist_linear_layout);
                                progressBar.setVisibility(View.GONE);
                                linear.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }, error -> Log.e(TAG, "Error in JSON request", error));
            queue.add(jsonObjectRequest);


        }
        return view;
    }
}