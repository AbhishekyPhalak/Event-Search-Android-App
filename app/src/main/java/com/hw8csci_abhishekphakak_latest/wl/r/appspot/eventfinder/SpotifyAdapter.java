package com.hw8csci_abhishekphakak_latest.wl.r.appspot.eventfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SpotifyAdapter extends RecyclerView.Adapter<SpotifyAdapter.ArtistViewHolder> {

    private static final String TAG = "Image ka url";
    private Context mContext;

    private ArrayList<JSONObject> artistDataList;


    public static class ArtistViewHolder extends RecyclerView.ViewHolder {


        private final TextView textView;
        private final ImageView imageView;
        private final ImageView imageView_1;
        private final ImageView imageView_2;
        private final ImageView imageView_3;
        private final TextView textView_followers;
        private final TextView textView_pop;

        private final TextView spotify_link;

        private final CircularProgressBar circularProgressBar;

        public ArtistViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.textView_displayer2);

            imageView = view.findViewById(R.id.artist_image);

            imageView_1 = view.findViewById(R.id.artist_image1);
            imageView_2 = view.findViewById(R.id.artist_image2);
            imageView_3 = view.findViewById(R.id.artist_image3);

            textView_followers = view.findViewById(R.id.textView_followers);

            circularProgressBar = view.findViewById(R.id.circularProgressBar);

            textView_pop = view.findViewById(R.id.pop_val);

            spotify_link = view.findViewById(R.id.textView_spotify_link);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
        }
        public ImageView getImageView_1() {
            return imageView_1;
        }
        public ImageView getImageView_2() {
            return imageView_2;
        }
        public ImageView getImageView_3() {
            return imageView_3;
        }
        public TextView getTextView_followers() {
            return textView_followers;
        }
        public CircularProgressBar getCircularProgressBar() {
            return circularProgressBar;
        }
        public TextView getTextView_pop() {
            return textView_pop;
        }
        public TextView getTextView_spotify() {
            return spotify_link;
        }

    }


    public SpotifyAdapter(Context context, ArrayList<JSONObject> artistDataList) {
        this.mContext = context;
        this.artistDataList = artistDataList;
        Log.d(TAG, "SpotifyAdapter: dataset"+artistDataList);
    }

    public static String formatNumber(int number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1000000) {
            return String.format("%.1fk", number / 1000.0);
        } else {
            return String.format("%.1fM", number / 1000000.0);
        }
    }
    // Create new views (invoked by the layout manager)
    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_artist_item, viewGroup, false);

        return new ArtistViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ArtistViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        JSONObject event_data = artistDataList.get(position);
        Log.d(TAG, "spotify adapter: "+event_data);

        try {
            String name = event_data.getString("name");
            viewHolder.getTextView().setText(name);

            RequestQueue queue = Volley.newRequestQueue(mContext);
            String url_event = "https://hw8csci-abhishekphakak-latest.wl.r.appspot.com/api/spotify?artist_name="+name;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_event, null,
                    response2 -> {
                        try {
                            String name_of_the_artist = response2.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("name");
                            Log.d(TAG, "Artist fragment artist Data list:"+name_of_the_artist);

                            String imageUrl = response2.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONArray("images").getJSONObject(0).getString("url");

                            Picasso.get().load(imageUrl).into(viewHolder.getImageView());

                            String followers = response2.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("followers").getString("total");
                            String followers_final = formatNumber(Integer.parseInt(followers));
                            viewHolder.getTextView_followers().setText(followers_final);

                            String url_spotify = response2.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("external_urls").getString("spotify");
                            viewHolder.getTextView_spotify().setOnClickListener(view1 -> {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url_spotify));
                                mContext.startActivity(i);
                            });


                            String popularity = response2.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("popularity");
                            float popularityFloat = Float.parseFloat(popularity);
                            Log.d(TAG, "onBindViewHolder: dekh na bhai apna spinner ka value"+popularityFloat);
                            viewHolder.getCircularProgressBar().setProgress(popularityFloat);
                            viewHolder.getTextView_pop().setText(popularity);

                            String artist_id = response2.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("id");

                            RequestQueue queue3 = Volley.newRequestQueue(mContext);
                            String url_spotify_artist_albums = "https://hw8csci-abhishekphakak-latest.wl.r.appspot.com/api/spotifyAlbums?artist_id="+artist_id;

                            int timeout = 100000;
                            RetryPolicy policy = new DefaultRetryPolicy(timeout,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                            JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.GET, url_spotify_artist_albums, null,
                                    response3 -> {
                                        try {
                                            String url_iamge_1 = response3.getJSONArray("items").getJSONObject(0).getJSONArray("images").getJSONObject(0).getString("url");
                                            String url_iamge_2 = response3.getJSONArray("items").getJSONObject(1).getJSONArray("images").getJSONObject(0).getString("url");
                                            String url_iamge_3 = response3.getJSONArray("items").getJSONObject(2).getJSONArray("images").getJSONObject(0).getString("url");
                                            Log.d(TAG, "Image1: "+url_iamge_1);
                                            Log.d(TAG, "Image2: "+url_iamge_2);
                                            Log.d(TAG, "Image3: "+url_iamge_3);
                                            Picasso.get().load(url_iamge_1).into(viewHolder.getImageView_1());
                                            Picasso.get().load(url_iamge_2).into(viewHolder.getImageView_2());
                                            Picasso.get().load(url_iamge_3).into(viewHolder.getImageView_3());
                                        } catch (JSONException e) {
                                            Log.e(TAG, "Error parsing JSON response", e);
                                        }
                                    }, error -> Log.e(TAG, "Error in JSON request", error));
                            jsonObjectRequest3.setRetryPolicy(policy);
                            queue3.add(jsonObjectRequest3);


                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON response", e);
                        }
                    }, error -> Log.e(TAG, "Error in JSON request", error));
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return artistDataList.size();
    }
}