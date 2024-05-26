import java.util.Arrays;
import java.util.HashMap;
import java.io.File;
import java.util.Random;

public class Main extends Reader {
    public static void main(String[] args) {
        int no_runs = 1;

        // for marker - can change to false if you only want to check one
        boolean runGA = true;
        boolean runLocalSearch = true;

        HashMap<String, Double> optimums = new HashMap<String, Double>();
        optimums.put("f1_l-d_kp_10_269", 295.0);
        optimums.put("f2_l-d_kp_20_878", 1024.0);
        optimums.put("f3_l-d_kp_4_20", 35.0);
        optimums.put("f4_l-d_kp_4_11", 23.0);
        optimums.put("f5_l-d_kp_15_375", 481.0694);
        optimums.put("f6_l-d_kp_10_60", 52.0);
        optimums.put("f7_l-d_kp_7_50", 107.0);
        optimums.put("f8_l-d_kp_23_10000", 9767.0);
        optimums.put("f9_l-d_kp_5_80", 130.0);
        optimums.put("f10_l-d_kp_20_879", 1025.0);
        optimums.put("knapPI_1_100_1000_1", 9147.0);

        HashMap<String, Knapsack> knapsacks = new HashMap<String, Knapsack>();
        String folderPath = "Knapsack Instances";
        System.out.println("---- Reading file: " + folderPath);

        knapsacks = readKnapsackData(folderPath);

        boolean first = true;

        // Run Genetic Algorithm for each knapsack instance
        if (runGA) {
            for (String key : knapsacks.keySet()) {
                Knapsack knapsack = knapsacks.get(key);
                Random random = new Random();
                GA ga = new GA(knapsack);

                if (first) {
                    first = false;


                    System.out.println("----------------------------------------");
                    System.out.println("\033[38;5;127mRunning the GA algorithm.\033[0m ");
                    System.out.println("----------------------------------------");
                }

                // Get the seed value
                int seedValue = random.nextInt();

                // Run the GA
                long startTime = System.currentTimeMillis();
                ga.run(no_runs, seedValue);
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                // Display results
                System.out.print("\033[38;5;127mProblem Instance:\033[0m ");
                System.out.println(key);
                System.out.print("\033[38;5;127mAlgorithm: GA\033[0m ");
                System.out.print("\033[38;5;127mSeed Value:\033[0m ");
                System.out.println(seedValue);
                System.out.print("\033[38;5;127mBest Solution:\033[0m ");
                System.out.println(ga.getBestFitness());
                // System.out.print("\033[38;5;127mBest SOL for GA:\033[0m ");
                System.out.println(Arrays.toString(ga.getBestKnapsack()));

                // Boolean[] knapsackprint = ga.getBestKnapsack();
                // String[] printableKnapsack = new String[knapsackprint.length];

                // for (int i = 0; i < knapsackprint.length; i++) {
                //     printableKnapsack[i] = knapsackprint[i] ? "t" : "f";
                // }

                // System.out.println(Arrays.toString(printableKnapsack));

                System.out.print("\033[38;5;127mKnown Optimum:\033[0m ");
                System.out.println(optimums.getOrDefault(key, -1.0));
                System.out.print("\033[38;5;127mRuntime (seconds):\033[0m ");
                System.out.println(executionTime);


                System.out.println();
            }
        }

        // run GA_w/_LS
        if (runLocalSearch) {
            System.out.println("----------------------------------------------");
            System.out.println("\033[34mRunning the GA algorithm with a local search.\033[0m");
            System.out.println("----------------------------------------------");

            for (String instance : knapsacks.keySet()) {
                Knapsack knapsack = knapsacks.get(instance);
                Random random = new Random(); // Create a new random object for each instance
                GAWithLS gaLocalSearch = new GAWithLS(knapsack);

                // Get the seed value
                int seedValue = random.nextInt();

                // Run the GA with Local Search
                long startTime = System.currentTimeMillis();
                gaLocalSearch.run();

                // local search 
                gaLocalSearch.applyLocalSearch();
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

               // to store bset sol
               Boolean[] solution = gaLocalSearch.getBestKnapsack();

            //    // calc fit
            //    int tw = knapsack.getWeight(solution);
            //    int tv = knapsack.getValue(solution);

                // Display results
                System.out.print("\033[38;5;27mProblem Instance:\033[0m ");
                System.out.println(instance);
                System.out.print("\033[38;5;27mAlgorithm: GA with LS\033[0m ");

                System.out.print("\033[38;5;27mSeed Value:\033[0m ");
                System.out.println(seedValue);
                System.out.print("\033[38;5;27mBest Solution:\033[0m ");
                System.out.println(gaLocalSearch.getBestFitness());
                System.out.println(Arrays.toString(solution));
                // System.out.print("\033[38;5;27mBest Solution for GAwithLS:\033[0m ");
                // below statement and print (check func)
                // System.out.print("\033[38;5;27mFitness:\033[0m ");
                // System.out.println("\033[38;5;27m" + knapsack.getTotalValue() + "\033[0m");
                System.out.print("\033[38;5;27mKnown Optimum:\033[0m ");
                System.out.println(optimums.getOrDefault(instance, -1.0));
                System.out.print("\033[38;5;27mRuntime (seconds):\033[0m ");
                System.out.println(executionTime);
                // System.out.println("\033[38;5;27mTotal Weight: " + knapsack.getTotalWeight() + "\033[0m");
                // System.out.println("\033[38;5;27mTotal Value: " + knapsack.getTotalValue() + "\033[0m");
                System.out.println();

            }
        }
    }
}

