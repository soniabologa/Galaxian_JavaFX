import javafx.scene.Node;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains the logic for all game mechanics, including the interaction between
 * the player and the enemies.
 */
public class GameManager {
    private final Player player;
    private final List<List<Enemy>> enemies;
    private final List<Enemy> divingEnemies = new ArrayList<>();
    private final Color[] ENEMY_COLORS = {Color.YELLOW, Color.RED, Color.PURPLE,
            Color.TURQUOISE, Color.TURQUOISE, Color.TURQUOISE};
    private final List<Missile> missiles;
    private final List<Node> nodesToRemove = new ArrayList<>();
    private static final double DIVE_PROBABILITY = 0.001;
    private int score;
    private int lives;
    private int fleetsDestroyed;
    private final GameOverScreen gameOverScreen;

    /**
     * Initializes Game Manager fields
     */
    public GameManager() {
        player = new Player();
        enemies = new ArrayList<>();
        missiles = new ArrayList<>();
        score = 0;
        lives = 3;
        fleetsDestroyed = 0;
        gameOverScreen = new GameOverScreen(0);
        Main.root.getChildren().add(gameOverScreen);

       createEnemyFleet();
    }

    /**
     * Creates a full fleet of enemies in their initial positions.
     */
    public void createEnemyFleet() {
        for(int row = 0; row < 6; row++) {
            List<Enemy> enemyRow = new ArrayList<>();

            int numEnemies;
            int startCol;
            switch (row) {
                case 0 -> {
                    for (int col = 0; col < 10; col++) {
                        if (col == 3 || col == 6) {
                            Enemy enemy = new Enemy(ENEMY_COLORS[row], this, row, col);
                            enemy.resetPosition(col, row);
                            enemyRow.add(enemy);
                        }
                    }
                    enemies.add(enemyRow);
                    continue;
                }
                case 1 -> {
                    numEnemies = 6;
                    startCol = 2;
                }
                case 2 -> {
                    numEnemies = 8;
                    startCol = 1;
                }
                case 3, 4, 5 -> {
                    numEnemies = 10;
                    startCol = 0;
                }
                default -> {
                    numEnemies = 0;
                    startCol = 0;
                }
            }

            for (int col = 0; col < numEnemies; col++) {
                Enemy enemy = new Enemy(ENEMY_COLORS[row], this, row, startCol + col);
                enemy.resetPosition(startCol + col, row);
                enemyRow.add(enemy);
            }
            enemies.add(enemyRow);
        }
    }

    /**
     * Gets the horizontal position of the player.
     * @return double value indicating the player's x position
     */
    public double getPlayerXPosition() {
        return player.getXPosition();
    }

    /**
     * Gets the vertical position of the player.
     * @return double value indicating the player's y position
     */
    public double getPlayerYPosition() {
        return player.getYPosition();
    }

    /**
     * Updates the game logic to reflect collisions, diving enemies, newly spawned
     * enemies, and missiles shot by the player and by the enemies. Removes
     * game objects when necessary.
     */
    public void update() {

        for (List<Enemy> enemyRow : enemies) {
            for (Enemy enemy : enemyRow) {
                enemy.movePattern();
            }
        }

        List<Missile> missilesToRemove = new ArrayList<>();
        List<Enemy> enemiesToRemove = new ArrayList<>();
        List<Enemy> diversToRemove = new ArrayList<>();

        for (Missile missile : missiles) {
            missile.move(missile.getDirectionX() * missile.getSpeed(), missile.getDirectionY() * missile.getSpeed());
            for (List<Enemy> enemyRow : enemies) {
                for (Enemy enemy : enemyRow) {
                    if (missile.collidesWith(enemy) && missile.getShooter() instanceof Player) {
                        nodesToRemove.add(missile.getShape());
                        nodesToRemove.add(enemy.getShape());
                        score += 10;
                        missilesToRemove.add(missile);
                        enemiesToRemove.add(enemy);
                    }
                }
            }

            for (Enemy diver : divingEnemies) {
                if (missile.collidesWith(diver) && missile.getShooter() instanceof Player) {
                    nodesToRemove.add(missile.getShape());
                    nodesToRemove.add(diver.getShape());
                    score += 20;
                    missilesToRemove.add(missile);
                    diversToRemove.add(diver);
                } else if (diver.getShape().getLayoutY() > Main.SCREEN_HEIGHT) {
                    diversToRemove.add(diver);
                    nodesToRemove.add(diver.getShape());
                }
            }

            if (missile.collidesWith(player) && missile.getShooter() instanceof Enemy) {
                nodesToRemove.add(missile.getShape());
                missilesToRemove.add(missile);
                lives--;
                if(lives <= 0) {
                    gameOverScreen.showGameOverScreen(score);
                }
            }
        }

        for (Enemy diver : divingEnemies) {
            if(player.collidesWith(diver)) {
                nodesToRemove.add(diver.getShape());
                diversToRemove.add(diver);
                lives--;
                if (lives <= 0) {
                    gameOverScreen.showGameOverScreen(score);
                }
            }
            else if (diver.getShape().getLayoutY() > Main.SCREEN_HEIGHT) {
                diversToRemove.add(diver);
                nodesToRemove.add(diver.getShape());
            }
        }

        for (Enemy enemy : enemiesToRemove) {
            if(Math.random() < 0.1) {
                createNewEnemyAtTop(enemy);
            }
        }

        divingEnemies.removeAll(diversToRemove);
        for (List<Enemy> enemyRow : enemies) {
            enemyRow.removeAll(diversToRemove);
        }

        missiles.removeAll(missilesToRemove);
        for (List<Enemy> enemyRow : enemies) {
            enemyRow.removeAll(enemiesToRemove);
        }

        missiles.removeIf(missile -> nodesToRemove.contains(missile.getShape()));
        for (List<Enemy> enemyRow : enemies) {
            enemyRow.removeIf(enemy -> nodesToRemove.contains(enemy.getShape()));
        }

        enemiesToRemove.clear();
        checkAndSpawnNewFleet();
    }

