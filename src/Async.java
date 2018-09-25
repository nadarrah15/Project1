import Interfaces.Actor;
import Interfaces.WorkerThread;

import java.io.*;
import java.util.*;

public class Async {

    private volatile static boolean found = false;
    private static WorkerThread workers = new WorkerThread();


    public static void main(String[] args) throws IOException {
        Scanner userInput = new Scanner(System.in);

        System.out.print("Please enter the path to the IMDB directory: ");
        String path = userInput.nextLine();

        while(true){
            //get the name of the actor
            System.out.print("Please enter a name: ");
            String name = userInput.nextLine();
            workers.add(new ActorSearcher(path, name));

            workers.start();
//            if(actor == null)
//                System.out.println("Actor/ess not found");
//            else {
//
//                //writes the name on the first line
//                BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./out/" + name.substring(name.lastIndexOf(" ") + 1) + ".roles")));
//                writer.write(name);
//                writer.newLine();
//
//                //writes the job
//                for (String job : actor.getJobs()) {
//                    writer.newLine();
//                    writer.write("Job: " + job);
//                    writer.newLine();
//                    writer.write("Titles: ");
//                    writer.newLine();
//                    for(Title title : actor.getTitles(job)){
//                        writer.write(title.getName());
//                        writer.newLine();
//                    }
//                    writer.newLine();
//                }
//
//                writer.flush();
//                writer.close();
//            }


        }
    }

    private static class ActorSearcher implements Runnable{

        private String path;
        private String name;

        public ActorSearcher(String path, String name) {
            this.path = path;
            this.name = name;
        }

        @Override
        public void run() {
            try {   // lazy catching
                BufferedReader br = new BufferedReader(new FileReader(new File(path + "/name.basics.tsv")));
                String line = br.readLine();
                while(line != null){

                    String[] lineSplit = line.split("\t");

                    if(name.equalsIgnoreCase(lineSplit[1])) {   // 1 = primaryName, 0 = nconst
                        Actor actor = new Actor(lineSplit[1], lineSplit[0]);
                        File file = new File("./out/" + actor.getName().substring(actor.getName().lastIndexOf(" ") + 1) + ".roles");
                        workers.add(new PrinterThread(actor.getName() + "\n", file, true));
                        workers.add(new TitlesThread(actor, path));
                        break;
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static class TitlesThread implements Runnable {

        private Actor actor;
        private String path;

        public TitlesThread(Actor actor, String path) {
            this.actor = actor;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                //Parse the principals file

                BufferedReader br = new BufferedReader(new FileReader(new File(path + "/title.principals.tsv")));

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

                s = br.readLine();
                while(s != null) {
                    String[] line = s.split("\t");

                    for(String key : results.keySet()) {
                        List list = results.get(key);
                        if (list.contains(line[0])){ // 0 = tconst, 2 = primaryTitle
                            list.set(list.indexOf(line[0]), line[2]);
                        }
                    }

                    s = br.readLine();
                }

                File file = new File("./out/" + actor.getName().substring(actor.getName().lastIndexOf(" ") + 1) + ".roles");
                for(String job : results.keySet()){
                    workers.add(new PrinterThread("Job: " + job + "\nTitle:",file,true));
                    List<String> list = results.get(job);
                    for(String title : list) {
                        workers.add(new PrinterThread(title, file, true));
                    }
                    workers.add(new PrinterThread("\n", file, true));
                }
                System.out.println("1");
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static class PrinterThread implements Runnable {

        private String printMe;
        private boolean append;
        private File file;

        public PrinterThread(String print, File file, boolean append) {
            printMe = print;
            this.append = append;
            this.file = file;
        }

        @Override
        public void run() {
            try {   // lazy catching
                synchronized (file){
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));
                    writer.append(printMe);
                    writer.newLine();
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
