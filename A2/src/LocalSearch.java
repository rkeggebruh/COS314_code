public class LocalSearch {
    private Knapsack knapsack;

    public LocalSearch(Knapsack knapsack) {
        this.knapsack = knapsack;
    }

    public Boolean[] runLocalSearch(Boolean[] initialSolution) {
        // System.out.println("in the local serach------------");
        
        Boolean[] currentSolution = initialSolution.clone();
        double currentFitness = fitEval(currentSolution);

        // Perform hill climbing
        boolean improved;
        do {
            improved = false;
            for (int i = 0; i < currentSolution.length; i++) {
                // Flip the bit
                currentSolution[i] = !currentSolution[i];
                double newFitness = fitEval(currentSolution);
                if (newFitness > currentFitness) {
                    // Accept the new solution
                    currentFitness = newFitness;
                    improved = true;
                } else {
                    // Revert the change
                    currentSolution[i] = !currentSolution[i];
                }
            }
        } while (improved);

        return currentSolution;
    }

    private double fitEval(Boolean[] solution) {
        double totalValue = 0;
        double totalWeight = 0;

        for (int i = 0; i < solution.length; i++) {
            if (solution[i]) {
                totalValue += knapsack.getItems().get(i).getValue();
                totalWeight += knapsack.getItems().get(i).getWeight();
            }
        }

        // Check if the solution violates the capacity constraint
        if (totalWeight > knapsack.getCapacity()) {
            return 0; // Penalize solutions exceeding capacity
        }

        return totalValue;
    }
}
