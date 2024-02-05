package GameLogic.models;

import GameLogic.services.GameContext;

public class Player {
    public static final int POSITION_SIZE = 2;
    private int[] position;
    private GameContext gameGameContext;
    public Player(GameContext gameGameContext) {
        this.gameGameContext = gameGameContext;
        this.position = new int[POSITION_SIZE];
    }
    public int[] getposition() {
        return position;
    }
    public void setposition(int x, int y) {
        if (x >= 0 && y >= 0) {
            position[0] = x;
            position[1] = y;
        } else {
            throw new IllegalArgumentException("position must be non-negative");
        }
    }
}