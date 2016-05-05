import java.util.EnumMap;
import java.util.LinkedList;

public class Node {
    private int food;
    private int pheromone;
    private boolean revealed;
    private LinkedList<Ant> colonyAnts;
    private LinkedList<Ant> enemyAnts;
    private EnumMap<Ant.AntType, Integer> antsOfType;

    public Node() {
        food = 0;
        pheromone = 0;
        revealed = false;
        colonyAnts = new LinkedList<>();
        enemyAnts = new LinkedList<>();
        antsOfType = new EnumMap<>(Ant.AntType.class);
    }

    public void addAnt(Ant a) {
        if (a.type != Ant.AntType.BALA) {
            colonyAnts.add(a);
        } else {
            enemyAnts.add(a);
        }

        if (antsOfType.containsKey(a.type)) {
            int current = antsOfType.get(a.type);
            antsOfType.put(a.type, current+1);
        } else {
            antsOfType.put(a.type, 1);
        }
    }

    public void removeAnt(Ant a) {
        if (a.type != Ant.AntType.BALA) {
            colonyAnts.remove(a);
        } else {
            enemyAnts.remove(a);
        }

        int current = antsOfType.get(a.type);
        antsOfType.put(a.type, current-1);
    }

    public void addFood(int amt) {
        food += amt;
    }

    public void takeFood() {
        food--;
    }
}
