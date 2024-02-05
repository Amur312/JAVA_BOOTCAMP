package GameLogic.views;

import GameLogic.services.GameContext;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import edu.school21.game.logic.models.GameObjectTypes;

public class GameMapPainter {

    private GameContext gameContext;

    public GameMapPainter(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void paintMap(int[][] map) {
        clearConsole();
        int width = gameContext.getWeight();
        int height = gameContext.getHeight();
        ColoredPrinter coloredPrinter = createColoredPrinter();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                printGameObject(map[i][j], coloredPrinter);
            }
            System.out.println();
        }
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private ColoredPrinter createColoredPrinter() {
        return new ColoredPrinter.Builder(1, false).background(Ansi.BColor.WHITE).build();
    }

    private void printGameObject(int gameObjectType, ColoredPrinter coloredPrinter) {
        if (gameObjectType == GameObjectTypes.ENEMY.getValue()) {
            char enemyChar = gameContext.getEnemyChar().charAt(0);
            printColoredCharacter(enemyChar, gameContext.getEnemyColor(), coloredPrinter);
        } else if (gameObjectType == GameObjectTypes.WALLS.getValue()) {
            char wallChar = gameContext.getWallChar().charAt(0);
            printColoredCharacter(wallChar, gameContext.getWallColor(), coloredPrinter);
        } else if (gameObjectType == GameObjectTypes.PLAYER.getValue()) {
            char playerChar = gameContext.getPlayerChar().charAt(0);
            printColoredCharacter(playerChar, gameContext.getPlayerColor(), coloredPrinter);
        } else if (gameObjectType == GameObjectTypes.GOAL.getValue()) {
            char goalChar = gameContext.getGoalChar().charAt(0);
            printColoredCharacter(goalChar, gameContext.getGoalColor(), coloredPrinter);
        } else {
            char emptyChar = gameContext.getEmptyChar().charAt(0);
            printColoredCharacter(emptyChar, gameContext.getEmptyColor(), coloredPrinter);
        }
    }


    private void printColoredCharacter(char character, String color, ColoredPrinter coloredPrinter) {
        coloredPrinter.print(character, Ansi.Attribute.NONE, Ansi.FColor.NONE, Ansi.BColor.valueOf(color));
    }
}
