package com.example.anant.moviesdb.Activities;

import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.anant.moviesdb.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            ImageView imageView = (ImageView)findViewById(R.id.imageView2);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
}
