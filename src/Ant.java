public abstract class Ant {


    static enum AntType {
        QUEEN,
        FORAGER,
        SCOUT,
        SOLDIER,
        BALA
    }

    private int id;
    private AntType type;
    private int lifespan;
    private int birthDay;
    private boolean alive;
    private Node currentNode;

    private static int cid = 0;

    public Ant(AntType antType, int bday) {
        id = ++cid;
        type = antType;
        lifespan = 3650;
        birthDay = bday;
        alive = true;
    }

    void setCurrentNode(Node n) {
        currentNode = n;
    }

    void kill() {
        alive = false;
    }

    boolean isAlive() {
        return alive;
    }

    Node getCurrentNode() {
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

}
