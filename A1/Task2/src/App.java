// SIMULATED ANNEALING
import java.util.Arrays;
import java.util.Random;

public class App {

    // consts
    private static final int MAX_ITERS = 1000;
    private static final double INIT_TEMP = 1000.0;
    private static final double COOL_RATE = 0.95;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Cost matrix between campuses
        int[][] costMatrix = {
                {0, 15, 20, 22, 30},
                {15, 0, 10, 12, 25},
                {20, 10, 0, 8, 22},
                {22, 12, 8, 0, 18},
                {30, 25, 22, 18, 0}
        };

        String[] names= {"Hatfield", "Hillcrest", "Groenkloef", "Prinshof", "Mamelodi"};


        // Run Simulated Annealing
        int[] bR = runSimulatedAnnealing(costMatrix);
        System.out.println("Best Route: " + Arrays.toString(bR));
    
        System.out.print("Best Route: ");

        for (int r = 0; r < bR.length; r++) {
            // System.out.println("br.length " + bR.length);
            // System.out.println("r " + r);
            System.out.print(names[bR[r]]);
            if(r < bR.length-1){
                System.out.print(" -> ");
            }
        }
        System.out.print(" -> Back to Hatfield");

        System.out.println();

        int cost = calcTC(bR, costMatrix);
        System.out.print("Total Cost: " + cost);


        // ----------------------------------------- //
        //          capture execution time
        System.out.println();

        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Run time for Simulated Annealing alg: " + executionTime + " milliseconds");
    
    }

    public static int[] runSimulatedAnnealing(int[][] costMatrix) {
        int numCampuses = costMatrix.length;

        int[] currentsol = genRandom(numCampuses);
        int[] bestsol = Arrays.copyOf(currentsol, numCampuses);
        int currentCost = calcTC(currentsol, costMatrix);
        int bestCost = currentCost;

        double temperature = INIT_TEMP;

        for (int i = 0; i < MAX_ITERS; i++) {
            // perturb
            int[] newsol = perturbArr(currentsol);
            int newCost = calcTC(newsol, costMatrix);

            if (acceptNewsol(currentCost, newCost, temperature)) {
                currentsol = Arrays.copyOf(newsol, numCampuses);
                currentCost = newCost;

                // update if better found
                if (currentCost < bestCost) {
                    bestsol = Arrays.copyOf(currentsol, numCampuses);
                    bestCost = currentCost;
                }
            }

            // cool
            temperature *= COOL_RATE;
        }

        return bestsol;
    }

    private static int[] genRandom(int size) {
        // make initial arr full of randoms
        int[] sol = new int[size];
        for (int i = 0; i < size; i++) {
            sol[i] = i;
        }
        shuffle(sol);
        return sol;
    }

    // private static int[] genRandom(int size) {
    //     int[] sol = new int[size];

    //     int[] indices = new int[size - 1];
    //     int index = 0;
    //     for (int i = 1; i < size; i++) { // exclude hatifle
    //         indices[index++] = i + 1;
    //     }

    //     // Hat = 1st
    //     sol[0] = 1;
    //     for (int i = 1; i < size; i++) {
    //         sol[i] = indices[i - 1];
    //     }

    //     shuffle(sol);
    //     return sol;
    // }

    private static void shuffle(int[] array) {
        Random random = new Random();
        // swaps to make it shuffled - ensure hat 1st
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i) + 1;

                int temp = array[index];
                array[index] = array[i];
                array[i] = temp;
        }
    }

    private static int[] perturbArr(int[] sol) {
        // this perturb swaps 2 indexes of arr ( 2 vals switch with each other )
        Random random = new Random();
        int ind1 = random.nextInt(sol.length);
        int ind2 = random.nextInt(sol.length);

        int[] newsol = Arrays.copyOf(sol, sol.length);
        int temp = newsol[ind1];
        newsol[ind1] = newsol[ind2];
        newsol[ind2] = temp;

        return newsol;
    }

    // helper func (T/F) - accp or mot
    private static boolean acceptNewsol(int currentCost, int newCost, double temperature) {
        //accpt if new is smaller (shorter == better)
        if (newCost < currentCost) {
            return true;
        } else {
            // diff of costs
            int costDiff = currentCost - newCost;
            
            // calc accpt - notes (use temp and exp)
            double exp = (double) costDiff / temperature;
            double acceptanceProbability = Math.exp(exp);
            
            double rando = Math.random();
            
            if (rando < acceptanceProbability) {
                // if rando val < acceptance probability, accept the newSol
                return true;
            } else {
                // else, reject the newSol
                return false;
            }
        }
    }

    // helper func to calculate the total cost for sol
    private static int calcTC(int[] sol, int[][] costMatrix) {
        System.out.println();
        int TC = 0;
        for (int i = 0; i < sol.length - 1; i++) {
            System.out.print("Adding: " + costMatrix[sol[i]][sol[i + 1]] + " ");
            TC += costMatrix[sol[i]][sol[i + 1]];
            if(i == sol.length-2){
                System.out.print("Add back to hat: " + costMatrix[sol[0]][sol[i+1]]);
                TC += costMatrix[sol[0]][sol[i+1]];
            }
        }
        return TC;
    }
}
