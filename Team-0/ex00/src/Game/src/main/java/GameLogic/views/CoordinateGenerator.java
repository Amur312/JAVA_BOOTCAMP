package GameLogic.views;

import java.util.Set;

public class CoordinateGenerator {
    public static int[] generateUniqueCoordinates(Set<Integer> occupiedCoordinates, int weight, int height) {
        int x, y;
        do {
            x = (int) (Math.random() * weight);
            y = (int) (Math.random() * height);
        } while (!occupiedCoordinates.add(y * weight + x));

        return new int[]{x, y};
    }
}
