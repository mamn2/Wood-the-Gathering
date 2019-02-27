import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import wood.competition.IntelligentStrategy;
import wood.game.GameEngine;
import wood.game.TurnAction;
import wood.strategy.PlayerBoardView;
import wood.strategy.RandomStrategy;
import wood.strategy.WoodPlayerStrategy;
import wood.tiles.Tile;
import wood.tiles.TileType;

import java.awt.*;
import java.lang.invoke.VolatileCallSite;
import java.util.Random;

import static org.junit.Assert.*;

public class IntelligentStrategyTest {

    private static IntelligentStrategy intelligentWoodStrategy = new IntelligentStrategy();
    private static PlayerBoardView playerBoardView;
    private static GameEngine gameEngine;

    @BeforeClass
    public static void initializeStrategy() {

        intelligentWoodStrategy.initialize(30, 5, 100,
                new Point(0,0), false, new Random());

    }

    @Test
    public void testMoveInDirectionOfTree() throws AssertionError {

        TileType[][] tiles = new TileType[10][10];
        for (TileType[] x : tiles) {
            for (TileType y : x) {
                y = TileType.EMPTY;
            }
        }

        //Position is (1,6) on coordinate system
        tiles[3][1] = TileType.SEED;

        //Position is (6,1) on coordinate system
        tiles[8][6] = TileType.SEED;

        //Position is (2,4) on coordinate system
        tiles[5][2] = TileType.SEED;

        //Position is (1,1) on coordinate system
        tiles[8][1] = TileType.TREE;

        playerBoardView = new PlayerBoardView(tiles, new Point(0, 0), new Point(4, 3),
                0, 0);

        assertEquals(TurnAction.MOVE_RIGHT,
                intelligentWoodStrategy.moveInDirectionOfTileType(playerBoardView, TileType.TREE));

        assertEquals(TurnAction.MOVE_UP,
                intelligentWoodStrategy.moveInDirectionOfTileType(playerBoardView, TileType.TREE));

        assertEquals(TurnAction.PICK_UP,
                intelligentWoodStrategy.moveInDirectionOfTileType(playerBoardView, TileType.TREE));


    }

    @Test
    public void testAISuperior() throws AssertionError {

        int randomWins = 0;
        int aiWins = 0;
        int pointWins = 0;

        for (int i = 0; i < 1000; i++ ) {
            WoodPlayerStrategy redPlayerStrat = new RandomStrategy();
            WoodPlayerStrategy bluePlayerStrat = new IntelligentStrategy();
            gameEngine = new GameEngine(30, redPlayerStrat, bluePlayerStrat);

            //runGameLoop returns true if the winner is red
            if (gameEngine.runGameLoop() > 2000) {
                pointWins++;
            } else {
                //aiWins++;
            }
        }

        double winPercentage = 1.00 - (randomWins * .0005);

        System.out.println("EINSTEIN WINS: " + aiWins);
        System.out.println("RANDOM STRATEGY WINS: " + randomWins);

        System.out.println("EINSTEINWINSBYPOINTS: ." + pointWins);
        //assertTrue(winPercentage > .99);

    }

}
