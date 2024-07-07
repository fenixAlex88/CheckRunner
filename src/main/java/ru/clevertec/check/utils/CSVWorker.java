package ru.clevertec.check.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVWorker {
    /**
     * Читает данные из CSV-файла и возвращает их в виде списка массивов строк.
     *
     * @param filePath Путь к CSV-файлу.
     * @param delimiter Разделитель данных в файле.
     * @return Список массивов строк с данными.
     */
    public static List<String[]> readCSV(String filePath, String delimiter) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(delimiter);
                data.add(lineData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Записывает данные в CSV-файл.
     *
     * @param filePath Путь к CSV-файлу для записи.
     * @param data Список массивов строк с данными для записи.
     * @param delimiter Разделитель данных в файле.
     */
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
}
