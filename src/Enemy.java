import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * Contains methods for Enemy movement, behavior, and graphics.
 */
public class Enemy implements Sprite {
    private final Rectangle shape;
    private final double speed;
    private boolean diving;
    private boolean movingDown;
    static int direction = 1;
    private final int row;
    private final int column;

    private final GameManager gameManager;

    /**
     * Initializes Enemy fields
     * @param color Color of the enemy in the GUI
     * @param gameManager Instance of game manager that the enemy is associated with
     * @param row Row that the enemy occupies in the formation
     * @param column Column that the enemy occupies in the formation
     */
    public Enemy(Color color, GameManager gameManager, int row, int column) {
        this.gameManager = gameManager;
        this.row = row;
        this.column = column;
        this.shape = new Rectangle(30, 30, color);
        this.speed = 0.2;
        this.diving = false;
        this.movingDown = false;
    }

    /**
     * Implements the move method from the interface.
     * @param xIncrement Double value representing the number of pixels that the enemy will move side to side
     * @param yIncrement Double value representing the number of pixels that the enemy will move up and down
     */
    @Override
    public void move(double xIncrement, double yIncrement) {
        shape.setLayoutX(shape.getLayoutX() + xIncrement);
        shape.setLayoutY(shape.getLayoutY() + yIncrement);
    }

    /**
     * Gets the horizontal position of the node
     * @return Double representing the x position
     */
    public double getXPosition() {
        return shape.getLayoutX();
    }

    /**
     * Used when an enemy spawns and starts to move downward into a formation.
     * @param targetRow Integer, the row that the spawned enemy will stop in
     */
    public void startMovingDown(int targetRow) {
        this.movingDown = true;
    }

    /**
     * Gets the row that an enemy occupies in the formation
     * @return Integer value of an enemy's row
     */
    public int getEnemyRow() {
        return row;
    }

    /**
     * Gets the column that an enemy occupies in the formation.
     * @return Integer value of an enemy's column
     */
    public int getEnemyColumn(){
        return column;
    }

    /**
     * Defines the movement pattern for enemies in the formation, as well as diving
     * enemies, and for newly spawned enemies.
     */
    public void movePattern() {
        double distanceFromBottom = Main.SCREEN_HEIGHT - shape.getLayoutY() - shape.getHeight();
        if (movingDown) {
            shape.setLayoutY(shape.getLayoutY() + speed);
            if (shape.getLayoutY() >= row * (shape.getHeight() + 10) + 70) {
                movingDown = false;
            }
        }
        else if (diving) {
            if (distanceFromBottom > 200) {
                double targetX = gameManager.getPlayerXPosition();
                double targetY = gameManager.getPlayerYPosition();

                double dx = targetX - shape.getLayoutX();
                double dy = targetY - shape.getLayoutY();

                double magnitude = Math.sqrt(dx * dx + dy * dy);
                if (magnitude > 0) {
                    dx /= magnitude;
                    dy /= magnitude;
                }

                shape.setLayoutX(shape.getLayoutX() + dx * (speed/1.5));
                shape.setLayoutY(shape.getLayoutY() + dy * (speed/1.5));
            }
            else {
                shape.setLayoutY(shape.getLayoutY() + speed);
            }
        }
        else {
            shape.setLayoutX(shape.getLayoutX() + direction * speed/1.5);

            if (shape.getLayoutX() > Main.SCREEN_WIDTH - shape.getWidth()) {
                direction = -1;
            } else if (shape.getLayoutX() < 0) {
                direction = 1;
            }
        }
    }

    /**
     * Allows diving enemies to shoot missiles towards the player.
     * @return Missile that moves towards the player
     */
    public Missile shootTowardsPlayer() {
        double distanceFromBottom = Main.SCREEN_HEIGHT - shape.getLayoutY() - shape.getHeight();
        if (distanceFromBottom <= 200) {
            return null;
        }

        double targetX = gameManager.getPlayerXPosition();
        double targetY = gameManager.getPlayerYPosition();

        double dx = targetX - shape.getLayoutX();
        double dy = targetY - shape.getLayoutY();

        double missileStartX = shape.getLayoutX() + shape.getWidth() / 2;
        double missileStartY = shape.getLayoutY() + shape.getHeight();

        double magnitude = Math.sqrt(dx * dx + dy * dy);
        if (magnitude > 0) {
            dx /= magnitude;
            dy /= magnitude;
        }

        Missile missile = new Missile(missileStartX, missileStartY, this);
        missile.setDirection(dx, dy);
        missile.setSpeed(0.2);

        return missile;
    }

    /**
     * Gets the color of an enemy
     * @return Color of the enemy's GUI representation
     */
    public Color getColor() {
        return (Color) shape.getFill();
    }

    /**
     * Signals that an enemy has been chosen for diving.
     */
    public void dive() {
        diving = true;
    }

    /**
     * Checks if the enemy has collided with the player of with the player's missiles.
     * @param other Instance of Sprite
     * @return boolean indicating whether the enemy collided or not
     */
    @Override
    public boolean collidesWith(Sprite other) {
        if (other instanceof Enemy) {
            return false;
        }
        return shape.getBoundsInParent().intersects(other.getShape().getBoundsInParent());
    }

    /**
     * Gets the shape that represents the enemy.
     * @return Rectangle that is the GUI representation of the enemy
     */
    public Rectangle getShape() {
        return shape;
    }

    /**
     * Sets an enemy's starting position when a fleet spawns
     * @param col Integer value that is the enemy's row
     * @param row Integer value that is the enemy's column
     */
    public void resetPosition(int col, int row) {
        double spacing = 10;
        double startX = 50;
        double startY = 70;

        double posX = startX + col * (shape.getWidth() + spacing);
        double posY = startY + row * (shape.getHeight() + spacing);
        shape.setLayoutX(posX);
        shape.setLayoutY(posY);
    }

    /**
     * Sets an enemy's spawn position when a new enemy enters from the top.
     * @param xPosition Double value that represents the spawning enemy's horizontal position
     */
    public void setSpawnPosition(double xPosition) {
        shape.setLayoutX(xPosition);
        shape.setLayoutY(0);
    }
}
