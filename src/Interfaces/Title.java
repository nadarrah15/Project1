package Interfaces;

import org.apache.commons.csv.CSVRecord;

public class Title {
    private String name;
    private String tconst;

    public String getName(){
        return name;
    }

    public String getNameToken(){
        return tconst;
    }

    public static Title fromRecord(CSVRecord record){
        Title title = new Title();
        title.tconst = record.get("tconst");
        title.name = record.get("primaryTitle");
        return title;
    }
}
