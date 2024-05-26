import java.util.ArrayList;
import java.util.Random;

public class GA extends Reader {

    // -------------------------------------------------------------------------
    //                 below are the initialization vars.
    // -------------------------------------------------------------------------

    Knapsack knapsack;
    ArrayList<Boolean[]> nextGenPop;
    ArrayList<Boolean[]> knapsackPop;

    Boolean[] bestKnapsack;

    double fittest;
    int noImprovement = 0;
    double avgFit = 0;

    ArrayList<Boolean[]> best;

    int bestIteration = 0;

    // GA Params
    final double TOURNAMENT = 0.2;
    final double CROSS = 0.7;
    final double MUTATE = 0.6;
    final int MAX_GEN = 700;
    final int STOP_ITERS = 350;
    final int PENALTY = 10;
    final double INIT_BIT_PROB = 0.1;
    final int POP_Mult = 10;

    // other ga params
    int popSize;
    int tournamentSize;

    public GA(Knapsack initalKnapsack) {
        knapsack = initalKnapsack;

        //get pop size from knapsakc and then make pop size bigger 
        popSize = (int) (initalKnapsack.getItems().size() * POP_Mult);

        // see helper func @ end of file
        tournamentSize = calculateTournamentSize(popSize);

        // see helper func
        initializePop();

        // evolves pop
        for (int i = 0; i < MAX_GEN; i++) {
            if (noImprovement >= STOP_ITERS) {
                break;
            }
            run();
        }

        setBestKnapsack();

    }








    // -------------------------------------------------------------------------
    //                 below are the main funcs for the GA alg
    // -------------------------------------------------------------------------

    public void run() {
        // basic steps for ga
        tournamentSelect();
        singleCrossover();
        singleBitMutate();
        replacePop();
        knapsackPop = nextGenPop;
    }

    // ------------------------- selection method (I chose tournament) ---------------------------
    public void tournamentSelect() {
        best = new ArrayList<Boolean[]>();

        while (knapsackPop.size() > 0 && best.size() < popSize) {
            ArrayList<Boolean[]> competitors = new ArrayList<Boolean[]>();
            for (int i = 0; i < tournamentSize; i++) {
                int randomIndex = (int) (Math.random() * knapsackPop.size());
                competitors.add(knapsackPop.get(randomIndex));
            }

            Boolean[] bestSol = new Boolean[knapsack.getItems().size()];
            double bestSolFitness = -1 * Double.MAX_VALUE;

            for (int i = 0; i < competitors.size(); i++) {
                double competitorFitness = getSumFitness(competitors.get(i));
                if (competitorFitness > bestSolFitness) {
                    bestSol = competitors.get(i);
                    bestSolFitness = competitorFitness;
                }
            }

            best.add(bestSol);
        }
    }

    // ------------------------- crossover (single point) ---------------------------
    public void singleCrossover() {
        nextGenPop = new ArrayList<Boolean[]>();

        for (int i = 0; i < best.size(); i += 2) {
            Boolean[] parent1 = best.get(i);
            Boolean[] parent2 = best.get(i + 1);

            if (Math.random() < CROSS) {
                int crossoverPoint = (int) (Math.random() * knapsack.getItems().size());
                Boolean[] child1 = new Boolean[knapsack.getItems().size()];
                Boolean[] child2 = new Boolean[knapsack.getItems().size()];

                for (int j = 0; j < crossoverPoint; j++) {
                    child1[j] = parent1[j];
                    child2[j] = parent2[j];
                }

                for (int j = crossoverPoint; j < knapsack.getItems().size(); j++) {
                    child1[j] = parent2[j];
                    child2[j] = parent1[j];
                }

                nextGenPop.add(child1);
                nextGenPop.add(child2);
            } else {
                nextGenPop.add(parent1);
                nextGenPop.add(parent2);
            }
        }
    }

