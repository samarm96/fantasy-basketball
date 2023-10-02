package com.fantasy.Queries;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fantasy.QueryParams.SeasonType;
import com.fantasy.Utilities.HttpUtilities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PlayerGameLogs {

    public Map<Object, Object> get(String initialDate, String finalDate, String playerId, SeasonType seasonType,
            String seasonYear) {

        Map<Object, Object> map = new HashMap<Object, Object>();

        try {
            String url = String.format(
                    "https://stats.nba.com/stats/playergamelog?DateFrom=%s&DateTo=%s&LeagueID=00&PlayerID=%s&Season=%s&SeasonType=%s",
                    initialDate,
                    finalDate,
                    playerId,
                    seasonYear,
                    seasonType.toString().replaceAll("_", "+"));

            OkHttpClient client = new OkHttpClient();

            Request request = HttpUtilities.createRequest(url);

            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();

            if (responseBody != null) {
                String jsonData = responseBody.string();

                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(jsonData);

                // Extract player statistics
                JSONObject results = (JSONObject) ((JSONArray) jsonObject.get("resultSets")).get(0);
                JSONArray headers = (JSONArray) results.get("headers");
                JSONArray data = (JSONArray) results.get("rowSet");

                for (int i = 0; i < data.length(); i++) {
                    JSONArray playerData = (JSONArray) data.get(i);
                    JSONArray headersVal = headers;

                    Map<Object, Object> innerMap = new HashMap<>();
                    for (int j = 0; j < headersVal.length(); j++) {
                        innerMap.put(headersVal.get(j), playerData.get(j));
                    }
                    map.put(i, innerMap);

                }

                return map;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

    
}
