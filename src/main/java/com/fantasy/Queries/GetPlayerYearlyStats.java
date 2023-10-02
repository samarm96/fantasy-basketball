package com.fantasy.Queries;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fantasy.QueryParams.PerMode;
import com.fantasy.Utilities.HttpUtilities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetPlayerYearlyStats {

    public static void main(String[] args) {
        GetPlayerYearlyStats g = new GetPlayerYearlyStats();
        g.get(PerMode.PerGame, 2544);
    }

    public void get(PerMode perMode, int playerId) {

        // Define the NBA Stats API endpoint for player statistics
        //String playerStatsUrl = "https://stats.nba.com/stats/playercareerstats?LeagueID=00&PerMode=PerGame&PlayerID=2544";
        String url = String.format("https://stats.nba.com/stats/playercareerstats?LeagueID=00&PerMode=%s&PlayerID=%s",
        perMode.toString(),
        String.valueOf(playerId));

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
                JSONObject results = (JSONObject) ((JSONArray) jsonObject.get("resultSets")).get(0);
                JSONArray headers = (JSONArray) results.get("headers");
                JSONArray data = (JSONArray) results.get("rowSet");

                Map<Object, Object> map = new HashMap<Object, Object>();

                    for (int i = 0; i < data.length(); i++) {
                        JSONArray playerData = (JSONArray) data.get(i);
                        String date = (String) playerData.get(1);
                        JSONArray headersVal = headers;
                        Map<Object, Object> innerMap = new HashMap<>();
                        for(int j = 0; j < headersVal.length(); j++){
                            if(j==1) {
                                continue;
                            }
                            innerMap.put(headersVal.get(j),playerData.get(j));
                        }
                        map.put(date, innerMap);

                    }
                
                // Print the Map
                for( Map.Entry<Object,Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue() );

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
