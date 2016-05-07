/**
 *
 * CSC 385 - Data Structures and Algorithms
 * Spring 2016
 * Ryan Case (rcase5@uis.edu)
 *
 */
import java.util.*;
import java.util.stream.Collectors;

class World {

    /* * *
     *  Attributes
     * * */

    // true while the simulation is running
    private boolean running;

    // current turn number, day number is turnNumber / 10
    private int turnNumber;

    // Array of all the EnvironmentNodes that make up the grid
    private EnvironmentNode[] environment;

    // pointer to the entrance node
    private EnvironmentNode entrance;

    // pointer to the queen
    private Ant queen;

    // updating list of all living ants
    private LinkedList<Ant> population;

    // seed used to generate the simulation
    private long seed;

    // random generator for all random environmental effects
    private Random rand;

    // updating list of all dead ants
    private List<Ant> antHeaven;

    /* * *
     *  Constructors
     * * */

    /**
     * Generates a new world from the given seed
     *
     * @param randomSeed long seed for the random generator
     */
    World(long randomSeed) {
        running = true;
        environment = new EnvironmentNode[729];
        for (int i=0; i<environment.length; i++) {
            environment[i] = new EnvironmentNode(this, i);
        }
        entrance = environment[364];
        population = new LinkedList<>();
        turnNumber = 0;
        seed = randomSeed;
        rand = new Random(seed);

        antHeaven = new ArrayList<>();
    }

    /**
     * Generates a new world using System.nanoTime() as the seed
     */
    World() {
        this(System.nanoTime());
    }

    /* * *
     *  Getters
     * * */

    /**
     * @param index index of the node to be retrieved
     * @return      node at the index
     */
    EnvironmentNode getNode(int index) {
        return environment[index];
    }

    /**
     * @return current turn number
     */
    int getTurnNumber() {
        return turnNumber;
    }

    /**
     * @return pointer to the entrance node
     */
    EnvironmentNode getEntrance() {
        return entrance;
    }

    /* * *
     *  Methods
     * * */

    /**
     * Seeds the nodes with food based on the set probabilities
     */
    void generate() {
        // Initialize the state of each node
        for (EnvironmentNode n : environment) {
            if (rand.nextFloat() >= 0.75) {
                n.addFood(500 + rand.nextInt(501));
            }
        }

        // Spawn the initial ants
        spawn(Ant.AntType.QUEEN, entrance);
        queen = population.get(0);

        for (int i=0; i<4; i++) {
            spawn(Ant.AntType.SCOUT, entrance);
        }

        for (int i=0; i<10; i++) {
            spawn(Ant.AntType.SOLDIER, entrance);
        }

        for (int i=0; i<50; i++) {
            spawn(Ant.AntType.FORAGER, entrance);
        }

        // Set the initial food supply and reveal the entrance
        entrance.setFoodCount(1000);
        entrance.setRevealed();

        List<EnvironmentNode> adjacent = entrance.getAdjacentNodes();
        for (EnvironmentNode node : adjacent) {
            node.setRevealed();
        }
    }

    /**
     * Spawns an ant of a given type at a given location
     *
     * @param type  type to spawn
     * @param node  node where the ant is created
     */
    private void spawn(Ant.AntType type, EnvironmentNode node) {
        Ant ant;
        switch (type) {
            case QUEEN: ant = new Queen(this);
                break;
            case FORAGER: ant = new Forager(this);
                break;
            case SCOUT: ant = new Scout(this);
                break;
            case SOLDIER: ant = new Soldier(this);
                break;
            case BALA: ant = new Bala(this);
                break;
            default: ant = new Forager(this);
                break;
        }

        population.add(ant);
        ant.setCurrentNode(node);
        node.addAnt(ant);
    }

    /**
     * Covers the spawning action of the Queen
     *
     * Attempting to modify the {@link #population} list from another class,
     * (like the Queen) eventually leads to a ConcurrentModificationException
     * so in lieu of having the Queen perform this operation directly, the code
     * is handled in {@link World} in a way that is consistent with the Queen's
     * ruleset.
     */
    private void queenSpawn() {
        if (turnNumber % 10 == 0) {
            float spawnSeed = rand.nextFloat();
            if (spawnSeed < 0.50) {
                spawn(Ant.AntType.FORAGER, entrance);
            } else if (spawnSeed < 0.75) {
                spawn(Ant.AntType.SCOUT, entrance);
            } else {
                spawn(Ant.AntType.SOLDIER, entrance);
            }
        }
    }

