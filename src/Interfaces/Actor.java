package Interfaces;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actor {
    private String name;
    private String nconst;
    private Map<String, ArrayList<Title>> jobs;

    public String getName(){
        return name;
    }

    public String getNameToken(){
        return nconst;
    }

    public ArrayList<String> getJobs() {
        return new ArrayList<String>(jobs.keySet());
    }

    public ArrayList<Title> getTitles(String job){
        return jobs.get(job);
    }

    public void addTitle(String job, Title title) {
        if(jobs.containsKey(job))
            jobs.get(job).add(title);
        else {
            ArrayList<Title> arr = new ArrayList<Title>();
            arr.add(title);
            jobs.put(job, arr);
        }
    }

    public static Actor fromRecord(CSVRecord record){
        Actor actor = new Actor();
        actor.name = record.get("primaryName");
        actor.nconst = record.get("nconst");
        actor.jobs = new HashMap<String, ArrayList<Title>>();
        return actor;
    }
}
