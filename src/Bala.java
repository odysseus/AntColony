/**
 *
 * CSC 385 - Data Structures and Algorithms
 * Spring 2016
 * Ryan Case (rcase5@uis.edu)
 *
 */
import java.util.LinkedList;
import java.util.List;

class Bala extends Ant {

    /* * *
     *  Attributes
     * * */

    // Class keeps a record of all failed and successful attacks across all balas
    static int ATK_SUCCESS = 0;
    static int ATK_FAIL = 0;

    /* * *
     *  Constructors
     * * */

    /**
     * {@inheritDoc}
     */
    Bala(World world) {
        super(AntType.BALA, world);
    }

    /* * *
     *  Methods
     * * */

    /**
     * Activation method for the bala ant. The bala moves randomly unless
     * it is in a square with a colony ant, in which case it attacks a
     * colony ant at random.
     */
    @Override
    protected void activate() {
        if (isAlive()) {
            if (currentNode.hasColonyAnts()) {
                List<Ant> colonyAnts = currentNode.getColonyAnts();
                Ant toAttack = colonyAnts.get(randInt(colonyAnts.size()));

                if (randFloat() > 0.50) {
                    toAttack.kill();
                    ATK_SUCCESS++;
                } else {
                    ATK_FAIL++;
                }
            } else {
                List<EnvironmentNode> adjacent = currentNode.getAdjacentNodes();
                randomMove(adjacent);
            }
        }
    }
}
