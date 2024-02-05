package edu.school21.game.logic.controllers;

import edu.school21.game.logic.models.Enemy;
import edu.school21.game.logic.services.GameService;
import edu.school21.game.logic.models.GameObjectTypes;

import java.util.List;

/**
 * Класс GameController предназначен для управления игровой логикой.
 * Он отвечает за перемещение врагов, проверку захвата игрока, обновление координат на карте.
 */
public class GameController {
    /**
     * Вероятность случайного шага врага.
     */
    private static final double RANDOM_STEP_PROBABILITY = 0.3;
    /**
     * Двумерный массив, представляющий игровую карту.
     */
    private int[][] map;
    /**
     * Сервис для взаимодействия с игровой логикой.
     */
    private GameService gameService;
    /**
     * Флаг, определяющий, пойман ли игрок.
     */
    private boolean isCaught = false;
    /**
     * Флаг для определения случайного шага врага.
     */
    private boolean step = false;
    /**
     * Координаты игрока на карте.
     */
    private int[] playerCoordinates;

    /**
     * Конструктор класса.
     *
     * @param map двумерный массив карты
     */
    public GameController(int[][] map) {
        this.map = map;
        this.gameService = new GameService(map, this);
    }

    /**
     * Обрабатывает ходы врагов.
     *
     * @param enemies           список врагов
     * @param playerCoordinates координаты игрока
     * @param height            высота карты
     * @param weight            ширина карты
     */
    public void processEnemyMoves(List<Enemy> enemies, int[] playerCoordinates, int height, int weight) {
        this.playerCoordinates = playerCoordinates;
        int playerX = playerCoordinates[0];
        int playerY = playerCoordinates[1];
        for (Enemy enemy : enemies) {
            step = Math.random() < RANDOM_STEP_PROBABILITY;
            int enemyX = enemy.getCurrentPosition()[0];
            int enemyY = enemy.getCurrentPosition()[1];
            checkEnemyMove(map.length, map[0].length, playerX, playerY, enemy, enemyX, enemyY);

        }
    }

    /**
     * Проверяет возможность хода врага.
     *
     * @param height  высота карты
     * @param weight  ширина карты
     * @param playerX координата X игрока
     * @param playerY координата Y игрока
     * @param enemy   объект врага
     * @param enemyX  координата X врага
     * @param enemyY  координата Y врага
     */
    private void checkEnemyMove(int height, int weight, int playerX, int playerY, Enemy enemy, int enemyX, int enemyY) {
        if (validatePlayerPosition(playerX, playerY, enemyX, enemyY)) {
            handleCapture(enemy, weight, height);
        } else {
            int difX = calculateDifference(playerX, enemyX, map.length);
            int difY = calculateDifference(playerY, enemyY, map[0].length);

            if (!step) {
                chooseMoveDirection(difY > difX, enemy, map[0].length, difX, difY);

            } else {
                chooseMoveDirection(difX > difY, enemy, map.length, difX, difY);
            }
        }
    }

    /**
     * Проверяет, находится ли игрок на соседней с врагом клетке.
     *
     * @param playerX координата X игрока
     * @param playerY координата Y игрока
     * @param enemyX  координата X врага
     * @param enemyY  координата Y врага
     * @return результат проверки
     */
    private boolean validatePlayerPosition(int playerX, int playerY, int enemyX, int enemyY) {
        return (enemyY + 1 == playerY && enemyX == playerX) ||
                (enemyY - 1 == playerY && enemyX == playerX) ||
                (enemyX + 1 == playerX && enemyY == playerY) ||
                (enemyX - 1 == playerX && enemyY == playerY);
    }

    /**
     * Обрабатывает захват игрока врагом.
     *
     * @param enemy  объект врага
     * @param weight ширина карты
     * @param height высота карты
     */
    private void handleCapture(Enemy enemy, int weight, int height) {
        if (gameService != null) {
            gameService.moveRight(enemy, weight);
        }
        isCaught = true;
    }

    /**
     * Вычисляет разницу между координатами.
     *
     * @param value1 первое значение
     * @param value2 второе значение
     * @param max    максимальное значение
     * @return разница между значениями
     */
    private int calculateDifference(int value1, int value2, int max) {
        int diff = Math.abs(value1 - value2);
        return Math.min(diff, max - diff);
    }

    /**
     * Выбирает направление хода врага.
     *
     * @param condition условие выбора направления
     * @param enemy     объект врага
     * @param parameter параметр для выбора направления
     * @param dif1      разница координат 1
     * @param dif2      разница координат 2
     */
    private void chooseMoveDirection(boolean condition, Enemy enemy, int parameter, int dif1, int dif2) {
        if (gameService != null) {
            if (condition) {
                if ((parameter - dif1) > dif1) {
                    gameService.moveBack(enemy, parameter);
                } else {
                    gameService.moveForward(enemy, parameter);
                }
            } else {
                if ((parameter - dif2) > dif2) {
                    gameService.moveLeft(enemy, parameter);
                } else {
                    gameService.moveRight(enemy, parameter);
                }
            }
        }
    }

    /**
     * Обновляет координаты врага на карте.
     *
     * @param map    двумерный массив карты
     * @param x      координата X
     * @param y      координата Y
     * @param object тип объекта на карте
     * @param enemy  объект врага
     * @return обновленный массив карты
     */

    public int[][] updateEnemyCoordinates(int[][] map, int x, int y, GameObjectTypes object, Enemy enemy) {
        if (map != null && enemy != null) {
            map[x][y] = object.getValue();
            enemy.setCurrentPosition(x, y);
        }
        return map;
    }

    /**
     * Проверяет, находится ли игрок на заданных координатах.
     *
     * @param x координата X
     * @param y координата Y
     */
    public void checkStatus(int x, int y) {
        if (playerCoordinates != null && x == playerCoordinates[0] && y == playerCoordinates[1]) {
            isCaught = true;
        }
    }

    /**
     * Возвращает игровую карту.
     *
     * @return двумерный массив карты
     */
    public int[][] getMap() {
        return map;
    }

    /**
     * Возвращает статус игры.
     *
     * @return флаг, пойман ли игрок
     */
    public boolean getStatus() {
        return isCaught;
    }
}