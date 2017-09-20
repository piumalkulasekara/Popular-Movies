package com.example.anant.moviesdb.Activities;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anant.moviesdb.R;
import com.example.anant.moviesdb.Utilities.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    private String mName;
    private String mOverview;
    private String mRating;
    private String posterPath;
    private String backgroundPath;
    private String datePath;

    @BindView(R.id.background_image)
    ImageView backgroundImage;
    @BindView(R.id.overview_movie)
    TextView movieOverview;
    @BindView(R.id.movie_name)
    TextView movieName;
    @BindView(R.id.movie_date)
    TextView movieDate;
    @BindView(R.id.poster_image)
    ImageView posterImage;
    @BindView(R.id.movie_rating)
    TextView movieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getOrientation();
        getIntentValues();
        setValues();

    }

    private void getOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            backgroundImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    private void setValues() {

        if(isNetworkOnline()) {

            Picasso.with(this)
                    .load(Constants.IMAGE_BASE_URL + Constants.FILE_SIZE_BACKGROUND + posterPath)
                    .placeholder(R.mipmap.ic_launcher).into(posterImage);

            Picasso.with(this)
                    .load(Constants.IMAGE_BASE_URL + Constants.FILE_SIZE_BACKGROUND + backgroundPath)
                    .placeholder(R.mipmap.ic_launcher).into(backgroundImage);
        }

        else {
            posterImage.setVisibility(View.GONE);
            backgroundImage.setVisibility(View.GONE);
        }

        movieDate.setText(datePath);
        movieName.setText(mName);
        movieOverview.setText(mOverview);
        movieRating.setText(mRating);

    }

    private void getIntentValues() {
        mName = getIntent().getExtras().getString(getString(R.string.name_movie));
        posterPath = getIntent().getExtras().getString(getString(R.string.posters_image));
        mOverview = getIntent().getExtras().getString(getString(R.string.overview_plot));
        mRating = getIntent().getExtras().getString(getString(R.string.rating_movie));
        backgroundPath = getIntent().getExtras().getString(getString(R.string.background_image));
        datePath = getIntent().getExtras().getString(getString(R.string.release_date));
    }

    private boolean isNetworkOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }
}