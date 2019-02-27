import org.junit.Before;
import org.junit.Test;
import wood.competition.IntelligentStrategy;
import wood.game.GameEngine;
import wood.game.TurnAction;
import wood.strategy.PlayerBoardView;
import wood.strategy.RandomStrategy;
import wood.strategy.WoodPlayerStrategy;
import wood.tiles.TileType;

import java.awt.*;
import java.util.Random;

import static org.junit.Assert.*;

public class IntelligentStrategyTest {

    private static IntelligentStrategy intelligentWoodStrategy = new IntelligentStrategy();
    private static PlayerBoardView playerBoardView;
    private static GameEngine gameEngine;
    private WoodPlayerStrategy redPlayerStrat = new RandomStrategy();
    private WoodPlayerStrategy bluePlayerStrat = new IntelligentStrategy();

    @Before
    public void initializeGame() {

        gameEngine = new GameEngine(20, redPlayerStrat, bluePlayerStrat);

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

        assertNull(intelligentWoodStrategy.moveInDirectionOfTileType(playerBoardView, TileType.TREE));


    }

    @Test
    public void testAISuperiorSize10() throws AssertionError {

        int pointWins = 0;

        for (int i = 0; i < 1000; i++ ) {

            intelligentWoodStrategy.initialize(10, 5, 2000,
                    new Point(0,0), false, new Random());


            //runGameLoop returns score of intelligent strategy
            if (gameEngine.runGameLoop() > 2000) {
                pointWins++;
            }

        }

        System.out.println("EINSTEIN WINS " + pointWins + "/1000");
        assertTrue((double) pointWins / 1000.0 > .99);

    }

    @Test
    public void testAISuperiorSize20() throws AssertionError {

        int pointWins = 0;

        for (int i = 0; i < 1000; i++ ) {

            intelligentWoodStrategy.initialize(20, 5, 2000,
                    new Point(0,0), false, new Random());


            //runGameLoop returns score of intelligent strategy
            if (gameEngine.runGameLoop() > 2000) {
                pointWins++;
            }

        }

        System.out.println("EINSTEIN WINS " + pointWins + "/1000");
        assertTrue((double) pointWins / 1000.0 > .99);

    }

    @Test
    public void testAISuperiorSize30() throws AssertionError {

        int pointWins = 0;

        for (int i = 0; i < 1000; i++ ) {

            intelligentWoodStrategy.initialize(30, 5, 2000,
                    new Point(0,0), false, new Random());


            //runGameLoop returns score of intelligent strategy
            if (gameEngine.runGameLoop() > 2000) {
                pointWins++;
            }

        }

        System.out.println("EINSTEIN WINS " + pointWins + "/1000");
        assertTrue((double) pointWins / 1000.0 > .99);

    }

}
