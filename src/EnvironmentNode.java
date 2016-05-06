import java.util.EnumMap;
import java.util.LinkedList;

class EnvironmentNode {
    private int number;
    private int foodCount;
    private int pheromoneCount;
    private boolean revealed;
    private LinkedList<Ant> colonyAnts;
    private LinkedList<Ant> enemyAnts;
    private EnumMap<Ant.AntType, Integer> antsOfType;

    EnvironmentNode(int i) {
        number = i;
        foodCount = 0;
        pheromoneCount = 0;
        revealed = false;
        colonyAnts = new LinkedList<>();
        enemyAnts = new LinkedList<>();
        antsOfType = new EnumMap<>(Ant.AntType.class);
    }

    boolean isRevealed() {
        return revealed;
    }

    void setRevealed() {
        revealed = true;
    }

    boolean hasAntOfType(Ant.AntType type) {
        return (antsOfType.containsKey(type) && antsOfType.get(type) > 0);
    }

    int getNumber() {
        return number;
    }

    int scoutCount() {
        if (antsOfType.containsKey(Ant.AntType.SCOUT)) {
            return antsOfType.get(Ant.AntType.SCOUT);
        } else {
            return 0;
        }
    }

    int soldierCount() {
        if (antsOfType.containsKey(Ant.AntType.SOLDIER)) {
            return antsOfType.get(Ant.AntType.SOLDIER);
        } else {
            return 0;
        }
    }

    int foragerCount() {
        if (antsOfType.containsKey(Ant.AntType.FORAGER)) {
            return antsOfType.get(Ant.AntType.FORAGER);
        } else {
            return 0;
        }
    }

    int balaCount() {
        if (antsOfType.containsKey(Ant.AntType.BALA)) {
            return antsOfType.get(Ant.AntType.BALA);
        } else {
            return 0;
        }
    }

    boolean hasEnemyAnts() {
        return (enemyAnts.size() > 0);
    }

    boolean hasColonyAnts() {
        return (colonyAnts.size() > 0);
    }

    int getFoodAmount() {
        return foodCount;
    }

    void setFoodCount(int food) {
        foodCount = food;
    }

    int getPheromoneAmount() {
        return pheromoneCount;
    }

    void addAnt(Ant a) {
        if (a.getType() != Ant.AntType.BALA) {
            colonyAnts.add(a);
        } else {
            enemyAnts.add(a);
        }

        if (antsOfType.containsKey(a.getType())) {
            int current = antsOfType.get(a.getType());
            antsOfType.put(a.getType(), current+1);
        } else {
            antsOfType.put(a.getType(), 1);
        }
    }

    void removeAnt(Ant a) {
        if (a.getType() != Ant.AntType.BALA) {
            colonyAnts.remove(a);
        } else {
            enemyAnts.remove(a);
        }

        int current = antsOfType.get(a.getType());
        antsOfType.put(a.getType(), current-1);
    }

    void addFood(int amt) {
        foodCount += amt;
    }

    void takeFood() {
        foodCount--;
    }
}
