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
                for (String job : actor.getJobs()) {
                    writer.newLine();
                    writer.write("Job: " + job);
                    writer.newLine();
                    writer.write("Titles: ");
                    writer.newLine();
                    for(Title title : actor.getTitles(job)){
                        writer.write(title.getName());
                        writer.newLine();
                    }
                    writer.newLine();
                }

                writer.flush();
                writer.close();
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
            actor = parseTitles(actor, path);
        }

        return actor;
    }

    private static Actor parseTitles(Actor actorParam, String path) throws IOException{

        Actor actor = actorParam;
        //Parse the principals file
        //CSVParser parser = CSVFormat.TDF.withNullString("/N").withFirstRecordAsHeader()
                //.parse(new FileReader(new File(path + "/title.principals.tsv")));

        //Using a buffered reader because the CSVParser hates these files for some reason
        BufferedReader br = new BufferedReader(new FileReader(new File(path + "/title.principals.tsv")));
        String[] header = br.readLine().split("\t");
//
//        for(String head : header) {
//            System.out.println(head);
//        }

        Map<String, ArrayList<String>> results = new HashMap<String, ArrayList<String>>();
        // iterate through the file
        String s = br.readLine();
        while (s != null) {
            String[] line = s.split("\t");

            if(line[2].equalsIgnoreCase(actor.getNameToken())){
                if(results.containsKey(line[4])){
                    results.get(line[4]).add(line[0]);  // 0 = tconst, 4 = job
                }
                else {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(line[0]);
                    results.put(line[4], list);
                }
            }
            s = br.readLine();
        }

        br = new BufferedReader(new FileReader(new File(path + "/title.basics.tsv")));

        header = br.readLine().split("\t");

//        for(String head : header) {
//            System.out.println(head);
//        }

        s = br.readLine();
        while(s != null) {
            String[] line = s.split("\t");

            for(String key : results.keySet()) {
                List list = results.get(key);
                if (list.contains(line[0])){
                    actor.addTitle(key, new Title(line[0], line[2]));   // 0 = tconst, 2 = primaryTitle
                }
            }

            s = br.readLine();
        }

        return actor;
    }
}
