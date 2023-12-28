import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

/**
 * Contains the logic for the player's behavior,
 * and methods for manipulating its graphical representation.
 */
public class Player implements Sprite {
    private final Polygon shape;

    /**
     * Initializes player fields and sets the position of the player.
     */
    public Player() {
        shape = createPlayerTriangle();
        resetPosition();
    }

    /**
     * Creates the graphical representation of the Player.
     * @return A triangle that represents the player.
     */
    private Polygon createPlayerTriangle() {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(0.0, 45.0,
                30.0, 45.0,
                15.0, 0.0);
        triangle.setFill(Color.BLUE);

        return triangle;
    }

    /**
     * Sets the player's position to the bottom middle of the window.
     */
    public void resetPosition() {
        shape.setLayoutX(385);
        shape.setLayoutY(525);
    }

    /**
     * Handles player movement, and ensure that the player can't move
     * outside the coundaries of the window.
     * @param xIncrement Double value representing the number of pixels that the player will move side to side
     * @param yIncrement Double value representing the number of pixels that the player will move up and down
     */
    public void move(double xIncrement, double yIncrement) {

        double newX = shape.getLayoutX() + xIncrement;
        double newY = shape.getLayoutY() + yIncrement;

        if (newX < 0) {
            newX = 0;
        }
        if (newX > Main.SCREEN_WIDTH - shape.getBoundsInLocal().getWidth()) {
            newX = Main.SCREEN_WIDTH - shape.getBoundsInLocal().getWidth();
        }
        shape.setLayoutX(newX);
        shape.setLayoutY(newY);
    }

    /**
     * Checks if the player is colliding  with enemy missiles or with enemies.
     * @param other An instance of Sprite that is not the player
     * @return boolean value that indicates whether a collision has occurred or not.
     */
    @Override
    public boolean collidesWith(Sprite other) {
        if (other instanceof Enemy || other instanceof Missile) {
            return shape.getBoundsInParent().intersects(other.getShape().getBoundsInParent());
        }
        return false;
    }

    /**
     * Allows the player to shoot missiles towards the enemies.
     * @return Missile that travels directly upwards
     */
    public Missile shoot() {
        Missile missile = new Missile(shape.getLayoutX() + shape.getBoundsInLocal().getWidth() / 2,
                shape.getLayoutY(), this);
        missile.setDirection(0, -1);
        return missile;
    }

    /**
     * Obtains the player's shape
     * @return Triangle representing the player's shape
     */
    public Polygon getShape() {
        return shape;
    }

    /**
     * Gets the player's horizontal position
     * @return Double value indicating the player's x position
     */
    public double getXPosition() {
        return shape.getLayoutX();
    }

    /**
     * Gets the player's vertical position
     * @return Double value indicating the player's y position
     */
    public double getYPosition() {
        return shape.getLayoutY();
    }
}
