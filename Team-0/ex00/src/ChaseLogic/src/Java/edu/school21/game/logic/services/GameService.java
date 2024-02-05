package edu.school21.game.logic.services;

import edu.school21.game.logic.controllers.GameController;
import edu.school21.game.logic.models.GameObjectTypes;
import edu.school21.game.logic.models.Enemy;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервис для игровой логики, связанной с перемещением врагов.
 */
public class GameService {
    private static final Logger logger = Logger.getLogger(GameService.class.getName());
    /**
     * Массив карты игры.
     */
    private int[][] map;
    /**
     * Экземпляр игрового контроллера.
     */
    private GameController gameController;

    /**
     * Конструктор.
     *
     * @param map            Массив карты игры
     * @param gameController Экземпляр игрового контроллера
     */
    public GameService(int[][] map, GameController gameController) {
        this.map = map;
        this.gameController = gameController;
    }

    /**
     * Перемещает врага вперед к меньшим индексам карты.
     *
     * @param enemy  Враг для перемещения
     * @param height Высота карты
     */
    public void moveForward(Enemy enemy, int height) {
        moveEnemy(enemy, height, MoveDirection.FORWARD);
    }

    /**
     * Перемещает врага назад к большим индексам карты.
     *
     * @param enemy  Враг для перемещения
     * @param height Высота карты
     */
    public void moveBack(Enemy enemy, int height) {
        moveEnemy(enemy, height, MoveDirection.BACKWARD);
    }

    /**
     * Перемещает врага влево к меньшим индексам по ширине.
     *
     * @param enemy  Враг для перемещения
     * @param weight Ширина карты
     */
    public void moveLeft(Enemy enemy, int weight) {
        moveEnemy(enemy, weight, MoveDirection.LEFT);
    }

    /**
     * Перемещает врага вправо к большим индексам по ширине.
     *
     * @param enemy  Враг для перемещения
     * @param weight Ширина карты
     */
    public void moveRight(Enemy enemy, int weight) {
        moveEnemy(enemy, weight, MoveDirection.RIGHT);
    }

    /**
     * Перемещает врага в указанном направлении.
     *
     * @param enemy     Враг для перемещения
     * @param max       Максимальный индекс по направлению
     * @param direction Направление перемещения
     */
    private void moveEnemy(Enemy enemy, int max, MoveDirection direction) {
        int x = enemy.getCurrentPosition()[0];
        int y = enemy.getCurrentPosition()[1];

        validateCoordinates(x, y);

        switch (direction) {
            case FORWARD:
                moveEnemyForward(enemy, max, x, y);
                break;
            case BACKWARD:
                moveEnemyBackward(enemy, max, x, y);
                break;
            case LEFT:
                moveEnemyLeft(enemy, max, x, y);
                break;
            case RIGHT:
                moveEnemyRight(enemy, max, x, y);
                break;
        }
    }

