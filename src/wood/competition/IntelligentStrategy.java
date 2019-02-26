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

    private ArrayList<TurnAction> possibleActions;
    private LinkedList<TurnAction> allActions;
    private int boardSize;
    private LinkedList<InventoryItem> inventoryItems;
    private int maxInventoryItems;
    private boolean isRedPlayer;
    private boolean collectingSeeds;
    private boolean plantingSeeds;
    private boolean collectingTrees;
    private boolean goingHome;

    public IntelligentStrategy() {

        this.possibleActions = new ArrayList<>(EnumSet.allOf(TurnAction.class));
        this.inventoryItems = new LinkedList<>();

    }

    @Override
    public void initialize(int boardSize, int maxInventorySize, int winningScore, Point startTileLocation,
                           boolean isRedPlayer, Random random) {

        this.boardSize = boardSize;
        this.maxInventoryItems = maxInventorySize;
        this.isRedPlayer = isRedPlayer;
        this.collectingSeeds = true;
        this.allActions = new LinkedList<>();

    }

    @Override
    public TurnAction getTurnAction(PlayerBoardView boardView, boolean isRedTurn) {

        TurnAction action = null;

        if (goingHome) {
            if (boardView.getTileTypeAtLocation(boardView.getYourLocation()) == TileType.START) {
                goingHome = false;
                collectingSeeds = true;
                plantingSeeds = false;
                collectingTrees = false;
                return getTurnAction(boardView, isRedTurn);
            } else {
                System.out.print("moving home");
                return moveInDirectionOfTileType(boardView, TileType.START);
            }
        } else if (collectingSeeds) {
            if (inventoryItems.size() == maxInventoryItems) {
                collectingSeeds = false;
                plantingSeeds = true;
                collectingTrees = false;
                goingHome = false;
                return getTurnAction(boardView, isRedTurn);
            } else {
                System.out.print("moving to seed ");
                return moveInDirectionOfTileType(boardView, TileType.SEED);
            }
        } else if (plantingSeeds) {
            if (inventoryItems.size() == 0) {
                plantingSeeds = false;
                collectingTrees = true;
                collectingSeeds = false;
                goingHome = false;
                return getTurnAction(boardView, isRedTurn);
            } else {
                System.out.print("moving to empty tile ");
                return moveInDirectionOfTileType(boardView, TileType.EMPTY);
            }
        } else if (collectingTrees) {
            if (inventoryItems.size() == maxInventoryItems) {
                collectingTrees = false;
                goingHome = true;
                collectingSeeds = false;
                plantingSeeds = false;
                return getTurnAction(boardView, isRedTurn);
            } else {
                System.out.print("moving to tree ");
                return moveInDirectionOfTileType(boardView, TileType.TREE);
            }
        }

        return null;

    }

    /**
     * Helper function for getTurnAction. Allows strategy to think of its next best move.
     * @param boardView is the current state of the board.
     * @param tileType is the TileType you are searching for.
     * @return the number of turns to reach that tile
     */
    public int shortestNumTurnsForTileType(PlayerBoardView boardView, TileType tileType) {

        int shortestNumTurns = Integer.MAX_VALUE;

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                TileType currentTile = boardView.getTileTypeAtLocation(x, y);
                if (currentTile == tileType) {
                    int numTurns = (int) (Math.abs(boardView.getYourLocation().getX() - x) + Math.abs(boardView.getYourLocation().getY() - y));
                    if (numTurns < shortestNumTurns) {
                        shortestNumTurns = numTurns;
                    }
                }
            }
        }

        return shortestNumTurns;

    }


    public TurnAction moveInDirectionOfTileType(PlayerBoardView boardView, TileType tileType) {

        int shortestNumTurns = Integer.MAX_VALUE;
        Point nearestTile = null;

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                TileType currentTile = boardView.getTileTypeAtLocation(x, y);
                if (currentTile == tileType) {
                    int numTurns = (int) (Math.abs(boardView.getYourLocation().getX() - x) + Math.abs(boardView.getYourLocation().getY() - y));
                    if (numTurns < shortestNumTurns) {
                        shortestNumTurns = numTurns;
                        nearestTile = new Point(x, y);
                    }
                }
            }
        }

        if (nearestTile.getX() < boardView.getYourLocation().getX()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x - 1, boardView.getYourLocation().y);
            System.out.println("left");
            allActions.add(TurnAction.MOVE_LEFT);
            return TurnAction.MOVE_LEFT;
        } else if (nearestTile.getX() > boardView.getYourLocation().getX()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x + 1, boardView.getYourLocation().y);
            System.out.println("right");
            allActions.add(TurnAction.MOVE_RIGHT);
            return TurnAction.MOVE_RIGHT;
        } else if (nearestTile.getY() < boardView.getYourLocation().getY()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x, boardView.getYourLocation().y - 1);
            System.out.println("Down");
            allActions.add(TurnAction.MOVE_DOWN);
            return TurnAction.MOVE_DOWN;
        } else if (nearestTile.getY() > boardView.getYourLocation().getY()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x, boardView.getYourLocation().y + 1);
            System.out.println("up");
            allActions.add(TurnAction.MOVE_UP);
            return TurnAction.MOVE_UP;
        } else if (tileType == TileType.EMPTY
                && boardView.getTileTypeAtLocation(boardView.getYourLocation()) == TileType.EMPTY) {
            System.out.println("planting seed");
            inventoryItems.removeFirst();
            return TurnAction.PLANT_SEED;
        }
        else {
            System.out.println("picking up");
            if (tileType == TileType.TREE) {
                receiveItem(new WoodItem(0));
            } else if (tileType == TileType.SEED) {
                receiveItem(new SeedItem(0));
            }
            //boardView.setTileType(currentLocation.x , currentLocation.y, TileType.EMPTY);
            //allActions.add(TurnAction.PICK_UP);
            return TurnAction.PICK_UP;
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