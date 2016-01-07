package com.example.lastestlistings;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + posterPath).into(imageView);

        Log.v(DiscoveryListAdapter.class.getSimpleName(), "%%%%%%%%%%%%%%%%%%% loading http://image.tmdb.org/t/p/w185/" + posterPath);

        return frame;
    }
}
