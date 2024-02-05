package GameLogic.views;

import GameLogic.services.GameContext;
import edu.school21.game.logic.models.Enemy;
import edu.school21.game.logic.models.GameObjectTypes;

import java.util.*;


public class MapBuilder {
    private static final int ENEMY_TYPE = 1;
    private final GameContext gameContext;
    private final int weight;
    private final int height;

    public MapBuilder(GameContext gameContext) {
        this.gameContext = Objects.requireNonNull(gameContext, "GameContext must not be null");
        this.weight = gameContext.getWeight();
        this.height = gameContext.getHeight();
    }

    public int[][] generateGameMap() {
        int[][] map = new int[height][weight];
        Set<Integer> occupiedCoordinates = new HashSet<>();

        validateParameters();

        generateEnemies(map, occupiedCoordinates);
        generateWalls(map, occupiedCoordinates);
        generatePlayer(map, occupiedCoordinates);
        generateGoal(map, occupiedCoordinates);

        return map;
    }

    private void validateParameters() {
        if ((gameContext.getEnemiesCount() + gameContext.getWallsCount()) > (gameContext.getHeight() * gameContext.getWeight())) {
            throw new RuntimeException("Not enough space for enemies and walls");
        }
    }

    private void generateEnemies(int[][] map, Set<Integer> occupiedCoordinates) {
        for (int i = 0; i < gameContext.getEnemiesCount(); i++) {
            int[] coordinates = CoordinateGenerator.generateUniqueCoordinates(occupiedCoordinates, weight, height);
            placeEnemy(coordinates[0], coordinates[1], map);
        }
    }

    private void generateWalls(int[][] map, Set<Integer> occupiedCoordinates) {
        int wallsCount = gameContext.getWallsCount();
        for (int i = 0; i < wallsCount; i++) {
            int[] coordinates = CoordinateGenerator.generateUniqueCoordinates(occupiedCoordinates, weight, height);
            map[coordinates[1]][coordinates[0]] = GameObjectTypes.WALLS.getValue();
        }
    }

    private void generatePlayer(int[][] map, Set<Integer> occupiedCoordinates) {
        int[] playerCoordinates = CoordinateGenerator.generateUniqueCoordinates(occupiedCoordinates, weight, height);
        map[playerCoordinates[1]][playerCoordinates[0]] = GameObjectTypes.PLAYER.getValue();
        gameContext.setPlayerCoordinates(playerCoordinates[1], playerCoordinates[0]);
    }

    private void generateGoal(int[][] map, Set<Integer> occupiedCoordinates) {
        int[] goalCoordinates = CoordinateGenerator.generateUniqueCoordinates(occupiedCoordinates, weight, height);
        map[goalCoordinates[1]][goalCoordinates[0]] = GameObjectTypes.GOAL.getValue();
    }

    private void placeEnemy(int x, int y, int[][] map) {
        Enemy enemy = createEnemy();
        setEnemyPosition(enemy, x, y);
        addEnemyToContext(enemy);
        map[y][x] = GameObjectTypes.ENEMY.getValue();
    }

    private Enemy createEnemy() {
        return new Enemy(gameContext.getPlayerCoordinates());
    }

    private void setEnemyPosition(Enemy enemy, int x, int y) {
        enemy.setCurrentPosition(y, x);
    }

    private void addEnemyToContext(Enemy enemy) {
        gameContext.setEnemies(enemy);
    }

}
