package ru.clevertec.check.utils;

import ru.clevertec.check.exception.InternalServerErrorException;

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
            throw new InternalServerErrorException();
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
            throw new InternalServerErrorException();
        }
    }

    public static void writeCSV(String filePath, String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("ERROR");
            bw.newLine();
            bw.write(message);
        } catch (IOException e) {
            throw new InternalServerErrorException();
        }
    }
}
