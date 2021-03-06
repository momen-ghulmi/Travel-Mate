package io.github.project_travel_mate.destinations.funfacts;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;

/**
 * Created by niranjanb on 14/06/17.
 */

class FunFactsPresenter {
    private final FunFactsView mFunFactsView;

    FunFactsPresenter(FunFactsView funFactsView) {
        mFunFactsView = funFactsView;
    }

    public void initPresenter(String id, String token) {
        getCityFacts(id, token);
    }

    // Fetch fun facts about city
    private void getCityFacts(String id, String token) {
        mFunFactsView.showProgressDialog();

        // to fetch city names
        String uri = API_LINK_V2 + "get-city-facts" + id;

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + token)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
                mFunFactsView.hideProgressDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();

                try {
                    JSONObject ob = new JSONObject(res);
                    JSONArray ar = ob.getJSONArray("facts");
                    mFunFactsView.setupViewPager(ar);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mFunFactsView.hideProgressDialog();
                }
                mFunFactsView.hideProgressDialog();
            }
        });
    }
}
