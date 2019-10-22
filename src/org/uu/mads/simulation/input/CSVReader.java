package org.uu.mads.simulation.input;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class CSVReader {

    public CSVReader() throws FileNotFoundException {

        CSVReader file1 = new CSVReader(new FileReader(
                "C:\\Users\\leoze\\Desktop\\artificial-input-data-passengers-01.csv"), ';');
        CSVReader file2 = new CSVReader(new FileReader("yourfile.csv"), ';');
        CSVReader file3 = new CSVReader(new FileReader("yourfile.csv"), ';');
        CSVReader file4 = new CSVReader(new FileReader("yourfile.csv"), ';');
        CSVReader file5 = new CSVReader(new FileReader("yourfile.csv"), ';');

    }


    public CSVReader(FileReader fileReader, char c) throws FileNotFoundException {
        System.out.println("File not found! " + fileReader);
    }
}
