package com.example.anant.moviesdb;

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

import com.example.anant.moviesdb.Utilities.MoviesList;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public MoviesAdapter mMoviesAdapter;
    public MoviesList mMoviesList;
    public ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvNumbersRecycler);
        mMoviesList = new MoviesList();
        new FetchJSON().execute();

        GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mProgress = (ProgressBar)findViewById(R.id.dialog_progress);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new FetchJSON().execute();
        return super.onOptionsItemSelected(item);
    }

    public class FetchJSON extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String s = null;
            try {
               s =  mMoviesList.getJSONResponse();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                try {
                    ArrayList<String> l = mMoviesList.parseJSONImage();
                    mMoviesAdapter = new MoviesAdapter(l.size(), l);
                    mRecyclerView.setAdapter(mMoviesAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.INVISIBLE);
            }
            super.onPostExecute(s);
        }
    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
