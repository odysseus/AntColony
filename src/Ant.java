import java.util.List;
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
    private World world;
    protected EnvironmentNode currentNode;
    private boolean naturalCauses;

    private static int cid = 0;

    public Ant(AntType t, World w) {
        id = cid++;
        world = w;
        type = t;
        lifespan = 3650;
        birthDay = world.getTurnNumber();
        alive = true;
        naturalCauses = false;
    }

    void setCurrentNode(EnvironmentNode n) {
        currentNode = n;
    }

    void kill() {
        alive = false;
    }

    void setNaturalCauses() {
        naturalCauses = true;
    }

    boolean diedNaturally() {
        return naturalCauses;
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

    protected abstract void activate();

    protected void move(EnvironmentNode n) {
        currentNode.removeAnt(this);
        n.addAnt(this);
        currentNode = n;
    }

    protected void randomMove(List<EnvironmentNode> nodeList) {
        int rand = RAND.nextInt(nodeList.size());
        move(nodeList.get(rand));
    }

    protected int randInt(int max) {
        return RAND.nextInt(max);
    }

    protected float randFloat() {
        return RAND.nextFloat();
    }

    public String toString() {
        return String.format("%d: %s - Birth Turn: %d, Lifespan: %d", id, type, birthDay, lifespan);
    }

}
