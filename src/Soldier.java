import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Soldier extends Ant {

    static int ATK_SUCCESS = 0;
    static int ATK_FAIL = 0;

    Soldier(World world) {
        super(AntType.SOLDIER, world);
    }

    @Override
    protected void activate() {
        if (isAlive()) {
            if (currentNode.hasEnemyAnts()) {
                LinkedList<Ant> enemies = currentNode.getEnemyAnts();
                Ant toAttack = enemies.get(randInt(enemies.size()));
                if (randFloat() > 0.50) {
                    toAttack.kill();
                    ATK_SUCCESS++;
                } else {
                    ATK_FAIL++;
                }
            } else {
                List<EnvironmentNode> exploredAdjacentNodes = currentNode.getExploredAdjacentNodes();
                List<EnvironmentNode> enemyNodes = new ArrayList<>();
                for (EnvironmentNode node : exploredAdjacentNodes) {
                    if (node.hasEnemyAnts()) {
                        enemyNodes.add(node);
                    }
                }

                if (enemyNodes.size() > 0) {
                    randomMove(enemyNodes);
                } else {
                    randomMove(exploredAdjacentNodes);
                }
            }
        }
    }
}
