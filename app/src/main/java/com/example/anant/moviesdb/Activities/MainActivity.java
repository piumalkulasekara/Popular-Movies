package com.example.anant.moviesdb.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anant.moviesdb.Adapters.MoviesAdapter;
import com.example.anant.moviesdb.R;
import com.example.anant.moviesdb.Utilities.Constants;
import com.example.anant.moviesdb.Utilities.MoviesList;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    @BindView(R.id.rvNumbersRecycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.dialog_progress)
    ProgressBar mProgress;
    @BindView(R.id.error_network)
    TextView mErrorNetwork;

    private MoviesList mMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mMoviesList = new MoviesList();
        GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        new FetchJSON().execute(Constants.POPULAR_BASE_URL);

    }

    private boolean isNetworkOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.popular_sort_action){
            item.setChecked(true);
            new FetchJSON().execute(Constants.POPULAR_BASE_URL);
        }
        else if(item.getItemId()==R.id.top_sort_action){
            item.setChecked(true);
            new FetchJSON().execute(Constants.TOP_RATED);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void listItemClicked(int index) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intentValues(intent, index);
        startActivity(intent);
    }

    private void intentValues(Intent intent, int index) {
        try {
            intent.putExtra(getString(R.string.overview_plot), mMoviesList.parseJSONLists(Constants.OVERVIEW).get(index));
            intent.putExtra(getString(R.string.background_image), mMoviesList.parseJSONLists(Constants.BACKGROUND).get(index));
            intent.putExtra(getString(R.string.rating_movie), mMoviesList.parseJSONLists(Constants.RATING).get(index));
            intent.putExtra(getString(R.string.posters_image), mMoviesList.parseJSONLists(Constants.POSTER_PATH).get(index));
            intent.putExtra(getString(R.string.name_movie), mMoviesList.parseJSONLists(Constants.NAME).get(index));
            intent.putExtra(getString(R.string.release_date), mMoviesList.parseJSONLists(Constants.DATE).get(index));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class FetchJSON extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            if(isNetworkOnline()) {
                mProgress.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                mErrorNetwork.setVisibility(View.INVISIBLE);
            }
            else {
                mProgress.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                mErrorNetwork.setVisibility(View.VISIBLE);
            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String s = null;
            try {
                s =  mMoviesList.getJSONResponse(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null && isNetworkOnline()){
                try {
                    ArrayList<String> posters = mMoviesList.parseJSONLists(Constants.POSTER_PATH);
                    MoviesAdapter mMoviesAdapter = new MoviesAdapter(posters.size(), posters, mRecyclerView, mProgress, MainActivity.this);
                    mRecyclerView.setAdapter(mMoviesAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(s);
        }
    }

    private int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }
}
