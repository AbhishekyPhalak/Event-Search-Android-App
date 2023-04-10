package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>{

    private ArrayList<JSONObject> FavoritesDataList;
    private Context mContext;

    private FavoritesFragment favoritesFragment;

    private RecyclerView mRecyclerView;

    private static final String TAG = "Fav_Adapter";

    private ArrayList<JSONObject> dataSet;

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fav_row, viewGroup, false);

        return new FavoritesViewHolder(view);
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {


        private final TextView textView;
        private final TextView venueView;
        private final TextView catView;
        private final TextView dateView;
        private final TextView timeView;

        private final ImageView imageView;

        private final ImageView imageView_like;



        public FavoritesViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.textView_fav_displayer);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setMarqueeRepeatLimit(-1);
            textView.post(() -> {textView.setSelected(true);});

            venueView = view.findViewById(R.id.textView_fav_venue);
            venueView.setSingleLine(true);
            venueView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            venueView.setMarqueeRepeatLimit(-1);
            venueView.post(() -> {textView.setSelected(true);});

            catView = view.findViewById(R.id.textView_fav_category);
            catView.setSingleLine(true);
            catView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            catView.setMarqueeRepeatLimit(-1);
            catView.post(() -> {textView.setSelected(true);});

            dateView = view.findViewById(R.id.textView_fav_date);
            dateView.setSingleLine(true);
            dateView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            dateView.setMarqueeRepeatLimit(-1);
            dateView.post(() -> {textView.setSelected(true);});

            timeView = view.findViewById(R.id.textView_fav_time);
            timeView.setSingleLine(true);
            timeView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            timeView.setMarqueeRepeatLimit(-1);
            timeView.post(() -> {textView.setSelected(true);});

            imageView = view.findViewById(R.id.fav_image);

            imageView_like = view.findViewById(R.id.fav_like);
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
        public ImageView getTextView_image() {
            return imageView;
        }

        public ImageView getTextView_image_like() {
            return imageView_like;
        }

    }

    public FavoritesAdapter(FavoritesFragment favoritesFragment, Context context, ArrayList<JSONObject> dataSet, RecyclerView recyclerView) {
        FavoritesDataList = dataSet;
        this.mContext = context;
        this.favoritesFragment = favoritesFragment;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder viewHolder, final int position) {
        try {
            JSONObject event_data = FavoritesDataList.get(position);
            String event_name = event_data.getString("event_name");
            String event_venue = event_data.getString("event_venue");
            String segment = event_data.getString("segment");
            String event_date = event_data.getString("event_date");
            String event_time = event_data.getString("event_time");
            String event_image = event_data.getString("image");
            String event_ids = event_data.getString("event_ids");
            viewHolder.getTextView().setText(event_name);
            viewHolder.getTextView_venue().setText(event_venue);
            viewHolder.getTextView_cat().setText(segment);
            viewHolder.getTextView_date().setText(event_date);
            viewHolder.getTextView_time().setText(event_time);
            Picasso.get().load(event_image).into(viewHolder.getTextView_image());
            viewHolder.getTextView_image_like().setOnClickListener(view -> {
                Log.d(TAG, "onClick: hello u clicked");
                SharedPreferences sharedPref = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(event_ids);
                editor.apply();

                Snackbar snackbar = Snackbar.make(view, event_name+" removed from favorites", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                snackbarText.setTextColor(Color.BLACK);
                snackbarView.setBackgroundResource(R.drawable.button2);
                snackbar.show();

                FavoritesDataList.remove(position);

                // Notify the adapter that the item has been removed
                notifyItemRemoved(position);

                FavoritesAdapter favoritesAdapter = (FavoritesAdapter) mRecyclerView.getAdapter();
                favoritesAdapter.favoritesFragment.updateViewVisibility();
            });

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public int getItemCount() {
        return FavoritesDataList.size();
    }
}
