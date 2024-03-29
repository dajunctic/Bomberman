package uet.oop.bomberman.explosive;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.generals.Point;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.generals.Vertex;
import uet.oop.bomberman.graphics.DeadAnim;
import uet.oop.bomberman.graphics.Renderer;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.maps.AreaMap;
import uet.oop.bomberman.maps.GameMap;
import uet.oop.bomberman.music.Audio;
import uet.oop.bomberman.music.DynamicSound;
import uet.oop.bomberman.music.Sound;
import uet.oop.bomberman.others.Physics;

import java.util.HashSet;

import static uet.oop.bomberman.game.BombermanGame.FPS;
import static uet.oop.bomberman.game.Gameplay.*;

public class Flame extends Mobile {
    DeadAnim flame;
    double length;
    protected double duration = 2;
    protected double dir_x;
    protected double dir_y;
    private int damage = 3;

    /** Khi nào gặp vật cản như brick hoặc wall thì dừng luôn */
    boolean stop = false;
    boolean friendly = false;
    HashSet<Integer> floors = new HashSet<>();
    private ColorAdjust effect;
    private DynamicSound audio;
    private boolean special = false;
    public Flame(double xPixel, double yPixel) {
        super(xPixel, yPixel);
        setMode(CENTER_MODE);
    }

    public Flame(double _x, double _y, double length,double dirX,double dirY, int damage, boolean friendly) {
        super(_x,_y);

        //set speed
        speed = length / 10;
        this.length = length;

        // Animation
            if(dirX > 0.5) {
                flame = new DeadAnim(SpriteSheet.flame_right, 5, 0.5);
            }
            else if(dirX < 0.4 && dirX > -0.4) {
                if(dirY > 0) flame = new DeadAnim(SpriteSheet.flame_down,5,0.5);
                    else flame = new DeadAnim(SpriteSheet.flame_up, 5, 0.5);
            }
            else {
                flame = new DeadAnim(SpriteSheet.flame_left, 5, 0.5);
            }
        direction = new Vertex(dirX, dirY);
        this.damage = damage;
            //friendly fire
        this.friendly = friendly;
        setMode(CENTER_MODE);
        audio = new DynamicSound(x, y, Audio.copy(Audio.flame), 0.5, 5 * Sprite.SCALED_SIZE, this);
        sounds.add(new Sound(x, y, Audio.copy(Audio.fire), duration, length * 2));
    }

    public Flame(double _x, double _y, double length,double dirX,double dirY, double timer,double duration, int damage, boolean friendly) {
        super(_x,_y);
        //set speed
        speed = length / (timer * FPS);
        this.length = length;
        // Animation
        if(Math.abs(dirX) > Math.abs(dirY)) {
            if(dirX > 0) flame = new DeadAnim(SpriteSheet.flame_right, 5, timer);
                else flame = new DeadAnim(SpriteSheet.flame_left, 5, timer);
        }
        else {
            if(dirY > 0) flame = new DeadAnim(SpriteSheet.flame_down,5, timer);
            else flame = new DeadAnim(SpriteSheet.flame_up, 5, timer);
        }
        this.damage = damage;
        this.duration = duration;
        this.direction = new Vertex(dirX, dirY);
        //friendly fire
        this.friendly = friendly;
        setMode(CENTER_MODE);
        audio = new DynamicSound(x, y, Audio.copy(Audio.flame), timer, 5 * Sprite.SCALED_SIZE, this);
        sounds.add(new Sound(x + length * dirX / 2, y + length * dirY / 2, Audio.copy(Audio.fire), duration, length * 2));
    }

    public Flame(double _x, double _y, double length,double dirX,double dirY, double timer,double duration, int damage, boolean friendly, boolean special) {
        super(_x,_y);
        //set speed
        speed = length / (timer * FPS);
        this.length = length;
        // Animation
        if(Math.abs(dirX) > Math.abs(dirY)) {
            if(dirX > 0) flame = new DeadAnim(SpriteSheet.flame_right, 5, timer);
            else flame = new DeadAnim(SpriteSheet.flame_left, 5, timer);
        }
        else {
            if(dirY > 0) flame = new DeadAnim(SpriteSheet.flame_down,5, timer);
            else flame = new DeadAnim(SpriteSheet.flame_up, 5, timer);
        }
        this.damage = damage;
        this.duration = duration;
        this.direction = new Vertex(dirX, dirY);
        //friendly fire
        this.friendly = friendly;
        setMode(CENTER_MODE);
        audio = new DynamicSound(x, y, Audio.copy(Audio.flame), timer, 5 * Sprite.SCALED_SIZE, this);
        sounds.add(new Sound(x + length * dirX / 2, y + length * dirY / 2, Audio.copy(Audio.fire), duration, length * 2));
        this.special = special;
    }
    @Override
    public void update() {
        //moving
        double ref_x = Math.max(0,Math.min(width * Sprite.SCALED_SIZE - this.getWidth(),x  +  speed * direction.getX()));
        double ref_y = Math.max(0,Math.min(height * Sprite.SCALED_SIZE - this.getHeight(),y  +  speed * direction.getY()));
        if(!checkCollision(ref_x,ref_y,15) && !stop) {
            length -= speed;
            if(length <= 0) speed = 0;
            x = ref_x;
            y = ref_y;

        } else {
            flame.setDead();
            if(audio != null) {
                audio.stop();
            }

        }
        //animation and status update
        flame.update();
        if(flame.isDead()) {
            if(audio != null) {
                audio.stop();
            }
        }
        if(player != null && audio != null) {
            audio.update(player);
        }
    }

