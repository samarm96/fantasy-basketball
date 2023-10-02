package com.fantasy.Utilities;

import okhttp3.Request;

public class HttpUtilities {
    public static Request createRequest(String url) {
        return new Request.Builder()
                .url(url)
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
    }
}
