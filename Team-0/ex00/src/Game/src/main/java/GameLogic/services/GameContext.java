package GameLogic.services;

import GameLogic.models.Player;
import edu.school21.game.logic.models.Enemy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameContext {
    private static final String ENEMY_CHAR_KEY = "enemy.char";
    private static final String PLAYER_CHAR_KEY = "player.char";
    private static final String WALL_CHAR_KEY = "wall.char";
    private static final String GOAL_CHAR_KEY = "goal.char";
    private static final String EMPTY_CHAR_KEY = "empty.char";

    private static final String ENEMY_COLOR_KEY = "enemy.color";
    private static final String PLAYER_COLOR_KEY = "player.color";
    private static final String WALL_COLOR_KEY = "wall.color";
    private static final String GOAL_COLOR_KEY = "goal.color";
    private static final String EMPTY_COLOR_KEY = "empty.color";

    private final Map<String, String> properties;
    private final Player player;
    private final GameConfigurationArgs gameConfigurationArgs;
    private final List<Enemy> enemies;

    public GameContext(Map<String, String> properties, GameConfigurationArgs gameConfigurationArgs) {
        this.properties = properties;
        this.gameConfigurationArgs = gameConfigurationArgs;
        this.player = new Player(this);
        this.enemies = new LinkedList<>();
    }

    public String getEnemyChar() {
        return properties.get(ENEMY_CHAR_KEY);
    }

    public String getPlayerChar() {
        return properties.get(PLAYER_CHAR_KEY);
    }

    public String getWallChar() {
        return properties.get(WALL_CHAR_KEY);
    }

    public String getGoalChar() {
        return properties.get(GOAL_CHAR_KEY);
    }

    public String getEmptyChar() {
        return properties.get(EMPTY_CHAR_KEY);
    }

    public String getEnemyColor() {
        return properties.get(ENEMY_COLOR_KEY);
    }

    public String getPlayerColor() {
        return properties.get(PLAYER_COLOR_KEY);
    }

    public String getWallColor() {
        return properties.get(WALL_COLOR_KEY);
    }

    public String getGoalColor() {
        return properties.get(GOAL_COLOR_KEY);
    }

    public String getEmptyColor() {
        return properties.get(EMPTY_COLOR_KEY);
    }

    public int getSizeMap() {
        return gameConfigurationArgs.getGameSize();
    }

    public int getWallsCount() {
        return gameConfigurationArgs.getNumberOfWalls();
    }

    public int getEnemiesCount() {
        return gameConfigurationArgs.getNumberOfEnemies();
    }

    public String getProfile() {
        return gameConfigurationArgs.getGameProfile();
    }

    public int getWeight() {
        return getSizeMap() * 60 / 100;
    }

    public int getHeight() {
        return getSizeMap() - getWeight();
    }

    public int[] getPlayerCoordinates() {
        return player.getposition();
    }

    public void setPlayerCoordinates(int x, int y) {
        player.setposition(x, y);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(Enemy enemy) {
        enemies.add(enemy);
    }
}
