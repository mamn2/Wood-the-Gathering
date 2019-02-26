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

    private int boardSize;
    private LinkedList<InventoryItem> inventoryItems;
    private int maxInventoryItems;
    private boolean collectingSeeds;
    private boolean plantingSeeds;
    private boolean collectingTrees;
    private boolean goingHome;
    private boolean noTrees = false;
    int numPlants = 0;

    public IntelligentStrategy() {

        this.inventoryItems = new LinkedList<>();
        this.collectingSeeds = true;

    }

    @Override
    public void initialize(int boardSize, int maxInventorySize, int winningScore, Point startTileLocation,
                           boolean isRedPlayer, Random random) {

        this.boardSize = boardSize;
        this.maxInventoryItems = maxInventorySize;

    }

    @Override
    public TurnAction getTurnAction(PlayerBoardView boardView, boolean isRedTurn) {

        if (goingHome) {
            if (boardView.getTileTypeAtLocation(boardView.getYourLocation()) == TileType.START) {
                LinkedList<InventoryItem> removeItems = new LinkedList<>();
                for (InventoryItem item : inventoryItems) {
                    if (item instanceof WoodItem) {
                        removeItems.add(item);
                    }
                }
                inventoryItems.removeAll(removeItems);
                if (numPlants < 3) {
                    goingHome = false;
                    collectingSeeds = true;
                    plantingSeeds = false;
                    collectingTrees = false;
                } else {
                    goingHome = false;
                    collectingTrees = true;
                    collectingSeeds = false;
                    plantingSeeds = false;
                }
                return getTurnAction(boardView, isRedTurn);
            } else {
                //System.out.print("moving home");
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
                //System.out.print("moving to seed ");
                return moveInDirectionOfTileType(boardView, TileType.SEED);
            }
        } else if (plantingSeeds) {
            boolean inventoryHasSeeds = false;
            for (InventoryItem item : inventoryItems) {
                if (item instanceof SeedItem) {
                    inventoryHasSeeds = true;
                }
            }

            if (inventoryHasSeeds) {
                //System.out.print("moving to empty tile ");
                return moveInDirectionOfTileType(boardView, TileType.EMPTY);
            } else {
                if (inventoryItems.size() == maxInventoryItems) {
                    plantingSeeds = false;
                    collectingTrees = false;
                    collectingSeeds = false;
                    goingHome = true;
                } else if (numPlants > 16) {
                    plantingSeeds = false;
                    collectingTrees = true;
                    collectingSeeds = false;
                    goingHome = false;
                } else {
                    collectingSeeds = true;
                    plantingSeeds = false;
                    collectingTrees = false;
                    goingHome = false;
                }
                return getTurnAction(boardView, isRedTurn);
            }
        } else if (collectingTrees) {
            if (inventoryItems.size() == maxInventoryItems) {
                collectingTrees = false;
                goingHome = true;
                collectingSeeds = false;
                plantingSeeds = false;
                return getTurnAction(boardView, isRedTurn);
            } else {
                //System.out.print("moving to tree ");
                if (noTrees) {
                    collectingSeeds = true;
                    plantingSeeds = false;
                    goingHome = false;
                    collectingTrees = false;
                    return getTurnAction(boardView, isRedTurn);
                } else {
                    return moveInDirectionOfTileType(boardView, TileType.TREE);
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
            //System.out.println("OEJFOAHFILA HGILUSAHFLIUAEHFILHEAIUFHAEILHUFLIAEUHFLIAUEHFILUEHFLIAHELF");
            if (tileType == TileType.TREE) {
                goingHome = true;
                collectingTrees = false;
                collectingSeeds = false;
                plantingSeeds = false;
                noTrees = true;
                return getTurnAction(boardView, true);
            } else if (tileType == TileType.SEED) {
                plantingSeeds = true;
                collectingSeeds = false;
                collectingTrees = false;
                goingHome = false;
                return getTurnAction(boardView, true);
            } else {
                return null;
            }
        } else if (nearestTile.getX() < boardView.getYourLocation().getX()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x - 1, boardView.getYourLocation().y);
            //System.out.println("left");
            return TurnAction.MOVE_LEFT;
        } else if (nearestTile.getX() > boardView.getYourLocation().getX()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x + 1, boardView.getYourLocation().y);
            //System.out.println("right");
            return TurnAction.MOVE_RIGHT;
        } else if (nearestTile.getY() < boardView.getYourLocation().getY()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x, boardView.getYourLocation().y - 1);
            //System.out.println("Down");
            return TurnAction.MOVE_DOWN;
        } else if (nearestTile.getY() > boardView.getYourLocation().getY()) {
            boardView.getYourLocation().setLocation(boardView.getYourLocation().x, boardView.getYourLocation().y + 1);
            //System.out.println("up");
            return TurnAction.MOVE_UP;
        } else if (tileType == TileType.EMPTY
                && boardView.getTileTypeAtLocation(boardView.getYourLocation()) == TileType.EMPTY) {
            //System.out.println("planting seed");
            for (InventoryItem item : inventoryItems) {
                if (item instanceof SeedItem) {
                    inventoryItems.remove(item);
                    break;
                }
            }
            numPlants++;
            noTrees = false;
            return TurnAction.PLANT_SEED;
        } else if (tileType == TileType.TREE
               && boardView.getTileTypeAtLocation(boardView.getYourLocation()) == TileType.TREE) {
            //System.out.println("cutting down");
            numPlants--;
            return TurnAction.CUT_TREE;
        } else if (tileType == TileType.SEED
               && boardView.getTileTypeAtLocation(boardView.getYourLocation()) == TileType.SEED) {
            //System.out.println("picking up");
            inventoryItems.add(new SeedItem(0));
            return TurnAction.PICK_UP;
        }

        //System.out.println("no action");
        return null;

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
