import javafx.scene.Node;

/**
 * Interface for all sprites. Defines methods for moving a sprite,
 * checking collisions, and obtaining the graphical representation
 * of a Sprite.
 */
public interface Sprite {

    public void move(double xIncrement, double yIncrement);

    public boolean collidesWith(Sprite other);

    public Node getShape();
}
