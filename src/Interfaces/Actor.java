package Interfaces;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

public class Actor {
    private String name;
    private String nconst;
    private ArrayList<Title> titles;

    public String getName(){
        return name;
    }

    public String getNameToken(){
        return nconst;
    }

    public ArrayList<Title> getTitles(){
        return titles;
    }

    public boolean addTitle(Title t) {
        return titles.add(t);
    }

    public boolean addTitle(List<Title> list){
        for (Title title : list) {
            if(!titles.add(title))
                return false;
        }

        return true;
    }

    public static Actor fromRecord(CSVRecord record){
        Actor actor = new Actor();
        actor.name = record.get("primaryName");
        actor.nconst = record.get("nconst");
        actor.titles = new ArrayList<Title>();
        return actor;
    }
}
