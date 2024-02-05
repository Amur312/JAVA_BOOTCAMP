package GameLogic.controllers;

import GameLogic.services.GameConfigurationArgs;
import GameLogic.services.GameContext;
import GameLogic.services.PropertiesFileParser;
import GameLogic.views.MapBuilder;
import GameLogic.views.GameMapPainter;
import edu.school21.game.logic.models.GameObjectTypes;


import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;

public class GameController {
    private static Scanner scanner;
    private GameContext gameContext;
    private PlayerController playerController;
    private GameMapPainter gameMapPainter;
    private edu.school21.game.logic.controllers.GameController gameController;
    private int[][] gameMap;
    private Path pathProperties;
    private GameConfigurationArgs jargs;
    private static boolean isRendered = false;
    private static boolean isDev = false;

    public GameController(Path pathProperties, GameConfigurationArgs jargs) {
        this.pathProperties = pathProperties;
        this.jargs = jargs;
        scanner = new Scanner(System.in);
    }

    public void startGame() throws FileNotFoundException {
        if (!isRendered) {
            init();
            renderMap();
            isRendered = true;
        }
        processGameInput();
    }

    private void init() throws FileNotFoundException {
        gameContext = new GameContext(PropertiesFileParser.parseProperties(pathProperties), jargs);
        isDev = "dev".equals(gameContext.getProfile());
        gameMapPainter = new GameMapPainter(gameContext);
        playerController = new PlayerController(gameContext, this);
        MapBuilder mapBuilder = new MapBuilder(gameContext);
        gameMap = mapBuilder.generateGameMap();
        gameController = new edu.school21.game.logic.controllers.GameController(gameMap);
    }

    private void renderMap() {
        gameMapPainter.paintMap(gameMap);
    }

    private void processGameInput() throws FileNotFoundException {
        String command;
        while (true) {
            System.out.print("Введите команду движения -> ");
            command = scanner.nextLine();
            if (command.equals("9")) {
                System.out.println("Пока!");
                System.exit(-1);
            }
            switch (command.toLowerCase()) {
                case "w":
                    playerController.moveForward(gameMap);
                    break;
                case "s":
                    playerController.moveBack(gameMap);
                    break;
                case "a":
                    playerController.moveLeft(gameMap);
                    break;
                case "d":
                    playerController.moveRight(gameMap);
                    break;
                default:
                    isRendered = false;
                    startGame();
            }
        }
    }

    public void checkGameStatus(int[][] map, int x, int y) throws FileNotFoundException {
        if (map[x][y] == GameObjectTypes.GOAL.getValue()) {
            System.out.println("Вы победили!");
            restartGame();
        } else {
            updateMap(map, x, y, GameObjectTypes.PLAYER);
        }

        if (gameController.getStatus()) {
            endGame();
        }
    }

    private void updateMap(int[][] map, int x, int y, GameObjectTypes object) throws FileNotFoundException {
        checkPlayerCollisions(map, x, y);

        map[x][y] = object.getValue();
        gameContext.setPlayerCoordinates(x, y);
        gameMapPainter.paintMap(map);

        if (isDev) {
            System.out.print("Введите 8 чтобы принять ход врага или 9 чтобы выйти -> ");
            String command = scanner.nextLine();

            if ("8".equals(command)) {
                gameController.processEnemyMoves(gameContext.getEnemies(), gameContext.getPlayerCoordinates(), gameContext.getHeight(), gameContext.getWeight());
                map = gameController.getMap();
                gameMapPainter.paintMap(map);

            } else if ("9".equals(command)) {
                System.exit(-1);

            } else {
                updateMap(map, x, y, object);
            }

        } else {
            System.out.println();

            gameController.processEnemyMoves(gameContext.getEnemies(), gameContext.getPlayerCoordinates(), gameContext.getHeight(), gameContext.getWeight());
            map = gameController.getMap();
            gameMapPainter.paintMap(map);
        }
    }


    private void checkPlayerCollisions(int[][] map, int x, int y) throws FileNotFoundException {
        if (map[x][y] == GameObjectTypes.ENEMY.getValue()) {
            endGame();
        }
    }

    private void endGame() throws FileNotFoundException {
        System.out.println("Вы проиграли!");

        restartGame();
    }

    private void restartGame() throws FileNotFoundException {

        System.out.print("Хотите попробовать еще раз? Yes/No? -> ");

        String command = scanner.nextLine();

        if ("Yes".equalsIgnoreCase(command)) {
            isRendered = false;
            startGame();

        } else {
            System.out.println("Игра завершена");
        }
    }

}
