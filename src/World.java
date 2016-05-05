import java.util.LinkedList;
import java.util.Random;

class World {

    private boolean running;
    private Node[] environment;
    private Node entrance;
    private Ant queen;
    private LinkedList<Ant> population;
    private int turnNumber;
    private long seed;
    private Random rand;

    World(long randomSeed) {
        running = true;
        environment = new Node[729];
        for (int i=0; i<environment.length; i++) {
            environment[i] = new Node();
        }
        entrance = environment[364];
        population = new LinkedList<>();
        turnNumber = 0;
        seed = randomSeed;
        rand = new Random(seed);
    }

    World() {
        this(System.nanoTime());
    }

    Node getNode(int index) {
        return environment[index];
    }

    public int nodeCount() {
        return environment.length;
    }

    void generate() {
        // Initialize the state of each node
        for (Node n : environment) {
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

        // Add the initial food supply and reveal the entrance
        entrance.addFood(1000);
        entrance.setRevealed();
    }

    private void spawn(Ant.AntType type, Node node) {
        Ant ant;
        switch (type) {
            case QUEEN: ant = new Queen(turnNumber);
                break;
            case FORAGER: ant = new Forager(turnNumber);
                break;
            case SCOUT: ant = new Scout(turnNumber);
                break;
            case SOLDIER: ant = new Soldier(turnNumber);
                break;
            case BALA: ant = new Bala(turnNumber);
                break;
            default: ant = new Forager(turnNumber);
                break;
        }

        population.add(ant);
        ant.setCurrentNode(node);
        node.addAnt(ant);
    }

    void dailySpawn() {
        float spawnSeed = rand.nextFloat();
        if (spawnSeed < 0.50) {
            spawn(Ant.AntType.FORAGER, entrance);
        } else if (spawnSeed < 0.75) {
            spawn(Ant.AntType.SCOUT, entrance);
        } else {
            spawn(Ant.AntType.SOLDIER, entrance);
        }
    }

    private void grim() {
        for (Ant a : population) {
            if (turnNumber - a.getBirthDay() > a.getLifespan()) {
                a.kill();
            }
        }
    }

    private void reap() {
        for (Ant a : population) {
            if (!a.isAlive()) {
                a.getCurrentNode().removeAnt(a);
                population.remove(a);
            }
        }
    }

    private void turnEffects() {
        turnNumber++;

        grim();
        reap();

        if (turnNumber % 10 == 0) {
            dailySpawn();
        }
    }

    void queenTest() {
        turnEffects();
        queen.activate();
    }

    String timeString() {
        return String.format("Day: %d -- Turn: %d",
                turnNumber / 10, turnNumber);
    }

}