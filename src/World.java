import java.util.LinkedList;
import java.util.Random;

public class World {

    private boolean running;
    private Node[] environment;
    private Node entrance;
    private LinkedList<Ant> population;
    private int turnNumber;
    private int dayNumber;

    public World(long seed) {
        running = true;
        environment = new Node[729];
        for (int i=0; i<environment.length; i++) {
            environment[i] = new Node();
        }
        entrance = environment[364];
        population = new LinkedList<>();
        turnNumber = 0;
        dayNumber = 0;

        generate(seed);
    }

    public World() {
        this(System.nanoTime());
    }

    public Node getNode(int index) {
        return environment[index];
    }

    public int nodeCount() {
        return environment.length;
    }

    private void generate(long seed) {
        Random rand = new Random(seed);

        // Initialize the state of each node
        for (Node n : environment) {
            if (rand.nextFloat() >= 0.75) {
                n.addFood(500 + rand.nextInt(501));
            }
        }

        // Spawn the initial ants
        spawn(Ant.AntType.QUEEN, entrance);

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
            case QUEEN: ant = new Queen(dayNumber);
                break;
            case FORAGER: ant = new Forager(dayNumber);
                break;
            case SCOUT: ant = new Scout(dayNumber);
                break;
            case SOLDIER: ant = new Soldier(dayNumber);
                break;
            case BALA: ant = new Bala(dayNumber);
                break;
            default: ant = new Forager(dayNumber);
                break;
        }

        population.add(ant);
        node.addAnt(ant);
    }

    public String timeString() {
        return String.format("Day: %d -- Turn: %d",
                dayNumber, turnNumber);
    }

}