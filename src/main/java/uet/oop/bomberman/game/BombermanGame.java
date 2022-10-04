package uet.oop.bomberman.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.others.Menu;
import uet.oop.bomberman.others.Text_Sc;

import java.io.IOException;
public class BombermanGame extends Application {

    /** 960 x 720 */
    public static final int WIDTH = 20;
    public static final int HEIGHT = 14;

    public static GraphicsContext gc;
    private Canvas canvas;
    public static Scene scene;
    public static Group root = new Group();
    public static HBox box = new HBox();
    private Gameplay level = new Gameplay();
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }
    //number of terrain styles
    public static int floor_styles = 2;
    public static int brick_styles = 1;
    public static int wall_styles = 1;

    private boolean exitGame=false;

    @Override
    public void start(Stage stage) throws IOException {
        load_tiles();

        stage.setTitle("Bomberman");
        //Canvas generates
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container

        this.root.getChildren().add(canvas);

        // Tao scene
        scene = new Scene(root);

        // Thêm scene vao stage
        stage.setScene(scene);
        stage.show();
        load_level();
        //Loads tiles properties
        level.update();

        //frame update
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
                if (Gameplay.state==String.valueOf(Menu.Menu_Switch.PLAY)) render();
                if(Gameplay.state==String.valueOf(Menu.Menu_Switch.QUIT)) {exitGame=true;}
                if(exitGame) {
                    System.exit(0);
                }
            }
        };
        timer.start();
       // System.out.println(timer);


    }

    /** updating */
    public void update() {
        level.update();
    }

    public void load_tiles() {
        Sprite.load_tiles(floor_styles, wall_styles);
        SpriteSheet.load_tiles(brick_styles);
    }

    public void load_level() throws IOException {
        //importing
        level.importing("src/main/resources/maps/sandbox_map.txt");
    }
    /** render objects */
    public void render() {
        gc.clearRect(0, 0,this.canvas.getWidth(), this.canvas.getHeight());
        level.render(gc);
        Menu.renderPlay(gc);
    }
}
