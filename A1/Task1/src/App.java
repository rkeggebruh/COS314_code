import java.util.Arrays; // only used for printing and debugging
import java.util.Random;

// ILS - from notes

// The algorithm can be summarized as follows: 
// 1. Generate an initial sol using a constructive heuristic or random method. 
// 2. Apply a local search algorithm to improve the sol. 
// 3. Obtain a local optimum. 
// 4. Perturb the local optimum to obtain a new sol. 
// 5. Apply the local search algorithm to the new sol.
// 6. If the new sol is better than the current sol, update the current sol.
// 7. Repeat steps 4-6 until a stopping criterion is met.

public class App {

    private static final int MAX_ITER = 1000;

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        // Distances from the spec from the spec
        int[][] distanceMatrix = {
                {0, 15, 20, 22, 30}, // Hatfield
                {15, 0, 10, 12, 25}, // Hillcrest
                {20, 10, 0, 8, 22}, // Groenkloef
                {22, 12, 8, 0, 18}, // Prinshof
                {30, 25, 22, 18, 0} // Mamelodi
        };

        String[] names= {"Hatfield", "Hillcrest", "Groenkloef", "Prinshof", "Mamelodi"};

        // get the best route
        int[] bR = ILS_Alg(distanceMatrix);
        // output
        System.out.println("Best Route: " + arrayToString(bR));

        // System.out.println("before FINAL CALL: " + arrayToString(bR));


        System.out.print("Best Route: ");

        for (int r = 0; r < bR.length; r++) {
            // System.out.println("br.length " + bR.length);
            // System.out.println("r " + r);
            System.out.print(names[bR[r]-1]);
            if(r < bR.length-1){
                System.out.print(" -> ");
            }
        }
        System.out.print(" -> Back to Hatfield");

        System.out.println();

        int cost = calcTC(bR, distanceMatrix);
        System.out.print("Total Cost: " + cost);
        System.out.println();



        // ----------------------------------------- //
        //          capture execution time

        
        // Thread.sleep(2000);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Run time for ILS alg: " + executionTime + " milliseconds");
    }

    public static int[] ILS_Alg(int[][] distanceMatrix) {
        // how many campuses
        int numC = distanceMatrix.length;

        // Initial sol.
        int[] curr = genRandom(numC);
        int[] best = curr.clone();
        int bestCost = calcDist(best, distanceMatrix);

        for (int iteration = 0; iteration < MAX_ITER; iteration++) {
            // System.out.println("b4 perturb: " + Arrays.toString(curr));
            
            // perturb curr sol. to change it - swaps 2 vals.
            perturbArr(curr);

            // System.out.println("after perturb: " + Arrays.toString(curr));

            // Local search
            localSearch(curr, distanceMatrix);

            // System.out.println("after SEARCH: " + Arrays.toString(curr));


            // update if better found
            int currentCost = calcDist(curr, distanceMatrix);
            if (currentCost < bestCost) {
                best = curr.clone();
                bestCost = currentCost;
            }
        }

        // System.out.println("best after ils: " + Arrays.toString(curr));


        return best;
    }

    private static int[] genRandom(int size) {
        // size == size of matrix
        int[] sol = new int[size];

        int[] indices = new int[size - 1];
        int index = 0;
        for (int i = 1; i < size; i++) { // exclude hatifle
            indices[index++] = i + 1;
        }

        // Hat = 1st
        sol[0] = 1;
        for (int i = 1; i < size; i++) {
            sol[i] = indices[i - 1];
        }

        // System.out.println("before shuffling: " + Arrays.toString(sol));


        // shuffle time
        shuffle(sol);





        // OLD - does'nt ensure hatfield at 1st
        // for (int i = 0; i < size; i++) {
        //     sol[i] = i + 1;
        // }
        // System.out.println("before shuffling: " + Arrays.toString(sol));
        // make a new array and fill it and then shuffle, then return
        // shuffle(sol);
        // System.out.println("after shuffling: " + Arrays.toString(sol));
        return sol;
    }

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

    private static void perturbArr(int[] sol) {
        // this perturb swaps 2 indexes of arr ( 2 values switch with each other )
        Random random = new Random();
        int ind1 = random.nextInt(sol.length);
        int ind2 = random.nextInt(sol.length);

        int temp = sol[ind1];
        sol[ind1] = sol[ind2];
        sol[ind2] = temp;
    }
        // other helper funcs

    private static int calcDist(int[] sol, int[][] distanceMatrix) {
        int totalDistance = 0;
        for (int i = 0; i < sol.length - 1; i++) {
            totalDistance += distanceMatrix[sol[i] - 1][sol[i + 1] - 1];
        }
        // Add distance from the last campus back to the starting campus
        totalDistance += distanceMatrix[sol[sol.length - 1] - 1][sol[0] - 1];
        return totalDistance;
    }
    
    private static String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int value : array) {
            sb.append(value).append(" ");
        }
        return sb.toString().trim();
    }

    // helper func to calculate the total cost for sol
    private static int calcTC(int[] sol, int[][] costMatrix) {
        int TC = 0;
        for (int i = 0; i < sol.length - 1; i++) {
            // System.out.print("Adding: " + costMatrix[sol[i]-1][sol[i + 1]-1] + " ");
            TC += costMatrix[sol[i]-1][sol[i + 1]-1];
            if(i == sol.length-2){
                int row = sol[i+1]-1;
                // System.out.println("row: " + row);

                // System.out.print("Add back to hat: " + costMatrix[row][0]);

                TC += costMatrix[row][0];
            }
        }
        return TC;
    }




















    // all for localSearch funcs - everything below this pt
    // 2. Apply a local search algorithm to improve the sol. 
    // checks end of arr and if the outcome will be better if swapped - do so.
    // until stopping criteria met



    // Hint:
    // You can use Iterated Local Search by first generating an initial sol, for example, using random selection.
    // You can then apply a local search to the initial sol e.g swapping two consecutive campuses on a sol and
    // then apply a perturbation operator to the sol, for example, by randomly swapping any two campuses. After
    // the perturbation, you can use local search to optimize the sol. The process of perturbation and local search
    // is repeated for a fixed number of iterations or until a stopping criterion is met such as no improvement after a set
    // number of consecutive iterations.

    private static void localSearch(int[] sol, int[][] distanceMatrix) {
        // LS (2-opt)
        boolean better;
        do {
            better = false;
            for (int i = 1; i < sol.length - 2; i++) {
                for (int j = i + 1; j < sol.length - 1; j++) {
                    int costifswapped = COSTDIFF(sol, i, j, distanceMatrix);
                    if (costifswapped < 0) {
                        reverseArr(sol, i, j);
                        better = true;
                    }
                }
            }
        } while (better);
    }

    // helper func for the LSearch - calcs the diff (cost) if two vals are swapped
    private static int COSTDIFF(int[] sol, int i, int j, int[][] distanceMatrix) {
        int a = sol[i - 1] - 1;
        int b = sol[i] - 1;
        int c = sol[j] - 1;
        int d = sol[j + 1] - 1;

        int before = distanceMatrix[a][b] + distanceMatrix[c][d];
        int after = distanceMatrix[a][c] + distanceMatrix[b][d];

        return after - before;
    }

    // helper func for the LSearch - V. basic
    private static void reverseArr(int[] array, int start, int end) {
        while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
    }





}
