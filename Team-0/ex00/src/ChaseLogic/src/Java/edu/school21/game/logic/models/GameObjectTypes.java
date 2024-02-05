package edu.school21.game.logic.models;

/**
 * Перечисление типов игровых объектов.
 */
public enum GameObjectTypes {
    /**
     * Тип - враг.
     */
    ENEMY,
    /**
     * Тип - игрок.
     */
    PLAYER,
    /**
     * Тип - стена.
     */
    WALLS,
    /**
     * Тип - цель.
     */
    GOAL;
    /**
     * Числовое значение типа.
     */
    private final int value;

    /**
     * Конструктор перечисления.
     * Присваивает каждому типу числовое значение на основе порядкового номера.
     */
    GameObjectTypes() {
        this.value = ordinal() + 1;
    }

    /**
     * Возвращает числовое значение типа.
     *
     * @return Числовое значение типа
     */
    public int getValue() {
        return value;
    }
}
