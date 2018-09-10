package Interfaces;

import org.apache.commons.csv.CSVRecord;

public class Title {
    private String name;
    private String tconst;
    private String job;

    public String getName(){
        return name;
    }

    public String getJob(){
        return job;
    }

    public String getNameToken(){
        return tconst;
    }

    public void setJob(String job){
        this.job = job;
    }

    public static Title fromRecord(CSVRecord record){
        Title title = new Title();
        title.tconst = record.get("tconst");
        title.name = record.get("primaryTitle");
        title.job = "";
        return title;
    }
}
