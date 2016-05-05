import java.util.EnumMap;
import java.util.LinkedList;

public class Node {
    private int foodCount;
    private int pheromoneCount;
    private boolean revealed;
    private LinkedList<Ant> colonyAnts;
    private LinkedList<Ant> enemyAnts;
    private EnumMap<Ant.AntType, Integer> antsOfType;

    public Node() {
        foodCount = 0;
        pheromoneCount = 0;
        revealed = false;
        colonyAnts = new LinkedList<>();
        enemyAnts = new LinkedList<>();
        antsOfType = new EnumMap<>(Ant.AntType.class);
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed() {
        revealed = true;
    }

    public boolean hasAntOfType(Ant.AntType type) {
        return (antsOfType.containsKey(type) && antsOfType.get(type) > 0);
    }

    public int scoutCount() {
        if (antsOfType.containsKey(Ant.AntType.SCOUT)) {
            return antsOfType.get(Ant.AntType.SCOUT);
        } else {
            return 0;
        }
    }

    public int soldierCount() {
        if (antsOfType.containsKey(Ant.AntType.SOLDIER)) {
            return antsOfType.get(Ant.AntType.SOLDIER);
        } else {
            return 0;
        }
    }

    public int foragerCount() {
        if (antsOfType.containsKey(Ant.AntType.FORAGER)) {
            return antsOfType.get(Ant.AntType.FORAGER);
        } else {
            return 0;
        }
    }

    public int balaCount() {
        if (antsOfType.containsKey(Ant.AntType.BALA)) {
            return antsOfType.get(Ant.AntType.BALA);
        } else {
            return 0;
        }
    }

    public int getFoodAmount() {
        return foodCount;
    }

    public int getPheromoneAmount() {
        return pheromoneCount;
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
        foodCount += amt;
    }

    public void takeFood() {
        foodCount--;
    }
}
