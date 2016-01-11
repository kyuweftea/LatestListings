package com.example.lastestlistings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (intent != null && intent.hasExtra(MovieDetailActivity.movieListingKEY)) {
            MovieListing movieListing = (MovieListing) intent
                    .getSerializableExtra(MovieDetailActivity.movieListingKEY);

            final Uri imageURLsmall = new Uri.Builder()
                    .scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w185")
                    .appendEncodedPath(movieListing.getPosterPath())
                    .build();

            final Uri imageURL = new Uri.Builder()
                    .scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w500")
                    .appendEncodedPath(movieListing.getPosterPath())
                    .build();

            final Context context = getActivity();
            final ImageView imageView = (ImageView) rootView.findViewById(R.id.poster);

            Picasso.with(context)
                    .load(imageURLsmall) // thumbnail url goes here
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(context)
                                    .load(imageURL) // image url goes here
                                    .placeholder(imageView.getDrawable())
                                    .into(imageView);
                        }

                        @Override
                        public void onError() {
                        }
                    });

            ((TextView) rootView.findViewById(R.id.title))
                    .setText(movieListing.getTitle());

            ((TextView) rootView.findViewById(R.id.releaseDate))
                    .setText(movieListing.getReleaseDate());

            ((TextView) rootView.findViewById(R.id.userRating))
                    .setText(((Double) movieListing.getUserRating()).toString());

            ((TextView) rootView.findViewById(R.id.plotSynopsis))
                    .setText(movieListing.getPlotSynopsis());
        }

        return rootView;
    }
}
