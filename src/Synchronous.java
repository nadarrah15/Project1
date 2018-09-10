import Interfaces.ActorParser;

import java.io.*;
import java.util.Scanner;

public class Synchronous {

    public static void main(String[] args) throws IOException {
        Scanner userInput = new Scanner(System.in);
        ActorParser actorParser = new ActorParser();

        System.out.print("Please enter the path to the IMDB directory: ");
        String path = userInput.nextLine();

        while(true){
            //get the name of the actor
            System.out.print("Please enter a name: ");
            String name = userInput.nextLine();

            //start parsing our files
            actorParser.parse(path, name);

            //writes the name on the first line
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./out/" + name.substring(name.lastIndexOf(" ") + 1) + ".roles")));
            writer.write(name, 0, name.length());
            writer.newLine();

            //writes the
        }
    }
}
