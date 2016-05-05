import java.util.EnumMap;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println(Ant.AntType.QUEEN.ordinal());

        EnumMap<Ant.AntType, Integer> antsOfType = new EnumMap<>(Ant.AntType.class);
        System.out.println(antsOfType.get(Ant.AntType.QUEEN));
    }

}
