import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Soldier extends Ant {

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
                }
            } else {
                List<EnvironmentNode> exploredAdjacentNodes = world.getExploredAdjacentNodes(currentNode.getNumber());
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
