/**
 *
 * CSC 385 - Data Structures and Algorithms
 * Spring 2016
 * Ryan Case (rcase5@uis.edu)
 *
 */
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class EnvironmentNode {
    /**
     * EnvironmentNodes represent each of the squares in the Ant Colony
     * simulation. They store information about their contents and a link
     * back to the world in which they are contained so they can fetch
     * adjacent nodes.
     */

    /* * *
     *  Attributes
     * * */

    // the containing world
    private World world;

    // the node number/ index for the current node
    private int number;

    // the amount of food currently present
    private int foodCount;

    // the amount of pheromone in the square
    private int pheromoneCount;

    // true if the square has been revealed by scouts, false otherwise
    private boolean revealed;

    // colony ants present in the square
    private LinkedList<Ant> colonyAnts;

    // enemy ants present in the square
    private LinkedList<Ant> enemyAnts;

    // map storing the count of each ant type present in the square
    private EnumMap<Ant.AntType, Integer> antsOfType;

    /* * *
     *  Constructors
     * * */

    /**
     * @param w the world the node is a part of
     * @param i the index of the node in the environment array
     */
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

    /* * *
     *  Getters
     * * */

    /**
     * @return true if revealed, false otherwise
     */
    boolean isRevealed() {
        return revealed;
    }

    /**
     * @param type The ant type to look for
     * @return true if that ant type is present in the node
     */
    boolean hasAntOfType(Ant.AntType type) {
        return (antsOfType.containsKey(type) && antsOfType.get(type) > 0);
    }

    /**
     * Long method to find all valid, adjacent nodes. It does this by finding
     * all *potential* adjacent nodes and then removing those which are invalid
     * by virtue of the node being on an edge.
     *
     * @return list of adjacent, legal nodes
     */
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

    /**
     * Much like {@link #getAdjacentNodes()} this seeks to simplify movement
     * for the ants, but in this case the list is filtered down to only those
     * nodes that have already been revealed.
     *
     * @return list of revealed, adjacent nodes
     */
    List<EnvironmentNode> getExploredAdjacentNodes() {
       return getAdjacentNodes().stream().filter(
               EnvironmentNode::isRevealed ).collect(Collectors.toList());
    }

    /**
     * @return true if the node in question is also the colony entrance
     */
    boolean isEntrance() {
        return (this == world.getEntrance());
    }

    /**
     * @return true if the node contains more than zero food
     */
    boolean hasFood() {
        return (this.foodCount > 0 && !this.isEntrance());
    }

    /**
     * @return number of scouts in the node
     */
    int scoutCount() {
        if (antsOfType.containsKey(Ant.AntType.SCOUT)) {
            return antsOfType.get(Ant.AntType.SCOUT);
        } else {
            return 0;
        }
    }

    /**
     * @return number of soldiers in the node
     */
    int soldierCount() {
        if (antsOfType.containsKey(Ant.AntType.SOLDIER)) {
            return antsOfType.get(Ant.AntType.SOLDIER);
        } else {
            return 0;
        }
    }

    /**
     * @return number of foragers in the node
     */
    int foragerCount() {
        if (antsOfType.containsKey(Ant.AntType.FORAGER)) {
            return antsOfType.get(Ant.AntType.FORAGER);
        } else {
            return 0;
        }
    }

    /**
     * @return number of balas in the node
     */
    int balaCount() {
        if (antsOfType.containsKey(Ant.AntType.BALA)) {
            return antsOfType.get(Ant.AntType.BALA);
        } else {
            return 0;
        }
    }

    /**
     * @return true if enemy ants are present
     */
    boolean hasEnemyAnts() {
        return (enemyAnts.stream().filter(Ant::isAlive).count() > 0);
    }

    /**
     * @return list of all enemy ants present
     */
    List<Ant> getEnemyAnts() {
        return enemyAnts.stream().filter(Ant::isAlive).collect(Collectors.toList());
    }

    /**
     * @return true if colony ants are present
     */
    boolean hasColonyAnts() {
        return (colonyAnts.stream().filter(Ant::isAlive).count() > 0);
    }

    /**
     * @return list of all colony ants present
     */
    List<Ant> getColonyAnts() {
        return colonyAnts.stream().filter(Ant::isAlive).collect(Collectors.toList());
    }

    /**
     * @return amount of food in the node
     */
    int getFoodAmount() {
        return foodCount;
    }

    /**
     * @return amount of pheromone in the node
     */
    int getPheromoneAmount() {
        return pheromoneCount;
    }

    /**
     * Sets the amount of food in the node
     *
     * @param food *total* amount of food in the node
     */
    void setFoodCount(int food) {
        foodCount = food;
    }

    /**
     * Adds pheromone to the amount already present
     *
     * @param amt amount to add
     */
    void addPheromone(int amt) {
        pheromoneCount += amt;
    }

    /**
     * Halves the amount of pheromone in a cell (rounded down)
     */
    void halvePheromone() {
        pheromoneCount /= 2;
    }

    /* * *
     *  Setters
     * * */

    /**
     * Sets the node as revealed
     */
    void setRevealed() {
        revealed = true;
    }

    /**
     * Adds an ant to the appropriate list of currently present ants
     *
     * @param a ant to add
     */
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

    /**
     * Removes an ant from the node
     *
     * @param a ant to remove
     */
    void removeAnt(Ant a) {
        if (a.getType() != Ant.AntType.BALA) {
            colonyAnts.remove(a);
        } else {
            enemyAnts.remove(a);
        }

        int current = antsOfType.get(a.getType());
        antsOfType.put(a.getType(), current-1);
    }

    /**
     * Adds an amount of food to the quantity already present
     *
     * @param amt amount to add
     */
    void addFood(int amt) {
        foodCount += amt;
    }

    /**
     * Remove a single unit of food from this node
     */
    void takeFood() {
        foodCount--;
    }
}
