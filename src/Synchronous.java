import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Synchronous {

    public static void main(String[] args) throws IOException {
        while(true){
            System.out.print("Please enter a name: ");
            Scanner userInput = new Scanner(System.in);
            String in = userInput.nextLine();

            CSVParser parser = CSVFormat.TDF.withHeader("nconst", "primaryName", "birthYear", "deathYear", "primaryProfession", "knownForTitles")
                    .parse(new BufferedReader(new FileReader(new File("~/IMDB/name.basics.tsv"))));

            List<CSVRecord> list = parser.getRecords();

            CSVRecord result = null;
            for (CSVRecord record : list) {
                if(record.get("primaryName").equalsIgnoreCase(in)) {
                    result = record;
                    break;
                }
            }

            if(result == null){
                System.out.println("Result not found");
            }
            else{

            }
        }
    }
}
