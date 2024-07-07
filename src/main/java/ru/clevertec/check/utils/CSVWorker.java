package ru.clevertec.check.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVWorker {

    public static List<String[]> readCSV(String filePath, String delimiter) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(delimiter);
                data.add(lineData);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return data;
    }


    public static void writeCSV(String filePath, List<String[]> data, String delimiter) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] lineData : data) {
                bw.write(String.join(delimiter, lineData));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeCSV(String filePath, Exception err) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("ERROR");
            bw.newLine();
            bw.write(err.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
