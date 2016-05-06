import java.util.*;
import java.util.stream.Collectors;

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
            environment[i] = new EnvironmentNode(this, i);
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

    EnvironmentNode getEntrance() {
        return entrance;
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

        List<EnvironmentNode> adjacent = entrance.getAdjacentNodes();
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

    private void balaSpawn() {
        if (rand.nextFloat() > 0.97) {
            spawn(Ant.AntType.BALA, environment[balaSpawnLocation()]);
        }
    }

    int balaSpawnLocation() {
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

    private void grim() {
        for (Ant a : population) {
            if (turnNumber > a.getBirthDay() + a.getLifespan()) {
                a.kill();
                a.setNaturalCauses();
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

    boolean isNewDay() {
        return (turnNumber % 10 == 0);
    }

    private void halvePheromones() {
        for (EnvironmentNode node : environment) {
            node.halvePheromone();
        }
    }

    private void beginTurn() {
        turnNumber++;

        grim();
        reap();

        if (isNewDay()) {
            halvePheromones();
        }
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

    void foragerTest() {
        beginTurn();
        for (Ant a : population) {
            if (a.getType() == Ant.AntType.FORAGER) {
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
            System.out.println(afterActionReport());
            return;
        }

        queenSpawn();
        balaSpawn();

        for (Ant a : population) {
            a.activate();
        }
    }

    String timeString() {
        return String.format("D: %d | T: %d | A: %d | D: %d",
                turnNumber / 10, turnNumber, population.size(), antHeaven.size());
    }

    String afterActionReport() {
        List<Ant> livingForagers = population.stream().filter(a -> a.getType() == Ant.AntType.FORAGER).collect(Collectors.toList());
        List<Ant> livingScouts = population.stream().filter(a -> a.getType() == Ant.AntType.SCOUT).collect(Collectors.toList());
        List<Ant> livingSoldiers = population.stream().filter(a -> a.getType() == Ant.AntType.SOLDIER).collect(Collectors.toList());
        List<Ant> livingBalas = population.stream().filter(a -> a.getType() == Ant.AntType.BALA).collect(Collectors.toList());

        List<Ant> deadForagers = antHeaven.stream().filter(a -> a.getType() == Ant.AntType.FORAGER).collect(Collectors.toList());
        List<Ant> deadScouts = antHeaven.stream().filter(a -> a.getType() == Ant.AntType.SCOUT).collect(Collectors.toList());
        List<Ant> deadSoldiers = antHeaven.stream().filter(a -> a.getType() == Ant.AntType.SOLDIER).collect(Collectors.toList());
        List<Ant> deadBalas = antHeaven.stream().filter(a -> a.getType() == Ant.AntType.BALA).collect(Collectors.toList());

        StringBuilder report = new StringBuilder();
        report.append("--- AFTER ACTION REPORT ---");
        report.append(String.format("\nSimulation lasted %d Turns and %d Days", turnNumber, turnNumber / 10));
        if (queen.diedNaturally()) {
            report.append("\nQueen died by natural causes");
        } else if (entrance.hasEnemyAnts()) {
            report.append("\nQueen died by a Bala attack");
        } else {
            report.append("\nQueen died of starvation");
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