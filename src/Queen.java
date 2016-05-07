/**
 *
 * CSC 385 - Data Structures and Algorithms
 * Spring 2016
 * Ryan Case (rcase5@uis.edu)
 *
 */
class Queen extends Ant {
    /**
     * There is only one Queen whose many jobs include spawning new ants, eating,
     * and nothing...
     */

    /* * *
     *  Constructors
     * * */

    /**
     * {@inheritDoc}
     */
    Queen(World world) {
        super(AntType.QUEEN, world);
        this.setLifespan(3650 * 20);
    }

    /* * *
     *  Methods
     * * */

    /**
     * A single activation for the queen
     *
     * Ant spawning is not actually done in this method for hackish reasons
     * explained further in {@link World}. The short version is that modifying
     * data structures contained in world from this class is not something
     * Java feels comfortable doing.
     */
    @Override
    protected void activate() {
        if (isAlive()) {
            if (currentNode.getFoodAmount() == 0) {
                kill();
            } else {
                getCurrentNode().takeFood();
            }
        }
    }

}
