package com.fukuhara.douglas.popularmoviesstage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fukuhara.douglas.popularmoviesstage2.R;
import com.fukuhara.douglas.popularmoviesstage2.movies.model.Video;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 20/11/17.
 */

public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.VideoViewHolder> {

    private ArrayList<Video> mVideos;
    private Context mContext;

    private TrailerItemClickListener mTrailerItemClickListener;

    public VideosListAdapter(TrailerItemClickListener listener) {
        mVideos = new ArrayList<>();
        mTrailerItemClickListener = listener;
    }

    public void setVideosList(ArrayList<Video> videosList) {
        this.mVideos = videosList;
    }

    @Override
    public VideosListAdapter.VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();

        int layoutIdForMovieTrailer = R.layout.movie_trailer;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForMovieTrailer, viewGroup, shouldAttachToParentImmediately);
        VideoViewHolder videoViewHolder = new VideoViewHolder(view);

        return videoViewHolder;

    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mVideos == null ? 0 : mVideos.size();
    }

    public interface TrailerItemClickListener {
        void onTrailerClick(Video video);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mMovieTrailerName;

        public VideoViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mMovieTrailerName = itemView.findViewById(R.id.tv_movie_trailer);
        }

        public void bind(int listIndex) {
            mMovieTrailerName.setText(mVideos.get(listIndex).getName());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mTrailerItemClickListener.onTrailerClick(mVideos.get(position));
        }
    }
}
