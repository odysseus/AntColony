import java.util.List;

class Scout extends Ant {

    Scout(World world) {
        super(AntType.SCOUT, world);
    }

    @Override
    protected void activate() {
        if (isAlive()) {
            List<EnvironmentNode> adjacent = currentNode.getAdjacentNodes();
            randomMove(adjacent);
            currentNode.setRevealed();
        }
    }

}
