package ss.gamedesign;

import java.util.HashMap;
import java.util.Map;

public abstract class Player {

    /**
     * Player name.
     */
    private String name;

    /**
     * The players points.
     */
    private int points;

    /**
     * The number of balls the player removed from the board.
     */
    private int numberOfBalls;

    /**
     * A map containing the types of balls as keys and the
     * number of balls of every colour the player removed
     * from the board stored as values to their respective colour.
     */
    private Map<Colour, Integer> playerBalls;

    private int blueCount = 0;
    private int redCount = 0;
    private int greenCount = 0;
    private int yellowCount = 0;
    private int purpleCount = 0;
    private int orangeCount = 0;


    /**
     * Creates a new Player object.
     * @requires name != null;
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        points = 0;
        numberOfBalls = 0;
        playerBalls = new HashMap<>();
        fillPlayerBalls();
    }

    public void increasePlayerBalls(Colour colour) {
        switch (colour) {
            case BLUE:
                blueCount++;
                break;

            case RED:
                redCount++;
                break;

            case GREEN:
                greenCount++;
                break;

            case YELLOW:
                yellowCount++;
                break;

            case ORANGE:
                orangeCount++;
                break;

            case PURPLE:
                purpleCount++;
                break;

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints() {
        for (Colour colour : playerBalls.keySet()) {
            points = points + playerBalls.get(colour) / 3;
        }
    }

    public int getNumberOfBalls() {
        return numberOfBalls;
    }

    public void setNumberOfBalls() {
        for (Colour colour : playerBalls.keySet()) {
            numberOfBalls = numberOfBalls + playerBalls.get(colour);
        }
    }

    public Map<Colour, Integer> getPlayerBalls() {
        return playerBalls;
    }

    public void setPlayerBalls(Map<Colour, Integer> playerBalls) {
        this.playerBalls = playerBalls;
    }

    public void fillPlayerBalls() {
        playerBalls.put(Colour.BLUE, blueCount);
        playerBalls.put(Colour.RED, redCount);
        playerBalls.put(Colour.GREEN, greenCount);
        playerBalls.put(Colour.ORANGE, orangeCount);
        playerBalls.put(Colour.YELLOW, yellowCount);
        playerBalls.put(Colour.PURPLE, purpleCount);
    }
}
