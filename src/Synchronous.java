import Interfaces.Actor;
import Interfaces.Title;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.*;

public class Synchronous {

    public static void main(String[] args) throws IOException {
        Scanner userInput = new Scanner(System.in);

        System.out.print("Please enter the path to the IMDB directory: ");
        String path = userInput.nextLine();

        while(true){
            //get the name of the actor
            System.out.print("Please enter a name: ");
            String name = userInput.nextLine();

            //start parsing our files
            Actor actor = parseActor(name, path);

            if(actor == null)
                System.out.println("Actor/ess not found");
            else {

                //writes the name on the first line
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./out/" + name.substring(name.lastIndexOf(" ") + 1) + ".roles")));
                writer.write(name);
                writer.newLine();

                //writes the job
                List<String> list = actor.getJobs();
                for (String job : list) {
                    writer.newLine();
                    writer.write(job);
                    writer.newLine();
                    for(Title title : actor.getTitles(job)){
                        writer.write(title.getName());
                        writer.newLine();
                    }
                }
            }
        }
    }

    private static Actor parseActor(String name, String path) throws IOException {
        Actor actor = null;

        CSVParser parser = CSVFormat.TDF.withFirstRecordAsHeader()
                .parse(new FileReader(new File(path + "/name.basics.tsv")));

        for (CSVRecord record : parser) {
            if (record.get("primaryName").equalsIgnoreCase(name)) {
                actor = Actor.fromRecord(record);
                break;
            }
        }

        if(actor != null) {
            parseTitles(actor, path);
        }

        return actor;
    }

    private static void parseTitles(Actor actor, String path) throws IOException{
        //Parse the principals file
        CSVParser parser = CSVFormat.TDF.withNullString("/N").withFirstRecordAsHeader()
                .parse(new FileReader(new File(path + "/title.principals.tsv")));

        // iterate through the parser
        for (CSVRecord record : parser) {
            if(record.get("nconst").equalsIgnoreCase(actor.getNameToken())) {
                // parse the basics file for the title information
                CSVParser parser2 = CSVFormat.TDF.withFirstRecordAsHeader()
                        .parse(new FileReader(new File(path + "/title.basics.tsv")));
                String titleToken = record.get("tconst");
                for (CSVRecord record2 : parser2) {
                    if(record2.get("tconst").equalsIgnoreCase(titleToken)){
                        actor.addTitle(record.get("job"), Title.fromRecord(record2));
                        break;
                    }
                }
            }
        }
    }
}