    // ------------------------- mutate (single point) ---------------------------
    public void singleBitMutate() {
        for (int i = 0; i < nextGenPop.size(); i++) {
            Boolean[] chromosome = nextGenPop.get(i);
            if (Math.random() < MUTATE) {
                int mutationPoint = (int) (Math.random() * knapsack.getItems().size());
                chromosome[mutationPoint] = !chromosome[mutationPoint];
            }
        }
    }

    // ------------------------- replace the pop. ----- ---------------------------
    public void replacePop() {
        double currentavgFit = getavgFit();
        if (currentavgFit > avgFit) {
            avgFit = currentavgFit;
            bestIteration += noImprovement;
            noImprovement = 0;
            knapsackPop = new ArrayList<Boolean[]>();
        } else {
            noImprovement++;

            while (nextGenPop.size() < popSize) {
                Boolean[] chromosome = new Boolean[knapsack.getItems().size()];
                for (int j = 0; j < knapsack.getItems().size(); j++) {
                    chromosome[j] = Math.random() < 0.5;
                }
                nextGenPop.add(chromosome);
            }

            knapsackPop = nextGenPop;
        }
    }


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // -------------------------------------------------------------------------
    //             variations of the run funcs() - needed for main
    // -------------------------------------------------------------------------

    // run GA with rando seed and no. iters
    public void run(int no_runs, int seed) {
        Random random = new Random(seed);
        for (int i = 0; i < no_runs; i++) {
            for (int j = 0; j < MAX_GEN; j++) {
                if (noImprovement >= STOP_ITERS) {
                    break;
                }
                // keep it random/stochastic (notes)
                Random localRandom = new Random(random.nextLong());
                run(localRandom);
            }
        }
    }

    // run GA with rando obj
    private void run(Random random) {
        run();
    }













    // -------------------------------------------------------------------------
    //                  below are all getters and setters
    // -------------------------------------------------------------------------


    public Boolean[] getBestKnapsack() {
        return bestKnapsack;
    }

    public double getBestFitness() {
        return fittest;
    }

    public double getPenFit(Boolean[] chromosome) {
        double fitness = 0;
        double weight = knapsack.getWeight(chromosome);
        double penalty = Math.max(0, weight - knapsack.getCapacity()) * PENALTY;
        fitness = knapsack.getValue(chromosome) - penalty;
        return fitness;
    }

    public double getSumFitness(Boolean[] chromosome) {
        double fitness = 0;
        double weight = knapsack.getWeight(chromosome);
        double value = knapsack.getValue(chromosome);
        if (weight <= knapsack.getCapacity()) {
            fitness = value;
        }

        if (fitness % 1 > 0.0001) {
            fitness = Math.round(fitness * 10000.0) / 10000.0;
        }

        return fitness;
    }

    public double getavgFit() {
        double avgFit = 0;
        for (int i = 0; i < popSize; i++) {
            avgFit += getPenFit(knapsackPop.get(i));
        }
        avgFit /= popSize;
        return avgFit;
    }

    public void setBestKnapsack() {
        int bestIndex = 0;
        double fittest = 0;

        for (int i = 0; i < popSize; i++) {
            double fitness = getSumFitness(knapsackPop.get(i));
            if (fitness > fittest) {
                fittest = fitness;
                bestIndex = i;
            }
        }

        bestKnapsack = knapsackPop.get(bestIndex);
        this.fittest = fittest;

    }















    // -------------------------------------------------------------------------
    //                 below are the helper funcs
    // -------------------------------------------------------------------------
    
    private int calculateTournamentSize(int populationSize) {
        int size = (int) (populationSize * TOURNAMENT);
        return Math.max(2, size % 2 == 1 ? size + 1 : size);
    }
    
    private void initializePop() {
        knapsackPop = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            knapsackPop.add(makeRandomChrome());
        }
    }
    
    private Boolean[] makeRandomChrome() {
        Boolean[] chromosome = new Boolean[knapsack.getItems().size()];
        for (int j = 0; j < chromosome.length; j++) {
            chromosome[j] = Math.random() < INIT_BIT_PROB;
        }
        return chromosome;
    }


}