    @Override
    public Image getImg() {
        return flame.getImage();
    }

    @Override
    public boolean isExisted() {
        return !flame.isDead();
    }

    //re-apply effects
    @Override
    public void render(GraphicsContext gc, Gameplay gameplay) {
        gc.setEffect(effect);
        // Whether object is on screen
        if(!onScreen(gameplay)) return;
            gc.drawImage(this.getImg(), x - gameplay.translate_x + gameplay.offsetX + shiftX
                    , y - gameplay.translate_y + gameplay.offsetY + shiftY);
        gc.setEffect(null);
    }

    @Override
    public void render(GraphicsContext gc, Renderer renderer) {
        if(special) return ;
        gc.setEffect(effect);
        renderer.renderImg(gc, this.getImg(), x + shiftX, y + shiftY, false);
        gc.setEffect(null);
    }
    @Override
    public boolean checkCollision(double ref_x, double ref_y, int margin) {
        if(ref_x < 0 || ref_y < 0
                || ref_x > width * Sprite.SCALED_SIZE - this.getWidth()
                || ref_y > height * Sprite.SCALED_SIZE - this.getHeight()) return true;

        Rectangle rect;
        if(mode == CENTER_MODE)
            rect = new Rectangle(ref_x - this.getWidth() / 2 + margin, ref_y - this.getHeight() / 2 + margin, this.getWidth() - margin * 2, this.getHeight() - margin * 2);
        else
            rect = new Rectangle(ref_x, ref_y, this.getWidth(), this.getHeight());

        // Thay vì ngồi debug code Hưng fake thì tôi kiểm tra tất cả các tiles xung quanh thực thể luôn.
        int tileStartX = (int) Math.max(0, Math.floor(rect.getX() / Sprite.SCALED_SIZE));
        int tileStartY = (int) Math.max(0, Math.floor(rect.getY() / Sprite.SCALED_SIZE));

        if(!areaMaps.get(currentArea).checkInArea(tileStartX, tileStartY)) return true;
        int tileEndX = (int) Math.ceil((rect.getX() + rect.getWidth()) / Sprite.SCALED_SIZE);
        int tileEndY = (int) Math.ceil((rect.getY() + rect.getHeight()) / Sprite.SCALED_SIZE);
        tileEndX = Math.min(tileEndX, Gameplay.width - 1);
        tileEndY = Math.min(tileEndY, Gameplay.height - 1);
        for (int i = tileStartX; i <= tileEndX; i++) {
            for (int j = tileStartY; j <= tileEndY; j++) {

                int tileX = i * Sprite.SCALED_SIZE;
                int tileY = j * Sprite.SCALED_SIZE;

                Rectangle tileRect = new Rectangle(tileX, tileY, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

                // Kiểm tra tilemap[j][i] là loại gì O(1)
                if (Gameplay.get(tile_map[j][i], i, j) == GameMap.WALL)  {
                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        if(!special) return true;
                    }
                }
                else if(Gameplay.get(tile_map[j][i], i, j) == GameMap.BRICK) {
                    //Do something
                    if (Physics.collisionRectToRect(rect, tileRect)) {
                        killTask.add(new Point(i,j));
                        if(!special) return true;
                    }
                }
                else if(Gameplay.get(tile_map[j][i], i, j) == GameMap.FLOOR) {
                    if (Physics.collisionRectToRect(rect, tileRect)) {

                        if (!floors.contains(i * 200 + j)) {
                            floors.add(i * 200 + j);
                            Gameplay.sqawnFire(i, j, Math.max(0.5, duration), damage, friendly, special, true);
                        }
                    }
                }

            }
        }

        return false;
    }

    public void free() {
        audio.free();
        audio = null;
        flame = null;
        floors.clear();
        floors = null;
    }
}
