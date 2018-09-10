package Interfaces;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActorParser {

    private String path;
    private String name;
    private Actor actor;

    public void parse(String path, String name) throws IOException {
        CSVParser parser = CSVFormat.TDF.withFirstRecordAsHeader()
                .parse(new FileReader(new File(path + "/name.basics.tsv")));

        Iterator<CSVRecord> it = parser.iterator();

        while(it.hasNext()){
            CSVRecord record = it.next();
            if(record.get(""))
        }

        if(actor == null){
            System.out.println("Result not found");
        }
        else{
            //Parse the principals file
            parser = CSVFormat.TDF.withFirstRecordAsHeader()
                    .parse(new FileReader(new File(path + "/title.basics.tsv")));

            list = parser.getRecords();

            List<Title> titleResults = new ArrayList<Title>();

            for (CSVRecord record : list) {
                if(record.get("nconst").equalsIgnoreCase(actor.getNameToken())){
                    titleResults.add(Title.fromRecord(record));
                }
            }

            parser = CSVFormat.TDF.withFirstRecordAsHeader()
                    .parse(new FileReader(new File(path + "/title.principals.tsv")));

            list = parser.;

            for(Title title: titleResults){

            }
        }
    }
    public Actor getActor(){
        return actor;
    }

}
