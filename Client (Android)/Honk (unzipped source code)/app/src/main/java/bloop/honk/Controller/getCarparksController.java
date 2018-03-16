package bloop.honk.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import bloop.honk.Model.CarPark;
import bloop.honk.Model.MapPlace;
import bloop.honk.View.PlacesAutoCompleteAdapter;

public class getCarparksController implements Filterable {
    private LatLngBounds mBounds;
    private AutocompleteFilter mPlaceFilter;
    private PlacesAutoCompleteAdapter mAdapter;
    private Context mContext;

    public getCarparksController(Context context, LatLngBounds bounds, AutocompleteFilter filter, PlacesAutoCompleteAdapter placesAdapter) {
        mBounds = bounds;
        mPlaceFilter = filter;
        mAdapter = placesAdapter;
        mContext = context;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    ArrayList<CarPark> mResultList = getAutocomplete(constraint);
                    mAdapter.setmResultList(mResultList);
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    mAdapter.notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    private ArrayList<CarPark> getAutocomplete(CharSequence constraint) {
        return null;

    }
}
