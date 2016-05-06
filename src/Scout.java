class Scout extends Ant {

    Scout(World world) {
        super(AntType.SCOUT, world);
    }

    @Override
    protected void activate() {
        EnvironmentNode[] adjacent = world.getAdjacentNodes(getCurrentNode().getNumber());
        randomMove(adjacent);
        currentNode.setRevealed();
    }

}
