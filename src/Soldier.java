/**
 *
 * CSC 385 - Data Structures and Algorithms
 * Spring 2016
 * Ryan Case (rcase5@uis.edu)
 *
 */
import java.util.ArrayList;
import java.util.List;

class Soldier extends Ant {

    /* * *
     *  Attributes
     * * */

    // Save attack results for all soldiers as a class variable
    static int ATK_SUCCESS = 0;
    static int ATK_FAIL = 0;

    /* * *
     *  Constructors
     * * */

    /**
     * {@inheritDoc}
     */
    Soldier(World world) {
        super(AntType.SOLDIER, world);
    }

    /* * *
     *  Methods
     * * */

    /**
     * A single activation for a soldier. Soldiers move randomly unless they
     * are in a square with, or adjacent to, a bala ant, in which case they
     * move to attack the bala ant.
     */
    @Override
    protected void activate() {
        if (isAlive()) {
            if (currentNode.hasEnemyAnts()) {
                List<Ant> enemies = currentNode.getEnemyAnts();
                Ant toAttack = enemies.get(randInt(enemies.size()));
                if (randFloat() > 0.50) {
                    toAttack.kill();
                    ATK_SUCCESS++;
                } else {
                    ATK_FAIL++;
                }
            } else {
                List<EnvironmentNode> exploredAdjacentNodes = currentNode.getExploredAdjacentNodes();
                List<EnvironmentNode> enemyNodes = new ArrayList<>();
                for (EnvironmentNode node : exploredAdjacentNodes) {
                    if (node.hasEnemyAnts()) {
                        enemyNodes.add(node);
                    }
                }

                if (enemyNodes.size() > 0) {
                    randomMove(enemyNodes);
                } else {
                    randomMove(exploredAdjacentNodes);
                }
            }
        }
    }
}
