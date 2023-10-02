package com.fantasy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import com.fantasy.Queries.PlayerGameLogs;
import com.fantasy.QueryParams.SeasonType;
import com.fantasy.QueryParams.SeasonYear;
import com.opencsv.CSVWriter;

import lombok.AllArgsConstructor;
import lombok.Data;

public class GetStats {

    public static void main(String[] args) {
        GetStats g = new GetStats();

        String initialDate = "02/14/2022";
        String finalDate = "04/14/2023";
        String playerId = "1630578";
        SeasonType seasonType = SeasonType.Regular_Season;
        String seasonYear = SeasonYear.Y22_23;

        g.processPlayerList("TestTeam", initialDate, finalDate, seasonType, seasonYear);
    }

    public void processPlayerList(String inputFileName, String initialDate, String finalDate, SeasonType seasonType,
            String seasonYear) {

        String inputFile = ".\\src\\main\\java\\com\\fantasy\\resources\\" + inputFileName + ".txt";
        List<PlayerStats> statsList = new ArrayList<>();
        List<String> names = new ArrayList<>();

        try {
            // Read values from the text file
            var inputValues = readValuesFromFile(inputFile);

            // Process the input values (replace with your own processing logic)
            for (var player : inputValues.entrySet()) {
                names.add(player.getKey());
                statsList.add(retrieveStats(initialDate, finalDate, player.getValue(), seasonType, seasonYear));
            }
            // Write the processed values to a CSV file
            writeValuesToCSV(inputFileName, statsList, names);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PlayerStats retrieveStats(String initialDate, String finalDate, String playerId, SeasonType seasonType,
            String seasonYear) {

        PlayerGameLogs gameLogRetrieval = new PlayerGameLogs();

        Map<Object, Object> results = gameLogRetrieval.get(initialDate, finalDate, playerId, seasonType, seasonYear);

        List<Double> points = new ArrayList<>();
        List<Double> assists = new ArrayList<>();
        List<Double> oreb = new ArrayList<>();
        List<Double> tss = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : results.entrySet()) {
            Map<Object, Object> values = (Map<Object, Object>) entry.getValue();
            Double PTS = ((Integer) values.get("PTS")).doubleValue();
            Double AST = ((Integer) values.get("AST")).doubleValue();
            Double OREB = ((Integer) values.get("OREB")).doubleValue();
            Double FGA = ((Integer) values.get("FGA")).doubleValue();
            Double FTA = ((Integer) values.get("FTA")).doubleValue();

            double ts = PTS / (2 * (FGA + 0.44 * FTA));
            oreb.add((OREB));
            tss.add(ts);
            assists.add(AST);
            points.add(PTS);
        }

        return new PlayerStats(
                playerId,
                average(points),
                average(tss),
                average(assists),
                average(oreb));

    }

    private double average(List<Double> list) {
        OptionalDouble average = list
                .stream()
                .mapToDouble(a -> a)
                .average();

        return average.getAsDouble();
    }

    // Function to write values to a CSV file
    private static void writeValuesToCSV(String fileName, List<PlayerStats> values, List<String> names)
            throws IOException {

        String path = ".\\src\\main\\java\\com\\fantasy\\resources\\output\\" + fileName + ".csv";
        File file = new File(path);

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // create a List which contains String array
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] { "Name", "ID", "PPG", "APG", "OREB", "TS%" });

            for (int i = 0; i < values.size(); i++) {
                var playerStats = values.get(i);
                data.add(new String[] { names.get(i), playerStats.getId(), playerStats.getPpg().toString(),
                        playerStats.getApg().toString(), playerStats.getOrbpg().toString(),
                        playerStats.getTspg().toString() });

            }

            writer.writeAll(data);

            // closing writer connection
            writer.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // Function to read values from a text file and return them as a list
    private static Map<String, String> readValuesFromFile(String filePath) throws IOException {
        Map<String, String> values = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(",");
                String name = splitLine[0];
                String id = splitLine[1];
                values.put(name, id);
            }
        }
        return values;
    }

    @AllArgsConstructor
    @Data
    private class PlayerStats {
        String id;
        Double ppg;
        Double tspg;
        Double apg;
        Double orbpg;

    }

}
/*
 * PPG ---
 * TS% ---
 * APG ---
 * 3PAr ---
 * FTr --
 * ORB% ---
 * DRB% ---
 * STL%
 * TOV% --
 * USG%
 */
// Turnover Ratio Formula=(Turnovers)*100)/ [(Field Goal Attempts)+(Free Throw
// Attempts*0.44)+(Assists)+(Turnovers)]