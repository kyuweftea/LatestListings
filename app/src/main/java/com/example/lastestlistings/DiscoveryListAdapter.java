package com.example.lastestlistings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Philip on 16-01-06.
 */
public class DiscoveryListAdapter extends ArrayAdapter<MovieListing> {

    Context context;
    int layoutResourceId;
    ArrayList<MovieListing> data = null;

    public DiscoveryListAdapter(Context context, int layoutResourceId, ArrayList<MovieListing> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View frame = convertView;
        ImageView imageView = null;

        if (frame == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            frame = inflater.inflate(layoutResourceId, parent, false);

            imageView = (ImageView) frame.findViewById(R.id.movie_poster_in_discovery);
            frame.setTag(imageView);
        } else {
            imageView = (ImageView) frame.getTag();
        }

        MovieListing movieListing = data.get(position);
        String posterPath = movieListing.getPosterPath();

        Uri imageURL = new Uri.Builder()
                .scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendEncodedPath(posterPath)
                .build();

        Picasso.with(context).load(imageURL).into(imageView);

        return frame;
    }
}
