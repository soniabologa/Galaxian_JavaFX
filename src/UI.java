import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

/**
 * Hold elements of the user interface.
 */
public class UI {
    private final Text scoreText;
    private final Text livesText;
    private final Text fleetsDestroyedText;

    /**
     * Constructor for UI elements; Adds text to display the score, number of
     * lives remaining, and the number of fleets destroyed.
     */
    public UI() {
        fleetsDestroyedText = new Text("Fleets Destroyed: 0");
        fleetsDestroyedText.setFont(new Font(15));
        fleetsDestroyedText.setLayoutX(10);
        fleetsDestroyedText.setLayoutY(70);

        scoreText = new Text("Score: 0");
        scoreText.setFont(new Font(15));
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(20);

        livesText = new Text("Lives: 3");
        livesText.setFont(new Font(15));
        livesText.setLayoutX(10);
        livesText.setLayoutY(45);
    }

    /**
     * Updates the score.
     * @param score Integer that holds the player's score in the game.
     */
    public void updateScore(int score) {
        scoreText.setText("Score: " + score);
    }

    /**
     * Updates the number of lives remaining,
     * @param lives Integer that holds the number of lives remaining.
     */
    public void updateLives(int lives) {
        livesText.setText("Lives: " + lives);
    }

    /**
     * Updates the number of fleets destroyed.
     * @param fleetsDestroyed Integer that holds the number of fleets destroyed.
     */
    public void updateFleetsDestroyed(int fleetsDestroyed) {
        fleetsDestroyedText.setText("Fleets Destroyed: " + fleetsDestroyed);
    }

    /**
     * Gets the text nodes.
     * @return List of Nodes of UI elements
     */
    public List<Node> getUINodes() {
        List<Node> uiElements = new ArrayList<>();
        uiElements.add(fleetsDestroyedText);
        uiElements.add(scoreText);
        uiElements.add(livesText);
        return uiElements;
    }
}
