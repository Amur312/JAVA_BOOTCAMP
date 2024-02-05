package edu.school21.game.logic.models;

/**
 * Представляет врага в игре.
 */
public class Enemy {
    /**
     * Массив для хранения координат врага.
     */
    private int[] coordinates;

    /**
     * Конструктор класса.
     *
     * @param playerCoordinates координаты игрока
     */
    public Enemy(int[] playerCoordinates) {
        coordinates = new int[2];
    }

    /**
     * Возвращает текущие координаты врага.
     *
     * @return массив координат
     */
    public int[] getCurrentPosition() {
        return coordinates;
    }

    /**
     * Устанавливает новые координаты врага.
     *
     * @param x координата X
     * @param y координата Y
     */
    public void setCurrentPosition(int x, int y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }
}
