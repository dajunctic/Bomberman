package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.others.Basic;
import uet.oop.bomberman.others.Physics;

import static uet.oop.bomberman.game.Gameplay.*;

/** Everything that moves */
public class Mobile extends Entity{
    protected double speed;
    protected Vertex direction;
    // inheritance
    public Mobile(double xPixel, double yPixel) {
        super(xPixel, yPixel);
    }

    public Mobile(double xUnit, double yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }

    //extension
    public boolean checkCollision(double ref_x, double ref_y, int margin) {
        /* * Kiểm tra border map */
        if(ref_x < 0 || ref_y < 0
                || ref_x > width * Sprite.SCALED_SIZE - this.getWidth()
                || ref_y > height * Sprite.SCALED_SIZE - this.getHeight()) return true;

        Rectangle rect;
        if(mode == CENTER_MODE)
            rect = new Rectangle(ref_x - this.getWidth() / 2 + margin, ref_y - this.getHeight() / 2 + margin, this.getWidth() - margin, this.getHeight() - margin);
        else
            rect = this.getRect(ref_x, ref_y, this.getWidth(), this.getHeight());

        /* * Kiểm tra tất cả các tiles xung quanh thực thể. */

        int tileStartX = (int) Math.max(0, Math.floor(rect.getX() / Sprite.SCALED_SIZE));
        int tileStartY = (int) Math.max(0, Math.floor(rect.getY() / Sprite.SCALED_SIZE));
        int tileEndX = (int) Math.ceil((rect.getX() + rect.getWidth()) / Sprite.SCALED_SIZE);
        int tileEndY = (int) Math.ceil((rect.getY() + rect.getHeight()) / Sprite.SCALED_SIZE);
        tileEndX = Math.min(tileEndX, Gameplay.width - 1);
        tileEndY = Math.min(tileEndY, Gameplay.height - 1);
        for (int i = tileStartX; i <= tileEndX; i++) {
            for (int j = tileStartY; j <= tileEndY; j++) {

                int tileX = i * Sprite.SCALED_SIZE;
                int tileY = j * Sprite.SCALED_SIZE;

                Rectangle tileRect = new Rectangle(tileX, tileY, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

                /* * Kiểm tra tile có phải kiểu WALL hoặc BRICK không! */

                if (Gameplay.get(tile_map[j][i], i, j) == GameMap.WALL
                        || Gameplay.get(tile_map[j][i], i, j) == GameMap.BRICK) {

                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        return true;
                    }
                }

            }
        }

        return false;
    }

    public void move() {
        double ref_x = Math.max(0,Math.min(width*Sprite.SCALED_SIZE - this.getWidth(),x  +  speed * direction.getX()));
        double ref_y = Math.max(0,Math.min(height*Sprite.SCALED_SIZE - this.getHeight(),y  +  speed * direction.getY()));
        if(!checkCollision(ref_x,ref_y,5)) {
            x = ref_x;
            y = ref_y;
        }
    }

    public Rectangle getRect(double x, double y, double w, double h) {
        return new Rectangle(x, y, w, h);
    }


}
