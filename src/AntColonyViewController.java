public class AntColonyViewController {

    private AntSimGUI gui;
    private World world;
    private ColonyView colonyView;
    private ColonyNodeView[] nodeViews;

    public AntColonyViewController() {
        nodeViews = new ColonyNodeView[729];
        for (int i=0; i<nodeViews.length; i++) {
            nodeViews[i] = new ColonyNodeView();
        }

        gui = new AntSimGUI();
        world = new World();
        colonyView = new ColonyView(27,27);

        gui.initGUI(colonyView);
        addNodes();
        updateNodes();
    }

    public void updateNodes() {
        for (int i=0; i<nodeViews.length; i++) {
           updateNode(i);
        }
    }

    public void addNodes() {
        for (int i=0; i<nodeViews.length; i++) {
            int x = i % 27;
            int y = i / 27;
            ColonyNodeView node = nodeViews[i];
            colonyView.addColonyNodeView(node, x, y);
        }
    }

    private void updateNode(int index) {
        Node modelNode = world.getNode(index);
        ColonyNodeView viewNode = nodeViews[index];

        if (modelNode.isRevealed()) {
            viewNode.showNode();
        } else {
            viewNode.hideNode();
        }

        viewNode.setID(String.format("%d, %d", index % 27, index / 27));

        viewNode.setFoodAmount(modelNode.getFoodAmount());
        viewNode.setPheromoneLevel(modelNode.getPheromoneAmount());

        if (modelNode.hasAntOfType(Ant.AntType.QUEEN)) {
            viewNode.setQueen(true);
            viewNode.showQueenIcon();
        } else {
            viewNode.setQueen(false);
            viewNode.hideQueenIcon();
        }

        int scoutCount = modelNode.scoutCount();
        viewNode.setScoutCount(scoutCount);
        if (scoutCount > 0) {
            viewNode.showScoutIcon();
        } else {
            viewNode.hideScoutIcon();
        }

        int soldierCount = modelNode.soldierCount();
        viewNode.setSoldierCount(soldierCount);
        if (soldierCount > 0) {
            viewNode.showSoldierIcon();
        } else {
            viewNode.hideSoldierIcon();
        }

        int foragerCount = modelNode.foragerCount();
        viewNode.setForagerCount(foragerCount);
        if (foragerCount > 0) {
            viewNode.showForagerIcon();
        } else {
            viewNode.hideForagerIcon();
        }

        int balaCount = modelNode.balaCount();
        viewNode.setBalaCount(balaCount);
        if (balaCount > 0) {
            viewNode.showBalaIcon();
        } else {
            viewNode.hideBalaIcon();
        }
    }

}
