import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * Contains functions for the logic, behavior, and graphical representation
 * of missiles.
 */
public class Missile implements Sprite {

    private final Rectangle shape;
    private double speed;
    private double directionX;
    private double directionY;
    private final Sprite shooter;

    /**
     * Initializes Missie variable
     * @param startX Double value that indicates the horizontal starting position
     * @param startY Double value that indicates the vertical starting position
     * @param shooter Sprite that the missile belongs to
     */
    public Missile(double startX, double startY, Sprite shooter) {
        shape = new Rectangle(5, 15, Color.RED);
        speed = 0.05;
        shape.setLayoutX(startX - shape.getWidth() / 2);
        shape.setLayoutY(startY - shape.getHeight());
        this.shooter = shooter;
    }

    /**
     * Implements the move method from the interface
     * @param xIncrement Double value representing the number of pixels that the missile will move side to side
     * @param yIncrement Double value representing the number of pixels that the missile will move up and down
     */
    @Override
    public void move(double xIncrement, double yIncrement) {
        shape.setLayoutX(shape.getLayoutX() + xIncrement);
        shape.setLayoutY(shape.getLayoutY() + yIncrement);
    }

    /**
     * Gets the speed of the missile
     * @return Double value indicating the speed of the missile
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Gets the object that the missile originated from
     * @return Sprite indicating the object that shot the missile
     */
    public Sprite getShooter() {
        return shooter;
    }

    /**
     * Gets the x direction of a missile
     * @return Double value indicating horizontal travel direction
     */
    public double getDirectionX() {
        return directionX;
    }

    /**
     * Gets the y direction of a missile
     * @return Double value indicating vertical travel direction
     */
    public double getDirectionY() {
        return directionY;
    }

    /**
     * Sets the speed of the missile
     * @param newSpeed Double value indicating the nre speed of the missile
     */
    public void setSpeed(double newSpeed) {
        this.speed = newSpeed;
    }

    /**
     * Sets the direction of a missile
     * @param xDirection Double value that tells the missile if it is travelling right or left
     * @param yDirection Double value that tells the missile if it is travelling up or down
     */
    public void setDirection(double xDirection, double yDirection) {
        this.directionX = xDirection;
        this.directionY = yDirection;
    }

    /**
     * Checks whether the missile has hit an enemy or the player
     * @param other Instance of sprite
     * @return boolen value true or false that tells us whether a collision has occurred or not
     */
    @Override
    public boolean collidesWith(Sprite other) {
        if (other instanceof Missile) {
            return false;
        }
        return shape.getBoundsInParent().intersects(other.getShape().getBoundsInParent());
    }

    /**
     * Gets the GUI representation of the missile
     * @return Shape that represents the missile
     */
    public Rectangle getShape() {
        return shape;
    }
}
