package com.example.lastestlistings;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiscoveryActivityFragment extends Fragment {

    private DiscoveryListAdapter mDiscoveryListAdapter;

    public DiscoveryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discovery, container, false);

        mDiscoveryListAdapter = new DiscoveryListAdapter(
                getActivity(),
                R.layout.item_movie_discovery,
                new ArrayList<MovieListing>()
        );

        GridView discoveryItems = (GridView) rootView.findViewById(R.id.discovery_items);
        discoveryItems.setAdapter(mDiscoveryListAdapter);
        discoveryItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailIntent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra(MovieDetailActivity.movieListingKEY,
                                mDiscoveryListAdapter.getItem(i));
                startActivity(detailIntent);
            }
        });

        return rootView;
    }

    private void updateMovieList() {
        new FetchMovieListTask().execute("testing123");
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    public class FetchMovieListTask extends AsyncTask<String, Void, MovieListing[]> {

        private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();

        @Override
        protected void onPostExecute(MovieListing[] movieListings) {
            if (movieListings != null) {
                mDiscoveryListAdapter.clear();
                for (MovieListing movieListing : movieListings) {
                    mDiscoveryListAdapter.add(movieListing);
                }
            }
            super.onPostExecute(movieListings);
        }

        @Override
        protected MovieListing[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                if (params.length == 0) {
                    return null;
                }

                URL url = new URL(
                        new Uri.Builder()
                                .scheme("https")
                                .authority("api.themoviedb.org")
                                .appendPath("3")
                                .appendPath("discover")
                                .appendPath("movie")
                                .appendQueryParameter("api_key", getResources().getString(R.string.TMDbAPIKEY))
                                .toString()
                );

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream iStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (iStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(iStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                movieJsonStr = buffer.toString();

                try {
                    return getMovieDataFromJson(movieJsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                return null;

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        private MovieListing[] getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            ArrayList<MovieListing> movieData = new ArrayList<>();

            JSONObject movieListingJSON = new JSONObject(movieJsonStr);
            JSONArray results = movieListingJSON.getJSONArray(MovieListing.resultsKEY);

            for (int i = 0; i < results.length(); i ++) {
                JSONObject result = results.getJSONObject(i);
                movieData.add(new MovieListing(
                        result.getString(MovieListing.titleKEY),
                        result.getString(MovieListing.posterPathKEY),
                        result.getString(MovieListing.plotSynopsisKEY),
                        result.getDouble(MovieListing.userRatingKEY),
                        result.getString(MovieListing.releaseDateKEY)
                ));
            }

            MovieListing[] movieDataReturn = new MovieListing[movieData.size()];
            movieDataReturn = movieData.toArray(movieDataReturn);
            return movieDataReturn;
        }
    }
}
