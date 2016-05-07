/**
 *
 * CSC 385 - Data Structures and Algorithms
 * Spring 2016
 * Ryan Case (rcase5@uis.edu)
 *
 */

import java.util.List;
import java.util.Random;

public abstract class Ant {
    /**
     * Covers the majority of the functionality for all ants. The main difference
     * is the {@link #activate()} method which is implemented by all subclasses
     * to achieve their different behaviors
     */

    enum AntType {
        QUEEN,
        FORAGER,
        SCOUT,
        SOLDIER,
        BALA
    }

    /* * *
     *  Attributes
     * * */

    // Current ID number
    private static int CID = 0;

    // Handles random number generation for all ants
    private static final Random RAND = new Random();

    // Unique id number
    private int id;

    // Ant type
    private AntType type;

    // Lifespan (expressed in number of turns)
    private int lifespan;

    // birthDay is actually the birth turn, not the full day number
    private int birthDay;

    // true if the ant is currently alive
    private boolean alive;

    // World is the model object containing the ant
    private World world;

    // The node the ant currently occupies
    protected EnvironmentNode currentNode;

    // true if the ant died of natural causes
    private boolean naturalCauses;

    /* * *
     *  Constructors
     * * */

    /**
     * Primary constructor for all ants
     *
     * @param t The type of ant
     * @param w The world into which the ant is being spawned
     */
    public Ant(AntType t, World w) {
        id = CID++;
        world = w;
        type = t;
        lifespan = 3650;
        birthDay = world.getTurnNumber();
        alive = true;
        naturalCauses = false;
    }

    /* * *
     *  Getters
     * * */

    /**
     * @return true if the ant died of old age, false otherwise
     */
    boolean diedNaturally() {
        return naturalCauses;
    }

    /**
     * @return true if the ant is currently alive, false otherwise
     */
    boolean isAlive() {
        return alive;
    }

    /**
     * @return the node the ant is currently occupying
     */
    EnvironmentNode getCurrentNode() {
        return currentNode;
    }

    /**
     * @return the turn on which the ant was spawned
     */
    int getBirthDay() {
        return birthDay;
    }

    /**
     * @return the type of the ant
     */
    AntType getType() {
        return type;
    }

    /**
     * @return the maximum lifespan of the ant, in turns
     */
    int getLifespan() {
       return lifespan;
    }

    /* * *
     *  Setters
     * * */

    /**
     * Allows the ant to change the node it is currently occupying
     *
     * @param n The new {@link #currentNode}
     */
    void setCurrentNode(EnvironmentNode n) {
        currentNode = n;
    }

    /**
     * Kills the ant
     */
    void kill() {
        alive = false;
    }

    /**
     * Sets the cause of death to naturalCauses = true
     * False is the default.
     */
    void setNaturalCauses() {
        naturalCauses = true;
    }

    /**
     * @param ls the maximum lifespan, in turns
     */
    void setLifespan(int ls) {
        lifespan = ls;
    }

    /* * *
     *  Methods
     * * */

    /**
     * Moves from one square to the next
     *
     * @param n the node to move to
     */
    protected void move(EnvironmentNode n) {
        currentNode.removeAnt(this);
        n.addAnt(this);
        currentNode = n;
    }

    /**
     * Given a list of {@link EnvironmentNode}s, moves randomly to
     * one of them
     *
     * @param nodeList the list of nodes to select from
     */
    protected void randomMove(List<EnvironmentNode> nodeList) {
        int rand = RAND.nextInt(nodeList.size());
        move(nodeList.get(rand));
    }

    /**
     * Generates a random integer for use in randomizing behavior.
     * Int is in the range 0 (inclusive) to max (exclusive)
     *
     * @param max The maximum integer to be returned (exclusive)
     * @return a random integer in the range [0,max)
     */
    protected int randInt(int max) {
        return RAND.nextInt(max);
    }

    /**
     * Generates a random floating point number between 0 and 1.0
     * for randomizing behavior
     *
     * @return a float between 0 and 1
     */
    protected float randFloat() {
        return RAND.nextFloat();
    }

    /**
     * Displays simple details about the ant
     *
     * @return string description of the ant
     */
    public String toString() {
        return String.format("%d: %s - Birth Turn: %d, Lifespan: %d",
                id, type, birthDay, lifespan);
    }

    /**
     * This method must be implemented by any subclass to define the unique
     * behavior for the subclass of ant. activate() is called on every ant,
     * every turn.
     */
    protected abstract void activate();

}
