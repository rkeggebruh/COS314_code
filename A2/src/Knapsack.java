import java.util.ArrayList;

public class Knapsack {

    private int capacity;
    private ArrayList<Item> items;

    public Knapsack(int capacity, int numItems) {
        this.capacity = capacity;
        items = new ArrayList<Item>();
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public double getWeight(Boolean[] solution) {
        double totalWeight = 0;
        for (int i = 0; i < items.size(); i++) {
            if (solution[i]) {
                totalWeight += items.get(i).getWeight();
            }
        }
        return totalWeight;

    }

    public double getValue(Boolean[] solution) {
        double totalValue = 0;
        for (int i = 0; i < items.size(); i++) {
            if (solution[i]) {
                totalValue += items.get(i).getValue();
            }
        }
        return totalValue;
    }

    public double getTotalWeight() {
        double totalWeight = 0;
        for (int i = 0; i < items.size(); i++) {
            totalWeight += items.get(i).getWeight();
        }
        return totalWeight;
    }

    public double getTotalValue() {
        double totalValue = 0;
        for (int i = 0; i < items.size(); i++) {
            totalValue += items.get(i).getValue();
        }
        return totalValue;
    }
    
}
