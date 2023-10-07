package com.fantasy.GUI;

import javax.swing.*;

import com.fantasy.GetStats;
import com.fantasy.QueryParams.SeasonType;
import com.fantasy.QueryParams.SeasonYear;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BasketballStatsApplication extends JFrame {
    private JComboBox<String> fileComboBox;
    private JDateChooser initialDateChooser;
    private JDateChooser finalDateChooser;
    private JComboBox<SeasonType> seasonTypeComboBox;
    private JComboBox<String> seasonYearComboBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BasketballStatsApplication();
            }
        });
    }

    public BasketballStatsApplication() {
        try{
                    // Set up the JFrame
                    setTitle("Basketball Stats Retriever");
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    setSize(400, 300);
                    this.setLayout(new GridLayout(7, 2) );
                    
                    // Create a combo box to select the team text file to load
                    fileComboBox = new JComboBox<String>(); 
                    configureFileNameCombo(fileComboBox); 
                    fileComboBox.setLocation(0, 1);

                    // Create date choosers for initial and final dates
                    initialDateChooser = new JDateChooser();
                    initialDateChooser.setLocation(0, 2);

                    finalDateChooser = new JDateChooser();
                    finalDateChooser.setLocation(0, 3);

                    // Create a combo box for the season type
                    seasonTypeComboBox = new JComboBox<SeasonType>(SeasonType.values()); 
                    seasonTypeComboBox.setLocation(0, 4);

                    // Create a combo box for the season year
                    seasonYearComboBox = new JComboBox<>(SeasonYear.returnAsArray());
                    seasonYearComboBox.setLocation(0, 5);

                    // Create a button to submit the selections
                    JButton submitButton = new JButton("Submit");
                    submitButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");                            // Perform actions based on the selected values

                            // Retrieve selected values
                            String selectedFile = (String) fileComboBox.getSelectedItem();
                            Date initialDate = initialDateChooser.getDate();
                            Date finalDate = finalDateChooser.getDate();
                            SeasonType seasonType = (SeasonType) seasonTypeComboBox.getSelectedItem();
                            String seasonYear = (String) seasonYearComboBox.getSelectedItem();

                            // Use the inputted data to create the CSV
                            GetStats getStats = new GetStats();

                            getStats.processPlayerList(selectedFile, 
                                                    dateFormat.format(initialDate).toString(), 
                                                    dateFormat.format(finalDate).toString(), 
                                                    seasonType, 
                                                    seasonYear);
                        }
                    });
                    submitButton.setLocation(0, 6);

                    // Add components to the JFrame
                    add(new JLabel("Select File: "));
                    add(fileComboBox);
                    add(new JLabel("Select Initial Date: "));
                    add(initialDateChooser);
                    add(new JLabel("Select Final Date: "));
                    add(finalDateChooser);
                    add(new JLabel("Select Season Type: "));
                    add(seasonTypeComboBox);
                    add(new JLabel("Select Season year: "));
                    add(seasonYearComboBox);
                    add(new JLabel(""));
                    add(submitButton);

                    // Make the JFrame visible
                    setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    private void configureFileNameCombo(JComboBox<String> fileComboBox) {

        // Create a folder with some sample files
        File folder = new File("./src/main/java/com/fantasy/resources/"); // Change this to your specified folder
        String[] fileNames = folder.list();
        // Create a combo box for file names
        for(int i = 0; i < fileNames.length; i++) {
            String value = fileNames[i];
            if(value.contains(".txt")) {
                fileComboBox.addItem(value);
            }
            
        }  
    }

}
