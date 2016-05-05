public abstract class Ant {


    protected enum AntType {
        QUEEN,
        FORAGER,
        SCOUT,
        SOLDIER,
        BALA
    }

    protected int id;
    protected AntType type;
    protected int lifespan;
    protected int birthDay;
    protected boolean alive;
    protected Node currentNode;

    private static int cid = 0;

    public Ant(AntType antType, int bday) {
        id = ++cid;
        type = antType;
        lifespan = 365;
        birthDay = bday;
        alive = true;
    }

    public String toString() {
        return String.format("Type: %s -- ID: %d -- Lifespan: %d",
                type, id, lifespan);
    }

    protected abstract void activate();

}
