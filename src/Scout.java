/**
 *
 * CSC 385 - Data Structures and Algorithms
 * Spring 2016
 * Ryan Case (rcase5@uis.edu)
 *
 */
import java.util.List;

class Scout extends Ant {
    /**
     * Scouts explore new territory for the colony and move randomly, they
     * are capable of moving into any valid square without restriction.
     */

    /* * *
     *  Constructors
     * * */

    /**
     * {@inheritDoc}
     */
    Scout(World world) {
        super(AntType.SCOUT, world);
    }

    /* * *
     *  Methods
     * * */

    /**
     * A single activation for a scout. Move randomly, reveal wherever you
     * just moved.
     */
    @Override
    protected void activate() {
        if (isAlive()) {
            List<EnvironmentNode> adjacent = currentNode.getAdjacentNodes();
            randomMove(adjacent);
            currentNode.setRevealed();
        }
    }

}