    /**
     * Проверка, что координаты x и y в пределах границ карты.
     *
     * @param x Координата x
     * @param y Координата y
     */
    private void validateCoordinates(int x, int y) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) {
            logger.log(Level.WARNING, "Invalid coordinates: x={0}, y={1}", new Object[]{x, y});
            throw new IllegalArgumentException("Invalid coordinates");
        }
    }

    /**
     * Перемещает врага вперед к меньшим индексам карты.
     *
     * @param enemy Враг для перемещения
     * @param max   Максимальный индекс по высоте
     * @param x     Текущая координата X
     * @param y     Текущая координата Y
     */
    private void moveEnemyForward(Enemy enemy, int max, int x, int y) {
        if (x == 0) {
            handleBoundaryMove(enemy, max - 1, y);
        } else {
            handleRegularMove(enemy, x - 1, y);
        }
    }

    /**
     * Перемещает врага назад к большим индексам карты.
     *
     * @param enemy Враг для перемещения
     * @param max   Максимальный индекс по высоте
     * @param x     Текущая координата X
     * @param y     Текущая координата Y
     */
    private void moveEnemyBackward(Enemy enemy, int max, int x, int y) {
        if (x == max - 1) {
            handleBoundaryMove(enemy, 0, y);
        } else {
            handleRegularMove(enemy, x + 1, y);
        }
    }

    /**
     * Перемещает врага влево к меньшим индексам по ширине.
     *
     * @param enemy Враг для перемещения
     * @param max   Максимальный индекс по ширине
     * @param x     Текущая координата X
     * @param y     Текущая координата Y
     */
    private void moveEnemyLeft(Enemy enemy, int max, int x, int y) {
        if (y == 0) {
            handleBoundaryMove(enemy, x, max - 1);
        } else {
            handleRegularMove(enemy, x, y - 1);
        }
    }

    /**
     * Перемещает врага вправо к большим индексам по ширине.
     *
     * @param enemy Враг для перемещения
     * @param max   Максимальный индекс по ширине
     * @param x     Текущая координата X
     * @param y     Текущая координата Y
     */
    private void moveEnemyRight(Enemy enemy, int max, int x, int y) {
        if (y == max - 1) {
            handleBoundaryMove(enemy, x, 0);
        } else {
            handleRegularMove(enemy, x, y + 1);
        }
    }

    /**
     * Обрабатывает перемещение врага при достижении границы карты.
     *
     * @param enemy Враг для перемещения
     * @param newX  Новая координата X
     * @param newY  Новая координата Y
     */
    private void handleBoundaryMove(Enemy enemy, int newX, int newY) {
        if (isValidMove(enemy, newX, newY)) {
            map[enemy.getCurrentPosition()[0]][enemy.getCurrentPosition()[1]] = 0;
            enemy.setCurrentPosition(newX, newY);
            gameController.updateEnemyCoordinates(map, newX, newY, GameObjectTypes.ENEMY, enemy);
        }
    }

    /**
     * Обрабатывает стандартное перемещение врага.
     *
     * @param enemy Враг для перемещения
     * @param newX  Новая координата X
     * @param newY  Новая координата Y
     */
    private void handleRegularMove(Enemy enemy, int newX, int newY) {
        if (isValidMove(enemy, newX, newY)) {
            map[enemy.getCurrentPosition()[0]][enemy.getCurrentPosition()[1]] = 0;
            enemy.setCurrentPosition(newX, newY);
            gameController.updateEnemyCoordinates(map, newX, newY, GameObjectTypes.ENEMY, enemy);
        }
    }

    /**
     * Проверяет, валидно ли перемещение врага в указанные координаты.
     *
     * @param enemy Враг для перемещения
     * @param x     Координата X
     * @param y     Координата Y
     * @return true если перемещение валидно, иначе false
     */
    private boolean isValidMove(Enemy enemy, int x, int y) {

        // Проверка индексов
        if (x < 0 || x >= map.length) {
            return false;
        }

        if (y < 0 || y >= map[x].length) {
            return false;
        }
//
//        // Вывод индексов и размеров для отладки
//        System.out.println("x: " + x + ", y: " + y);
//        System.out.println("map.length: " + map.length + ", map[x].length: " + map[x].length);

        return map[x][y] != GameObjectTypes.WALLS.getValue()
                && map[x][y] != GameObjectTypes.GOAL.getValue()
                && map[x][y] != GameObjectTypes.ENEMY.getValue();
    }


    /**
     * Перечисление направлений перемещения.
     */
    private enum MoveDirection {

        /**
         * Вперед по оси X к меньшим индексам.
         */
        FORWARD,

        /**
         * Назад по оси X к большим индексам.
         */
        BACKWARD,

        /**
         * Влево по оси Y к меньшим индексам.
         */
        LEFT,

        /**
         * Вправо по оси Y к большим индексам.
         */
        RIGHT
    }

}

