import java.util.HashMap;
import java.io.*;

// this class is for reading the files

public class Reader {

    public static HashMap<String, Knapsack> readKnapsackData(String folderName) {
        
        // System.out.println("in reader");

        HashMap<String, Knapsack> knapsacks = new HashMap<>();
        File folder = new File(folderName);
    
        File[] listOfFiles = folder.listFiles();
    
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    Knapsack knapsack = null;
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        // System.out.println("---- Reading file: " + file.getName());
                        
                        
                        // ignore the excel file
                        if ("Known Optimums.xlsx".equals(file.getName())) {
                            // System.out.println("found the xlsx file");
                            break;
                        }
                        
                        

                        // First line has two numbers, the first is the number of items, the second is
                        // the capacity of the knapsack
                        String line = br.readLine();
                        // System.out.println("Reading line: " + line);

                        
                        String[] lineSplit = line.split(" ");
                        int numItems = Integer.parseInt(lineSplit[0]);
                        int capacity = Integer.parseInt(lineSplit[1]);
                        knapsack = new Knapsack(capacity, numItems);
                        
                        if(!line.equals("") || line != null){
                            while ((line = br.readLine()) != null) {
                                // System.out.println("Reading line: ." + line + ". ..");
    
                                
                                lineSplit = line.split(" ");
                                if (line.isEmpty()){
                                    // System.out.println("stupid null");
                                }
                                else{
                                    // System.out.println("not empty 1: " + lineSplit[0] + ". ..");

                                    Item item = new Item(Double.parseDouble(lineSplit[0]), Double.parseDouble(lineSplit[1]));
                                    knapsack.addItem(item);
                                }
                                
                            }
                        }
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (knapsack != null) {
                        knapsacks.put(file.getName(), knapsack);
                    }
                }
            }
        } else {
            System.out.println("No files found in the folder: " + folderName);
        }
    
        return knapsacks;
    }

}
