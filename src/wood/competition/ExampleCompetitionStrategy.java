package wood.competition; // This is the "competition" package

import wood.game.TurnAction;
import wood.item.InventoryItem;
import wood.strategy.PlayerBoardView;
import wood.strategy.WoodPlayerStrategy;
// ^ These classes were provided to you, they do not need to be in the competition package

import java.awt.*;
import java.util.Random;
// ^ These classes are a part of Java, they also do not need to be in the competition package

/**
 * Because this class is in the competition package, it will be compiled and run in the competition.
 * You cannot put more than one WoodPlayerStrategy implementation in the competition package, so you must
 *  either delete or modify this class in order to submit your strategy implementation
 */
public class ExampleCompetitionStrategy implements WoodPlayerStrategy {
    @Override
    public void initialize(int boardSize, int maxInventorySize, int winningScore, Point startTileLocation,
                           boolean isRedPlayer, Random random) {

    }

    @Override
    public TurnAction getTurnAction(PlayerBoardView boardView, boolean isRedTurn) {
        return null; // This strategy is just an example, it actually does nothing
    }

    @Override
    public void receiveItem(InventoryItem itemReceived) {
        // System.out.println("imgur.com/a/ur8UYP7")
        /* ^ As mentioned in the assignment documentation, do not print things in your competition strategy
           If you do print things in your strategy, you need to comment or remove these, otherwise
            your strategy will not be run in the competition */
    }

    @Override
    public String getName() {
        return ExampleOtherClass.exampleOtherMethod();
    }

    @Override
    public void endRound(int pointsScored, int opponentPointsScored) {

    }
}
