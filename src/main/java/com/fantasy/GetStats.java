package com.fantasy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetStats {

    public static void main(String[] args) {

        // Define the NBA Stats API endpoint for player statistics
        String leagueLeaderUrl = "https://stats.nba.com/stats/leagueLeaders?LeagueID=00&PerMode=Totals&Scope=S&Season=2022-23&SeasonType=Regular+Season&StatCategory=FG3M";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(leagueLeaderUrl)
                .addHeader("Host", "stats.nba.com")
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0")
                .addHeader("Accept", "application/json, text/plain, */*'")
                .addHeader("Accept-Language", "en-US,en;q=0.5")
                .addHeader("x-nba-stats-origin", "stats")
                .addHeader("x-nba-stats-token", "true")
                .addHeader("Connection", "keep-alive")
                .addHeader("Referer", "https://stats.nba.com/")
                .addHeader("Pragma", "no-cache")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "cors")
                .build();

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
