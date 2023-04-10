package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;



public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private static final String TAG = "Image ka url";
    private ArrayList<JSONObject> localDataSet;
    private Context mContext;

    private ArrayList<JSONObject> dataSet;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView venueView;
        private final TextView catView;
        private final TextView dateView;
        private final TextView timeView;
        private final ImageView imageView;
        private final LinearLayout linearLayout;
        private final ImageView imageView_like;




        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.textView_displayer);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setMarqueeRepeatLimit(-1);
            textView.post(() -> {textView.setSelected(true);});

            venueView = view.findViewById(R.id.textView_venue);
            venueView.setSingleLine(true);
            venueView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            venueView.setMarqueeRepeatLimit(-1);
            venueView.post(() -> {venueView.setSelected(true);});

            catView = view.findViewById(R.id.textView_category);

            dateView = view.findViewById(R.id.textView_date);

            timeView = view.findViewById(R.id.textView_time);

            imageView = view.findViewById(R.id.event_image);

            linearLayout = view.findViewById(R.id.event_row);

            imageView_like = view.findViewById((R.id.like));

        }

        public TextView getTextView() {
            return textView;
        }
        public TextView getTextView_venue() {
            return venueView;
        }
        public TextView getTextView_cat() {
            return catView;
        }
        public TextView getTextView_date() {
            return dateView;
        }
        public TextView getTextView_time() {
            return timeView;
        }
        public ImageView getImageView() {
            return imageView;
        }
        public ImageView getImageView_like() {
            return imageView_like;
        }
    }


    public CustomAdapter(Context context, ArrayList<JSONObject> dataSet) {
        localDataSet = dataSet;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        JSONObject event_data = localDataSet.get(position);
        try {
            String name = event_data.getString("name");
            String id = event_data.getString("id");
            viewHolder.getTextView().setText(name);

            String venueName = event_data.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
            viewHolder.getTextView_venue().setText(venueName);

            String catName = event_data.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
            viewHolder.getTextView_cat().setText(catName);

            String dateName = event_data.getJSONObject("dates").getJSONObject("start").getString("localDate");
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date date = inputDateFormat.parse(dateName);
            String formattedDate = outputDateFormat.format(date);
            viewHolder.getTextView_date().setText(formattedDate);
            String formattedtime2 = " ";
            try {
                if (event_data.getJSONObject("dates").getJSONObject("start").has("localTime")) {
                    String timeName = event_data.getJSONObject("dates").getJSONObject("start").getString("localTime");
                    SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");
                    Date time = inputFormat.parse(timeName);
                    formattedtime2 = outputFormat.format(time);
                    viewHolder.getTextView_time().setText(formattedtime2);
                } else {
                    String timeName = "";
                    viewHolder.getTextView_time().setText(timeName);
                    // Handle the case where "localTime" exists but is null
                }
            } catch (JSONException e) {
                String timeName = "";
                viewHolder.getTextView_time().setText(timeName);
                // Handle the case where "start" does not exist
            }

            SharedPreferences sharedPref2 = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            if (sharedPref2.contains(id)) {
                viewHolder.getImageView_like().setImageResource(R.drawable.heart_filled);
            } else {
                viewHolder.getImageView_like().setImageResource(R.drawable.heart_outline);
            }

            String imageUrl = event_data.getJSONArray("images").getJSONObject(0).getString("url");
            Log.d(TAG, "dekhho image url "+imageUrl);
            Picasso.get().load(imageUrl).into(viewHolder.getImageView());

            String finalFormattedtime = formattedtime2;
            viewHolder.getImageView_like().setOnClickListener(view1 -> {
                Drawable currentDrawable = viewHolder.getImageView_like().getDrawable();
                if (currentDrawable != null && currentDrawable.getConstantState().equals(mContext.getResources().getDrawable(R.drawable.heart_filled).getConstantState())) {
                    viewHolder.getImageView_like().setImageResource(R.drawable.heart_outline);
                    SharedPreferences sharedPref = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove(id);
                    editor.apply();
                    Snackbar snackbar = Snackbar.make(view1, name+" removed from favorites", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackbarText.setTextColor(Color.BLACK);
                    snackbarView.setBackgroundResource(R.drawable.button2);
                    snackbar.show();
                } else {
                    viewHolder.getImageView_like().setImageResource(R.drawable.heart_filled);
                    SharedPreferences sharedPref = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    JSONObject eventObject = new JSONObject();
                    try {
                        eventObject.put("event_name", name);
                        eventObject.put("event_venue", venueName);
                        eventObject.put("segment", catName);
                        eventObject.put("event_date", formattedDate);
                        eventObject.put("event_time", finalFormattedtime);
                        eventObject.put("image", imageUrl);
                        eventObject.put("event_ids",id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    editor.putString(id, eventObject.toString());
                    editor.apply();

                    Snackbar snackbar = Snackbar.make(view1, name+" added to favorites", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackbarText.setTextColor(Color.BLACK);
                    snackbarView.setBackgroundResource(R.drawable.button2);
                    snackbar.show();
                }
            });



            viewHolder.linearLayout.setOnClickListener(v -> {
                Log.d(TAG, "Linear Layout clicked ");
                String event_ka_name =  viewHolder.getTextView().getText().toString();
                String venue_ka_name =  viewHolder.getTextView_venue().getText().toString();
                String event_ka_id = id;

                Log.d(TAG, "onBindViewHolder: "+venue_ka_name + event_ka_name + event_ka_id);

                Context context = v.getContext();
                Intent intent = new Intent(context, MainActivity2_event_details_tab.class);

                // Add extra data to the intent
                intent.putExtra("event_id", event_ka_id);
                intent.putExtra("event_name", event_ka_name);
                intent.putExtra("venue_name", venue_ka_name);

                // Start the activity
                context.startActivity(intent);
            });



        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
