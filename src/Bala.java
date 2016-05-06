import java.util.LinkedList;
import java.util.List;

class Bala extends Ant {

    static int ATK_SUCCESS = 0;
    static int ATK_FAIL = 0;

    Bala(World world) {
        super(AntType.BALA, world);
    }

    @Override
    protected void activate() {
        if (currentNode.hasColonyAnts()) {
            LinkedList<Ant> colonyAnts = currentNode.getColonyAnts();
            Ant toAttack = colonyAnts.get(randInt(colonyAnts.size()));

            if (randFloat() > 0.50) {
                toAttack.kill();
                ATK_SUCCESS++;
            } else {
                ATK_FAIL++;
            }
        } else {
            List<EnvironmentNode> adjacent = currentNode.getAdjacentNodes();
            randomMove(adjacent);
        }
    }
}
