package com.example.earthquakeapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(@NonNull Context context, ArrayList<Earthquake> earthquakeArrayList) {
        super(context, 0, earthquakeArrayList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        Earthquake currentEarthquake = getItem(position);

        // Find the TextView with view ID magnitude
        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude_text_view);
        // Format the magnitude to show 1 decimal place
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());
        // Display the magnitude of the current earthquake in that TextView
        magnitudeTextView.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // Get the original location string from the Earthquake object,
        // which can be in the format of "5km N of Cairo, Egypt" or "Pacific-Antarctic Ridge".
        String originalLocation = currentEarthquake.getLocation();

        // If the original location string (i.e. "5km N of Cairo, Egypt") contains
        // a primary location (Cairo, Egypt) and a location offset (5km N of that city)
        // then store the primary location separately from the location offset in 2 Strings,
        // so they can be displayed in 2 TextViews.
        String primaryLocation;
        String offsetLocation;

        // Check whether the originalLocation string contains the " of " text
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            // Split the string into different parts (as an array of Strings)
            // based on the " of " text. We expect an array of 2 Strings, where
            // the first String will be "5km N" and the second String will be "Cairo, Egypt".
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            // Location offset should be "5km N " + " of " --> "5km N of"
            offsetLocation = parts[0] + LOCATION_SEPARATOR;
            // Primary location should be "Cairo, Egypt"
            primaryLocation = parts[1];
        } else {
            // Otherwise, there is no " of " text in the originalLocation string.
            // Hence, set the default location offset to say "Near the".
            offsetLocation = getContext().getString(R.string.near_the);
            // The primary location will be the full location string "Pacific-Antarctic Ridge".
            primaryLocation = originalLocation;
        }

        // Find the TextView with view ID offset location
        TextView offsetLocationTextView = (TextView) listItemView.findViewById(R.id.offset_location_text_view);
        // Display the offset location of the current earthquake in that TextView
        offsetLocationTextView.setText(offsetLocation);

        // Find the TextView with view ID primary location
        TextView primaryLocationTextView = (TextView) listItemView.findViewById(R.id.primary_location_text_view);
        // Display the offset location of the current earthquake in that TextView
        primaryLocationTextView.setText(primaryLocation);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        // Find the TextView with view ID date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        dateTextView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_text_view);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeTextView.setText(formattedTime);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    /**
     * Return the color for the magnitude circle based on the intensity of the earthquake.
     *
     * @param magnitude of the earthquake
     */
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
    }

