package com.fantasy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetEstimatedStats {
    
    public static void main(String[] args) {
        GetEstimatedStats ges = new GetEstimatedStats();
        ges.get("2022-23", SeasonType.Regular_Season);
    }
    public void get(String seasonYr, SeasonType seasonType) {

        // Define the NBA Stats API endpoint for player statistics
        String baseUrl = String.format("https://stats.nba.com/stats/playerestimatedmetrics?LeagueID=00&Season=%s&SeasonType=%s",
        seasonYr,
        seasonType.toString().replaceAll("_", "+"));

        //String estimatedStatsUrl = "https://stats.nba.com/stats/playerestimatedmetrics?LeagueID=00&Season=2022-23&SeasonType=Regular+Season";

        OkHttpClient client = new OkHttpClient();

        Request request = HttpUtilities.createRequest(baseUrl);

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();

            if (responseBody != null) {
                String jsonData = responseBody.string();

                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(jsonData);

                System.out.println(jsonObject.keySet());

                // Extract player statistics
                JSONObject results = (JSONObject) jsonObject.get("resultSet");
                                
                System.out.println(results.keySet());
                JSONArray headers = (JSONArray) results.get("headers");
                JSONArray data = (JSONArray) results.get("rowSet");

                System.out.println(headers);
                // JSONArray data =
                // jsonObject.getJSONArray("resultSet").getJSONObject(0).getJSONArray("rowSet");
                Map<Object, Object> map = new HashMap<Object, Object>();
                                // Display player statistics

                    for (int i = 0; i < data.length(); i++) {
                        JSONArray playerData = (JSONArray) data.get(i);
                        String playerName = (String) playerData.get(1);
                        JSONArray headersVal = headers;
                        Map<Object, Object> innerMap = new HashMap<>();
                        for(int j = 0; j < headersVal.length(); j++){
                            if(j==1) {
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
