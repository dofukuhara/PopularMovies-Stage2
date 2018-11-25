package com.fukuhara.douglas.popularmoviesstage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fukuhara.douglas.popularmoviesstage2.R;
import com.fukuhara.douglas.popularmoviesstage2.movies.model.Movie;
import com.fukuhara.douglas.popularmoviesstage2.utils.Const;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 18/11/17.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder> {

    private ArrayList<Movie> mMoviesList;
    private Context mContext;

    private MovieItemClickListener mMovieItemClickListener;

    public MoviesListAdapter(MovieItemClickListener listener) {
        mMoviesList = new ArrayList<>();
        mMovieItemClickListener = listener;
    }

    public void setMoviesList(ArrayList<Movie> list) {
        mMoviesList = list;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();

        int layoutIdForGridItem = R.layout.movie_thumb_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, viewGroup, shouldAttachToParentImmediately);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMoviesList != null ? mMoviesList.size() : 0;
    }

    public interface MovieItemClickListener {
        void onMovieClick(Movie movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mImageView = itemView.findViewById(R.id.img_movie_thumbnail);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mMovieItemClickListener.onMovieClick(mMoviesList.get(position));
        }

        public void bind(int listIndex) {
            String image = Const.THE_MOVIE_DB_IMAGE_API_URL +
                    Const.THE_MOVIE_DB_DEFAULT_IMAGE_SIZE +
                    mMoviesList.get(listIndex).getPoster_path();
            Picasso.with(mContext).load(image).into(mImageView);
        }

    }
}
