package GameLogic.controllers;

import GameLogic.services.GameContext;
import edu.school21.game.logic.models.GameObjectTypes;


import java.io.FileNotFoundException;

public class PlayerController {
    private static final int UP = -1;
    private static final int DOWN = 1;
    private static final int NO_MOVE = 0;

    private GameContext gameContext;
    private GameController gameController;

    public PlayerController(GameContext gameContext, GameController gameController) {
        this.gameContext = gameContext;
        this.gameController = gameController;
    }

    private void move(int[][] map, int xChange, int yChange) throws FileNotFoundException {
        int x = gameContext.getPlayerCoordinates()[0];
        int y = gameContext.getPlayerCoordinates()[1];

        int newX = (x + xChange + gameContext.getHeight()) % gameContext.getHeight();
        int newY = (y + yChange + gameContext.getWeight()) % gameContext.getWeight();

        if (map[newX][newY] != GameObjectTypes.WALLS.getValue()) {
            map[x][y] = 0;
            gameController.checkGameStatus(map, newX, newY);
        } else {
            gameController.checkGameStatus(map, x, y);
        }
    }

    public void moveForward(int[][] map) throws FileNotFoundException {
        move(map, UP, NO_MOVE);
    }

    public void moveBack(int[][] map) throws FileNotFoundException {
        move(map, DOWN, NO_MOVE);
    }

    public void moveLeft(int[][] map) throws FileNotFoundException {
        move(map, NO_MOVE, -1);
    }

    public void moveRight(int[][] map) throws FileNotFoundException {
        move(map, NO_MOVE, 1);
    }
}
