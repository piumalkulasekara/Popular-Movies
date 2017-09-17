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
import android.widget.Toast;

import com.example.anant.moviesdb.Utilities.Constants;
import com.example.anant.moviesdb.Utilities.MoviesList;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

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
        GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mProgress = (ProgressBar)findViewById(R.id.dialog_progress);
        new FetchJSON().execute(Constants.POPULAR_BASE_URL);

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
        Toast.makeText(this, String.valueOf(index), Toast.LENGTH_SHORT).show();
    }

    public class FetchJSON extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            mProgress.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
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
            if(s!=null){
                try {
                    ArrayList<String> l = mMoviesList.parseJSONImage();
                    mMoviesAdapter = new MoviesAdapter(l.size(), l, mRecyclerView, mProgress, MainActivity.this);
                    mRecyclerView.setAdapter(mMoviesAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
