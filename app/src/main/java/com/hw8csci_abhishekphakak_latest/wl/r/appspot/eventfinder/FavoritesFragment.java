package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class FavoritesFragment extends Fragment {


    private static final String TAG = "FavoritesFragment";
    private RecyclerView recyclerView;
    private ImageView imageView;

    private  LinearLayout linearLayout;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_fav);
        imageView = view.findViewById(R.id.fav_image);
        linearLayout = view.findViewById(R.id.no_fav_linear_layout);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPref.getAll();

        updateViewVisibility();

        ArrayList<JSONObject> eventJsonObjects = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String eventJsonString = entry.getValue().toString();
            try {
                JSONObject eventJsonObject = new JSONObject(eventJsonString);
                eventJsonObjects.add(eventJsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        FavoritesAdapter favoritesAdapterAdapter = new FavoritesAdapter(this, getActivity(),eventJsonObjects, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(favoritesAdapterAdapter);



        Log.d(TAG, String.format("onResume: array-list %s", eventJsonObjects));

        Log.d(TAG, "onResume: Chal toh raha hai");
        Log.d(TAG, "onResume: eventjsonstring"+allEntries);
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String eventJsonString = entry.getValue().toString();
            Log.d(TAG, "onResume: eventjsonstring"+eventJsonString);
            Log.d(TAG, "onResume: Chal toh raha hai");
            try {
                JSONObject eventObject = new JSONObject(eventJsonString);
                Log.d(TAG, "onResume: jsonobject"+ eventObject);
                String eventName = eventObject.getString("event_name");
                String venue = eventObject.getString("event_venue");
                String segment = eventObject.getString("segment");
                String date = eventObject.getString("event_date");
                String time = eventObject.getString("event_time");
                String event_ids =  eventObject.getString("event_ids");


                Log.d(TAG, "-----------------------------");
                Log.d(TAG, "Event Name: " + eventName);
                Log.d(TAG, "Venue: " + venue);
                Log.d(TAG, "Segment: " + segment);
                Log.d(TAG, "Date: " + date);
                Log.d(TAG, "Time: " + time);
                Log.d(TAG, "Event-id: " + event_ids);
                Log.d(TAG, "-----------------------------");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Delete the item from SharedPreferences
                String event_id = null;
                try {
                    event_id = eventJsonObjects.get(position).getString("event_ids");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(event_id);
                editor.apply();

                // Show a Snackbar
                Snackbar snackbar = Snackbar.make(recyclerView, "Event removed from favorites", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                snackbarText.setTextColor(Color.BLACK);
                snackbarView.setBackgroundResource(R.drawable.button2);
                snackbar.show();

                eventJsonObjects.remove(position);
                recyclerView.getAdapter().notifyItemRemoved(position);

                updateViewVisibility();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

    public void updateViewVisibility() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPref.getAll();

        if (allEntries.isEmpty()) {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}