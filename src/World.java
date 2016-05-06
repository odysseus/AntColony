import java.util.*;

class World {

    private boolean running;
    private int turnNumber;
    private EnvironmentNode[] environment;
    private EnvironmentNode entrance;
    private Ant queen;
    private LinkedList<Ant> population;
    private long seed;
    private Random rand;

    private List<Ant> antHeaven;

    World(long randomSeed) {
        running = true;
        environment = new EnvironmentNode[729];
        for (int i=0; i<environment.length; i++) {
            environment[i] = new EnvironmentNode(i);
        }
        entrance = environment[364];
        population = new LinkedList<>();
        turnNumber = 0;
        seed = randomSeed;
        rand = new Random(seed);

        antHeaven = new ArrayList<>();
    }

    World() {
        this(System.nanoTime());
    }

    EnvironmentNode getNode(int index) {
        return environment[index];
    }

    int getTurnNumber() {
        return turnNumber;
    }

    List<EnvironmentNode> getAdjacentNodes(int nodeNo) {
        List<Integer> indices = new ArrayList<>();
        indices.add(nodeNo - 26);
        indices.add(nodeNo - 27);
        indices.add(nodeNo - 28);
        indices.add(nodeNo + 26);
        indices.add(nodeNo + 27);
        indices.add(nodeNo + 28);
        indices.add(nodeNo - 1);
        indices.add(nodeNo + 1);

        if (nodeNo / 27 == 0) {
            // We are on the top row
            indices.remove((Integer) (nodeNo - 26));
            indices.remove((Integer) (nodeNo - 27));
            indices.remove((Integer) (nodeNo - 28));
        }

        if (nodeNo / 27 == 26) {
            // We are on the bottom row
            indices.remove((Integer) (nodeNo + 26));
            indices.remove((Integer) (nodeNo + 27));
            indices.remove((Integer) (nodeNo + 28));
        }

        if (nodeNo % 27 == 0) {
            // We are on the left row
            indices.remove((Integer) (nodeNo - 28));
            indices.remove((Integer) (nodeNo - 1));
            indices.remove((Integer) (nodeNo + 26));
        }

        if (nodeNo % 27 == 26) {
            // We are on the right row
            indices.remove((Integer) (nodeNo - 26));
            indices.remove((Integer) (nodeNo + 1));
            indices.remove((Integer) (nodeNo + 28));
        }

        List<EnvironmentNode> adjacent = new ArrayList<>();
        for (int i : indices) {
            adjacent.add(environment[i]);
        }

        return adjacent;
    }

    List<EnvironmentNode> getExploredAdjacentNodes(int nodeNo) {
        List<EnvironmentNode> adjacent = getAdjacentNodes(nodeNo);
        List<EnvironmentNode> explored = new ArrayList<>();

        for (EnvironmentNode node : adjacent) {
           if (node.isRevealed()) {
               explored.add(node);
           }
        }

        return explored;
    }

    public int nodeCount() {
        return environment.length;
    }

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

        List<EnvironmentNode> adjacent = getAdjacentNodes(entrance.getNumber());
        for (EnvironmentNode node : adjacent) {
            node.setRevealed();
        }
    }

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

    void queenSpawn() {
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

    private void grim() {
        for (Ant a : population) {
            if (turnNumber > a.getBirthDay() + a.getLifespan()) {
                a.kill();
            }
        }
    }

    private void reap() {
        Ant ant;
        for (Iterator<Ant> iter = population.iterator(); iter.hasNext(); ) {
            ant = iter.next();
            if (!ant.isAlive()) {
                ant.getCurrentNode().removeAnt(ant);
                antHeaven.add(ant);
                iter.remove();
            }
        }
    }

    private void beginTurn() {
        System.out.println(antHeaven);
        turnNumber++;

        grim();
        reap();
    }

    void queenTest() {
        beginTurn();
        queenSpawn();
        queen.activate();
    }

    void scoutTest() {
        beginTurn();
        for (Ant a : population) {
            if (a.getType() == Ant.AntType.SCOUT) {
                a.activate();
            }
        }
    }

    void soldierTest() {
        beginTurn();
        for (Ant a : population) {
            if (a.getType() == Ant.AntType.SOLDIER) {
                a.activate();
            }
        }
    }

    void fullRun() {
        while (running) {
            nextTurn();
        }
    }

    void nextTurn() {
        beginTurn();

        if (!queen.isAlive()) {
            running = false;
            return;
        }

        queenSpawn();

        for (Ant a : population) {
            a.activate();
        }
    }

    String timeString() {
        return String.format("D: %d | T: %d | A: %d | D: %d",
                turnNumber / 10, turnNumber, population.size(), antHeaven.size());
    }

}