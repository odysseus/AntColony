import java.util.Random;

public abstract class Ant {

    enum AntType {
        QUEEN,
        FORAGER,
        SCOUT,
        SOLDIER,
        BALA
    }

    private static final Random RAND = new Random();

    private int id;
    private AntType type;
    private int lifespan;
    private int birthDay;
    private boolean alive;
    protected World world;
    protected EnvironmentNode currentNode;

    private static int cid = 0;

    public Ant(AntType t, World w) {
        id = ++cid;
        world = w;
        type = t;
        lifespan = 3650;
        birthDay = world.getTurnNumber();
        alive = true;
    }

    void setCurrentNode(EnvironmentNode n) {
        currentNode = n;
    }

    void kill() {
        alive = false;
    }

    boolean isAlive() {
        return alive;
    }

    EnvironmentNode getCurrentNode() {
        return currentNode;
    }

    int getBirthDay() {
        return birthDay;
    }

    AntType getType() {
        return type;
    }

    int getLifespan() {
       return lifespan;
    }

    void setLifespan(int ls) {
        lifespan = ls;
    }

    public String toString() {
        return String.format("Type: %s -- ID: %d -- Lifespan: %d",
                type, id, lifespan);
    }

    protected abstract void activate();

    protected void move(EnvironmentNode n) {
        currentNode.removeAnt(this);
        n.addAnt(this);
        currentNode = n;
    }

    protected void randomMove(EnvironmentNode[] ns) {
        move(ns[RAND.nextInt(ns.length)]);
    }

}
