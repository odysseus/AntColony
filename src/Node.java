import java.util.LinkedList;

public class Node {
    private int food;
    private int pheromone;
    private boolean revealed;
    private LinkedList<Ant> colonyAnts;
    private LinkedList<Ant> enemyAnts;

    public Node() {
        food = 0;
        pheromone = 0;
        revealed = false;
        colonyAnts = new LinkedList<>();
        enemyAnts = new LinkedList<>();
    }

    public void addAnt(Ant a) {
        if (a.type != Ant.AntType.BALA) {
            colonyAnts.add(a);
        } else {
            enemyAnts.add(a);
        }
    }

    public void removeAnt(Ant a) {
        if (a.type != Ant.AntType.BALA) {
            colonyAnts.remove(a);
        } else {
            enemyAnts.remove(a);
        }
    }

    public void addFood(int amt) {
        food += amt;
    }

    public void takeFood() {
        food--;
    }
}
