package com.fukuhara.douglas.popularmoviesstage2.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fukuhara.douglas.popularmoviesstage2.R;
import com.fukuhara.douglas.popularmoviesstage2.adapter.MoviesListAdapter;
import com.fukuhara.douglas.popularmoviesstage2.databinding.ActivityMoviesListBinding;
import com.fukuhara.douglas.popularmoviesstage2.movies.data.MovieContract;
import com.fukuhara.douglas.popularmoviesstage2.movies.model.Movie;
import com.fukuhara.douglas.popularmoviesstage2.movies.model.MoviesList;
import com.fukuhara.douglas.popularmoviesstage2.movies.network.TheMovieDbClient;
import com.fukuhara.douglas.popularmoviesstage2.utils.Const;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesListActivity extends AppCompatActivity
        implements MoviesListAdapter.MovieItemClickListener {

    private MenuItem mSortByPopular;
    private MenuItem mSortByHighest;
    private MenuItem mSortByFavorite;

    private String mSortType;

    private Context mContext;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private ActivityMoviesListBinding mBiding;

    private MoviesListAdapter mMoviesListAdapter;

    private ArrayList<Movie> mMoviesListArray = null;

    protected void onSaveInstanceState(Bundle outState) {
        // We will save the Sort Type in the Bundle, so we can retrieve it more
        // easily rather than fetching SharedPreferences when transiting from
        // Details Activity to Movies List
        outState.putString(
                getString(R.string.movies_list_sort_type),
                mSortType
        );

        if (mMoviesListArray != null) {
            outState.putParcelableArrayList(
                    getString(R.string.movies_list_bundle),
                    mMoviesListArray
            );
        }

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSortType.equals(getString(R.string.movies_list_sort_by_favorite))) {
            loadFavList();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        mContext = this;

        initiateLayoutParams();

        checkForInfoInBundleOrPref(savedInstanceState);
    }

    private void initiateLayoutParams() {
        mBiding = DataBindingUtil.setContentView(this, R.layout.activity_movies_list);

        // Getting the X-asis from the device
        // We will calculate how many columns will be spanned in the GridLayout
        // For this calculation, we are using as a parameter the value of the half of the
        // Nexus 4 width
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int columns = size.x / getResources().getInteger(R.integer.default_width_divider);

        if (columns == 0) {
            // If the result of columns is 0, we will adjust to spam 1 column (at least)
            columns = 1;
        }

        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(this, columns);

        mBiding.rvMoviesList.setLayoutManager(gridLayoutManager);

        mMoviesListAdapter = new MoviesListAdapter(this);
    }

    private void checkForInfoInBundleOrPref(Bundle savedInstanceState) {
        mSharedPreferences = getPreferences(MODE_PRIVATE);

        // If we can, retrieve the Sort Type from Saved Instance...
        if (savedInstanceState != null) {
            mSortType = savedInstanceState.getString(getString(R.string.movies_list_sort_type));

        } else {
            // ... otherwise, get the Sort Type from SharedPreferences
            mSortType = mSharedPreferences.getString(
                    getString(R.string.movies_list_sort_type),
                    getString(R.string.movies_list_sort_by_popular));
        }

        if (!mSortType.equals(getString(R.string.movies_list_sort_by_favorite))) {
            if (savedInstanceState == null ||
                    !savedInstanceState.containsKey(getString(R.string.movies_list_bundle))) {
                createListMovies(getString(R.string.movies_list_refresh_from_on_create));
            } else {
                mBiding.pbLoadMoviesList.setVisibility(View.GONE);

                mMoviesListArray = savedInstanceState.getParcelableArrayList(
                        getString(R.string.movies_list_bundle));
                mMoviesListAdapter.setMoviesList(mMoviesListArray);
                mBiding.rvMoviesList.setAdapter(mMoviesListAdapter);
            }
        }

    }

    private void createListMovies(final String refreshFrom) {
        final ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        // If there is no Internet Connection, we will not even try to fetch TheMovieDBClient...
        if (!isConnected) {

            mBiding.tvMovieListInternetError.setVisibility(View.VISIBLE);
            mBiding.tvMovieListNoFavMovies.setVisibility(View.GONE);

            String message = getString(R.string.try_again);
            Toast.makeText(MoviesListActivity.this, message, Toast.LENGTH_LONG).show();

            handleOnNetworkError(refreshFrom);

            return;
        }

        mBiding.tvMovieListInternetError.setVisibility(View.GONE);
        mBiding.tvMovieListNoFavMovies.setVisibility(View.GONE);
        mBiding.pbLoadMoviesList.setVisibility(View.VISIBLE);

        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl(Const.THE_MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retroBuilder.build();

        TheMovieDbClient client = retrofit.create(TheMovieDbClient.class);

        Call<MoviesList> call = client.listOfMovies(mSortType, Const.API_KEY);

        call.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {

                // Retrieve the list of Movies in "MoviesList" obj from Retrofit
                MoviesList list = response.body();
                // and get the ArrayList of it.
                mMoviesListArray = list.getMovies();

                // As the Retrofit could acquire a response from the Internet,
                // we must hide the Progress Bar loading image
                mBiding.pbLoadMoviesList.setVisibility(View.GONE);

                // And update the Adapter and the RecyclerView information
                mBiding.rvMoviesList.setVisibility(View.VISIBLE);
                mMoviesListAdapter.setMoviesList(mMoviesListArray);
                mBiding.rvMoviesList.setAdapter(mMoviesListAdapter);

            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();

                boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

                // In case that a connection Error happened, we must revert back the
                // Sort Type configuration from goingTo to refreshFrom
                handleOnNetworkError(refreshFrom);

                mBiding.pbLoadMoviesList.setVisibility(View.GONE);
                mBiding.tvMovieListInternetError.setVisibility(View.VISIBLE);
                mBiding.tvMovieListNoFavMovies.setVisibility(View.GONE);

                String message = getString(R.string.exceptional_error);
                if (!isConnected) {
                    message = getString(R.string.try_again);
                }
                Toast.makeText(MoviesListActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void handleOnNetworkError(String refreshFrom) {
        if (getString(R.string.movies_list_refresh_from_on_create).equals(refreshFrom)) {
            mBiding.tvMovieListInternetError.setVisibility(View.VISIBLE);
        } else if (!refreshFrom.equals(getString(R.string.movies_list_refresh_from_on_create)) ||
                !refreshFrom.equals(getString(R.string.movies_list_refresh_from_refresh))) {
            mSortType = refreshFrom;
            if (mSortType.equals(getString(R.string.movies_list_sort_by_popular))) {
                mSortByPopular.setChecked(true);
            } else if (mSortType.equals(getString(R.string.movies_list_sort_by_highest))) {
                mSortByHighest.setChecked(true);
            } else {
                mSortByFavorite.setChecked(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_list_menu, menu);

        if (mSortType.equals(getString(R.string.movies_list_sort_by_popular))) {
            mSortByPopular = menu.findItem(R.id.sort_by_popular);
            mSortByPopular.setChecked(true);
        } else if (mSortType.equals(getString(R.string.movies_list_sort_by_highest))) {
            mSortByHighest = menu.findItem(R.id.sort_by_highest);
            mSortByHighest.setChecked(true);
        } else {
            mSortByFavorite = menu.findItem(R.id.sort_by_favorite);
            mSortByFavorite.setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // "Re-acquiring" menu instances to be used in the callback 'onFailure' of Retrofit in
        // createMoviesList when no Internet connection is available and user tries to change
        // the sort type
        mSortByPopular = menu.findItem(R.id.sort_by_popular);
        mSortByHighest = menu.findItem(R.id.sort_by_highest);
        mSortByFavorite = menu.findItem(R.id.sort_by_favorite);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();

        switch (selectedItem) {
            case R.id.sort_by_popular:
                updateMenuAndRefreshList(mSortType, getString(R.string.movies_list_sort_by_popular), item);

                break;
            case R.id.sort_by_highest:
                updateMenuAndRefreshList(mSortType, getString(R.string.movies_list_sort_by_highest), item);

                break;
            case R.id.sort_by_favorite:
                updateMenuAndRefreshList(mSortType, getString(R.string.movies_list_sort_by_favorite), item);

                loadFavList();

                break;
            case R.id.activity_movies_list_actiion_refresh:

                createListMovies(getString(R.string.movies_list_refresh_from_refresh));
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMenuAndRefreshList(String previouSort, String sort, MenuItem item) {
        // We will only apply the query if the previous sort type selection is
        // different from the new one (it the user wants to refresh the current
        // screen content, s/he needs to click in REFRESH button instead)if (!mSortType.equals(sort)) {
        if (!previouSort.equals(sort)) {
            mSortType = sort;
            item.setChecked(true);

            mEditor = mSharedPreferences.edit();
            mEditor.putString(
                    getString(R.string.movies_list_sort_type),
                    sort
            );
            mEditor.commit();

            if (sort.equals(getString(R.string.movies_list_sort_by_favorite))) {
                loadFavList();
            } else {
                createListMovies(previouSort);
            }
        }

    }

    private void loadFavList() {
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        ArrayList<Movie> favoriteMoviesList = new ArrayList<>();

        if (cursor.getCount() == 0) {
            mBiding.tvMovieListInternetError.setVisibility(View.INVISIBLE);
            mBiding.tvMovieListNoFavMovies.setVisibility(View.VISIBLE);
            mBiding.rvMoviesList.setVisibility(View.INVISIBLE);

        } else {
            try {
                while (cursor.moveToNext()) {
                    favoriteMoviesList.add(new Movie(cursor));
                }
            } finally {
                cursor.close();

                mBiding.tvMovieListNoFavMovies.setVisibility(View.GONE);
                mBiding.tvMovieListInternetError.setVisibility(View.GONE);

                mMoviesListAdapter.setMoviesList(favoriteMoviesList);
                mBiding.rvMoviesList.setAdapter(mMoviesListAdapter);
            }
        }
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(mContext, MovieDetailsActivity.class);

        intent.putExtra(
                getString(R.string.movies_from_list_to_details_intent),
                movie);

        startActivity(intent);
    }
}
