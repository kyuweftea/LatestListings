package com.example.lastestlistings;

import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public DiscoveryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discovery, container, false);



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

    public class FetchMovieListTask extends AsyncTask<String, Void, ArrayList<MovieListing>> {

        private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();

        @Override
        protected void onPostExecute(ArrayList<MovieListing> movieListings) {
            //Toast.makeText(getActivity(), "task finished", Toast.LENGTH_SHORT).show();
            super.onPostExecute(movieListings);
        }

        @Override
        protected ArrayList<MovieListing> doInBackground(String... params) {

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

        private ArrayList<MovieListing> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            ArrayList<MovieListing> movieData = new ArrayList<>();

            final String resultsKEY = "results";
            final String titleKEY = "title";
            final String posterPathKEY = "poster_path";
            final String plotSynopsisKEY = "overview";
            final String userRatingKEY = "vote_average";
            final String releaseDateKEY = "release_date";

            JSONObject movieListingJSON = new JSONObject(movieJsonStr);
            JSONArray results = movieListingJSON.getJSONArray(resultsKEY);

            for (int i = 0; i < results.length(); i ++) {
                JSONObject result = results.getJSONObject(i);
                movieData.add(new MovieListing(
                        result.getString(titleKEY),
                        result.getString(posterPathKEY),
                        result.getString(plotSynopsisKEY),
                        result.getDouble(userRatingKEY),
                        result.getString(releaseDateKEY)
                ));
            }

            return movieData;
        }
    }
}
