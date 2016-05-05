import java.util.EnumMap;

class Queen extends Ant {

    Queen(int bday) {
        super(AntType.QUEEN, bday);
        this.setLifespan(3650 * 20);
    }

    @Override
    protected void activate() {
        getCurrentNode().takeFood();
    }

}
