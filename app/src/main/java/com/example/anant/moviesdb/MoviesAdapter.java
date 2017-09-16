package com.example.anant.moviesdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by anant on 16/9/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    public Context context;
    public int mNumberImages;
    public ImageView mMoviesImage;

    public MoviesAdapter(int numberOfImages){
        mNumberImages = numberOfImages;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.movie_card_view, parent, false);
        return new MoviesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(mMoviesImage);
    }

    @Override
    public int getItemCount() {
        return mNumberImages;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder{

        public MoviesViewHolder(View v){
            super(v);
            mMoviesImage = (ImageView)v.findViewById(R.id.item_image);
        }
    }
}
