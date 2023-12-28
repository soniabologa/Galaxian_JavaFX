import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Displays the Game Over screen.
 */
public class GameOverScreen extends VBox {
    private final Text gameOverText;
    private final Text scoreText;
    private final Button exitButton;
    //Main main;

    /**
     * Initializes text fields and an exit button, and adds them to the pane
     * @param score Integer value that holds the player's score at the end of the game
     */
    public GameOverScreen(int score) {

        gameOverText = new Text("GAME OVER");
        gameOverText.setFont(new Font(40));

        scoreText = new Text("Score: " + score);
        scoreText.setFont(new Font(20));

        exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            System.exit(0);
        });

        StackPane.setAlignment(gameOverText, Pos.CENTER);
        StackPane.setAlignment(scoreText, Pos.CENTER);
        StackPane.setAlignment(exitButton, Pos.CENTER);

        setAlignment(Pos.CENTER);
        setSpacing(20);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        setPrefWidth(Main.SCREEN_WIDTH);
        setPrefHeight(Main.SCREEN_HEIGHT);
        setVisible(false);

        getChildren().addAll(gameOverText, scoreText, exitButton);
    }

    /**
     * Shows the game over screen.
     * @param score Integer value that is the player's score at the end of the game
     */
    public void showGameOverScreen(int score) {
        scoreText.setText("Score: " + score);
        setVisible(true);
        Main.root.getChildren().removeIf(node -> node != this);
    }
}
