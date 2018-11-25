package com.fukuhara.douglas.popularmoviesstage2.utils;

import com.fukuhara.douglas.popularmoviesstage2.movies.model.Video;

import java.util.Comparator;

/**
 * Created by dofukuhara on 26/11/17.
 */

// Comparator used to sort Video trailers by Name.
public class VideoNameComparator implements Comparator<Video> {
    public int compare(Video left, Video right) {
        return left.getName().compareTo(right.getName());
    }
}
