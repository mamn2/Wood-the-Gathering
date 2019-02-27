package wood.competition; // This is the "competition" package

import wood.game.TurnAction;
import wood.item.InventoryItem;
import wood.item.SeedItem;
import wood.item.WoodItem;
import wood.strategy.PlayerBoardView;
import wood.strategy.WoodPlayerStrategy;

import wood.tiles.TileType;

import java.awt.*;
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

    public int numberOfTurns = 0;

    /**
     *
     */
    public IntelligentStrategy() {

        this.inventoryItems = new LinkedList<>();

    }

    /**
     * Initializes instance variables of the strategy
     * @param boardSize The length and width of the square game board
     * @param maxInventorySize The maximum number of items that your player can carry at one time
     * @param winningScore The first player to reach this score wins the round
     * @param startTileLocation A Point representing your starting location in (x, y) coordinates
     *                          (0, 0) is the bottom left and (boardSize - 1, boardSize - 1) is the top right
     * @param isRedPlayer True if this strategy is the red player, false otherwise
     * @param random A random number generator, if your strategy needs random numbers you should use this.
     */
    @Override
    public void initialize(int boardSize, int maxInventorySize, int winningScore, Point startTileLocation,
                           boolean isRedPlayer, Random random) {

        this.boardSize = boardSize;
        this.maxInventoryItems = maxInventorySize;

    }

    /**
     * Gets next action for a player.
     * @param boardView A PlayerBoardView object representing all the information about the board and the other player
     *                  that your strategy is allowed to access
     * @param isRedTurn For use when two players attempt to move to the same spot on the same turn
     *                  If true: The red player will move to the spot, and the blue player will do nothing
     *                  If false: The blue player will move to the spot, and the red player will do nothing
     * @return next action to take.
     */
    @Override
    public TurnAction getTurnAction(PlayerBoardView boardView, boolean isRedTurn) {


        if (numberOfTurns < 500) {

            if (boardView.getTileTypeAtLocation(boardView.getYourLocation()) == TileType.TREE
                    && boardView.getCurrentTileValue() > 100) {
                numberOfTurns++;
                inventoryItems.addFirst(new WoodItem(0));
                return TurnAction.CUT_TREE;
            }

            if (inventoryItems.size() == 0) {
                numberOfTurns++;
                TurnAction action = moveInDirectionOfTileType(boardView, TileType.SEED);
                if (action == null) {
                    inventoryItems.add(new SeedItem(0));
                    return TurnAction.PICK_UP;
                } else {
                    return action;
                }
            } else {

                if (inventoryItems.getFirst() instanceof WoodItem) {
                    numberOfTurns++;
                    TurnAction action = moveInDirectionOfTileType(boardView, TileType.START);
                    LinkedList<InventoryItem> removeItems = new LinkedList<>();
                    if (action == null) {
                        for (InventoryItem item : inventoryItems) {
                            if (item instanceof WoodItem) {
                                removeItems.add(item);
                            }
                        }
                        inventoryItems.removeAll(removeItems);
                        return moveInDirectionOfTileType(boardView, TileType.SEED);
                    } else {
                        return action;
                    }
                } else {

                    numberOfTurns++;
                    TurnAction action = moveInDirectionOfTileType(boardView, TileType.EMPTY);
                    if (action == null) {
                        inventoryItems.removeFirst();
                        return TurnAction.PLANT_SEED;
                    } else {
                        return action;
                    }
                }
            }

        } else if (numberOfTurns == 500){

            if (inventoryItems.size() > 0) {

                boolean inventoryContainsWood = false;
                for (InventoryItem item : inventoryItems) {
                    if (item instanceof WoodItem) {
                        inventoryContainsWood = true;
                    }
                }

                if (inventoryContainsWood) {
                    TurnAction action = moveInDirectionOfTileType(boardView, TileType.START);
                    if (action == null) {
                        LinkedList<InventoryItem> removeItems = new LinkedList<>();
                        for (InventoryItem item : inventoryItems) {
                            if (item instanceof WoodItem) {
                                removeItems.add(item);
                            }
                        }
                        inventoryItems.removeAll(removeItems);
                        return moveInDirectionOfTileType(boardView, TileType.SEED);
                    } else {
                        return action;
                    }
                } else {

                    TurnAction action = moveInDirectionOfTileType(boardView, TileType.EMPTY);
                    if (action == null) {
                        inventoryItems.removeFirst();
                        return TurnAction.PLANT_SEED;
                    }
                    return action;
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

    }

    /**
     * Moves the player towards the tile type they are looking for.
     * @param boardView current state of the board
     * @param tileType the tile being searched for
     * @return direction to travel in or null if you are already standing on tile
     */
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
            return null;
        } else if (nearestTile.getX() < boardView.getYourLocation().getX()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x - 1, boardView.getYourLocation().y);
            return TurnAction.MOVE_LEFT;
        } else if (nearestTile.getX() > boardView.getYourLocation().getX()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x + 1, boardView.getYourLocation().y);
            return TurnAction.MOVE_RIGHT;
        } else if (nearestTile.getY() < boardView.getYourLocation().getY()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x, boardView.getYourLocation().y - 1);
            return TurnAction.MOVE_DOWN;
        } else if (nearestTile.getY() > boardView.getYourLocation().getY()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x, boardView.getYourLocation().y + 1);
            return TurnAction.MOVE_UP;
        } else {
            //returns null if you are currently standing on the tile.
            return null;
        }


    }

    /**
     * Adds an item to the inventory list. Called when player receives an item.
     * @param itemReceived The item received from the player's TurnAction on their last turn
     */
    @Override
    public void receiveItem(InventoryItem itemReceived) {

        if (inventoryItems.size() < maxInventoryItems) {
            inventoryItems.add(itemReceived);
        }

    }

    /**
     * Gets the name of the player
     * @return player name
     */
    @Override
    public String getName() {
        return "EINSTEIN";
    }

    /**
     * Resets the points for the round
     * @param pointsScored The total number of points this strategy scored
     * @param opponentPointsScored The total number of points the opponent's strategy scored
     */
    @Override
    public void endRound(int pointsScored, int opponentPointsScored) {

        inventoryItems = new LinkedList<>();
        numberOfTurns = 0;

    }

}

