package org.uu.mads.simulation.input;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*public class InputReader {

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
                double timeStart = (double) Float.parseFloat(data[2]);
                double timeEnd = (double) Float.parseFloat(data[3]);
                
                int hourStart  = (int) Math.floor(timeStart);
                int minStart = (int) ((timeStart - hourStart) * 60);

                LocalTime time1 = LocalTime.of(hourStart, minStart);
                
                int hourEnd =  (int) Math.floor(timeEnd);
                int minEnd  =  (int) ((timeEnd - hourEnd) * 60);
                
                LocalTime time2 = LocalTime.of(hourEnd, minEnd);

                float passIn = Float.parseFloat(data[4]);
                float passOut = Float.parseFloat(data[5]);

                dataList.add(new InputStationData(name, direction, time1, time2, passIn, passOut));
            }
            
            throw(new Error());
        }


    public void outPut() throws IOException {

        System.out.println(dataList.toString());

    }

    public InputReader(FileReader fileReader, char c) throws FileNotFoundException {
        System.out.println("File not found! " + fileReader);
    }
} */