    /**
     * Chooses when diving enemies shoot towards the player. Uses a
     * random number generator and a threshold to make the decision.
     */
    public void enemyShootMissile() {
        for (Enemy enemy : divingEnemies) {
            if (Math.random() < 0.0003) { //
                Missile missile = enemy.shootTowardsPlayer();
                if (missile != null) { //
                    missiles.add(missile);
                    Main.root.getChildren().add(missile.getShape());
                }
            }
        }
    }

    /**
     * Loops over the list of enemies in the formation, and
     * determines which ones are eligible to dive. Only enemies
     * on the perimeter of the formation are eligible to dive.
     * @return List of enemies eligible to dive.
     */
    private List<Enemy> getDivingCandidates() {
        List<Enemy> candidates = new ArrayList<>();

        for (List<Enemy> enemyRow : enemies) {
            if (!enemyRow.isEmpty()) {
                candidates.add(enemyRow.get(0));

                if (enemyRow.size() > 1) {
                    candidates.add(enemyRow.get(enemyRow.size() - 1));
                }
            }
        }
        return candidates;
    }

    /**
     * Chooses when an enemy leaves the formation and dives towards the player.
     */
    public void maybeTriggerDive() {
        if (Math.random() < DIVE_PROBABILITY) {
            List<Enemy> candidates = getDivingCandidates();
            if (!candidates.isEmpty()) {
                Enemy diver = candidates.get(new Random().nextInt(candidates.size()));
                diver.dive();
                divingEnemies.add(diver);
            }
        }
    }

    /**
     * Removes nodes from the GUI to reflect the game's inner state.
     */
    public void render() {
        for (Node node : nodesToRemove) {
            Main.root.getChildren().remove(node);
        }
        nodesToRemove.clear();
    }

    /**
     * Puts together a list of all graphical game elements that will be added to the root pane.
     * @return List of nodes to be added to the GUI
     */
    public List<Node> getGameObjects() {
        List<Node> gameObjects = new ArrayList<>();

        gameObjects.add(player.getShape());
        for (List<Enemy> enemyRow : enemies) {
            for (Enemy enemy : enemyRow) {
                gameObjects.add(enemy.getShape());
            }
        }

        for (Enemy diver : divingEnemies) {
            gameObjects.add(diver.getShape());
        }

        for (Missile missile : missiles) {
            gameObjects.add(missile.getShape());
        }
        return gameObjects;
    }

    /**
     * Gets the score of the game.
     * @return Integer representing the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the number of lives remaining.
     * @return Integer representing the number of lives remaining.
     */
    public int getLives() {
        return lives;
    }

    public int getFleetsDestroyed() {
        return fleetsDestroyed;
    }

    /**
     * Gets the current instance of the player.
     * @return Player object
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Gets the list of all missiles active.
     * @return List of missiles in play
     */
    public List<Missile> getMissiles() {
        return missiles;
    }

    /**
     * Spawns a new enemy in line with an enemy that left the formation.
     * @param oldEnemy The enemy whose position in the array the new enemy is replacing
     */
    private void createNewEnemyAtTop(Enemy oldEnemy) {
        int column = oldEnemy.getEnemyColumn();
        int row = oldEnemy.getEnemyRow();
        Color color = oldEnemy.getColor();
        Enemy newEnemy = new Enemy(color, this, row, column);
        newEnemy.setSpawnPosition(oldEnemy.getXPosition());
        newEnemy.startMovingDown(row);

        List<Enemy> firstRow = enemies.get(0);
        firstRow.add(newEnemy);
        Main.root.getChildren().add(newEnemy.getShape());
    }

    /**
     * Spawns a full new fleet of enemies if no enemies
     * remain on the screen.
     */
    public void checkAndSpawnNewFleet() {
        boolean allEnemiesRemoved = true;

        for (List<Enemy> enemyRow : enemies) {
            if (!enemyRow.isEmpty()) {
                allEnemiesRemoved = false;
                break;
            }
        }

        if (allEnemiesRemoved) {
            fleetsDestroyed++;
            createEnemyFleet();
            for (List<Enemy> enemyRow : enemies) {
                for (Enemy enemy : enemyRow) {
                    Main.root.getChildren().add(enemy.getShape());
                }
            }
        }
    }
}


