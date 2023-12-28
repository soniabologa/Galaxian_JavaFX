import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;

/**
 * This class launches the application, and contains the start function and the animation timer.
 */
public class Main extends Application {
    private GameManager gameManager;
    private UI ui;
    private final Set<KeyCode> activeKeys = new HashSet<>();
    public static double SCREEN_WIDTH = 800;
    public static double SCREEN_HEIGHT = 600;
    public static Pane root;

    /**
     * Launches the application
     * @param args Application arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates the Pane that holds the graphical elements, and sets abd shows the stage.
     * This functions also contains the keyboard listeners which control the
     * player's movement.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        gameManager = new GameManager();
        ui = new UI();

        scene.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            activeKeys.add(key);

            if (activeKeys.contains(KeyCode.SPACE)) {
                Missile missile = gameManager.getPlayer().shoot();
                missile.setDirection(0, -1);
                missile.setSpeed(0.5);
                gameManager.getMissiles().add(missile);
                root.getChildren().add(missile.getShape());
            }
        });

        scene.setOnKeyReleased(e -> {
            KeyCode key = e.getCode();
            activeKeys.remove(key);
        });


        root.getChildren().addAll(gameManager.getGameObjects());
        root.getChildren().addAll(ui.getUINodes());

        primaryStage.setTitle("Galaxian");
        primaryStage.setScene(scene);
        primaryStage.show();

        startGameLoop();
    }

    /**
     * Starts the game loop. Contains an animation timer, and implements the
     * Animation timer's handle function.
     */
    public void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameManager.update();

                gameManager.maybeTriggerDive();

                gameManager.enemyShootMissile();

                gameManager.render();

                if (activeKeys.contains(KeyCode.RIGHT)) {
                    gameManager.getPlayer().move(0.25, 0);
                }
                if (activeKeys.contains(KeyCode.LEFT)) {
                    gameManager.getPlayer().move(-0.25, 0);
                }

                ui.updateScore(gameManager.getScore());
                ui.updateLives(gameManager.getLives());
                ui.updateFleetsDestroyed(gameManager.getFleetsDestroyed());
            }
        };
        gameLoop.start();
    }
}