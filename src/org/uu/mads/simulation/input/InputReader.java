package org.uu.mads.simulation.input;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InputReader {

    public BufferedReader reader;
    BufferedReader reader2;
    BufferedReader reader3;
    BufferedReader reader4;
    BufferedReader reader5;

    public List<BufferedReader> bufferList;

    ArrayList<InputStationData> dataList;

    public InputReader(File file) throws IOException {

        dataList = new ArrayList<InputStationData>();
        this.reader = new BufferedReader(new FileReader(file));

            String row;
            reader.readLine();
            while ((row = reader.readLine()) != null) {
                String[] data = row.split(";");
                String name = data[0];
                int direction = Integer.parseInt(data[1]);
                int hour1 = (int) Float.parseFloat(data[2]);
                int hour2 = 0;
                if (hour1 == 21) {
                    hour2 = 30;
                }

                LocalTime time1 = LocalTime.of(hour1, hour2);
                hour1 = (int) Float.parseFloat(data[3]);
                hour2 = 0;
                if (hour1 == 21) {
                    hour2 = 30;
                }
                LocalTime time2 = LocalTime.of(hour1, hour2);

                float passIn = Float.parseFloat(data[4]);
                float passOut = Float.parseFloat(data[5]);

                dataList.add(new InputStationData(name, direction, time1, time2, passIn, passOut));
            }
        }


    public void outPut() throws IOException {

        System.out.println(dataList.toString());

    }

    public InputReader(FileReader fileReader, char c) throws FileNotFoundException {
        System.out.println("File not found! " + fileReader);
    }
}
