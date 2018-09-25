package Interfaces;

import org.apache.commons.csv.CSVRecord;

public class Title {
    private String name;
    private String tconst;

    public Title() {}

    public Title(String titleToken, String name) {
        tconst = titleToken;
        this.name = name;
    }

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
