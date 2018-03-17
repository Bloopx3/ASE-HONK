package bloop.honk.Model;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bloop.honk.R;
import bloop.honk.View.CarParkAdapter;
import bloop.honk.View.CarParkFragment;
import bloop.honk.View.WebViewFragment;

import static android.content.ContentValues.TAG;

public class CarParkManager {

    public void fetchCarPark(Activity activity, CarParkAdapter adapter, List<CarPark> carParks) {
        new DownloadJSONTask(activity, adapter, carParks).execute(Config.CAR_PARK_URL);
    }

    public void launchWebView(CarParkFragment carParkFragment, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("Link", url);

        Fragment fragment = new WebViewFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = carParkFragment.getActivity().getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null); //uncomment to enable backpress to return to previous fragment
        ft.replace(R.id.main_frame_container, fragment);
        ft.commit();
    }

    public static class DownloadJSONTask extends AsyncTask<String, Void, String>{
        private Activity activity;
        private CarParkAdapter adapter;
        private List<CarPark> carParks;

        public DownloadJSONTask(Activity activity, CarParkAdapter adapter, List<CarPark> carParks) {
            this.activity = activity;
            this.adapter = adapter;
            this.carParks = carParks;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return loadJSONFromNetwork(params[0], carParks);
            } catch (IOException e) {
                return activity.getResources().getString(R.string.connection_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            adapter.notifyDataSetChanged();
        }
    }
    private static String loadJSONFromNetwork(String urlString, List<CarPark> carParks) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        List<String> carParkIterator = new ArrayList<>();
        carParkIterator.add("");
        carParkIterator.add("?$skip=500");
        carParkIterator.add("?$skip=1000");
        carParkIterator.add("?$skip=1500");
        try {
            URL url;
            StringBuffer buffer;
            for (String str:carParkIterator) {
                buffer = new StringBuffer();
                url = new URL(urlString+str);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("AccountKey", "5WoML31xSZWmAOqK49JDKw==");
                connection.setRequestProperty("accept", "application/json");
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                JSONObject jsonObject = new JSONObject(buffer.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("value");
                for (int i=0;i<jsonArray.length();i++){
                    CarPark carPark = new CarPark();
                    carPark.setAgency(jsonArray.getJSONObject(i).getString("Agency"));
                    carPark.setArea(jsonArray.getJSONObject(i).getString("Area"));
                    carPark.setAvailableLots(jsonArray.getJSONObject(i).getInt("AvailableLots"));
                    carPark.setCarParkID(jsonArray.getJSONObject(i).getString("CarParkID"));
                    String development = jsonArray.getJSONObject(i).getString("Development");
                    carPark.setDevelopment(development);
                    carPark.setLocation(jsonArray.getJSONObject(i).getString("Location"));
                    String lotType = jsonArray.getJSONObject(i).getString("LotType");
                    switch (lotType){
                        case "C":
                            carPark.setLotType("Cars");
                            break;
                        case "Y":
                            carPark.setLotType("Motorcycles");
                            break;
                        case "H":
                            carPark.setLotType("Heavy Vehicles");
                            break;
                    }
                    carParks.add(carPark);
//                    carParks.put(development, carPark);
//                    Log.v("Carpark", ""+carParks.size());
                }
                //Log.v("Load data", String.valueOf(carParks.keySet().size()));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
