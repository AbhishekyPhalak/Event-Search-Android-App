    package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;

    import android.os.Bundle;

    import android.text.TextUtils;
    import android.widget.ArrayAdapter;
    import android.widget.AutoCompleteTextView;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.ProgressBar;
    import android.widget.Toolbar;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.Fragment;
    import androidx.navigation.Navigation;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toolbar;

    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.JsonObjectRequest;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;

    import org.json.JSONException;
    import org.json.JSONObject;
    import org.json.JSONArray;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.List;


    //Navigate one fragment to another Youtube - KB coder
    public class EventFragment extends Fragment {

        private ImageView back_arrow;
    
        RecyclerView recyclerView;

        private String location2 = "";
        private String keyword2 = "";
        private String category2 = "";
        private String distance2 = "";
        private static final String TAG = "EventFragment";
    

        public EventFragment() {
            // Required empty public constructor
        }
    
    
    
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_event, container, false);



            return view;
        }

        @Override
        public void onResume() {
            super.onResume();

            back_arrow = getView().findViewById(R.id.imageView_back_arrow);
            Bundle bundle = getArguments();

            if (bundle != null){
                location2 = bundle.getString("location");
                keyword2 = bundle.getString("keyword");
                category2 = bundle.getString("category");
                distance2 = bundle.getString("distance");
            }

            Bundle bundle7 = new Bundle();
            bundle7.putString("keyword", keyword2);
            bundle7.putString("distance", distance2);
            bundle7.putString("category", category2);
            bundle7.putString("location", location2);

            back_arrow.setOnClickListener(buttonView -> Navigation.findNavController(getView()).navigate(R.id.action_eventFragment_to_searchFragment, bundle7));

            if (bundle != null){
                String location3 = bundle.getString("location");
                if(location3 != ""){
                    String location = bundle.getString("location");
                    String keyword = bundle.getString("keyword");
                    String encodedKeyword = keyword.replace(" ", "%20");
                    String category = bundle.getString("category");
                    String distance = bundle.getString("distance");


                    ProgressBar progressBar = getView().findViewById(R.id.progressBar);
                    RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
                    LinearLayout linear =getView().findViewById(R.id.no_events_linear_layout);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    progressBar.setVisibility(View.VISIBLE);

                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    RequestQueue queue2 = Volley.newRequestQueue(getActivity());
                    String url_geocoding = "https://maps.googleapis.com/maps/api/geocode/json?address="+location+ "&key=AIzaSyA5x3gVYXsFMVq3HlxA1_vM4jBxgNYrUa4";
                    JsonObjectRequest jsonObjectRequest_event = new JsonObjectRequest(Request.Method.GET, url_geocoding, null,
                            response -> {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    int flag_wrong_loc = 0;

                                    if(response.has("results")){
                                        flag_wrong_loc = 1;
                                        JSONArray resultsArray = response.getJSONArray("results");
                                        JSONObject firstResult = resultsArray.getJSONObject(0);
                                        JSONObject geometry = firstResult.getJSONObject("geometry");
                                        JSONObject location2 = geometry.getJSONObject("location");
                                        double lat = location2.getDouble("lat");
                                        double lng = location2.getDouble("lng");
                                        Log.d(TAG, "Lat: " + lat + ", Lng: " + lng);

                                        String url_event = "https://hw8csci-abhishekphakak-latest.wl.r.appspot.com/api/form?keyword="+encodedKeyword+"&distance="+distance+"&latitude="+lat+"&longitude="+lng+"&selected_cat="+category;
                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_event, null,
                                                response2 -> {
                                                    JSONArray events = null;
                                                    int flaggg = 0;
                                                    try {
                                                        if(response2.has("_embedded")){
                                                            if(response2.getJSONObject("_embedded").has("events")){
                                                                if(response2.getJSONObject("_embedded").getJSONArray("events").length()>0){
                                                                    flaggg = 1;
                                                                    JSONObject embedded = response2.getJSONObject("_embedded");
                                                                    events = embedded.getJSONArray("events");

                                                                    Comparator<JSONObject> eventComparator = new Comparator<JSONObject>() {
                                                                        @Override
                                                                        public int compare(JSONObject event1, JSONObject event2) {
                                                                            try {
                                                                                // Get the local date and time strings from the events
                                                                                String localDate1 = event1.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                                                                String localTime1 = event1.getJSONObject("dates").getJSONObject("start").getString("localTime");
                                                                                String localDate2 = event2.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                                                                String localTime2 = event2.getJSONObject("dates").getJSONObject("start").getString("localTime");

                                                                                // Concatenate the local date and time strings for each event
                                                                                String dateTime1 = localDate1 + " " + localTime1;
                                                                                String dateTime2 = localDate2 + " " + localTime2;

                                                                                // Use the String compareTo() method to compare the date/time strings
                                                                                return dateTime1.compareTo(dateTime2);
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                                return 0;
                                                                            }
                                                                        }
                                                                    };

                                                                    List<JSONObject> eventList = new ArrayList<>();
                                                                    for (int i = 0; i < events.length(); i++) {
                                                                        try {
                                                                            eventList.add(events.getJSONObject(i));
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }

                                                                    Collections.sort(eventList, eventComparator);


                                                                    JSONArray sortedEvents = new JSONArray(eventList);

                                                                    ArrayList<JSONObject> event_DataList = new ArrayList<>();
                                                                    for (int i = 0; i < sortedEvents.length(); i++) {
                                                                        JSONObject jsonObject = sortedEvents.getJSONObject(i);
                                                                        event_DataList.add(jsonObject);
                                                                    }
                                                                    JSONObject event = sortedEvents.getJSONObject(0);
                                                                    String name = event.getString("name");
                                                                    Log.d(TAG, "Attraction name: " + name);
                                                                    Log.d("abe dkha na yaar",sortedEvents.toString());
                                                                    CustomAdapter customAdapter = new CustomAdapter(getActivity(), event_DataList);
                                                                    recyclerView.setAdapter(customAdapter);
                                                                    recyclerView.setVisibility(View.VISIBLE);
                                                                }
                                                            }
                                                        }

                                                        if(flaggg==0){
                                                            progressBar.setVisibility(View.GONE);
                                                            recyclerView.setVisibility(View.GONE);
                                                            linear.setVisibility(View.VISIBLE);

                                                        }

                                                    } catch (JSONException e) {
                                                        Log.e(TAG, "Error parsing JSON response", e);
                                                    }
                                                }, error -> Log.e(TAG, "Error in JSON request", error));
                                        queue.add(jsonObjectRequest);

                                    }

                                    if(flag_wrong_loc==0){
                                        recyclerView.setVisibility(View.GONE);
                                        linear.setVisibility(View.VISIBLE);
                                    }

                                } catch (JSONException e) {
                                    Log.e(TAG, "Error parsing JSON response", e);
                                }
                            }, error -> Log.e(TAG, "Error in JSON request", error));
                    queue2.add(jsonObjectRequest_event);


//                    TextView locationTextView = view.findViewById(R.id.textView_demo);
//                    locationTextView.setText(location + " " + keyword + " " + category + " " +distance);
                }

                if(location3 == ""){
                    Log.d(TAG, "Hello");
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    RequestQueue queue2 = Volley.newRequestQueue(getActivity());
                    String ip_info = "https://ipinfo.io/?token=3261999ed827bf";

                    ProgressBar progressBar = getView().findViewById(R.id.progressBar);
                    RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    LinearLayout linear = getView().findViewById(R.id.no_events_linear_layout);

                    progressBar.setVisibility(View.VISIBLE);

                    String location = bundle.getString("location");
                    String keyword = bundle.getString("keyword");
                    String encodedKeyword = keyword.replace(" ", "%20");
                    String category = bundle.getString("category");
                    String distance = bundle.getString("distance");

                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, ip_info, null,
                            response5 -> {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    String loc = response5.getString("loc");
                                    String[] latLng = loc.split(",");
                                    String latitude = latLng[0];
                                    String longitude = latLng[1];

                                    String url_event = "https://hw8csci-abhishekphakak-latest.wl.r.appspot.com/api/form?keyword="+encodedKeyword+"&distance="+distance+"&latitude="+latitude+"&longitude="+longitude+"&selected_cat="+category;
                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_event, null,
                                            response2 -> {
                                                JSONArray events = null;
                                                try {
                                                    int flag_not_to_display = 0;
                                                    if(response2.has("_embedded")) {
                                                        if (response2.getJSONObject("_embedded").has("events")) {
                                                            if (response2.getJSONObject("_embedded").getJSONArray("events").length() > 0) {
                                                                flag_not_to_display = 1;
                                                                JSONObject embedded = response2.getJSONObject("_embedded");
                                                                events = embedded.getJSONArray("events");

                                                                Comparator<JSONObject> eventComparator = new Comparator<JSONObject>() {
                                                                    @Override
                                                                    public int compare(JSONObject event1, JSONObject event2) {
                                                                        try {
                                                                            // Get the local date and time strings from the events
                                                                            String localDate1 = event1.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                                                            String localTime1 = event1.getJSONObject("dates").getJSONObject("start").getString("localTime");
                                                                            String localDate2 = event2.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                                                            String localTime2 = event2.getJSONObject("dates").getJSONObject("start").getString("localTime");

                                                                            // Concatenate the local date and time strings for each event
                                                                            String dateTime1 = localDate1 + " " + localTime1;
                                                                            String dateTime2 = localDate2 + " " + localTime2;

                                                                            // Use the String compareTo() method to compare the date/time strings
                                                                            return dateTime1.compareTo(dateTime2);
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                            return 0;
                                                                        }
                                                                    }
                                                                };

                                                                List<JSONObject> eventList = new ArrayList<>();
                                                                for (int i = 0; i < events.length(); i++) {
                                                                    try {
                                                                        eventList.add(events.getJSONObject(i));
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }

                                                                Collections.sort(eventList, eventComparator);

                                                                JSONArray sortedEvents = new JSONArray(eventList);

                                                                ArrayList<JSONObject> event_DataList = new ArrayList<>();
                                                                for (int i = 0; i < sortedEvents.length(); i++) {
                                                                    JSONObject jsonObject = sortedEvents.getJSONObject(i);
                                                                    event_DataList.add(jsonObject);
                                                                }
                                                                JSONObject event = sortedEvents.getJSONObject(0);
                                                                String name = event.getString("name");
                                                                Log.d(TAG, "Attraction name: " + name);
                                                                Log.d("abe dkha na yaar",events.toString());
                                                                CustomAdapter customAdapter = new CustomAdapter(getActivity(), event_DataList);
                                                                recyclerView.setAdapter(customAdapter);

                                                                recyclerView.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    }

                                                    if(flag_not_to_display==0){
                                                        progressBar.setVisibility(View.GONE);
                                                        recyclerView.setVisibility(View.GONE);
                                                        linear.setVisibility(View.VISIBLE);
                                                    }

                                                } catch (JSONException e) {
                                                    Log.e(TAG, "Error parsing JSON response", e);
                                                }
                                            }, error -> Log.e(TAG, "Error in JSON request", error));
                                    queue.add(jsonObjectRequest);

                                } catch (JSONException e) {
                                    Log.e(TAG, "Error parsing JSON response", e);
                                }
                            }, error -> Log.e(TAG, "Error in JSON request", error));
                    queue2.add(jsonObjectRequest2);
                }
            }
        }
    }


