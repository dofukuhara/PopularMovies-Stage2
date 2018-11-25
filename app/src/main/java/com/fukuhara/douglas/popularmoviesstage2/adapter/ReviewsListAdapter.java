package com.fukuhara.douglas.popularmoviesstage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fukuhara.douglas.popularmoviesstage2.R;
import com.fukuhara.douglas.popularmoviesstage2.movies.model.Review;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 26/11/17.
 */

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ReviewViewHolder> {

    private ArrayList<Review> mReviewList;
    private Context mContext;

    public ReviewsListAdapter() {
        mReviewList = new ArrayList<>();
    }

    public void setReviewsList(ArrayList<Review> reviewsList) {
        mReviewList = reviewsList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();

        int layoutIdToBeInflated = R.layout.movie_review;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdToBeInflated, viewGroup, shouldAttachToParentImmediately);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);

        return reviewViewHolder;

    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mReviewList != null ? mReviewList.size() : 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView tvReviewAuthor;
        TextView tvReviewComment;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            tvReviewAuthor = itemView.findViewById(R.id.tv_review_author);
            tvReviewComment = itemView.findViewById(R.id.tv_review_comment);
        }

        public void bind(int listIndex) {
            tvReviewAuthor.setText(mReviewList.get(listIndex).getAuthor());
            tvReviewComment.setText(mReviewList.get(listIndex).getContent());
        }
    }
}
