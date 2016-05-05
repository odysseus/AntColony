public class AntColonyViewController {

    private AntSimGUI gui;
    private World world;

    public AntColonyViewController() {
        gui = new AntSimGUI();
        world = new World();

        gui.initGUI(new ColonyView(27,27));
    }

}
