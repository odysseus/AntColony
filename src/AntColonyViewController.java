/**
 *
 * CSC 385 - Data Structures and Algorithms
 * Spring 2016
 * Ryan Case (rcase5@uis.edu)
 *
 */

public class AntColonyViewController implements SimulationEventListener {
    /**
     * Handles all of the communication between the model object {@link World},
     * and the gui object {@link AntSimGUI}
     */

    /* * *
     *  Attributes
     * * */

    // GUI object
    private AntSimGUI gui;

    // Model object
    private World world;

    // Used by the gui to render the view
    private ColonyView colonyView;

    // Each nodeview represents a single node, passed to the colonyView
    private ColonyNodeView[] nodeViews;


    /* * *
     *  Constructors
     * * */

    /**
     * Initializes all the required gui components, model is not initialized
     * until {@link #init()} is called
     */
    public AntColonyViewController() {
        nodeViews = new ColonyNodeView[729];
        for (int i=0; i<nodeViews.length; i++) {
            nodeViews[i] = new ColonyNodeView();
        }

        gui = new AntSimGUI();
        colonyView = new ColonyView(27,27);

        gui.initGUI(colonyView);
        gui.addSimulationEventListener(this);
    }

    /* * *
     *  Methods
     * * */

    /**
     * Initializes the model object and adds the view nodes
     * to the {@link #colonyView}
     */
    private void init() {
        world = new World();
        world.generate();
        addNodes();
    }

    /**
     * Calls {@link #updateNode(int)} on every view node
     */
    private void updateNodes() {
        for (int i=0; i<nodeViews.length; i++) {
           updateNode(i);
        }
    }

    /**
     * Calls both {@link #updateNodes()} and {@link AntSimGUI#setTime(String)}
     */
    private void updateView() {
        updateNodes();
        gui.setTime(world.timeString());
    }

    /**
     * Adds all nodes from the controller array to the {@link #colonyView}
     */
    private void addNodes() {
        for (int i=0; i<nodeViews.length; i++) {
            int x = i % 27;
            int y = i / 27;
            ColonyNodeView node = nodeViews[i];
            colonyView.addColonyNodeView(node, x, y);
        }
    }

    /**
     * Updates the given node based on the information from the model
     *
     * @param index index of node, should be identical between the
     *              model and the gui
     */
    private void updateNode(int index) {
        EnvironmentNode modelNode = world.getNode(index);
        ColonyNodeView viewNode = nodeViews[index];

        // Revealed status
        if (modelNode.isRevealed()) {
            viewNode.showNode();
        } else {
            viewNode.hideNode();
        }

        // X, Y position
        viewNode.setID(String.format("%d, %d", index % 27, index / 27));

        // Show Queen if present
        if (modelNode.hasAntOfType(Ant.AntType.QUEEN)) {
            viewNode.setQueen(true);
            viewNode.showQueenIcon();
        } else {
            viewNode.setQueen(false);
            viewNode.hideQueenIcon();
        }

        // Show # of scouts and scout icon
        int scoutCount = modelNode.scoutCount();
        viewNode.setScoutCount(scoutCount);
        if (scoutCount > 0) {
            viewNode.showScoutIcon();
        } else {
            viewNode.hideScoutIcon();
        }

        // Show # of soldiers and soldier icon
        int soldierCount = modelNode.soldierCount();
        viewNode.setSoldierCount(soldierCount);
        if (soldierCount > 0) {
            viewNode.showSoldierIcon();
        } else {
            viewNode.hideSoldierIcon();
        }

        // Show # of foragers and forager icon
        int foragerCount = modelNode.foragerCount();
        viewNode.setForagerCount(foragerCount);
        if (foragerCount > 0) {
            viewNode.showForagerIcon();
        } else {
            viewNode.hideForagerIcon();
        }

        // Show # of balas and bala icon
        int balaCount = modelNode.balaCount();
        viewNode.setBalaCount(balaCount);
        if (balaCount > 0) {
            viewNode.showBalaIcon();
        } else {
            viewNode.hideBalaIcon();
        }

        // Food and pheromone amounts
        viewNode.setFoodAmount(modelNode.getFoodAmount());
        viewNode.setPheromoneLevel(modelNode.getPheromoneAmount());

    }

    /**
     * Implemented from the {@link SimulationEventListener} interface.
     * Catch-all event fired on any button press within the gui, this
     * links those presses into the model
     *
     * @param simEvent Type of simulation event
     */
    public void simulationEventOccurred(SimulationEvent simEvent) {
        switch (simEvent.getEventType()) {
            case SimulationEvent.NORMAL_SETUP_EVENT:
                init();
                break;
            case SimulationEvent.QUEEN_TEST_EVENT:
                world.queenTest();
                break;
            case SimulationEvent.SCOUT_TEST_EVENT:
                world.scoutTest();
                break;
            case SimulationEvent.SOLDIER_TEST_EVENT:
                world.soldierTest();
                break;
            case SimulationEvent.FORAGER_TEST_EVENT:
                world.foragerTest();
                break;
            case SimulationEvent.STEP_EVENT:
                world.nextTurn();
                break;
            case SimulationEvent.RUN_EVENT:
                world.fullRun();
                break;
        }

        updateView();
    }
}
