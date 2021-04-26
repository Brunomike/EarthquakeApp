package com.example.earthquakeapp;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    private static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2021-01-01&endtime=2021-04-26&minfelt=50&minmagnitude=5";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private EarthquakeAdapter mEarthquakeAdapter;
    private TextView mAlternativeText;
    private ProgressBar mProgressBar;
    private ListView mEarthquakesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: Earthquake Activity onCreate() called ...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);
        mAlternativeText =findViewById(R.id.alternativeText);
        mProgressBar=findViewById(R.id.loadProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);



        /**
         ArrayList<Earthquake> earthquakeArrayList = QueryUtils.extractEarthquakes();
         //ArrayList<Earthquake> earthquakeArrayList=new ArrayList<>();
         earthquakeArrayList.add(new Earthquake("7.2", "San Francisco", "Feb 2, 2016"));
         earthquakeArrayList.add(new Earthquake("6.1", "London", "July 20, 2015"));
         earthquakeArrayList.add(new Earthquake("5.9", "Tokyo", "Nov 10, 2014"));
         earthquakeArrayList.add(new Earthquake("5.4", "Mexico City", "May 3, 2014"));
         earthquakeArrayList.add(new Earthquake("2.8", "Moscow", "Jan 31, 2013"));
         earthquakeArrayList.add(new Earthquake("4.9", "Rio de Janeiro", "Aug 19, 2012"));
         earthquakeArrayList.add(new Earthquake("1.6", "Paris", "Oct 30, 2011"));


         mEarthquakeAdapter = new EarthquakeAdapter(EarthquakeActivity.this, earthquakeArrayList);
         **/

        mEarthquakesList = findViewById(R.id.list_items);
        mEarthquakesList.setEmptyView(mAlternativeText);
        mEarthquakeAdapter = new EarthquakeAdapter(EarthquakeActivity.this, new ArrayList<Earthquake>());
        mEarthquakesList.setAdapter(mEarthquakeAdapter);

        mEarthquakesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthquake = mEarthquakeAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mProgressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mAlternativeText.setText(R.string.no_internet_connection);
            mAlternativeText.setVisibility(View.VISIBLE);
        }

    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(LOG_TAG, "TEST: onCreateLoader() called ....");
        mAlternativeText.setVisibility(View.GONE);
        return new EarthquakeLoader(this, USGS_REQUEST_URL);

        //return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {
        Log.i(LOG_TAG, "TEST: onLoadFinished() called ...");
        mEarthquakeAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mEarthquakeAdapter.addAll(data);
            mProgressBar.setVisibility(View.GONE);
        }else if (data==null && data.isEmpty()){
            mAlternativeText.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, "TEST: onLoadReset() called ...");
        mEarthquakeAdapter.clear();
    }

    /**ONCREATELOADER()
     *  Log.i(LOG_TAG, "TEST: onCreateLoader() called ....");
     *
     *         return new EarthquakeLoader(this, USGS_REQUEST_URL);
     *
     *ONLOADFINISHED()
     *  Log.i(LOG_TAG, "TEST: onLoadFinished() called ...");
     *         mEarthquakeAdapter.clear();
     *         if (data != null && !data.isEmpty()) {
     *             mEarthquakeAdapter.addAll(data);
     *         }
     *
     *ONLOADERRESET()
     *          Log.i(LOG_TAG, "TEST: onLoadReset() called ...");
     *         mEarthquakeAdapter.clear();
     * **/


}