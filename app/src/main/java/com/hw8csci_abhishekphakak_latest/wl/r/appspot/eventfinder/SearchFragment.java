package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchFragment extends Fragment {

    EditText editText_distance;
    EditText editText_location;
    Spinner spinner1;
    Button button_clear;
    SwitchCompat switchCompat;
    private String savedKeyword = "";
    private String savedDistance = "";
    private String savedLocation = "";
    private String savedCategory = "";

    private Button button_search;

    private ProgressBar progressBar33;

    private AutoCompleteTextView autoCompleteTextView;

    private ArrayAdapter<String> adapter3;

    private static final String TAG = "SearchFragment";
    private Spinner spinner;

    private Spinner spinner2;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        String[] items = {"All", "Music", "Sports", "Arts & Theatre", "Film", "Miscellaneous"};

        spinner = view.findViewById(R.id.spinner1);
        autoCompleteTextView = view.findViewById(R.id.autocomplete);
        editText_distance = view.findViewById(R.id.editText_distance);
        editText_location = view.findViewById(R.id.editText_location);
        button_clear = view.findViewById(R.id.button_clear);
        switchCompat = view.findViewById(R.id.switch1);
        button_search = view.findViewById(R.id.button_search);
        button_clear = view.findViewById(R.id.button_clear);
        progressBar33 = view.findViewById(R.id.progressBar9);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), R.layout.color_spinner_layout, items);
        adapter2.setDropDownViewResource(R.layout.sinner_dropdown_layout);
        spinner.setAdapter(adapter2);


        Bundle bundle = getArguments();

        if (bundle != null){
            String location2 = bundle.getString("location");
            String keyword2 = bundle.getString("keyword");
            String category2 = bundle.getString("category");
            String distance2 = bundle.getString("distance");
            autoCompleteTextView.setText(keyword2);
            editText_distance.setText(distance2);
            editText_location.setText(location2);
            if(location2.equals("")){
                switchCompat.setChecked(true);
                Log.d(TAG, "onCreateView: hello_switchcompat");
                ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) button_search.getLayoutParams();
                ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) button_clear.getLayoutParams();
                editText_location.setVisibility(View.GONE);
                params1.topMargin = 960;
                params2.topMargin = 960;
            }
            for (int i = 0; i < items.length; i++) {
                Log.d(TAG, "onCreateView: item"+items[i]);
                if (items[i].equals(category2)) {
                    Log.d(TAG, "onCreateView: if ke andar"+items[i]);
                    spinner.setSelection(i);
                    break;
                }
            }
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.keyword_dropdown_layout);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener((parent, view2, position, id) -> {
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                progressBar33.setVisibility(View.VISIBLE);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url_event_dynamic = "https://androidapp-hw9.wl.r.appspot.com/api/dynamic?keywordss=" + s.toString();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_event_dynamic, null,
                        response2 -> {
                            progressBar33.setVisibility(View.GONE);
                            try {
                                final String[] search_results = new String[5];
                                JSONArray attractions =  response2.getJSONObject("_embedded").getJSONArray("attractions");
                                for (int i = 0; i < 5; i++) {
                                    if (i < attractions.length()) {
                                        search_results[i] = attractions.getJSONObject(i).getString("name");
                                    } else {
                                        search_results[i] = "";
                                    }
                                }
                                adapter.clear();
                                adapter.addAll(search_results);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG, response2.toString());
                            } catch (JSONException e) {
                                Log.e(TAG, "Error parsing JSON response", e);
                            }
                        }, error -> Log.e(TAG, "Error in JSON request", error));
                queue.add(jsonObjectRequest);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this implementation
            }
        });




        button_search.setOnClickListener(buttonView -> {

            boolean isSwitchOn = switchCompat.isChecked();
            if(isSwitchOn){
                String keyword = autoCompleteTextView.getText().toString();
                String distance = editText_distance.getText().toString();
                String location = editText_location.getText().toString();
                String category = spinner.getSelectedItem().toString();

                if (keyword.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(buttonView, "Please fill all fields", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackbarText.setTextColor(Color.BLACK);
                    snackbarView.setBackgroundResource(R.drawable.button2);
                    snackbar.show();
                    return;
                }

                if (distance.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(buttonView, "Please fill all fields", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackbarText.setTextColor(Color.BLACK);
                    snackbarView.setBackgroundResource(R.drawable.button2);
                    snackbar.show();
                    return;
                }

                Bundle bundle2 = new Bundle();
                bundle2.putString("keyword", keyword);
                bundle2.putString("distance", distance);
                bundle2.putString("category", category);
                bundle2.putString("location", "");

                Navigation.findNavController(buttonView)
                        .navigate(R.id.action_searchFragment_to_eventFragment, bundle2);
            }

            if(!isSwitchOn){
                String keyword = autoCompleteTextView.getText().toString();
                String distance = editText_distance.getText().toString();
                String location = editText_location.getText().toString();
                String category = spinner.getSelectedItem().toString();

                if (keyword.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(buttonView, "Please fill all fields", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackbarText.setTextColor(Color.BLACK);
                    snackbarView.setBackgroundResource(R.drawable.button2);
                    snackbar.show();
                    return;
                }

                if (distance.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(buttonView, "Please fill all fields", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackbarText.setTextColor(Color.BLACK);
                    snackbarView.setBackgroundResource(R.drawable.button2);
                    snackbar.show();
                    return;
                }

                if (location.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(buttonView, "Please fill all fields", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackbarText.setTextColor(Color.BLACK);
                    snackbarView.setBackgroundResource(R.drawable.button2);
                    snackbar.show();
                    return;
                }

                Bundle bundle3 = new Bundle();
                bundle3.putString("keyword", keyword);
                bundle3.putString("distance", distance);
                bundle3.putString("category", category);
                bundle3.putString("location", location);


                Navigation.findNavController(buttonView)
                        .navigate(R.id.action_searchFragment_to_eventFragment, bundle3);
            }


        });


        switchCompat.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) button_search.getLayoutParams();
            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) button_clear.getLayoutParams();

            if (isChecked) {
                editText_location.setVisibility(View.GONE);
                editText_location.setText("");
                params1.topMargin = 960;
                params2.topMargin = 960;
            } else {
                editText_location.setVisibility(View.VISIBLE);
                params1.topMargin = 1140;
                params2.topMargin = 1140;
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        button_clear.setOnClickListener(v -> {
            autoCompleteTextView.setText("");
            editText_distance.setText("10");
            editText_location.setText("");
            spinner.setSelection(0);
            switchCompat.setChecked(false);
        });
    }
}