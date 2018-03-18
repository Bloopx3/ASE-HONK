package bloop.honk.Controller;


import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import java.util.List;
import java.util.Map;

import bloop.honk.Model.CarPark;
import bloop.honk.Model.CarParkManager;
import bloop.honk.View.CarParkAdapter;
import bloop.honk.View.CarParkFragment;

public class CarParkController {

    private CarParkAdapter adapter;
    private Activity activity;
    private CarParkManager carParkMgr;


    public CarParkController(FragmentActivity activity, CarParkAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        this.carParkMgr = new CarParkManager();
    }

    public void fetchCarParks(final List<CarPark> carParks) {
        carParkMgr.fetchCarPark(activity, adapter, carParks);
    }

    public void launchWebView(CarParkFragment carParkFragment, String url) {
        carParkMgr.launchWebView(carParkFragment,url);
    }
}