    /**
     * Called every turn, 3% of the time this results in spawning a bala at a
     * random location on the edge of the map
     */
    private void balaSpawn() {
        if (rand.nextFloat() > 0.97) {
            spawn(Ant.AntType.BALA, environment[balaSpawnLocation()]);
        }
    }

    /**
     * Generates a valid index for a bala spawn. For the most part this is
     * random, but due to the way the math works the balas will spawn on
     * the edges more often than the top or bottom
     *
     * @return valid environment node # for a bala spawn
     */
    private int balaSpawnLocation() {
        int seed = rand.nextInt(environment.length);
        if (seed / 27 == 0 || seed / 27 == 26 || seed % 27 == 0 || seed % 27 == 26) {
            return  seed;
        } else {
            if (rand.nextFloat() > 0.50) {
                for ( ; seed % 27 != 26; seed++);
            } else {
                for ( ; seed % 27 != 0; seed--);
            }
            return seed;
        }
    }

    /**
     * Runs through the population and looks for any ants that have died of
     * old age and marks them as dead of natural causes.
     */
    private void grim() {
        for (Ant a : population) {
            if (turnNumber > a.getBirthDay() + a.getLifespan()) {
                a.kill();
                a.setNaturalCauses();
            }
        }
    }

    /**
     * Runs through the population and removes all ants that have died of
     * any causes and places them in {@link #antHeaven}
     */
    private void reap() {
        Ant ant;
        for (Iterator<Ant> iter = population.iterator(); iter.hasNext(); ) {
            ant = iter.next();
            if (!ant.isAlive()) {
                if (ant.getType() == Ant.AntType.FORAGER) {
                    Forager forager = (Forager) ant;
                    if (forager.hasFood()) {
                        forager.currentNode.addFood(1);
                    }
                }
                ant.getCurrentNode().removeAnt(ant);
                antHeaven.add(ant);
                iter.remove();
            }
        }
    }

    /**
     * @return true if the current turn is the first of a new day
     */
    private boolean isNewDay() {
        return (turnNumber % 10 == 0);
    }

    /**
     * Halves the pheromone amount at every node, rounding down
     */
    private void halvePheromones() {
        for (EnvironmentNode node : environment) {
            node.halvePheromone();
        }
    }

    /**
     * Handles actions that should occur at the beginning of every turn:
     *      - increment turn number
     *      - kill old ants
     *      - remove dead ants
     *      - halve pheromones if the turn also begins a new day
     */
    private void beginTurn() {
        turnNumber++;

        grim();
        reap();

        if (isNewDay()) {
            halvePheromones();
        }
    }

    /**
     * Activates only the Queen to test queen behavior.
     * Balas do not spawn during test turns
     */
    void queenTest() {
        beginTurn();
        queenSpawn();
        queen.activate();
    }

    /**
     * Activates only the scouts to test scout behavior
     * Balas do not spawn during test turns
     */
    void scoutTest() {
        beginTurn();
        for (Ant a : population) {
            if (a.getType() == Ant.AntType.SCOUT) {
                a.activate();
            }
        }
    }

    /**
     * Activates only the soldiers to test soldier behavior
     * Balas do not spawn during test turns
     */
    void soldierTest() {
        beginTurn();
        for (Ant a : population) {
            if (a.getType() == Ant.AntType.SOLDIER) {
                a.activate();
            }
        }
    }

    /**
     * Activates only the foragers to test forager behavior
     * Balas do not spawn during test turns
     */
    void foragerTest() {
        beginTurn();
        for (Ant a : population) {
            if (a.getType() == Ant.AntType.FORAGER) {
                a.activate();
            }
        }
    }

    /**
     * Runs the simulation continuously until the queen is dead
     */
    void fullRun() {
        while (running) {
            nextTurn();
        }
    }

    /**
     * Runs the simulation for a single turn
     */
    void nextTurn() {
        beginTurn();

        if (!queen.isAlive()) {
            running = false;
            System.out.println(afterActionReport());
            return;
        }

        queenSpawn();
        balaSpawn();

        for (Ant a : population) {
            a.activate();
        }
    }

    /**
     * @return A string summarizing the current state of the simulation
     */
    String timeString() {
        return String.format("Day: %d | Turn: %d | Alive: %d | Dead: %d",
                turnNumber / 10, turnNumber, population.size(), antHeaven.size());
    }

