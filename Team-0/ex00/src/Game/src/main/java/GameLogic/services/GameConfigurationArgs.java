package GameLogic.services;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class GameConfigurationArgs {
    @Parameter(names = "--enemiesCount", description = "Number of enemies in the game")
    private int numberOfEnemies;

    @Parameter(names = "--wallsCount", description = "Number of walls in the game")
    private int numberOfWalls;

    @Parameter(names = "--size", description = "Size of the game")
    private int gameSize;

    @Parameter(names = "--profile", description = "Profile for game settings")
    private String gameProfile;

    public int getNumberOfEnemies() {
        return numberOfEnemies;
    }

    public int getNumberOfWalls() {
        return numberOfWalls;
    }

    public int getGameSize() {
        return gameSize;
    }

    public String getGameProfile() {
        return gameProfile;
    }

    @Override
    public String toString() {
        return String.format("--enemiesCount=%d --wallsCount=%d --size=%d --profile=%s",
                numberOfEnemies, numberOfWalls, gameSize, gameProfile);
    }
}
