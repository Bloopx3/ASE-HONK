package bloop.honk.View;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import bloop.honk.Controller.CarParkController;
import bloop.honk.Model.CarPark;
import bloop.honk.R;

public class CarParkFragment extends Fragment {
    private List<CarPark> carParks = new ArrayList<>();
    private List<CarPark> search_result = new ArrayList<>();
    private View rootView;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private CarParkAdapter defaultAdapter;
    private CarParkAdapter resultAdapter;
    private CarParkController con;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carparks, container, false);
        getActivity().setTitle("Car Parks");

        rootView = view.findViewById(R.id.carpark_root);
        recyclerView = view.findViewById(R.id.carparksrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        defaultAdapter = new CarParkAdapter(getActivity(), carParks);
        recyclerView.setAdapter(defaultAdapter);

        con = new CarParkController(getActivity(), defaultAdapter);

        con.fetchCarParks(carParks);
        defaultAdapter.setClickListener(new CarParkAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchView.clearFocus();
                hideKeyboard();
            }
        });

        searchView = view.findViewById(R.id.carpark_search);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
                searchView.requestFocus();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query))
                {
                    recyclerView.setAdapter(defaultAdapter);
                }
                else {
                    search_result.clear();
                    for (CarPark carPark : carParks){
                        if (carPark.getDevelopment().toLowerCase().contains(query.toLowerCase())) {
                            search_result.add(carPark);
                            Log.v("Submit", carPark.getDevelopment());
                        }
                    }
                }
                resultAdapter = new CarParkAdapter(getActivity(), search_result);
                resultAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(resultAdapter);
                hideKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText))
                {
                    recyclerView.setAdapter(defaultAdapter);
                }
                else {
                    search_result.clear();
                    for (CarPark carPark : carParks){
                        if (carPark.getDevelopment().toLowerCase().contains(newText.toLowerCase())) {
                            search_result.add(carPark);
                            Log.v("Change", carPark.getDevelopment());
                        }
                    }
                }
                resultAdapter = new CarParkAdapter(getActivity(), search_result);
                resultAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(resultAdapter);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                recyclerView.setAdapter(defaultAdapter);
                searchView.clearFocus();
                hideKeyboard();
                return true;
            }
        });

        return view;
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
