package com.fantasy.Queries;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fantasy.Utilities.HttpUtilities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetLeagueLeaderStats {

    public static void main(String[] args) {

        // Define the NBA Stats API endpoint for player statistics
        String url = "https://stats.nba.com/stats/leagueLeaders?LeagueID=00&PerMode=Totals&Scope=S&Season=2022-23&SeasonType=Regular+Season&StatCategory=FG3M";

        OkHttpClient client = new OkHttpClient();

        Request request = HttpUtilities.createRequest(url);


        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();

            if (responseBody != null) {
                String jsonData = responseBody.string();

                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(jsonData);

                // Extract player statistics
                JSONObject results = (JSONObject) jsonObject.get("resultSet");
                JSONArray headers = (JSONArray) results.get("headers");
                JSONArray data = (JSONArray) results.get("rowSet");

                System.out.println(results.toMap().keySet());
                // JSONArray data =
                // jsonObject.getJSONArray("resultSet").getJSONObject(0).getJSONArray("rowSet");
                Map<Object, Object> map = new HashMap<Object, Object>();
                System.out.println(headers);
                                // Display player statistics

                    for (int i = 0; i < data.length(); i++) {
                        JSONArray playerData = (JSONArray) data.get(i);
                        String playerName = (String) playerData.get(2);
                        JSONArray headersVal = headers;
                        Map<Object, Object> innerMap = new HashMap<>();
                        for(int j = 0; j < headersVal.length(); j++){
                            if(j==2) {
                                continue;
                            }
                            innerMap.put(headersVal.get(j),playerData.get(j));
                        }
                        map.put(playerName, innerMap);

                    }
                
                // Print the Map
                for( Map.Entry<Object,Object> entry : map.entrySet()) {
                  System.out.println(entry.getKey() + " : " + entry.getValue() );

                }

                System.out.println(map.get("Fred VanVleet"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
