import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

class EnvironmentNode {
    private World world;
    private int number;
    private int foodCount;
    private int pheromoneCount;
    private boolean revealed;
    private LinkedList<Ant> colonyAnts;
    private LinkedList<Ant> enemyAnts;
    private EnumMap<Ant.AntType, Integer> antsOfType;

    EnvironmentNode(World w, int i) {
        world = w;
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

    List<EnvironmentNode> getAdjacentNodes() {
        List<Integer> indices = new ArrayList<>();

        indices.add(number - 26);
        indices.add(number - 27);
        indices.add(number - 28);
        indices.add(number + 26);
        indices.add(number + 27);
        indices.add(number + 28);
        indices.add(number - 1);
        indices.add(number + 1);

        if (number / 27 == 0) {
            // We are on the top row
            indices.remove((Integer) (number - 26));
            indices.remove((Integer) (number - 27));
            indices.remove((Integer) (number - 28));
        }

        if (number / 27 == 26) {
            // We are on the bottom row
            indices.remove((Integer) (number + 26));
            indices.remove((Integer) (number + 27));
            indices.remove((Integer) (number + 28));
        }

        if (number % 27 == 0) {
            // We are on the left row
            indices.remove((Integer) (number - 28));
            indices.remove((Integer) (number - 1));
            indices.remove((Integer) (number + 26));
        }

        if (number % 27 == 26) {
            // We are on the right row
            indices.remove((Integer) (number - 26));
            indices.remove((Integer) (number + 1));
            indices.remove((Integer) (number + 28));
        }

        List<EnvironmentNode> adjacent = new ArrayList<>();
        for (int i : indices) {
            adjacent.add(world.getNode(i));
        }

        return adjacent;
    }

    List<EnvironmentNode> getExploredAdjacentNodes() {
        List<EnvironmentNode> adjacent = getAdjacentNodes();
        List<EnvironmentNode> explored = new ArrayList<>();

        for (EnvironmentNode node : adjacent) {
            if (node.isRevealed()) {
                explored.add(node);
            }
        }

        return explored;
    }

    boolean isEntrance() {
        return (this == world.getEntrance());
    }

    boolean hasFood() {
        return (this.foodCount > 0 && !this.isEntrance());
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

    LinkedList<Ant> getEnemyAnts() {
        return enemyAnts;
    }

    boolean hasColonyAnts() {
        return (colonyAnts.size() > 0);
    }

    LinkedList<Ant> getColonyAnts() {
        return colonyAnts;
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

    void addPheromone(int amt) {
        pheromoneCount += amt;
    }

    void halvePheromone() {
        pheromoneCount /= 2;
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
