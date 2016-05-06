import java.util.List;

class Scout extends Ant {

    Scout(World world) {
        super(AntType.SCOUT, world);
    }

    @Override
    protected void activate() {
        if (isAlive()) {
            List<EnvironmentNode> adjacent = world.getAdjacentNodes(getCurrentNode().getNumber());
            randomMove(adjacent);
            currentNode.setRevealed();
        }
    }

}
