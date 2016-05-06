import java.util.EnumMap;

class Queen extends Ant {

    Queen(World world) {
        super(AntType.QUEEN, world);
        this.setLifespan(3650 * 20);
    }

    @Override
    protected void activate() {
        if (currentNode.getFoodAmount() == 0) {
            kill();
        } else {
            getCurrentNode().takeFood();
        }
    }

}
