package org.uu.mads.simulation;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class CSVReader {

    CSVReader reader = new CSVReader(new FileReader("yourfile.csv"), ';');


    public CSVReader(FileReader fileReader, char c) throws FileNotFoundException {
    }
}
