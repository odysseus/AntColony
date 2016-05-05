public class Queen extends Ant {

    public Queen(int bday) {
        super(AntType.QUEEN, bday);
        this.lifespan = 365 * 20;
    }

    @Override
    protected void activate() {
    }

}