    /**
     * Detailed report of the simulation that has just run. Including cause of
     * death for the queen, total numbers for each ant type and how they died.
     * For bala and soldiers also includes the percentage of hits on attacks.
     *
     * @return description string
     */
    String afterActionReport() {
        List<Ant> livingForagers = population.stream(). filter( a -> a.getType() == Ant.AntType.FORAGER). collect( Collectors.toList());
        List<Ant> livingScouts = population.stream().filter(a -> a.getType() == Ant.AntType.SCOUT).collect(Collectors.toList());
        List<Ant> livingSoldiers = population.stream().filter(a -> a.getType() == Ant.AntType.SOLDIER).collect(Collectors.toList());
        List<Ant> livingBalas = population.stream().filter(a -> a.getType() == Ant.AntType.BALA).collect(Collectors.toList());

        List<Ant> deadForagers = antHeaven.stream().filter(a -> a.getType() == Ant.AntType.FORAGER).collect(Collectors.toList());
        List<Ant> deadScouts = antHeaven.stream().filter(a -> a.getType() == Ant.AntType.SCOUT).collect(Collectors.toList());
        List<Ant> deadSoldiers = antHeaven.stream().filter(a -> a.getType() == Ant.AntType.SOLDIER).collect(Collectors.toList());
        List<Ant> deadBalas = antHeaven.stream().filter(a -> a.getType() == Ant.AntType.BALA).collect(Collectors.toList());

        StringBuilder report = new StringBuilder();
        report.append("--- AFTER ACTION REPORT ---");
        report.append(String.format("Seed: %ld", seed));
        report.append(String.format("\nSimulation lasted %d Turns and %d Days", turnNumber, turnNumber / 10));
        if (queen.diedNaturally()) {
            report.append("\nQueen died of old age");
        } else if (entrance.getFoodAmount() == 0) {
            report.append("Queen died of starvation");
        } else {
            report.append("\nQueen died by a Bala attack");
        }

        report.append("\n\nTOTAL ANTS:");
        report.append(String.format("\nAlive: %d\nDead: %d", population.size(), antHeaven.size()));

        report.append("\n\nREPORT BY ANT TYPE:");

        report.append("\n\nFORAGERS:\n----------------");
        report.append(String.format("\nTotal: %d\nAlive: %d\nDied Naturally: %d\nDied Violently: %d",
                livingForagers.size() + deadForagers.size(),
                livingForagers.size(),
                deadForagers.stream().filter(a -> a.diedNaturally()).count(),
                deadForagers.stream().filter(a -> !a.diedNaturally()).count()));

        report.append("\n\nSCOUTS:\n----------------");
        report.append(String.format("\nTotal: %d\nAlive: %d\nDied Naturally: %d\nDied Violently: %d",
                livingScouts.size() + deadScouts.size(),
                livingScouts.size(),
                deadScouts.stream().filter(a -> a.diedNaturally()).count(),
                deadScouts.stream().filter(a -> !a.diedNaturally()).count()));

        report.append("\n\nSOLDIERS:\n----------------");
        report.append(String.format("\nTotal: %d\nAlive: %d\nDied Naturally: %d\nDied Violently: %d",
                livingSoldiers.size() + deadSoldiers.size(),
                livingSoldiers.size(),
                deadSoldiers.stream().filter(a -> a.diedNaturally()).count(),
                deadSoldiers.stream().filter(a -> !a.diedNaturally()).count()));
        report.append(String.format("\nAttacks: %d\nKills: %d\nPercentage: %.4f",
                Soldier.ATK_SUCCESS + Soldier.ATK_FAIL,
                Soldier.ATK_SUCCESS,
                (float) Soldier.ATK_SUCCESS / (Soldier.ATK_SUCCESS + Soldier.ATK_FAIL)));


        report.append("\n\nBALAS\n----------------");
        report.append(String.format("\nTotal: %d\nAlive: %d\nDied Naturally: %d\nDied Violently: %d",
                livingBalas.size() + deadBalas.size(),
                livingBalas.size(),
                deadBalas.stream().filter(a -> a.diedNaturally()).count(),
                deadBalas.stream().filter(a -> !a.diedNaturally()).count()));
        report.append(String.format("\nAttacks: %d\nKills: %d\nPercentage: %.4f",
                Bala.ATK_SUCCESS + Bala.ATK_FAIL,
                Bala.ATK_SUCCESS,
                (float) Bala.ATK_SUCCESS / (Bala.ATK_SUCCESS + Bala.ATK_FAIL)));


        return report.toString();
    }

}