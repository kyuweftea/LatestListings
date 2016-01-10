package com.example.lastestlistings;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

            Uri imageURL = new Uri.Builder()
                    .scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w780")
                    .appendEncodedPath(movieListing.getPosterPath())
                    .build();

            Picasso.with(getActivity()).load(imageURL).into((ImageView) rootView.findViewById(R.id.poster));

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
