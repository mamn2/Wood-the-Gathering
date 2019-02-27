package wood.competition; // This is the "competition" package

import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import wood.game.TurnAction;
import wood.game.WoodPlayer;
import wood.item.InventoryItem;
import wood.item.SeedItem;
import wood.item.WoodItem;
import wood.strategy.PlayerBoardView;
import wood.strategy.WoodPlayerStrategy;
import wood.tiles.Tile;
import wood.tiles.TileType;
import wood.tiles.TreeTile;
// ^ These classes were provided to you, they do not need to be in the competition package

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Random;
// ^ These classes are a part of Java, they also do not need to be in the competition package

/**
 * Because this class is in the competition package, it will be compiled and run in the competition.
 */
public class IntelligentStrategy implements WoodPlayerStrategy {

    private int boardSize;
    private LinkedList<InventoryItem> inventoryItems;
    private int maxInventoryItems;

    int numberOfTurns = 0;

    public IntelligentStrategy() {

        this.inventoryItems = new LinkedList<>();

    }

    @Override
    public void initialize(int boardSize, int maxInventorySize, int winningScore, Point startTileLocation,
                           boolean isRedPlayer, Random random) {

        this.boardSize = boardSize;
        this.maxInventoryItems = maxInventorySize;

    }

    @Override
    public TurnAction getTurnAction(PlayerBoardView boardView, boolean isRedTurn) {


        if (numberOfTurns < 500) {

            if (inventoryItems.size() == 0) {
                numberOfTurns++;
                TurnAction action = moveInDirectionOfTileType(boardView, TileType.SEED);
                if (action == null) {
                    System.out.println("picking up");
                    inventoryItems.add(new SeedItem(0));
                    return TurnAction.PICK_UP;
                } else {
                    return action;
                }
            } else {
                numberOfTurns++;
                TurnAction action = moveInDirectionOfTileType(boardView, TileType.EMPTY);
                if (action == null) {
                    System.out.println("planting seed");
                    inventoryItems.removeFirst();
                    return TurnAction.PLANT_SEED;
                } else {
                    return action;
                }
            }

        } else if (numberOfTurns == 500){

            System.out.println("REACHED THRESHOLDojfowiejfoiwejfoiwjfoiwjfowejfowejfo;iwejf;oijfo;");

            if (inventoryItems.size() > 0) {
                TurnAction action = moveInDirectionOfTileType(boardView, TileType.EMPTY);
                if (action == null) {
                    inventoryItems.removeFirst();
                    return TurnAction.PLANT_SEED;
                }
            } else {
                numberOfTurns++;
                return TurnAction.MOVE_DOWN;
            }

        } else {

            numberOfTurns++;

            if (inventoryItems.size() != maxInventoryItems) {
                TurnAction action = moveInDirectionOfTileType(boardView, TileType.TREE);
                if (action == null) {
                    inventoryItems.add(new WoodItem(0));
                    return TurnAction.CUT_TREE;
                } else {
                    return action;
                }
            } else {
                TurnAction action = moveInDirectionOfTileType(boardView, TileType.START);
                if (action == null) {
                    inventoryItems = new LinkedList<>();
                    return moveInDirectionOfTileType(boardView, TileType.TREE);
                } else {
                    return action;
                }
            }

        }

        return null;


    }


    public TurnAction moveInDirectionOfTileType(PlayerBoardView boardView, TileType tileType) {

        int shortestNumTurns = Integer.MAX_VALUE;
        Point nearestTile = null;

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                TileType currentTile = boardView.getTileTypeAtLocation(x, y);
                if (currentTile == tileType) {
                    int numTurns = (int) (Math.abs(boardView.getYourLocation().getX() - x)
                            + Math.abs(boardView.getYourLocation().getY() - y));
                    if (numTurns < shortestNumTurns) {
                        shortestNumTurns = numTurns;
                        nearestTile = new Point(x, y);
                    }
                }
            }
        }

        if (nearestTile == null) {
            System.out.println("NO SEEDS");
            return null;
        } else if (nearestTile.getX() < boardView.getYourLocation().getX()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x - 1, boardView.getYourLocation().y);
            System.out.println("left");
            return TurnAction.MOVE_LEFT;
        } else if (nearestTile.getX() > boardView.getYourLocation().getX()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x + 1, boardView.getYourLocation().y);
            System.out.println("right");
            return TurnAction.MOVE_RIGHT;
        } else if (nearestTile.getY() < boardView.getYourLocation().getY()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x, boardView.getYourLocation().y - 1);
            return TurnAction.MOVE_DOWN;
        } else if (nearestTile.getY() > boardView.getYourLocation().getY()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x, boardView.getYourLocation().y + 1);
            return TurnAction.MOVE_UP;
        } else {
            return null;
        }


    }

    @Override
    public void receiveItem(InventoryItem itemReceived) {

        if (inventoryItems.size() < maxInventoryItems) {
            inventoryItems.add(itemReceived);
        }

    }

    @Override
    public String getName() {
        return "EINSTEIN";
    }

    @Override
    public void endRound(int pointsScored, int opponentPointsScored) {

    }

}
