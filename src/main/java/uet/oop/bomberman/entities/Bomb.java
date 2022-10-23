package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.others.Physics;

public class Bomb extends Entity{

    //animation
    DeadAnim explosion = new DeadAnim(SpriteSheet.explosion, 6, 1);
    public DeadAnim bomb = new DeadAnim(SpriteSheet.bomb, 12, 2.5);
    boolean exploded = false;
    boolean friendly = false;
    //tí sửa sau :)))
    private int radius = 3;
    private int damage = 4;
    public void relocate() {

    }
    public Bomb(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        mode = CENTER_MODE;
    }
    public Bomb(double xPixel, double yPixel, double timer) {
        super(xPixel, yPixel);
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
        x += (double) Sprite.SCALED_SIZE / 2;
        y += (double) Sprite.SCALED_SIZE / 2;
        bomb = new DeadAnim(SpriteSheet.bomb, 15, timer);
        mode = CENTER_MODE;
        explosion.setScaleFactor(2);
    }

    public Bomb(double xPixel, double yPixel, double timer, int radius, int damage, boolean friendly) {
        super(xPixel, yPixel);
        x *= Sprite.SCALED_SIZE;
        y *= Sprite.SCALED_SIZE;
        x += (double) Sprite.SCALED_SIZE / 2;
        y += (double) Sprite.SCALED_SIZE / 2;
        bomb = new DeadAnim(SpriteSheet.bomb, 15, timer);
        mode = CENTER_MODE;
        explosion.setScaleFactor(2);
        this.radius = radius;
        this.damage = damage;
        this.friendly = friendly;
    }
    @Override
    public void update() {
        if (bomb.isDead()) {
            explosion.update();
        } else {
            bomb.update();
        }
    }


    public Image getImg() {
        if (bomb.isDead()) {
            return explosion.getImage();
        } else {
            return bomb.getImage();
        }
    }
    @Override
    public boolean isExisted() {
        return !explosion.isDead();
    }

    @Override
    public void deadAct(Gameplay gameplay) {
        if(exploded) return;
        exploded = true;
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, 1,0, damage, friendly));
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, 0, 1, damage, friendly));
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, 0,-1, damage, friendly));
        gameplay.generate(new Flame(x, y, radius * Sprite.SCALED_SIZE, -1,0, damage, friendly));


        /* ****** Attack Mobile Entity ********** */
        for (Mobile mobile : Mobile.mobiles) {
            Rectangle a = new Rectangle(x - getWidth() / 2, y - getHeight() / 2, getWidth() ,getHeight());
            Rectangle b = mobile.getRectCollision();

            if (Physics.collisionRectToRect(a, b)) {
                mobile.subtractHP(Mobile.EXPLOSION_SUBTRACT_HP);
            }
        }
    }

    @Override
    public double getWidth() {
        if (bomb.isDead())
            return explosion.getImage().getWidth();
        return bomb.getImage().getWidth();
    }

    @Override
    public double getHeight() {
        if (bomb.isDead())
            return explosion.getImage().getHeight();
        return bomb.getImage().getHeight();
    }
}
